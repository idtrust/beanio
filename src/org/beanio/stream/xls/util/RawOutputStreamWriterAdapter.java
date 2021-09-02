package org.beanio.stream.xls.util;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

/** adapter de writer para formato binario */
public class RawOutputStreamWriterAdapter extends Writer {
  private final OutputStream os;

  public RawOutputStreamWriterAdapter(OutputStream os) {
    this.os = os;
  }

  @Override
  public void write(char[] cbuf, int off, int len) throws IOException {
    byte[] b = new byte[cbuf.length];
    for (int i = 0; i < cbuf.length; i++) {
      b[i] = (byte) cbuf[i];
    }

    os.write(b, off, len);
  }

  @Override
  public void flush() throws IOException {
    this.os.flush();
  }

  @Override
  public void close() throws IOException {
    this.os.close();
  }
}
