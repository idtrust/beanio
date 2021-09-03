package org.beanio;

import org.beanio.internal.util.Settings;

public abstract class AbstractBeanioInputOutput implements AutoCloseable {
  private static final String PROPERTY_FORMAT = "org.beanio.%s.binaryFormat";

  protected Boolean binary;
  protected Settings settings;

  public void setSettings(Settings settings) {
    this.settings = settings;
  }

  public void setFormat(String format) {
    this.binary = settings.getBoolean(String.format(PROPERTY_FORMAT, format));
  }
}
