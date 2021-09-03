package org.beanio;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class BeanioOutput extends AbstractBeanioInputOutput {

  private OutputStream outputStream;
  private Writer writer;

  private BeanioOutput(OutputStream os) {
    this.outputStream = os;
  }

  private BeanioOutput(Writer writer) {
    this.writer = writer;
  }

  public static BeanioOutput ofWriter(Writer writer) {
    return new BeanioOutput(writer);
  }

  public static BeanioOutput ofOutputStream(OutputStream outputStream) {
    return new BeanioOutput(outputStream);
  }

  public Writer getWriter() {
    if (binary) {
      throw new IllegalArgumentException("format is binary but writer was requested");
    }
    return writer == null ? new OutputStreamWriter(outputStream) : writer;
  }

  public OutputStream getOutputStream() {
    if (!binary) {
      throw new IllegalArgumentException("format is not binary but output stream was requested");
    }
    return this.outputStream;
  }

  @Override
  public void close() throws Exception {
    if (this.writer != null) writer.close();
    if (this.outputStream != null) outputStream.close();
  }
}
