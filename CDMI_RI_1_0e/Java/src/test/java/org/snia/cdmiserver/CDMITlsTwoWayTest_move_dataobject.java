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

import java.io.File;
import java.io.FileInputStream;
import org.apache.http.HttpResponse;
import org.apache.http.Header;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import java.security.KeyStore;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

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

public class CDMITlsTwoWayTest_move_dataobject {
    private final static String KEYSTORE = "/certs/client/keystore.jks";
    private final static String KEYSTORE_PASSWORD = "test123";
    private final static String KEYSTORE_TYPE = "JKS";
    private final static String TRUSTSTORE = "/certs/client/truststore.jks";
    private final static String TRUSTSTORE_PASSWORD = "test123";
    private final static String TRUSTSTORE_TYPE = "JKS";
    private final static String DATAOBJECT_OBJECTID_FROM = "0000053F0028003400000000000000000000000000000000D42AE8F7BD7D49E8A5A502DEB904E42D"; //it is a 80 characters hex number
    private final static String DATAOBJECT_PATH_TO = "/TestContainer2/TestObject.txt";

    static {
        System.setProperty("javax.net.debug", "ssl,handshake,record");
    }

    public static class HelperClass {
        public static void sleep(long ms) {
            //try {
            //    Thread.sleep(ms);
            //} catch (InterruptedException ex) {
            //    Logger.getLogger(CDMITlsTwoWayTest.class.getName()).log(Level.SEVERE, null, ex);
            //}
        }
    }

    @Test
    public void testObjectMove() throws Exception {
        HelperClass.sleep(3000);
        HttpClient httpclient;

        try {
            KeyStore keystore = KeyStore.getInstance(KEYSTORE_TYPE);
            FileInputStream keystoreInput = new FileInputStream(new File(KEYSTORE));
            keystore.load(keystoreInput, KEYSTORE_PASSWORD.toCharArray());
            KeyStore truststore = KeyStore.getInstance(TRUSTSTORE_TYPE);
            FileInputStream truststoreIs = new FileInputStream(new File(TRUSTSTORE));
            truststore.load(truststoreIs, TRUSTSTORE_PASSWORD.toCharArray());
            SSLSocketFactory socketFactory = new SSLSocketFactory(keystore, KEYSTORE_PASSWORD, truststore);
            Scheme scheme = new Scheme("https", 8543, socketFactory);
            SchemeRegistry registry = new SchemeRegistry();
            registry.register(scheme);
            ClientConnectionManager ccm = new PoolingClientConnectionManager(registry);
            httpclient = new DefaultHttpClient(ccm);

            // Create the request
            HttpResponse response = null;
            HttpPut httpput = new HttpPut(
                    "https://localhost:8543" + DATAOBJECT_PATH_TO);
            httpput.setHeader("Content-Type", "application/cdmi-object");
            httpput.setHeader("X-CDMI-Specification-Version", "1.0.2");
            String respStr = "{\n";
            respStr = respStr + "\"mimetype\" : \"" + "text/plain" + "\",\n";
            respStr = respStr + "\"move\" : \"" + "/cdmi_objectid/" + DATAOBJECT_OBJECTID_FROM + "\",\n";
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
}
