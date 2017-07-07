package com.base.application.baseapplication.jncax.coordinatorlayout;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.base.application.baseapplication.R;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created by chenaxing on 2016/12/23.
 */

public class DrawerLayoutWithNestedSrcollViewActivity extends AppCompatActivity
{
    private Toolbar toolbar;
    private ActionBarDrawerToggle drawerToggle;
    private DrawerLayout drawerLayout;
    private LinearLayout drawerView;
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acjn_activity_tablayout_nestedscrollview);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerView = (LinearLayout) findViewById(R.id.drawer_view);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setTitle("测试");
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.drawable.acjn_service_medical_icon);
        // 设置返回键可用
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.acjn_open, R.string.acjn_close);
        drawerToggle.syncState();
        drawerLayout.setDrawerListener(drawerToggle);

        Button btn = (Button) findViewById(R.id.open_drawer);
        btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                drawerLayout.openDrawer(drawerView);
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }
}
