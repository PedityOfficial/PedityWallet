package com.example.pwallet.peditywallet;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    Toolbar toolbar;
    private IntentIntegrator qrscan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Dashboard");
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        qrscan = new IntentIntegrator(this);

       DrawerLayout drawer = findViewById(R.id.drawer_layout);
       ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
              this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
       drawer.addDrawerListener(toggle);
       toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Fragment fragment = new HomeActivity();
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flContent, fragment);
            ft.commit();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_scan) {
            qrscan.setOrientationLocked(true);
            qrscan.setBeepEnabled(true);
            qrscan.setCaptureActivity(CaptureActivityPortrait.class);
            qrscan.setPrompt("Place PEDI QR code inside rectangle");
            qrscan.initiateScan();
            Toast toast =Toast.makeText(this, "Scan Address", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        Intent intent = new Intent(this, SendPage.class);
        if (result != null) {
            if (result.getContents() == null) {
                Toast toast =Toast.makeText(this, "Scan failed", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            } else {
                intent.putExtra("Dest_Acc_Id", result.getContents());
                Toast toast = Toast.makeText(this, "Scanned successfully", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }

            startActivity(intent);

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Fragment fragment = null;
        Bundle bundle = new Bundle();

        if (id == R.id.nav_home) {
            toolbar.setTitle("Dashboard");
            fragment = new HomeActivity();

        } else if (id == R.id.nav_send) {
            toolbar.setTitle("Send");
            fragment = new SendActivity();

        } else if (id == R.id.nav_receive) {
            toolbar.setTitle("Receive");
            fragment = new ReceiveActivity();
        } else if (id == R.id.nav_manage) {
            toolbar.setTitle("History");
            fragment = new HistoryActivity();

        } else if (id == R.id.nav_twitter) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.twitter.com/Pedity_official"));
            startActivity(browserIntent);
        } else if (id == R.id.nav_medium) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.medium.com/@Pedity"));
            startActivity(browserIntent);
        }
        else if (id == R.id.nav_github) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.github.com/PedityOfficial"));
            startActivity(browserIntent);
        }
        else if(id == R.id.nav_logout) {
            MySingleTon.getInstance().deleteAlias("SecretKeyAlias_PEDITY6");
            startActivity(new Intent(this, LoginActivity.class));
        }
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flContent, fragment);
            ft.commit();
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
