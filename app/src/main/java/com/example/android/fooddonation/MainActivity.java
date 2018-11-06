package com.example.android.fooddonation;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final int NAV_MY_REQUESTS = R.id.nav_my_requests;
    public static final int NAV_MY_DONATIONS = R.id.nav_my_donations;
    public static final int NAV_REQUESTS = R.id.nav_requests;
    public static final int NAV_DONATIONS = R.id.nav_donations;
    private int selectedNav;
    private DrawerLayout drawer;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("donations");

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView textViewEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView=findViewById(R.id.list_donations);
        DividerItemDecoration dividerItemDecoration=new DividerItemDecoration(MainActivity.this, new LinearLayoutManager(MainActivity.this).getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(MainActivity.this, DonationActivity.class), 0);
            }
        });

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(NAV_DONATIONS);
        selectedNav=NAV_DONATIONS;
        progressBar=findViewById(R.id.progressBar_list);
        textViewEmpty=findViewById(R.id.text_list_empty);

    }


    @Override
    protected void onStart() {
        super.onStart();
        getData();
    }


    private void getData() {
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        Query finalReference=databaseReference.orderByChild("donation");
        switch (selectedNav){
            case NAV_DONATIONS:
                finalReference=databaseReference.orderByChild("donation").equalTo(true);
                break;
            case NAV_REQUESTS:
                finalReference=databaseReference.orderByChild("donation").equalTo(false);
                break;

        }

        finalReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Item> names=new ArrayList<>();
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Donation donation=snapshot.getValue(Donation.class);
                    Item item =new Item();
                    item.setFood(donation.getFood());
                    item.setId(snapshot.getKey());
                    item.setUserId(donation.getUserId());
                    names.add(item);
                }
                progressBar.setVisibility(View.GONE);
                recyclerView.setAdapter(new DonationAdapter(MainActivity.this, names));
                recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                if (names.isEmpty()){
                    textViewEmpty.setText("No items found");
                    textViewEmpty.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
                else {
                    textViewEmpty.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Database Error", databaseError.getMessage());

            }

        });
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
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivity(new Intent(this, LoginActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        selectedNav=id;
        switch (id){
            case NAV_DONATIONS:
                setTitle("Food Donations");
                getData();
                break;
            case NAV_REQUESTS:
                setTitle("Donation Requests");
                getData();
                break;
            case NAV_MY_DONATIONS:
                setTitle("My Donations");
                getData();
                break;
            case NAV_MY_REQUESTS:
                setTitle("My Donation Requests");
                getData();
                break;
        }


        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
