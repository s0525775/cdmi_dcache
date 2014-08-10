/* dCache - http://www.dcache.org/
 *
 * Copyright (C) 2014 Deutsches Elektronen-Synchrotron
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.snia.cdmiserver;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.apache.axiom.om.util.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.Header;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 * REMARK (from Jana):
 * The CDMItest may also tell "success" if all tests fail. Please also check the 'Output' window
 * in NetBeans IDE for CDMItest and the dCacheDomain.log file for error messages - always! A test
 * is successful if no relevant error messages appeared in the 'Output' window of NetBeans and in
 * the dCacheDomain.log file. Since the current dcache-cdmi version is still not stable, the
 * CDMItest still needs a HelperClass which causes a break of 3 seconds between every test. Tests
 * can be included by the '@Test' annotation and excluded by the '@Test' annotation. This example
 * of CDMItest will include the first 5 tests and exclude the last 2 tests. dCacheDomain.log is
 * principally used for investigating problems and tests. dcache-cdmi also generates .log files
 * below the /tmp directory at the moment which is still necessary for some tests. The .log files
 * will be removed later again when the corresponding source code part is fully implemented.
 * Those .log files don't get deleted automatically and may need to get deleted manually later.
 * The port in CDMItest can be replaced by a variable, or by using the replace function of NetBeans.
 */

// http://stackoverflow.com/questions/15754094/how-to-make-junit-test-run-methods-in-order
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CDMIPwTest_advanced {

    private final static String USERNAME = "test";
    private final static String PASSWORD = "test";
    private final String credentials;

    public CDMIPwTest_advanced() {
        String creds = USERNAME + ":" + PASSWORD;  //username:password
        credentials = Base64.encode(creds.getBytes());
    }

    public static class HelperClass {
        public static void sleep(long ms) {
            //try {
            //    Thread.sleep(ms);
            //} catch (InterruptedException ex) {
            //    Logger.getLogger(CDMIPwTest.class.getName()).log(Level.SEVERE, null, ex);
            //}
        }
    }

    @Test
    public void test10_Capabilities() throws Exception {
        HelperClass.sleep(3000);
        HttpClient httpclient = new DefaultHttpClient();

        try {
            // Create the request
            HttpResponse response = null;
            HttpGet httpget = new HttpGet("http://localhost:8542/cdmi_capabilities");
            httpget.setHeader("Accept", "application/cdmi-capability");
            httpget.setHeader("X-CDMI-Specification-Version", "1.0.2");
            httpget.setHeader("Authorization", "Basic " + credentials);
            response = httpclient.execute(httpget);

            Header[] hdr = response.getAllHeaders();
            System.out.println("Headers : " + hdr.length);
            for (int i = 0; i < hdr.length; i++) {
                System.out.println(hdr[i]);
            }
            System.out.println("---------");
            System.out.println(response.getProtocolVersion());
            System.out.println(response.getStatusLine().getStatusCode());
            Assert.assertEquals(200, response.getStatusLine().getStatusCode());

            System.out.println(response.getStatusLine().getReasonPhrase());
            System.out.println(response.getStatusLine().toString());
            System.out.println("---------");
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                long len = entity.getContentLength();
                if (len != -1 && len < 2048) {
                    System.out.println(EntityUtils.toString(entity));
                }
            }

        } catch (Exception ex) {
            System.out.println(ex);
        }// exception
    }

    @Test
    public void test20_ContainerCreate() throws Exception {
        HelperClass.sleep(3000);
        HttpClient httpclient = new DefaultHttpClient();

        try {
            // Create the request
            HttpResponse response = null;
            HttpPut httpput = new HttpPut("http://localhost:8542/TestContainer02");
            httpput.setHeader("Content-Type", "application/cdmi-container");
            httpput.setHeader("X-CDMI-Specification-Version", "1.0.2");
            httpput.setHeader("Authorization", "Basic " + credentials);
            //httpput.setEntity(new StringEntity("{ \"metadata\" : { } }"));
            httpput.setEntity(new StringEntity("{ \"metadata\" : { \"color\" : \"red\", \"test\" : \"Test\" } }"));
            response = httpclient.execute(httpput);

            Header[] hdr = response.getAllHeaders();
            System.out.println("Headers : " + hdr.length);
            for (int i = 0; i < hdr.length; i++) {
                System.out.println(hdr[i]);
            }
            System.out.println("---------");
            System.out.println(response.getProtocolVersion());
            System.out.println(response.getStatusLine().getStatusCode());
            Assert.assertEquals(201, response.getStatusLine().getStatusCode());

            System.out.println(response.getStatusLine().getReasonPhrase());
            System.out.println(response.getStatusLine().toString());
            System.out.println("---------");
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                long len = entity.getContentLength();
                if (len != -1 && len < 2048) {
                    System.out.println(EntityUtils.toString(entity));
                }
            }

        } catch (Exception ex) {
            System.out.println(ex);
        }// exception
    }

    @Test
    public void test21_ContainerCreate1() throws Exception {
        HelperClass.sleep(3000);
        HttpClient httpclient = new DefaultHttpClient();

        try {
            // Create the request
            HttpResponse response = null;
            HttpPut httpput = new HttpPut("http://localhost:8542/TestContainer21");
            httpput.setHeader("Content-Type", "application/cdmi-container");
            httpput.setHeader("X-CDMI-Specification-Version", "1.0.2");
            httpput.setHeader("Authorization", "Basic " + credentials);
            //httpput.setEntity(new StringEntity("{ \"metadata\" : { } }"));
            httpput.setEntity(new StringEntity("{ \"metadata\" : { \"color\" : \"red\", \"test\" : \"Test\" } }"));
            response = httpclient.execute(httpput);

            Header[] hdr = response.getAllHeaders();
            System.out.println("Headers : " + hdr.length);
            for (int i = 0; i < hdr.length; i++) {
                System.out.println(hdr[i]);
            }
            System.out.println("---------");
            System.out.println(response.getProtocolVersion());
            System.out.println(response.getStatusLine().getStatusCode());
            Assert.assertEquals(201, response.getStatusLine().getStatusCode());

            System.out.println(response.getStatusLine().getReasonPhrase());
            System.out.println(response.getStatusLine().toString());
            System.out.println("---------");
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                long len = entity.getContentLength();
                if (len != -1 && len < 2048) {
                    System.out.println(EntityUtils.toString(entity));
                }
            }

        } catch (Exception ex) {
            System.out.println(ex);
        }// exception
    }

    @Test
    public void test22_ContainerCreate2() throws Exception {
        HelperClass.sleep(3000);
        HttpClient httpclient = new DefaultHttpClient();

        try {
            // Create the request
            HttpResponse response = null;
            HttpPut httpput = new HttpPut("http://localhost:8542/TestContainer21/TestContainer22");
            httpput.setHeader("Content-Type", "application/cdmi-container");
            httpput.setHeader("X-CDMI-Specification-Version", "1.0.2");
            httpput.setHeader("Authorization", "Basic " + credentials);
            //httpput.setEntity(new StringEntity("{ \"metadata\" : { } }"));
            httpput.setEntity(new StringEntity("{ \"metadata\" : { \"color\" : \"red\", \"test\" : \"Test\" } }"));
            response = httpclient.execute(httpput);

            Header[] hdr = response.getAllHeaders();
            System.out.println("Headers : " + hdr.length);
            for (int i = 0; i < hdr.length; i++) {
                System.out.println(hdr[i]);
            }
            System.out.println("---------");
            System.out.println(response.getProtocolVersion());
            System.out.println(response.getStatusLine().getStatusCode());
            Assert.assertEquals(201, response.getStatusLine().getStatusCode());

            System.out.println(response.getStatusLine().getReasonPhrase());
            System.out.println(response.getStatusLine().toString());
            System.out.println("---------");
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                long len = entity.getContentLength();
                if (len != -1 && len < 2048) {
                    System.out.println(EntityUtils.toString(entity));
                }
            }

        } catch (Exception ex) {
            System.out.println(ex);
        }// exception
    }

    @Test
    public void test23_ContainerCreate3() throws Exception {
        HelperClass.sleep(3000);
        HttpClient httpclient = new DefaultHttpClient();

        try {
            // Create the request
            HttpResponse response = null;
            HttpPut httpput = new HttpPut("http://localhost:8542/TestContainer21/TestContainer22/TestContainer23");
            httpput.setHeader("Content-Type", "application/cdmi-container");
            httpput.setHeader("X-CDMI-Specification-Version", "1.0.2");
            httpput.setHeader("Authorization", "Basic " + credentials);
            //httpput.setEntity(new StringEntity("{ \"metadata\" : { } }"));
            httpput.setEntity(new StringEntity("{ \"metadata\" : { \"color\" : \"red\", \"test\" : \"Test\" } }"));
            response = httpclient.execute(httpput);

            Header[] hdr = response.getAllHeaders();
            System.out.println("Headers : " + hdr.length);
            for (int i = 0; i < hdr.length; i++) {
                System.out.println(hdr[i]);
            }
            System.out.println("---------");
            System.out.println(response.getProtocolVersion());
            System.out.println(response.getStatusLine().getStatusCode());
            Assert.assertEquals(201, response.getStatusLine().getStatusCode());

            System.out.println(response.getStatusLine().getReasonPhrase());
            System.out.println(response.getStatusLine().toString());
            System.out.println("---------");
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                long len = entity.getContentLength();
                if (len != -1 && len < 2048) {
                    System.out.println(EntityUtils.toString(entity));
                }
            }

        } catch (Exception ex) {
            System.out.println(ex);
        }// exception
    }

    @Test
    public void test30_ContainerMove() throws Exception {
        HelperClass.sleep(3000);
        HttpClient httpclient = new DefaultHttpClient();

        try {
            // Create the request
            HttpResponse response = null;
            HttpPut httpput = new HttpPut("http://localhost:8542/TestContainer2");
            httpput.setHeader("Content-Type", "application/cdmi-container");
            httpput.setHeader("X-CDMI-Specification-Version", "1.0.2");
            httpput.setHeader("Authorization", "Basic " + credentials);
            httpput.setEntity(new StringEntity("{ \"move\" : \"/TestContainer02\", \"metadata\" : { \"color\" : \"red\", \"test\" : \"Test\" } }"));
            response = httpclient.execute(httpput);

            Header[] hdr = response.getAllHeaders();
            System.out.println("Headers : " + hdr.length);
            for (Header hdr1 : hdr) {
                System.out.println(hdr1);
            }
            System.out.println("---------");
            System.out.println(response.getProtocolVersion());
            System.out.println(response.getStatusLine().getStatusCode());
            Assert.assertEquals(200, response.getStatusLine().getStatusCode());

            System.out.println(response.getStatusLine().getReasonPhrase());
            System.out.println(response.getStatusLine().toString());
            System.out.println("---------");
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                long len = entity.getContentLength();
                if (len != -1 && len < 2048) {
                    System.out.println(EntityUtils.toString(entity));
                }
            }

        } catch (Exception ex) {
            System.out.println(ex);
        }// exception
    }

    @Test
    public void test40_ContainerUpdate() throws Exception {
        HelperClass.sleep(3000);
        HttpClient httpclient = new DefaultHttpClient();

        try {
            // Create the request
            HttpResponse response = null;
            HttpPut httpput = new HttpPut("http://localhost:8542/TestContainer2");
            httpput.setHeader("Content-Type", "application/cdmi-container");
            httpput.setHeader("X-CDMI-Specification-Version", "1.0.2");
            httpput.setHeader("Authorization", "Basic " + credentials);
            //httpput.setEntity(new StringEntity("{ \"metadata\" : { } }"));
            httpput.setEntity(new StringEntity("{ \"metadata\" : { \"color\" : \"green\", \"test\" : \"Test\" } }"));
            response = httpclient.execute(httpput);

            Header[] hdr = response.getAllHeaders();
            System.out.println("Headers : " + hdr.length);
            for (int i = 0; i < hdr.length; i++) {
                System.out.println(hdr[i]);
            }
            System.out.println("---------");
            System.out.println(response.getProtocolVersion());
            System.out.println(response.getStatusLine().getStatusCode());
            Assert.assertEquals(200, response.getStatusLine().getStatusCode());

            System.out.println(response.getStatusLine().getReasonPhrase());
            System.out.println(response.getStatusLine().toString());
            System.out.println("---------");
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                long len = entity.getContentLength();
                if (len != -1 && len < 2048) {
                    System.out.println(EntityUtils.toString(entity));
                }
            }

        } catch (Exception ex) {
            System.out.println(ex);
        }// exception
    }

    @Test
    public void test50_ObjectCreate() throws Exception {
        HelperClass.sleep(3000);
        HttpClient httpclient = new DefaultHttpClient();

        try {
            // Create the request
            HttpResponse response = null;
            HttpPut httpput = new HttpPut(
                    "http://localhost:8542/TestObject.txt");
            httpput.setHeader("Content-Type", "application/cdmi-object");
            httpput.setHeader("X-CDMI-Specification-Version", "1.0.2");
            httpput.setHeader("Authorization", "Basic " + credentials);
            String respStr = "{\n";
            respStr = respStr + "\"mimetype\" : \"" + "text/plain" + "\",\n";
            respStr = respStr + "\"value\" : \"" + "This is a test" + "\",\n";
            respStr = respStr + "\"metadata\" : {" + "\"color\" : \"yellow\", \"test\" : \"Test2\"" + "}\n";
            respStr = respStr + "}\n";
            System.out.println(respStr);
            StringEntity entity = new StringEntity(respStr);
            httpput.setEntity(entity);
            response = httpclient.execute(httpput);

            Header[] hdr = response.getAllHeaders();
            System.out.println("Headers : " + hdr.length);
            for (int i = 0; i < hdr.length; i++) {
                System.out.println(hdr[i]);
            }
            System.out.println("---------");
            System.out.println(response.getProtocolVersion());
            System.out.println(response.getStatusLine().getStatusCode());
            Assert.assertEquals(201, response.getStatusLine().getStatusCode());

            System.out.println(response.getStatusLine().getReasonPhrase());
            System.out.println(response.getStatusLine().toString());
            System.out.println("---------");

        } catch (Exception ex) {
            System.out.println(ex);
        }// exception
    }

    @Test
    public void test51_ObjectCreate1() throws Exception {
        HelperClass.sleep(3000);
        HttpClient httpclient = new DefaultHttpClient();

        try {
            // Create the request
            HttpResponse response = null;
            HttpPut httpput = new HttpPut(
                    "http://localhost:8542/TestContainer21/TestContainer22/TestObject.txt");
            httpput.setHeader("Content-Type", "application/cdmi-object");
            httpput.setHeader("X-CDMI-Specification-Version", "1.0.2");
            httpput.setHeader("Authorization", "Basic " + credentials);
            String respStr = "{\n";
            respStr = respStr + "\"mimetype\" : \"" + "text/plain" + "\",\n";
            respStr = respStr + "\"value\" : \"" + "This is a test" + "\",\n";
            respStr = respStr + "\"metadata\" : {" + "\"color\" : \"yellow\", \"test\" : \"Test2\"" + "}\n";
            respStr = respStr + "}\n";
            System.out.println(respStr);
            StringEntity entity = new StringEntity(respStr);
            httpput.setEntity(entity);
            response = httpclient.execute(httpput);

            Header[] hdr = response.getAllHeaders();
            System.out.println("Headers : " + hdr.length);
            for (int i = 0; i < hdr.length; i++) {
                System.out.println(hdr[i]);
            }
            System.out.println("---------");
            System.out.println(response.getProtocolVersion());
            System.out.println(response.getStatusLine().getStatusCode());
            Assert.assertEquals(201, response.getStatusLine().getStatusCode());

            System.out.println(response.getStatusLine().getReasonPhrase());
            System.out.println(response.getStatusLine().toString());
            System.out.println("---------");

        } catch (Exception ex) {
            System.out.println(ex);
        }// exception
    }

    @Test
    public void test52_ObjectCreate2() throws Exception {
        HelperClass.sleep(3000);
        HttpClient httpclient = new DefaultHttpClient();

        try {
            // Create the request
            HttpResponse response = null;
            HttpPut httpput = new HttpPut(
                    "http://localhost:8542/TestContainer21/TestContainer22/TestContainer23/TestObject.txt");
            httpput.setHeader("Content-Type", "application/cdmi-object");
            httpput.setHeader("X-CDMI-Specification-Version", "1.0.2");
            httpput.setHeader("Authorization", "Basic " + credentials);
            URL url = new CDMIPwTest_advanced().getClass().getClassLoader().getResource("test.txt");
            String value = readFile(url.getPath());
            String respStr = "{\n";
            respStr = respStr + "\"mimetype\" : \"" + "text/plain" + "\",\n";
            respStr = respStr + "\"value\" : \"" + value + "\",\n";
            respStr = respStr + "\"metadata\" : {" + "\"color\" : \"yellow\", \"test\" : \"Test2\"" + "}\n";
            respStr = respStr + "}\n";
            System.out.println(respStr);
            StringEntity entity = new StringEntity(respStr);
            httpput.setEntity(entity);
            response = httpclient.execute(httpput);

            Header[] hdr = response.getAllHeaders();
            System.out.println("Headers : " + hdr.length);
            for (int i = 0; i < hdr.length; i++) {
                System.out.println(hdr[i]);
            }
            System.out.println("---------");
            System.out.println(response.getProtocolVersion());
            System.out.println(response.getStatusLine().getStatusCode());
            Assert.assertEquals(201, response.getStatusLine().getStatusCode());

            System.out.println(response.getStatusLine().getReasonPhrase());
            System.out.println(response.getStatusLine().toString());
            System.out.println("---------");

        } catch (Exception ex) {
            System.out.println(ex);
        }// exception
    }

    @Test
    public void test53_ObjectCreate20() throws Exception {
        HelperClass.sleep(3000);
        HttpClient httpclient = new DefaultHttpClient();

        try {
            // Create the request
            HttpResponse response = null;
            HttpPut httpput = new HttpPut(
                    "http://localhost:8542/TestContainer21/TestContainer22/TestContainer23/TestObject0.txt");
            httpput.setHeader("Content-Type", "application/cdmi-object");
            httpput.setHeader("X-CDMI-Specification-Version", "1.0.2");
            httpput.setHeader("Authorization", "Basic " + credentials);
            URL url = new CDMIPwTest_advanced().getClass().getClassLoader().getResource("test00.txt");
            String value = readFile(url.getPath());
            String respStr = "{\n";
            respStr = respStr + "\"mimetype\" : \"" + "text/plain" + "\",\n";
            respStr = respStr + "\"value\" : \"" + value + "\",\n";
            respStr = respStr + "\"metadata\" : {" + "\"color\" : \"yellow\", \"test\" : \"Test2\"" + "}\n";
            respStr = respStr + "}\n";
            System.out.println(respStr);
            StringEntity entity = new StringEntity(respStr);
            httpput.setEntity(entity);
            response = httpclient.execute(httpput);

            Header[] hdr = response.getAllHeaders();
            System.out.println("Headers : " + hdr.length);
            for (int i = 0; i < hdr.length; i++) {
                System.out.println(hdr[i]);
            }
            System.out.println("---------");
            System.out.println(response.getProtocolVersion());
            System.out.println(response.getStatusLine().getStatusCode());
            Assert.assertEquals(201, response.getStatusLine().getStatusCode());

            System.out.println(response.getStatusLine().getReasonPhrase());
            System.out.println(response.getStatusLine().toString());
            System.out.println("---------");

        } catch (Exception ex) {
            System.out.println(ex);
        }// exception
    }

    @Test
    public void test54_ObjectCreate21() throws Exception {
        HelperClass.sleep(3000);
        HttpClient httpclient = new DefaultHttpClient();

        try {
            // Create the request
            HttpResponse response = null;
            HttpPut httpput = new HttpPut(
                    "http://localhost:8542/TestContainer21/TestContainer22/TestContainer23/TestObject1.txt");
            httpput.setHeader("Content-Type", "application/cdmi-object");
            httpput.setHeader("X-CDMI-Specification-Version", "1.0.2");
            httpput.setHeader("Authorization", "Basic " + credentials);
            URL url = new CDMIPwTest_advanced().getClass().getClassLoader().getResource("test01.txt");
            String value = readFile(url.getPath());
            String respStr = "{\n";
            respStr = respStr + "\"mimetype\" : \"" + "text/plain" + "\",\n";
            respStr = respStr + "\"value\" : \"" + value + "\",\n";
            respStr = respStr + "\"metadata\" : {" + "\"color\" : \"yellow\", \"test\" : \"Test2\"" + "}\n";
            respStr = respStr + "}\n";
            System.out.println(respStr);
            StringEntity entity = new StringEntity(respStr);
            httpput.setEntity(entity);
            response = httpclient.execute(httpput);

            Header[] hdr = response.getAllHeaders();
            System.out.println("Headers : " + hdr.length);
            for (int i = 0; i < hdr.length; i++) {
                System.out.println(hdr[i]);
            }
            System.out.println("---------");
            System.out.println(response.getProtocolVersion());
            System.out.println(response.getStatusLine().getStatusCode());
            Assert.assertEquals(201, response.getStatusLine().getStatusCode());

            System.out.println(response.getStatusLine().getReasonPhrase());
            System.out.println(response.getStatusLine().toString());
            System.out.println("---------");

        } catch (Exception ex) {
            System.out.println(ex);
        }// exception
    }

    @Test
    public void test55_ObjectCreate22() throws Exception {
        HelperClass.sleep(3000);
        HttpClient httpclient = new DefaultHttpClient();

        try {
            // Create the request
            HttpResponse response = null;
            HttpPut httpput = new HttpPut(
                    "http://localhost:8542/TestContainer21/TestContainer22/TestContainer23/TestObject2.txt");
            httpput.setHeader("Content-Type", "application/cdmi-object");
            httpput.setHeader("X-CDMI-Specification-Version", "1.0.2");
            httpput.setHeader("Authorization", "Basic " + credentials);
            URL url = new CDMIPwTest_advanced().getClass().getClassLoader().getResource("test02.txt");
            String value = readFile(url.getPath());
            String respStr = "{\n";
            respStr = respStr + "\"mimetype\" : \"" + "text/plain" + "\",\n";
            respStr = respStr + "\"value\" : \"" + value + "\",\n";
            respStr = respStr + "\"metadata\" : {" + "\"color\" : \"yellow\", \"test\" : \"Test2\"" + "}\n";
            respStr = respStr + "}\n";
            System.out.println(respStr);
            StringEntity entity = new StringEntity(respStr);
            httpput.setEntity(entity);
            response = httpclient.execute(httpput);

            Header[] hdr = response.getAllHeaders();
            System.out.println("Headers : " + hdr.length);
            for (int i = 0; i < hdr.length; i++) {
                System.out.println(hdr[i]);
            }
            System.out.println("---------");
            System.out.println(response.getProtocolVersion());
            System.out.println(response.getStatusLine().getStatusCode());
            Assert.assertEquals(201, response.getStatusLine().getStatusCode());

            System.out.println(response.getStatusLine().getReasonPhrase());
            System.out.println(response.getStatusLine().toString());
            System.out.println("---------");

        } catch (Exception ex) {
            System.out.println(ex);
        }// exception
    }

    @Test
    public void test56_ObjectCreate23() throws Exception {
        HelperClass.sleep(3000);
        HttpClient httpclient = new DefaultHttpClient();

        try {
            // Create the request
            HttpResponse response = null;
            HttpPut httpput = new HttpPut(
                    "http://localhost:8542/TestContainer21/TestContainer22/TestContainer23/TestObject03.txt");
            httpput.setHeader("Content-Type", "application/cdmi-object");
            httpput.setHeader("X-CDMI-Specification-Version", "1.0.2");
            httpput.setHeader("Authorization", "Basic " + credentials);
            URL url = new CDMIPwTest_advanced().getClass().getClassLoader().getResource("test03.txt");
            String value = readFile(url.getPath());
            String respStr = "{\n";
            respStr = respStr + "\"mimetype\" : \"" + "text/plain" + "\",\n";
            respStr = respStr + "\"value\" : \"" + value + "\",\n";
            respStr = respStr + "\"metadata\" : {" + "\"color\" : \"yellow\", \"test\" : \"Test2\"" + "}\n";
            respStr = respStr + "}\n";
            System.out.println(respStr);
            StringEntity entity = new StringEntity(respStr);
            httpput.setEntity(entity);
            response = httpclient.execute(httpput);

            Header[] hdr = response.getAllHeaders();
            System.out.println("Headers : " + hdr.length);
            for (int i = 0; i < hdr.length; i++) {
                System.out.println(hdr[i]);
            }
            System.out.println("---------");
            System.out.println(response.getProtocolVersion());
            System.out.println(response.getStatusLine().getStatusCode());
            Assert.assertEquals(201, response.getStatusLine().getStatusCode());

            System.out.println(response.getStatusLine().getReasonPhrase());
            System.out.println(response.getStatusLine().toString());
            System.out.println("---------");

        } catch (Exception ex) {
            System.out.println(ex);
        }// exception
    }

    @Test
    public void test57_ObjectCreate24() throws Exception {
        HelperClass.sleep(3000);
        HttpClient httpclient = new DefaultHttpClient();

        try {
            // Create the request
            HttpResponse response = null;
            HttpPut httpput = new HttpPut(
                    "http://localhost:8542/TestContainer21/TestContainer22/TestContainer23/TestObject4.txt");
            httpput.setHeader("Content-Type", "application/cdmi-object");
            httpput.setHeader("X-CDMI-Specification-Version", "1.0.2");
            httpput.setHeader("Authorization", "Basic " + credentials);
            URL url = new CDMIPwTest_advanced().getClass().getClassLoader().getResource("test04.txt");
            String value = readFile(url.getPath());
            String respStr = "{\n";
            respStr = respStr + "\"mimetype\" : \"" + "text/plain" + "\",\n";
            respStr = respStr + "\"value\" : \"" + value + "\",\n";
            respStr = respStr + "\"metadata\" : {" + "\"color\" : \"yellow\", \"test\" : \"Test2\"" + "}\n";
            respStr = respStr + "}\n";
            System.out.println(respStr);
            StringEntity entity = new StringEntity(respStr);
            httpput.setEntity(entity);
            response = httpclient.execute(httpput);

            Header[] hdr = response.getAllHeaders();
            System.out.println("Headers : " + hdr.length);
            for (int i = 0; i < hdr.length; i++) {
                System.out.println(hdr[i]);
            }
            System.out.println("---------");
            System.out.println(response.getProtocolVersion());
            System.out.println(response.getStatusLine().getStatusCode());
            Assert.assertEquals(201, response.getStatusLine().getStatusCode());

            System.out.println(response.getStatusLine().getReasonPhrase());
            System.out.println(response.getStatusLine().toString());
            System.out.println("---------");

        } catch (Exception ex) {
            System.out.println(ex);
        }// exception
    }

    @Test
    public void test58_ObjectCreate25() throws Exception {
        HelperClass.sleep(3000);
        HttpClient httpclient = new DefaultHttpClient();

        try {
            // Create the request
            HttpResponse response = null;
            HttpPut httpput = new HttpPut(
                    "http://localhost:8542/TestContainer21/TestContainer22/TestContainer23/TestObject5.txt");
            httpput.setHeader("Content-Type", "application/cdmi-object");
            httpput.setHeader("X-CDMI-Specification-Version", "1.0.2");
            httpput.setHeader("Authorization", "Basic " + credentials);
            URL url = new CDMIPwTest_advanced().getClass().getClassLoader().getResource("test05.txt");
            String value = readFile(url.getPath());
            String respStr = "{\n";
            respStr = respStr + "\"mimetype\" : \"" + "text/plain" + "\",\n";
            respStr = respStr + "\"value\" : \"" + value + "\",\n";
            respStr = respStr + "\"metadata\" : {" + "\"color\" : \"yellow\", \"test\" : \"Test2\"" + "}\n";
            respStr = respStr + "}\n";
            System.out.println(respStr);
            StringEntity entity = new StringEntity(respStr);
            httpput.setEntity(entity);
            response = httpclient.execute(httpput);

            Header[] hdr = response.getAllHeaders();
            System.out.println("Headers : " + hdr.length);
            for (int i = 0; i < hdr.length; i++) {
                System.out.println(hdr[i]);
            }
            System.out.println("---------");
            System.out.println(response.getProtocolVersion());
            System.out.println(response.getStatusLine().getStatusCode());
            Assert.assertEquals(201, response.getStatusLine().getStatusCode());

            System.out.println(response.getStatusLine().getReasonPhrase());
            System.out.println(response.getStatusLine().toString());
            System.out.println("---------");

        } catch (Exception ex) {
            System.out.println(ex);
        }// exception
    }

    @Test
    public void test59_ObjectCreate26() throws Exception {
        HelperClass.sleep(3000);
        HttpClient httpclient = new DefaultHttpClient();

        try {
            // Create the request
            HttpResponse response = null;
            HttpPut httpput = new HttpPut(
                    "http://localhost:8542/TestContainer21/TestContainer22/TestObject6.txt");
            httpput.setHeader("Content-Type", "application/cdmi-object");
            httpput.setHeader("X-CDMI-Specification-Version", "1.0.2");
            httpput.setHeader("Authorization", "Basic " + credentials);
            URL url = new CDMIPwTest_advanced().getClass().getClassLoader().getResource("test06.txt");
            String value = readFile(url.getPath());
            String respStr = "{\n";
            respStr = respStr + "\"mimetype\" : \"" + "text/plain" + "\",\n";
            respStr = respStr + "\"value\" : \"" + value + "\",\n";
            respStr = respStr + "\"metadata\" : {" + "\"color\" : \"yellow\", \"test\" : \"Test2\"" + "}\n";
            respStr = respStr + "}\n";
            System.out.println(respStr);
            StringEntity entity = new StringEntity(respStr);
            httpput.setEntity(entity);
            response = httpclient.execute(httpput);

            Header[] hdr = response.getAllHeaders();
            System.out.println("Headers : " + hdr.length);
            for (int i = 0; i < hdr.length; i++) {
                System.out.println(hdr[i]);
            }
            System.out.println("---------");
            System.out.println(response.getProtocolVersion());
            System.out.println(response.getStatusLine().getStatusCode());
            Assert.assertEquals(201, response.getStatusLine().getStatusCode());

            System.out.println(response.getStatusLine().getReasonPhrase());
            System.out.println(response.getStatusLine().toString());
            System.out.println("---------");

        } catch (Exception ex) {
            System.out.println(ex);
        }// exception
    }

    @Test
    public void test60_ObjectMove() throws Exception {
        HelperClass.sleep(3000);
        HttpClient httpclient = new DefaultHttpClient();

        try {
            // Create the request
            HttpResponse response = null;
            HttpPut httpput = new HttpPut(
                    "http://localhost:8542/TestContainer2/TestObject.txt");
            httpput.setHeader("Content-Type", "application/cdmi-object");
            httpput.setHeader("X-CDMI-Specification-Version", "1.0.2");
            httpput.setHeader("Authorization", "Basic " + credentials);
            String respStr = "{\n";
            respStr = respStr + "\"mimetype\" : \"" + "text/plain" + "\",\n";
            respStr = respStr + "\"move\" : \"" + "/TestObject.txt" + "\",\n";
            respStr = respStr + "\"metadata\" : {" + "\"color\" : \"orange\", \"test\" : \"Test2\"" + "}\n";
            respStr = respStr + "}\n";
            System.out.println(respStr);
            StringEntity entity = new StringEntity(respStr);
            httpput.setEntity(entity);
            response = httpclient.execute(httpput);

            Header[] hdr = response.getAllHeaders();
            System.out.println("Headers : " + hdr.length);
            for (int i = 0; i < hdr.length; i++) {
                System.out.println(hdr[i]);
            }
            System.out.println("---------");
            System.out.println(response.getProtocolVersion());
            System.out.println(response.getStatusLine().getStatusCode());
            Assert.assertEquals(200, response.getStatusLine().getStatusCode());

            System.out.println(response.getStatusLine().getReasonPhrase());
            System.out.println(response.getStatusLine().toString());
            System.out.println("---------");

        } catch (Exception ex) {
            System.out.println(ex);
        }// exception
    }

    @Test
    public void test61_ObjectMove1() throws Exception {
        HelperClass.sleep(3000);
        HttpClient httpclient = new DefaultHttpClient();

        try {
            // Create the request
            HttpResponse response = null;
            HttpPut httpput = new HttpPut(
                    "http://localhost:8542/TestContainer21/TestContainer22/TestContainer23/TestObject6.txt");
            httpput.setHeader("Content-Type", "application/cdmi-object");
            httpput.setHeader("X-CDMI-Specification-Version", "1.0.2");
            httpput.setHeader("Authorization", "Basic " + credentials);
            String respStr = "{\n";
            respStr = respStr + "\"mimetype\" : \"" + "text/plain" + "\",\n";
            respStr = respStr + "\"move\" : \"" + "/TestContainer21/TestContainer22/TestObject6.txt" + "\",\n";
            respStr = respStr + "\"metadata\" : {" + "\"color\" : \"orange\", \"test\" : \"Test2\"" + "}\n";
            respStr = respStr + "}\n";
            System.out.println(respStr);
            StringEntity entity = new StringEntity(respStr);
            httpput.setEntity(entity);
            response = httpclient.execute(httpput);

            Header[] hdr = response.getAllHeaders();
            System.out.println("Headers : " + hdr.length);
            for (int i = 0; i < hdr.length; i++) {
                System.out.println(hdr[i]);
            }
            System.out.println("---------");
            System.out.println(response.getProtocolVersion());
            System.out.println(response.getStatusLine().getStatusCode());
            Assert.assertEquals(200, response.getStatusLine().getStatusCode());

            System.out.println(response.getStatusLine().getReasonPhrase());
            System.out.println(response.getStatusLine().toString());
            System.out.println("---------");

        } catch (Exception ex) {
            System.out.println(ex);
        }// exception
    }

    @Test
    public void test70_ObjectUpdate() throws Exception {
        HelperClass.sleep(3000);
        HttpClient httpclient = new DefaultHttpClient();

        try {
            // Create the request
            HttpResponse response = null;
            HttpPut httpput = new HttpPut(
                    "http://localhost:8542/TestContainer2/TestObject.txt");
            httpput.setHeader("Content-Type", "application/cdmi-object");
            httpput.setHeader("X-CDMI-Specification-Version", "1.0.2");
            httpput.setHeader("Authorization", "Basic " + credentials);
            String respStr = "{\n";
            respStr = respStr + "\"mimetype\" : \"" + "text/plain" + "\",\n";
            respStr = respStr + "\"value\" : \"" + "This is a new test" + "\",\n";
            respStr = respStr + "\"metadata\" : {" + "\"color\" : \"orange\", \"test\" : \"Test2\"" + "}\n";
            respStr = respStr + "}\n";
            System.out.println(respStr);
            StringEntity entity = new StringEntity(respStr);
            httpput.setEntity(entity);
            response = httpclient.execute(httpput);

            Header[] hdr = response.getAllHeaders();
            System.out.println("Headers : " + hdr.length);
            for (int i = 0; i < hdr.length; i++) {
                System.out.println(hdr[i]);
            }
            System.out.println("---------");
            System.out.println(response.getProtocolVersion());
            System.out.println(response.getStatusLine().getStatusCode());
            Assert.assertEquals(200, response.getStatusLine().getStatusCode());

            System.out.println(response.getStatusLine().getReasonPhrase());
            System.out.println(response.getStatusLine().toString());
            System.out.println("---------");

        } catch (Exception ex) {
            System.out.println(ex);
        }// exception
    }

    private static String readFile(String path) throws IOException
    {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, "UTF-8");
    }
}
