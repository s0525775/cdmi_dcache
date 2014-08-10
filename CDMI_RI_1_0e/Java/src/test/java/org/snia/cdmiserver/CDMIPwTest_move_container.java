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

import org.apache.axiom.om.util.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.Header;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
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

public class CDMIPwTest_move_container {

    private final static String USERNAME = "test";
    private final static String PASSWORD = "test";
    private final static String CONTAINER_OBJECTID_FROM = "0000053F00280046000000000000000000000000000000006D651B3363FE41FDAB2E96CE7D54AC7C"; //it is a 80 characters hex number
    private final static String CONTAINER_PATH_TO = "TestContainer4";
    private final String credentials;

    public CDMIPwTest_move_container() {
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
    public void testContainerMove() throws Exception {
        HelperClass.sleep(3000);
        HttpClient httpclient = new DefaultHttpClient();

        try {
            // Create the request
            HttpResponse response = null;
            HttpPut httpput = new HttpPut("http://localhost:8542" + CONTAINER_PATH_TO);
            httpput.setHeader("Content-Type", "application/cdmi-container");
            httpput.setHeader("X-CDMI-Specification-Version", "1.0.2");
            httpput.setHeader("Authorization", "Basic " + credentials);
            httpput.setEntity(new StringEntity("{ \"move\" : \"/cdmi_objectid/" + CONTAINER_OBJECTID_FROM + "\", \"metadata\" : { \"color\" : \"red\", \"test\" : \"Test\" } }"));
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
}
