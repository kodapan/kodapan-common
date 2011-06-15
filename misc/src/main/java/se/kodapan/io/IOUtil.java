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

/**
 * @author kalle
 * @since 2010-jan-09 20:30:36
 */
public class IOUtil {

  public static void copy(InputStream input, OutputStream output) throws IOException {
    copy(input, output, 49152, true);
  }

  public static void copy(InputStream input, OutputStream output, int bufferSize, boolean closeStreams) throws IOException {
    copy(input, output, new byte[bufferSize], closeStreams);
  }

  public static void copy(InputStream input, OutputStream output, byte[] buffer, boolean closeStreams) throws IOException {

    int read;
    while ((read = input.read(buffer)) > -1) {
      output.write(buffer, 0, read);
    }
    if (closeStreams) {
      input.close();
      output.close();
    }

  }


  public static void copy(Reader input, Writer output) throws IOException {
    copy(input, output, 49152, true);
  }

  public static void copy(Reader input, Writer output, int bufferSize, boolean closeStreams) throws IOException {
    copy(input, output, new char[bufferSize], closeStreams);
  }

  public static void copy(Reader input, Writer output, char[] buffer, boolean closeStreams) throws IOException {

    int read;
    while ((read = input.read(buffer)) > -1) {
      output.write(buffer, 0, read);
    }
    if (closeStreams) {
      input.close();
      output.close();
    }

  }


  public static File createTemporaryDirectory(String group, String artifact) throws IOException {
    File tmpDirectory = File.createTempFile(group, artifact);
    if (!tmpDirectory.delete()) {
      throw new IOException("Could not delete tmp directory " + tmpDirectory.getAbsolutePath());
    }
    if (!tmpDirectory.mkdirs()) {
      throw new IOException("Could not create tmp directory " + tmpDirectory.getAbsolutePath());
    }
    return tmpDirectory;
  }

}

