package de.jasineri.common.util.jws.axis2;

import org.apache.axis2.client.Options;
import org.apache.axis2.java.security.TrustAllTrustManager;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.axis2.transport.http.security.SSLProtocolSocketFactory;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import java.security.SecureRandom;

/**
 * This utility provides functionality to trust all (self signed) X509 certificates and
 * hostname verification while using the SSL over the HTTP protocol.
 * <p/>
 * WARNING for development/testing purposes only!
 * <p/>
 * Usage: {@code Axis2SslTrustHelper.makeAxis2WebServiceClientTrustEveryone(stub)}
 * <p/>
 *
 * @since Axis2 v1.7.8
 */
public class Axis2SslTrustHelper {

    private Axis2SslTrustHelper() {
        throw new AssertionError();
    }

    public static void makeAxis2WebServiceClientTrustEveryone(Options stubServiceClientOptions) {
        try {
            SSLContext sslCtx = SSLContext.getInstance("SSL");
            sslCtx.init(null, new TrustManager[]{new TrustAllTrustManager()}, new SecureRandom());
            stubServiceClientOptions.setProperty(
                    HTTPConstants.CUSTOM_PROTOCOL_HANDLER,
                    new Protocol("https", (ProtocolSocketFactory) new SSLProtocolSocketFactory(sslCtx), 443));
        } catch (Exception ex) {
//            ex.printStackTrace();
        }
    }

}
