package om.superquizz.diginamic.superquizz.ui.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;

import om.superquizz.diginamic.superquizz.R;
import om.superquizz.diginamic.superquizz.api.APIClient;
import om.superquizz.diginamic.superquizz.database.QuestionDatabase;
import om.superquizz.diginamic.superquizz.model.Question;
import om.superquizz.diginamic.superquizz.ui.Fragment.NewQuestionFragment;
import om.superquizz.diginamic.superquizz.ui.Fragment.QuestionsFragment;
import om.superquizz.diginamic.superquizz.ui.Fragment.ScoreFragment;
import om.superquizz.diginamic.superquizz.ui.Fragment.SettingsFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, QuestionsFragment.OnListFragmentInteractionListener, NewQuestionFragment.OnCreateQuestionListener
 {
     Question onLongClickItem;
     final Context context = this;

     public void onQuestionCreated(Question q) {
         QuestionDatabase.getInstance(this).addQuestion(q);
         APIClient.getInstance().addQuestion(q);
     }

    public void onListQuestionClick(Question item) {
        Intent i = new Intent(MainActivity.this,QuestionActivity.class);

        i.putExtra("question",item);
        startActivity(i);
    }

     public void onListQuestionLongClick(Question item) {

         onLongClickItem = item;

         AlertDialog.Builder builder;

         builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
         builder.setTitle(getResources().getString(R.string.delete_or_edit_popup))
                 .setNegativeButton(getResources().getString(R.string.delete), new DialogInterface.OnClickListener() {
                     @Override
                     public void onClick(DialogInterface dialog, int which) {
                         QuestionDatabase.getInstance(context).deleteQuestion(onLongClickItem);
                         FragmentManager fragmentManager = getSupportFragmentManager();
                         FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                         startQuestion(fragmentTransaction);
                     }
                 })
                 .setPositiveButton(getResources().getString(R.string.edit), new DialogInterface.OnClickListener() {
                     @Override
                     public void onClick(DialogInterface dialog, int which) {
                         FragmentManager fragmentManager = getSupportFragmentManager();
                         FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                         newOrEditQuestion(fragmentTransaction, onLongClickItem);
                     }
                 });

         builder.show();
     }

     private void initializeListQuestions() {
        // Initialisation des questions par défaut
     }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        initializeListQuestions();

        // Launch default fragment

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        startQuestion(fragmentTransaction);

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
        getMenuInflater().inflate(R.menu.main, menu);
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

    private void startQuestion(FragmentTransaction fragmentTransaction) {

        QuestionsFragment fragment = new QuestionsFragment();

        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void newOrEditQuestion(FragmentTransaction fragmentTransaction, Question item) {
        NewQuestionFragment fragment = new NewQuestionFragment();

        if (item != null) {
            Bundle bundle = new Bundle();
            bundle.putParcelable("question", item);
            fragment.setArguments(bundle);
        }

        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = ((FragmentManager) fragmentManager).beginTransaction();

        if (id == R.id.nav_info) {

            Intent intent = new Intent(this, InfoActivity.class);
            startActivity(intent);

            // Handle the camera action
        } else if (id == R.id.nav_play) {

            startQuestion(fragmentTransaction);

        } else if (id == R.id.nav_score) {

            ScoreFragment fragment = new ScoreFragment();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        } else if (id == R.id.nav_manage) {
            SettingsFragment fragment = new SettingsFragment();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else if (id == R.id.new_question) {

            newOrEditQuestion(fragmentTransaction, null);

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
 }
