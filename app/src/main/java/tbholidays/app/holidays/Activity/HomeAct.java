package tbholidays.app.holidays.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import tbholidays.app.holidays.Fragments.AboutUs;
import tbholidays.app.holidays.Fragments.AddUser;
import tbholidays.app.holidays.Fragments.AgentUserList;
import tbholidays.app.holidays.Fragments.HomeFragment;
import tbholidays.app.holidays.Fragments.MyKitty;
import tbholidays.app.holidays.Fragments.MyReward;
import tbholidays.app.holidays.Fragments.Profile;
import tbholidays.app.holidays.Fragments.ReferAFriend;
import tbholidays.app.holidays.Fragments.ShowUser;
import tbholidays.app.holidays.Fragments.TermsCondition;
import tbholidays.app.holidays.Fragments.Transcation;
import tbholidays.app.holidays.Fragments.ViewAllKitty;
import tbholidays.app.holidays.R;
import tbholidays.app.holidays.Utils.MyPrefrences;

public class HomeAct extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //setTitle("Travel Pool");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);

        TextView name=(TextView)header.findViewById(R.id.name);
        TextView mobile=(TextView)header.findViewById(R.id.mobile);

//        if (MyPrefrences.getUserType(getApplicationContext()).equalsIgnoreCase("No")){
//            name.setText(MyPrefrences.getUSENAME(getApplicationContext()).toUpperCase()+" ("+"User"+")");
//        }
//        else{
//            name.setText(MyPrefrences.getUSENAME(getApplicationContext()).toUpperCase()+" ("+"Agent"+")");
//        }

        Menu menu = navigationView.getMenu();
        MenuItem nav_add = menu.findItem(R.id.nav_view_add_user);
        MenuItem nav_show = menu.findItem(R.id.nav_view_show_user);

        if (getIntent().getStringExtra("userType").equals("user")){
            name.setText(MyPrefrences.getUSENAME(getApplicationContext()).toUpperCase()+"  (User)");
            nav_add.setVisible(false);
            nav_show.setVisible(false);

        }

        else if (getIntent().getStringExtra("userType").equals("agent")){
            name.setText(MyPrefrences.getUSENAME(getApplicationContext()).toUpperCase()+"  (Agent)");
            nav_add.setVisible(true);
            nav_show.setVisible(true);
        }


        mobile.setText(MyPrefrences.getMobile(getApplicationContext()));

        Fragment fragment = new HomeFragment();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.replace(R.id.content_frame, fragment).commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_Home) {

            Fragment fragment = new HomeFragment();
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.content_frame, fragment).addToBackStack(null).commit();

        } else if (id == R.id.nav_profile) {

            startActivity(new Intent(HomeAct.this,ProfileAct.class));


//            Fragment fragment = new Profile();
//            FragmentManager manager = getSupportFragmentManager();
//            FragmentTransaction ft = manager.beginTransaction();
//            ft.replace(R.id.content_frame, fragment).addToBackStack(null).commit();

        } else if (id == R.id.nav_my_kitty) {

            Fragment fragment = new MyKitty();
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.content_frame, fragment).addToBackStack(null).commit();

        } else if (id == R.id.nav_view_add_user) {



            startActivity(new Intent(HomeAct.this,AddUserAct.class));
//            Fragment fragment = new AddUser();
//            FragmentManager manager = getSupportFragmentManager();
//            FragmentTransaction ft = manager.beginTransaction();
//            ft.replace(R.id.content_frame, fragment).addToBackStack(null).commit();


        } else if (id == R.id.nav_view_show_user) {

        Fragment fragment = new ShowUser();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.replace(R.id.content_frame, fragment).addToBackStack(null).commit();


        } else if (id == R.id.nav_view_kitty) {

            Fragment fragment = new ViewAllKitty();
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.content_frame, fragment).addToBackStack(null).commit();

          } else if (id == R.id.nav_transcation) {

                    Fragment fragment = new Transcation();
                    FragmentManager manager = getSupportFragmentManager();
                    FragmentTransaction ft = manager.beginTransaction();
                    ft.replace(R.id.content_frame, fragment).addToBackStack(null).commit();

          } else if (id == R.id.nav_tremCondition) {

                    Fragment fragment = new TermsCondition();
                    FragmentManager manager = getSupportFragmentManager();
                    FragmentTransaction ft = manager.beginTransaction();
                    ft.replace(R.id.content_frame, fragment).addToBackStack(null).commit();
          } else if (id == R.id.nav_aboutus) {

                            Fragment fragment = new AboutUs();
                            FragmentManager manager = getSupportFragmentManager();
                            FragmentTransaction ft = manager.beginTransaction();
                            ft.replace(R.id.content_frame, fragment).addToBackStack(null).commit();

          } else if (id == R.id.nav_refer) {

                    Fragment fragment = new ReferAFriend();
                    FragmentManager manager = getSupportFragmentManager();
                    FragmentTransaction ft = manager.beginTransaction();
                    ft.replace(R.id.content_frame, fragment).addToBackStack(null).commit();

            } else if (id == R.id.nav_reward) {

                                Fragment fragment = new MyReward();
                                FragmentManager manager = getSupportFragmentManager();
                                FragmentTransaction ft = manager.beginTransaction();
                                ft.replace(R.id.content_frame, fragment).addToBackStack(null).commit();


        } else if (id == R.id.nav_logout) {
            MyPrefrences.resetPrefrences(getApplicationContext());
            Intent intent = new Intent(HomeAct.this, Login.class);
            startActivity(intent);
            finishAffinity();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
