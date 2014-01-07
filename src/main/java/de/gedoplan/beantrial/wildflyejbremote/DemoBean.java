package de.gedoplan.beantrial.wildflyejbremote;

import javax.ejb.Stateless;

@Stateless
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

}
