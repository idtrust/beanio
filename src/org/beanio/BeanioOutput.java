package org.beanio;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class BeanioOutput extends AbstractBeanioInputOutput {

  private OutputStream outputStream;
  private Writer writer;

  protected BeanioOutput() {}

  private BeanioOutput(OutputStream os) {
    this.outputStream = os;
  }

  private BeanioOutput(Writer writer) {
    this.writer = writer;
  }

  private BeanioOutput(Writer writer, OutputStream outputStream) {
    this.writer = writer;
    this.outputStream = outputStream;
  }

  public static BeanioOutput of(Writer writer, OutputStream outputStream) {
    return new BeanioOutput(writer, outputStream);
  }

  public static BeanioOutput ofWriter(Writer writer) {
    return new BeanioOutput(writer);
  }

  public static BeanioOutput ofOutputStream(OutputStream outputStream) {
    return new BeanioOutput(outputStream);
  }

  public boolean isBinary() {
    return this.binary;
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
