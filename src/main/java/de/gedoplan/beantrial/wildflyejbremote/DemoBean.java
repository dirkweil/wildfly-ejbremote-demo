package de.gedoplan.beantrial.wildflyejbremote;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;

@Stateless
@PermitAll
public class DemoBean implements Demo
{
  @Override
  public String getHello()
  {
    return "Hello, world!";
  }

  @Override
  public void getException()
  {
    throw new UnsupportedOperationException("Not implemented");
  }

  @RolesAllowed("demoRole")
  public void restrictedMethod()
  {

  }

}
