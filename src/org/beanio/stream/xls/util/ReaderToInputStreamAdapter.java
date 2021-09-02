package org.beanio.stream.xls.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

public class ReaderToInputStreamAdapter extends InputStream {

  private final Reader reader;

  public ReaderToInputStreamAdapter(Reader reader) {
    this.reader = reader;
  }

  @Override
  public int read() throws IOException {
    return reader.read();
  }
}
