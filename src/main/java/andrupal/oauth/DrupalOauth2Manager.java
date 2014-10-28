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
import android.text.TextUtils;
import android.content.Context;
import andrupal.WebDialog;

public class DrupalOauth2Manager {
    protected String endpoint;
    protected String clientId;
    protected String clientSecret;
    //protected String responseType = "code";
    protected String state = "null"; // modify here
    //protected String grantType = "authorization_code";
    protected String cookie;

    /**
     * hybridauth_ulogin/hybridauth_ulogin.admin.inc
     *
     * "vkontakte" => "Vkontakte",
     * "odnoklassniki" => "Odnoklassniki",
     * "mailru" => "Mailru",
     * "facebook" => "Facebook",
     * "twitter" => "Twitter",
     * "google" => "Google",
     * "yandex" => "Yandex",
     * "livejournal" => "",
     * "openid" => "OpenID",
     * "lastfm" => "LastFM",
     * "linkedin" => "LinkedIn",
     * "liveid" => "Live",
     * "soundcloud" => "",
     * "steam" => "Steam",
     * "flickr" => "",
     * "vimeo" => "",
     * "youtube" => "",
     * "webmoney" => "",
     *
     * additional-providers/hybridauth-*/Providers/*
     *
     * px500
     * Deezer
     * Disqus
     * Draugiem
     * DrupalOAuth2
     * Freeagent
     * GitHub
     * Goodreads
     * Google
     * Identica
     * Instagram
     * LastFM
     * Latch
     * Mailru
     * Murmur
     * Odnoklassniki
     * PaypalOpenID
     * Paypal
     * PixelPin
     * Pixnet
     * Plurk
     * QQ
     * Sina
     * Skyrock
     * Steam
     * Tumblr
     * TwitchTV
     * Viadeo
     * Vimeo
     * Vkontakte
     * XING
     * Yahoo
     * Yammer
     * Yandex
     */
    public static final String DEEZER        = "Deezer";
    public static final String DISQUS        = "Disqus";
    public static final String DRAUGIEM      = "Draugiem";
    public static final String DRUPALOAUTH2  = "DrupalOAuth2";
    public static final String FACEBOOK      = "Facebook";
    public static final String FLICKR        = "flickr";
    public static final String FREEAGENT     = "Freeagent";
    public static final String GITHUB        = "GitHub";
    public static final String GOODREADS     = "Goodreads";
    public static final String GOOGLE        = "Google";
    public static final String IDENTICA      = "Identica";
    public static final String INSTAGRAM     = "Instagram";
    public static final String LASTFM        = "LastFM";
    public static final String LATCH         = "Latch";
    public static final String LINKEDIN      = "LinkedIn";
    public static final String LIVEJOURNAL   = "livejournal";
    public static final String LIVE          = "Live";
    public static final String MAILRU        = "Mailru";
    public static final String MURMUR        = "Murmur";
    public static final String ODNOKLASSNIKI = "Odnoklassniki";
    public static final String OPENID        = "OpenID";
    public static final String PAYPALOPENID  = "PaypalOpenID";
    public static final String PAYPAL        = "Paypal";
    public static final String PIXELPIN      = "PixelPin";
    public static final String PIXNET        = "Pixnet";
    public static final String PLURK         = "Plurk";
    public static final String PX500         = "px500";
    public static final String QQ            = "QQ";
    public static final String SINA          = "Sina";
    public static final String SKYROCK       = "Skyrock";
    public static final String SOUNDCLOUD    = "soundcloud";
    public static final String STEAM         = "Steam";
    public static final String TUMBLR        = "Tumblr";
    public static final String TWITCHTV      = "TwitchTV";
    public static final String TWITTER       = "Twitter";
    public static final String VIADEO        = "Viadeo";
    public static final String VIMEO         = "vimeo";
    public static final String VIMEO         = "Vimeo";
    public static final String VKONTAKTE     = "Vkontakte";
    public static final String WEBMONEY      = "webmoney";
    public static final String XING          = "XING";
    public static final String YAHOO         = "Yahoo";
    public static final String YAMMER        = "Yammer";
    public static final String YANDEX        = "Yandex";
    public static final String YOUTUBE       = "youtube";

    protected String provider = FACEBOOK;

    public static class Builder {
        String endpoint;
        String clientId;
        String clientSecret;
        String cookie;
        Context context;

        public Builder() {
        }

        public Builder(Context context) {
            this.context = context;
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
            DrupalOauth2Manager manager = new DrupalOauth2Manager(context, endpoint, clientId, clientSecret);
            manager.setCookie(cookie);
            return manager;
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

    /**
     * getAccessTokenByPassword
     */
    public void getAccessToken(String username, String password, Callback<AccessToken> callback) {
        mService.token(
            clientId,
            clientSecret,
            "password",
            state,
            username,
            password,
            callback
        );
    }

    public void getAccessToken(String cookie, final Callback<AccessToken> callback) {
        mRequestInterceptor.setCookie(cookie);

        final Callback authorizeCallback = new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                Log8.d();
                Uri uri = Uri.parse(response.getUrl());
                String code = uri.getQueryParameter("code");
                if (TextUtils.isEmpty(code)) {
                    callback.failure(RetrofitError.unexpectedError(response.getUrl(), new RuntimeException()));
                } else {
                    mService.token(code, clientId, clientSecret, "authorization_code", state, callback);
                }
            }
            @Override
            public void failure(RetrofitError error) {
                callback.failure(error);
                Log8.d(error);
            }
        };

        mService.authorize(
            clientId,
            clientSecret,
            "code",
            state,
            authorizeCallback
        );
    }

    public void getAccessToken(final Callback<AccessToken> callback) {
        getAccessToken(context, provider, token, callback);
    }

    public void getAccessToken(Context context, final Callback<AccessToken> callback) {
        getAccessToken(context, provider, token, callback);
    }

    public void getAccessToken(String provider, String token, final Callback<AccessToken> callback) {
        getAccessToken(context, provider, token, callback);
    }

    public void getAccessToken(Context context, String provider, String token, final Callback<AccessToken> callback) {
        requestHybridauthCookie(contextk, provider, token, new Callback<String>() {
            @Override
            public void success(String cookie, Response response) {
                Log8.d();
                getAccessToken(cookie, callback);
            }
            @Override
            public void failure(RetrofitError error) {
                Log8.d();
                callback.failure(error);
            }
        });
    }

    protected String token;

    public void setToken(String token) {
        this.token = token;
    }

    /**
     * https://github.com/yongjhih/drupal-hybridauth/commit/268b72a598665b0738e3b06e7b59dcb3bda5b999
     *
     * Allow sign-up with access_token.
     */
    private void requestHybridauthCookie(Context context, String provider, String token, Callback<String> callback) {
        if (context == null) return;
        if (TextUtils.isEmpty(token)) return;

        Uri uri = Uri.parse(endpoint);

        new WebDialog(context, uri.getScheme() + "://" + uri.getAuthority() + "/hybridauth/window/" + provider + "?destination=node&destination_error=node&access_token=" + token, callback).show();
    }

    protected Context context;

    public void setContext(Context context) {
        this.context = context;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public DrupalOauth2Manager(String endpoint, String clientId, String clientSecret) {
        this((Context) null, endpoint, clientId, clientSecret);
    }

    public DrupalOauth2Manager(Context context, String endpoint, String clientId, String clientSecret) {
        setContext(context);
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
