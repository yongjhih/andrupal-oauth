andrupal-oauth2
===============

Usage
=====

* authorize & token with username and password
```java
DrupalOauth2Manager drupalOauth2Manager = new DrupalOauth2Manager.Builder().setEndpoint("https://example.com/oauth2").setClientId("id").setClientSecret("secret").setCookie("SESS=XXX;").build();

drupalOauth2Manager.getAccessToken("username", "password", new Callback<AccessToken>() {
    @Override
    public void success(AccessToken accessToken, Response response) {
        //accessToken.access_token;
    }
    @Override
    public void failure(RetrofitError error) {
    }
});
```

* authorize & token with cookie
```java
DrupalOauth2Manager drupalOauth2Manager = new DrupalOauth2Manager.Builder().setEndpoint("https://example.com/oauth2").setClientId("id").setClientSecret("secret").setCookie("SESS=XXX;").build();

drupalOauth2Manager.getAccessToken(new Callback<AccessToken>() {
    @Override
    public void success(AccessToken accessToken, Response response) {
        //accessToken.access_token;
    }
    @Override
    public void failure(RetrofitError error) {
    }
});
```

