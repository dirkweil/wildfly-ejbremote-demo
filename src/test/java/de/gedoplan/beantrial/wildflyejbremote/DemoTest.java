package de.gedoplan.beantrial.wildflyejbremote;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.rmi.RemoteException;
import java.util.Properties;

import javax.ejb.EJBAccessException;
import javax.ejb.EJBException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.hamcrest.CustomTypeSafeMatcher;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class DemoTest
{
  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  private Demo             demo;

  @Before
  public void before() throws NamingException
  {
    Properties jndiProps = new Properties();

    jndiProps.put(Context.INITIAL_CONTEXT_FACTORY, "org.jboss.naming.remote.client.InitialContextFactory");
    jndiProps.put(Context.PROVIDER_URL, "http-remoting://localhost:8080");

    jndiProps.setProperty(Context.SECURITY_PRINCIPAL, "anonymous");
    jndiProps.setProperty(Context.SECURITY_CREDENTIALS, "anonymous_123");

    jndiProps.put("jboss.naming.client.connect.options.org.xnio.Options.SASL_DISALLOWED_MECHANISMS", "JBOSS-LOCAL-USER");
    jndiProps.put("jboss.naming.client.connect.options.org.xnio.Options.SASL_POLICY_NOPLAINTEXT", false);

    jndiProps.put("jboss.naming.client.ejb.context", true);

    InitialContext jndiContext = new InitialContext(jndiProps);

    this.demo = (Demo) jndiContext.lookup("wildfly-ejbremote-demo/DemoBean!de.gedoplan.beantrial.wildflyejbremote.Demo");
  }

  @Test
  public void testGetHello()
  {
    String hello = this.demo.getHello();
    assertThat(hello, is("Hello, world!"));
  }

  @Test
  public void testGetException()
  {
    this.expectedException.expect(exceptionRootIs(UnsupportedOperationException.class));
    this.demo.getException();
  }

  @Test
  public void testRestrictedMethod() throws Exception
  {
    this.expectedException.expect(exceptionRootIs(EJBAccessException.class));
    this.demo.restrictedMethod();
  }

  private Matcher<?> exceptionRootIs(final Class<? extends Throwable> rootClass)
  {
    return new CustomTypeSafeMatcher<Throwable>(rootClass + " as (in)directly causing exception")
    {
      @Override
      protected boolean matchesSafely(Throwable exception)
      {
        while (exception != null && (exception instanceof RemoteException || exception instanceof EJBException))
        {
          exception = exception.getCause();
        }

        return exception != null && rootClass.isAssignableFrom(exception.getClass());
      }
    };
  }
}
