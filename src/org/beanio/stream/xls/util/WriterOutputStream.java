/*
 * Copyright (c) 2002-2017, the original author or authors.
 *
 * This software is distributable under the BSD license. See the terms of the
 * BSD license in the documentation provided with this software.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package org.beanio.stream.xls.util;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.nio.charset.Charset;

public class WriterOutputStream extends OutputStream {

  /**
   * Redirects an {@link OutputStream} to a {@link Writer} by decoding the data using the specified
   * {@link Charset}.
   *
   * <p><b>Note:</b> This class should only be used if it is necessary to redirect an {@link
   * OutputStream} to a {@link Writer} for compatibility purposes. It is much more efficient to
   * write to the {@link Writer} directly.
   */
  private final Writer out;

  public WriterOutputStream(Writer out) {
    this.out = out;
  }

  @Override
  public void write(int b) throws IOException {
    write(new byte[] {(byte) b}, 0, 1);
  }

  @Override
  public void write(byte[] b) throws IOException {
    write(b, 0, b.length);
  }

  @Override
  public void write(byte[] b, int off, int len) throws IOException {
    char[] c = new char[b.length];
    for (int i = 0; i < b.length; i++) {
      c[i] = (char) b[i];
    }
    this.out.write(c, off, len);
    flush();
  }

  @Override
  public void flush() throws IOException {
    out.flush();
  }

  @Override
  public void close() throws IOException {
    flush();
    out.close();
  }
}
