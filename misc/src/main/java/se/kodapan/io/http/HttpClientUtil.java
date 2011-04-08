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

import org.apache.http.client.methods.HttpRequestBase;

/**
 * @author kalle
 * @since 2010-jul-09 22:44:22
 */
public class HttpClientUtil {

  public static void addResponseHeaders(HttpRequestBase method) {
//
//    Host	www.hemnet.se
//User-Agent	Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.5; sv-SE; rv:1.9.1.2) Gecko/20090729 Firefox/3.5.2
//Accept	text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8
//Accept-Language	sv-se,sv;q=0.8,en-us;q=0.5,en;q=0.3
//Accept-Encoding	gzip,deflate
//Accept-Charset	ISO-8859-1,utf-8;q=0.7,*;q=0.7
//Keep-Alive	300
//Connection	keep-alive
//Referer	http://www.hemnet.se/?sortering=pris&sortering_ordning=desc

    method.addHeader("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.5; sv-SE; rv:1.9.1.2) Gecko/20090729 Firefox/3.5.2");
    method.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
    method.addHeader("Accept-Language", "sv-se,sv;q=0.8,en-us;q=0.5,en;q=0.3");
    method.addHeader("Accept-Charset", "ISO-8859-1,utf-8;q=0.7,*;q=0.7");
  }

}
