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

package se.kodapan.io.http;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import se.kodapan.io.GermanicInputStreamReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author kalle
 * @since 2010-apr-25 22:11:29
 */
public class HttpGetReader extends Reader {

  URI uri;
  HttpClient httpClient;

  public HttpGetReader(URI uri) {
    this(uri, new DefaultHttpClient());
  }

  public HttpGetReader(URI uri, HttpClient httpClient) {
    this.uri = uri;
    this.httpClient = httpClient;
  }

  public HttpGetReader(String uri) throws URISyntaxException {
    this(uri, new DefaultHttpClient());
  }

  public HttpGetReader(String uri, HttpClient httpClient) throws URISyntaxException {
    this.uri = new URI(uri);
    this.httpClient = httpClient;
  }


  private static final Pattern charsetPattern = Pattern.compile("^.+;\\s*charset\\s*=\\s*(.+)\\s*$", Pattern.CASE_INSENSITIVE);

  private Reader httpReader;

  @Override
  public int read(char[] chars, int i, int i1) throws IOException {

    if (httpReader == null) {

      HttpGet get = new HttpGet(uri);
      HttpClientUtil.addResponseHeaders(get);
      HttpResponse httpResponse = httpClient.execute(get);

      InputStream httpInputStream = httpResponse.getEntity().getContent();
      if (httpInputStream == null) {
        httpResponse.getEntity().consumeContent();
        throw new IOException(httpResponse.getStatusLine().toString());
      }

      String contentType = httpResponse.getEntity().getContentType() != null ? httpResponse.getEntity().getContentType().getValue() : null;
      String contentEncoding = httpResponse.getEntity().getContentEncoding() != null ? httpResponse.getEntity().getContentEncoding().getValue() : null;
      if (contentEncoding == null) {
        Matcher matcher = charsetPattern.matcher(contentType);
        if (matcher.matches()) {
          contentEncoding = matcher.group(1);
        }
      }

      if (contentEncoding == null) {
        httpReader = new GermanicInputStreamReader(httpInputStream);
      } else {
        httpReader = new InputStreamReader(httpInputStream, contentEncoding);
      }

    }

    return httpReader.read(chars, i, i1);

  }

  @Override
  public void close() throws IOException {
    if (httpReader != null) {
      httpReader.close();
    }
    httpClient.getConnectionManager().shutdown();
  }
}
