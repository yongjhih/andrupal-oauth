andrupal-oauth2
===============

Usage
=====

```java
DrupalOauth2Manager drupalOauth2Manager = new DrupalOauth2Manager.Builder().setEndpoint("https://example.com/oauth2").setClientId("id").setClientSecret("secret").build();

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

* authorize (https://example.com/oauth2/authorize)

```java
DrupalOauth2 drupalOauth2 = new DrupalOauth2("https://example.com/oauth2", "client_id", "client_secret");

drupalOauth2.authorize(new Callback<Response>() {
    @Override
    public void success(Response response, Response response2) {
    }
    @Override
    public void failure(RetrofitError error) {
    }
});
```

* token (https://example.com/oauth2/token)

```java
DrupalOauth2 drupalOauth2 = new DrupalOauth2("https://example.com/oauth2", "client_id", "client_secret");

drupalOauth2.authorize(new Callback<AccessToken>() {
    @Override
    public void success(AccessToken access_token, Response response) {
    }
    @Override
    public void failure(RetrofitError error) {
    }
});
```
