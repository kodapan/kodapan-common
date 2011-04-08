/*
 * Copyright 2010 Kodapan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package se.kodapan.io;

import java.io.*;
import java.nio.charset.Charset;

/**
 * Transparent access to UTF-8 and ISO-8859-1
 * <p/>
 * TODO only supports Swedish!! add æøÆØ etc
 * TODO Perhaps even better, create a classifier with byte 3-4(?) grams of UTF8, UTF16, UTF32 and ISO-8859-1
 *
 * @author kalle
 * @since 2010-apr-25 08:47:36
 */
public class GermanicInputStreamReader extends Reader {

  public static int DEFAULT_BUFFER_SIZE = 1024;

  private InputStream input;

  private ByteArrayInterpreter undetectedByteArrayInterpreter = ISO88591;
  private ByteArrayInterpreter selectedByteArrayInterpreter;
  private ByteArrayInterpreter[] byteArrayInterpreters;

  public GermanicInputStreamReader(InputStream input) {
    this(input, DEFAULT_BUFFER_SIZE, ISO88591, ISO88591, UTF8);
  }

  public GermanicInputStreamReader(InputStream input, int bufferSize, ByteArrayInterpreter undetectedByteArrayInterpreter, ByteArrayInterpreter... byteArrayInterpreters) {
    this.input = input;
    this.undetectedByteArrayInterpreter = undetectedByteArrayInterpreter;
    this.byteArrayInterpreters = byteArrayInterpreters;
  }



  public abstract static class ByteArrayInterpreter {
    private Charset charset;

    protected ByteArrayInterpreter(Charset charset) {
      this.charset = charset;
    }

    protected abstract int detect(byte[] bytes, int length);
  }


  public static byte[] ISO88591bytes = new byte[]{
      // Ä
      (byte) 196,
      // ä
      (byte) 228,
      // Å
      (byte) 197,
      // å
      (byte) 229,
      // Ö
      (byte) 214,
      // ö
      (byte) 246
  };

  public static final ByteArrayInterpreter ISO88591 = new ByteArrayInterpreter(Charset.forName("ISO-8859-1")) {
    @Override
    protected int detect(byte[] bytes, int length) {
      int matches = 0;
      for (int i = 0; i < length; i++) {
        for (byte b : ISO88591bytes) {
          if (bytes[i] == b) {
            matches++;
          }
        }
      }
      return matches;
    }
  };



  public static byte[][] UTF8pairs = new byte[][]{
      // Ä
      new byte[]{(byte) 0xC3, (byte) 0x84},
      // ä
      new byte[]{(byte) 0xC3, (byte) 0xA4},
      // Å
      new byte[]{(byte) 0xC3, (byte) 0x85},
      // å
      new byte[]{(byte) 0xC3, (byte) 0xA5},
      // Ö
      new byte[]{(byte) 0xC3, (byte) 0x96},
      // ö
      new byte[]{(byte) 0xC3, (byte) 0xB6},
  };

  public static final ByteArrayInterpreter UTF8 = new ByteArrayInterpreter(Charset.forName("UTF-8")) {
    @Override
    protected int detect(byte[] bytes, int length) {
      int matches = 0;
      for (int i = 0; i < length; i++) {
        for (byte[] b : UTF8pairs) {
          if (bytes[i] == b[0] && bytes[i + 1] == b[1]) {
            matches++;
          }
        }
      }
      return matches;
    }
  };

  public Charset getEncoding() throws IOException {
    if (selectedByteArrayInterpreter == null) {
      buffer();
    }
    return selectedByteArrayInterpreter.charset;
  }

  private InputStreamReader output;

  private void buffer() throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream(49152);
    byte[] buf = new byte[4196];
    int read;
    while ((read = input.read(buf)) > -1) {
      baos.write(buf, 0, read);
    }
    buf = baos.toByteArray();
    baos.close();

    input.close();
    input = null;

    selectedByteArrayInterpreter = undetectedByteArrayInterpreter;
    int topScore = Integer.MIN_VALUE;
    for (ByteArrayInterpreter byteArrayInterpreter : byteArrayInterpreters) {
      int score = byteArrayInterpreter.detect(buf, buf.length);
      if (score > topScore) {
        selectedByteArrayInterpreter = byteArrayInterpreter;
        topScore = score;
      }
    }

    output = new InputStreamReader(new ByteArrayInputStream(buf), selectedByteArrayInterpreter.charset);
  }

  @Override
  public int read(char[] chars, int i, int i1) throws IOException {
    if (output == null) {
      buffer();
    }
    return output.read(chars, i, i1);
  }

  @Override
  public void reset() throws IOException {
    if (input != null) {
      input.reset();
    }
    if (output != null) {
      output.reset();
    }
  }

  @Override
  public void close() throws IOException {
    if (input != null) {
      input.close();
    }
    if (output != null) {
      output.close();
    }
  }
}
