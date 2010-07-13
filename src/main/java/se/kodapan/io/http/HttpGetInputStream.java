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

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author kalle
 * @since 2010-apr-25 22:11:29
 */
public class HttpGetInputStream extends InputStream {

  URI uri;
  HttpClient httpClient;

  public HttpGetInputStream(URI uri) {
    this(uri, new DefaultHttpClient());
  }

  public HttpGetInputStream(URI uri, HttpClient httpClient) {
    this.uri = uri;
    this.httpClient = httpClient;
  }

  public HttpGetInputStream(String uri) throws URISyntaxException {
    this(uri, new DefaultHttpClient());
  }

  public HttpGetInputStream(String uri, HttpClient httpClient) throws URISyntaxException {
    this.uri = new URI(uri);
    this.httpClient = httpClient;
  }

  private InputStream httpInputStream;

  @Override
  public int read() throws IOException {
    if (httpInputStream == null) {
      factory();
    }
    return httpInputStream.read();
  }

  @Override
  public int read(byte[] bytes) throws IOException {
    if (httpInputStream == null) {
      factory();
    }
    return httpInputStream.read(bytes);
  }

  @Override
  public int read(byte[] bytes, int i, int i1) throws IOException {
    if (httpInputStream == null) {
      factory();
    }
    return httpInputStream.read(bytes, i, i1);
  }

  private void factory() throws IOException {
    HttpGet get = new HttpGet(uri);
    HttpClientUtil.addResponseHeaders(get);
    HttpResponse httpResponse = httpClient.execute(get);

    InputStream httpInputStream = httpResponse.getEntity().getContent();
    if (httpInputStream == null) {
      httpResponse.getEntity().consumeContent();
      throw new IOException(httpResponse.getStatusLine().toString());
    }

    this.httpInputStream = httpInputStream;
  }

  @Override
  public void close() throws IOException {
    if (httpInputStream != null) {
      httpInputStream.close();
    }
    httpClient.getConnectionManager().shutdown();
  }
}