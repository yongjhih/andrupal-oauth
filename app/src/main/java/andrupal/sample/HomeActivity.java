/*
 * Copyright (C) 2014 Antonio Leiva Gordillo.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package andrupal.oauth.sample;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;

import andrupal.oauth.DrupalOauth2Manager;
import andrupal.oauth.DrupalOauth2.AccessToken;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.client.Header;
import retrofit.mime.TypedFile;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.InjectViews;
import butterknife.OnClick;
import butterknife.OnItemClick;
import butterknife.OnLongClick;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Map;
import android.net.Uri;
import andrupal.Log8;
import andrupal.WebDialog;

public class HomeActivity extends ToolBarActivity {
    @InjectView(R.id.endpoint)
    EditText endpoint;
    @InjectView(R.id.client_id)
    EditText clientId;
    @InjectView(R.id.client_secret)
    EditText clientSecret;
    @InjectView(R.id.username)
    EditText username;
    @InjectView(R.id.password)
    EditText password;
    DrupalOauth2Manager drupalOauth2Manager;

    @OnClick(R.id.sign)
    public void sign() {
        drupalOauth2Manager = new DrupalOauth2Manager.Builder(this)
            .setEndpoint(endpoint.getText().toString())
            .setClientId(clientId.getText().toString())
            .setClientSecret(clientSecret.getText().toString())
            .build();

        drupalOauth2Manager.getAccessToken(username.getText().toString(), password.getText().toString(), new Callback<AccessToken>() {
            @Override
            public void success(AccessToken accessToken, Response response) {
                Log8.d(accessToken);
                Log8.d(accessToken.access_token);
                //accessToken.access_token;
                Toast.makeText(HomeActivity.this, "success", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void failure(RetrofitError error) {
                Log8.d();
                Toast.makeText(HomeActivity.this, "failure: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setActionBarIcon(R.drawable.ic_ab_drawer);

        drawer = (DrawerLayout) findViewById(R.id.drawer);
        drawer.setDrawerShadow(R.drawable.drawer_shadow, Gravity.START);
    }

    @Override protected int getContentView() {
        return R.layout.activity_home;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawer.openDrawer(Gravity.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
