package de.gedoplan.beantrial.wildflyejbremote;

import javax.ejb.Remote;

@Remote
public interface Demo
{
  public String getHello();

  public void getException();
}
