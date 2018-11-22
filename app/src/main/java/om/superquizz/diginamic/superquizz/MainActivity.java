package om.superquizz.diginamic.superquizz;

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

import java.util.List;

import om.superquizz.diginamic.superquizz.dao.QuestionMemDao;
import om.superquizz.diginamic.superquizz.database.QuestionDatabase;
import om.superquizz.diginamic.superquizz.model.Question;
import om.superquizz.diginamic.superquizz.ui.NewQuestionFragment;
import om.superquizz.diginamic.superquizz.ui.QuestionsFragment;
import om.superquizz.diginamic.superquizz.ui.ScoreFragment;
import om.superquizz.diginamic.superquizz.ui.SettingsFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, QuestionsFragment.OnListFragmentInteractionListener, NewQuestionFragment.OnCreateQuestionListener
 {
     QuestionMemDao dao;
     Question toDeleteItem;
     final Context context = this;

     public void onQuestionCreated(Question q) {
         QuestionDatabase.getInstance(this).addQuestion(q);
         dao.save(q);
     }

    public void onListQuestionClick(Question item) {
        Intent i = new Intent(MainActivity.this,QuestionActivity.class);

        i.putExtra("question",item);
        startActivity(i);
    }

     public void onListQuestionLongClick(Question item) {

         toDeleteItem = item;

         AlertDialog.Builder builder;

         builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
         builder.setTitle("Supprimer la question")
                 .setMessage("Êtes-vous sûr ?")
                 .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                     @Override
                     public void onClick(DialogInterface dialog, int which) {
                         dao.delete(toDeleteItem);
                         QuestionDatabase.getInstance(context).deleteQuestion(toDeleteItem);
                         FragmentManager fragmentManager = getSupportFragmentManager();
                         FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                         startQuestion(fragmentTransaction);
                     }
                 });

         builder.show();
     }

     private void initializeListQuestions() {
        // Initialisation des questions par défaut

         List<Question> questions = QuestionDatabase.getInstance(this).getAllQuestions();

         dao = new QuestionMemDao();

         for (Question quest : questions) {
             dao.save(quest);
         }
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

        Bundle bundle = new Bundle();
        bundle.putParcelable("dao", dao);
        fragment.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @SuppressWarnings("StatementWithEmptyBody")
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
            NewQuestionFragment fragment = new NewQuestionFragment();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
