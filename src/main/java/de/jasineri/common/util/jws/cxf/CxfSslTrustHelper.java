package de.jasineri.common.util.jws.cxf;

import org.apache.cxf.configuration.jsse.TLSClientParameters;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.transport.http.HTTPConduit;

import javax.net.ssl.*;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * This utility provides functionality to trust all (self signed) X509 certificates and
 * hostname verification while using the SSL over the HTTP protocol.
 * <p/>
 * WARNING for development/testing purposes only!
 * <p/>
 * Usage: {@code CxfSslTrustHelper.makeCxfWebServiceClientTrustEveryone(port)}
 * <p/>
 *
 * @since CXF v3.2.5
 */
public class CxfSslTrustHelper {

    private CxfSslTrustHelper() {
        throw new AssertionError();
    }

    /**
     * @param port Proxy object
     */
    public static void makeCxfWebServiceClientTrustEveryone(Object port) {
        try {
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, new TrustManager[]{new NaiveX509TrustManager()}, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());

            HostnameVerifier hostnameVerifier = (hostname, session) -> true;
            HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);

            Client client = ClientProxy.getClient(port);
            HTTPConduit conduit = (HTTPConduit) client.getConduit();
            TLSClientParameters tlsParams = new TLSClientParameters();
            tlsParams.setUseHttpsURLConnectionDefaultSslSocketFactory(true);
            tlsParams.setUseHttpsURLConnectionDefaultHostnameVerifier(true);
            conduit.setTlsClientParameters(tlsParams);
        } catch (Exception ex) {
//            ex.printStackTrace();
        }
    }

    private static class NaiveX509TrustManager implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }
}
