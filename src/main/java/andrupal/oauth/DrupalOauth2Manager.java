package andrupal.oauth;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.squareup.okhttp.OkHttpClient;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RequestInterceptor;
import retrofit.RetrofitError;
import retrofit.client.Client;
import retrofit.client.OkClient;
import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Path;
import retrofit.http.Streaming;
import retrofit.mime.TypedFile;

import proguard.annotation.Keep;
import proguard.annotation.KeepClassMembers;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.GeneralSecurityException;
import java.security.cert.X509Certificate;
import java.io.IOException;
import java.security.SecureRandom;
import andrupal.oauth.DrupalOauth2.AccessToken;
import andrupal.Log8;
import android.net.Uri;

public class DrupalOauth2Manager {
    protected String endpoint;
    protected String clientId;
    protected String clientSecret;
    protected String responseType = "code";
    protected String state = "null"; // modify here
    protected String grantType = "authorization_code";
    protected String cookie;

    public static class Builder {
        String endpoint;
        String clientId;
        String clientSecret;
        String cookie;

        public Builder() {
        }

        public Builder setEndpoint(String endpoint) {
            this.endpoint = endpoint;
            return this;
        }

        public Builder setClientId(String clientId) {
            this.clientId = clientId;
            return this;
        }

        public Builder setClientSecret(String clientSecret) {
            this.clientSecret = clientSecret;
            return this;
        }

        public Builder setCookie(String cookie) {
            this.cookie = cookie;
            return this;
        }

        public DrupalOauth2Manager build() {
            DrupalOauth2Manager manager = new DrupalOauth2Manager(endpoint, clientId, clientSecret);
            manager.setCookie(cookie);
        }
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    private DrupalOauth2 mService;

    public DrupalOauth2Manager(String endpoint) {
        this(endpoint, null, null);
    }

    public DrupalOauth2Manager(String endpoint, String clientId) {
        this(endpoint, clientId, null);
    }

    private boolean allTrust = true;

    public void disableAllTrust() {
        allTrust = false;
    }

    public void allTrust() {
        allTrust = true;
    }

    public void getAccessToken(final Callback<AccessToken> callback) {
        Log8.d(clientId);
        Log8.d(clientSecret);
        Log8.d(responseType);
        Log8.d(state);
        mService.authorize(
            clientId,
            clientSecret,
            responseType,
            state,
            new Callback<Response>() {
                @Override
                public void success(Response response, Response response2) {
                    Log8.d();
                    Uri uri = Uri.parse(response.getUrl());
                    String code = uri.getQueryParameter("code");
                    if (android.text.TextUtils.isEmpty(code)) {
                        callback.failure(RetrofitError.unexpectedError(response.getUrl(), new RuntimeException()));
                    } else {
                        mService.token(code, clientId, clientSecret, grantType, state, callback);
                    }
                }
                @Override
                public void failure(RetrofitError error) {
                    Log8.d(error);
                }
        });
    }

    public DrupalOauth2Manager(String endpoint, String clientId, String clientSecret) {
        setEndpoint(endpoint);
        setClientId(clientId);
        setClientSecret(clientSecret);

        OkHttpClient okHttpClient = new OkHttpClient();

        if (allTrust) {
            okHttpClient.setSslSocketFactory(getTrustedFactory());
            okHttpClient.setHostnameVerifier(getTrustedVerifier());
        }

        okHttpClient.setFollowSslRedirects(true);
        Client client = new OkClient(okHttpClient);

        if (mRequestInterceptor == null) {
            mRequestInterceptor = new SimpleRequestInterceptor();
        }
        mRequestInterceptor.setCookie(cookie);

        RestAdapter restAdapter = new RestAdapter.Builder()
            .setEndpoint(endpoint)
            .setRequestInterceptor(mRequestInterceptor)
            .setErrorHandler(new ErrorHandler())
            .setClient(client)
            .setConverter(new retrofit.converter.JacksonConverter())
            .build();

        mService = restAdapter.create(DrupalOauth2.class);
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
        if (mRequestInterceptor == null) {
            mRequestInterceptor = new SimpleRequestInterceptor();
        }

        mRequestInterceptor.setCookie(cookie);
    }

    class SimpleRequestInterceptor implements RequestInterceptor {
        private String cookie;

        public void setCookie(String cookie) {
            this.cookie = cookie;
        }

        @Override
        public void intercept(RequestFacade request) {
            if (!android.text.TextUtils.isEmpty(cookie)) {
                request.addHeader("Cookie", cookie);
            }
        }
    }

    protected SimpleRequestInterceptor mRequestInterceptor;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ErrorHandler implements retrofit.ErrorHandler {
        public ErrorHandler() {}

        @Override
        public Throwable handleError(RetrofitError cause) {
            cause.printStackTrace();
            return cause;
        }
    }

    private static SSLSocketFactory sTrustedFactory;

    public static SSLSocketFactory getTrustedFactory() {
        if (sTrustedFactory == null) {
            final TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {

                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }

                public void checkClientTrusted(X509Certificate[] chain, String authType) {
                    // Intentionally left blank
                }

                public void checkServerTrusted(X509Certificate[] chain, String authType) {
                    // Intentionally left blank
                }
            } };
            try {
                SSLContext context = SSLContext.getInstance("TLS");
                context.init(null, trustAllCerts, new SecureRandom());
                sTrustedFactory = context.getSocketFactory();
            } catch (GeneralSecurityException e) {
                IOException ioException = new IOException(
                        "Security exception configuring SSL context");
                ioException.initCause(e);
                e.printStackTrace();
            }
        }
        return sTrustedFactory;
    }

    private static HostnameVerifier sTrustedVerifier;

    public static HostnameVerifier getTrustedVerifier() {
        if (sTrustedVerifier == null) {
            sTrustedVerifier = new HostnameVerifier() {

                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };
        }

        return sTrustedVerifier;
    }
}
