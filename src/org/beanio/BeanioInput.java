package org.beanio;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

/** Selector between reader or inputstream, based on format option in beanio.properties */
public class BeanioInput extends AbstractBeanioInputOutput {

  private InputStream inputStream;
  private Reader reader;

  public static BeanioInput ofReader(Reader reader) {
    return new BeanioInput(reader);
  }

  public static BeanioInput ofInputStream(InputStream in) {
    return new BeanioInput(in);
  }

  private BeanioInput(InputStream inputStream) {
    this.inputStream = inputStream;
  }

  private BeanioInput(Reader reader) {
    this.reader = reader;
  }

  public Reader getReader() {
    if (binary) {
      throw new IllegalArgumentException("format is binary but reader was requested");
    }
    return this.reader != null ? this.reader : new InputStreamReader(this.inputStream);
  }

  public InputStream getInputStream() {
    if (!binary) {
      throw new IllegalArgumentException("format is not binary but input stream was requested");
    }
    return this.inputStream;
  }

  @Override
  public void close() throws Exception {
    if (this.reader != null) this.reader.close();
    if (this.inputStream != null) this.inputStream.close();
  }
}
