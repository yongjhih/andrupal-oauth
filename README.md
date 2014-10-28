andrupal-oauth
==============

![drupal](drupal-circle.png "drupal")

A drupal oauth2_server client for android.

![oauth](oauth_logo.png "oauth")

Usage
=====

* authorize & token with username and password
```java
DrupalOauth2Manager drupalOauth2Manager = new DrupalOauth2Manager.Builder().setEndpoint("https://example.com/oauth2").setClientId("id").setClientSecret("secret").build();

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
DrupalOauth2Manager drupalOauth2Manager = new DrupalOauth2Manager.Builder().setEndpoint("https://example.com/oauth2").setClientId("id").setClientSecret("secret").build();

drupalOauth2Manager.getAccessToken("cookie", new Callback<AccessToken>() {
    @Override
    public void success(AccessToken accessToken, Response response) {
        //accessToken.access_token;
    }
    @Override
    public void failure(RetrofitError error) {
    }
});
```

* authorize & token with other oauth provider's access token
```java
DrupalOauth2Manager drupalOauth2Manager = new DrupalOauth2Manager.Builder().setEndpoint("https://example.com/oauth2").setClientId("id").setClientSecret("secret").build();

drupalOauth2Manager.getAccessToken(context, DrupalOauth2Manager.FACEBOOK, "fb_access_token", new Callback<AccessToken>() {
    @Override
    public void success(AccessToken accessToken, Response response) {
        //accessToken.access_token;
    }
    @Override
    public void failure(RetrofitError error) {
    }
});
```

Misc
====

Works with drupal/oauth2_server for drupal, and bonus of other oauth provider with yongjhih/drupal-hybridauth ( https://github.com/yongjhih/drupal-hybridauth/commit/268b72a598665b0738e3b06e7b59dcb3bda5b999)

[License] (LICENSE)
===================

```
The MIT License (MIT)

Copyright (c) 2014 Andrew Chen

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
