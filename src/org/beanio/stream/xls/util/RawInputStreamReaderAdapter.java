package org.beanio.stream.xls.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

public class RawInputStreamReaderAdapter extends Reader {

  private final InputStream is;

  public RawInputStreamReaderAdapter(InputStream is) {
    this.is = is;
  }

  public InputStream getInputStream() {
    return is;
  }

  @Override
  public int read(char[] cbuf, int off, int len) throws IOException {
    byte[] b = new byte[len];
    for (int i = 0; i < len; i++) {
      b[i] = (byte) cbuf[i];
    }
    return is.read(b, off, len);
  }

  @Override
  public void close() throws IOException {
    this.is.close();
  }
}
