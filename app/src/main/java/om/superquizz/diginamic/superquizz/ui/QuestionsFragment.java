package om.superquizz.diginamic.superquizz.ui;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import om.superquizz.diginamic.superquizz.R;
import om.superquizz.diginamic.superquizz.api.APIClient;
import om.superquizz.diginamic.superquizz.dao.QuestionMemDao;
import om.superquizz.diginamic.superquizz.database.QuestionDatabase;
import om.superquizz.diginamic.superquizz.model.Question;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */

public class QuestionsFragment extends Fragment {

    private int mColumnCount = 1;
    private OnListFragmentInteractionListener listener;
    private MyQuestionsFragmentRecyclerViewAdapter adapter;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public QuestionsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        View view = inflater.inflate(R.layout.fragment_questionsfragment_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            adapter = new MyQuestionsFragmentRecyclerViewAdapter(QuestionDatabase.getInstance(getActivity()).getAllQuestions(), listener);
            recyclerView.setAdapter(adapter);
        }
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        reloadDataFromServer();
    }

    private void reloadDataFromServer() {
        APIClient.getInstance().getQuestions(new APIClient.APIResult<List<Question>>() {
            @Override
            public void onAPIGetQuestionsFail(IOException e) {

                Toast.makeText(getActivity(), "Cannot get questions.",
                        Toast.LENGTH_LONG).show();

            }

            @Override
            public void onAPIGetQuestions(final List<Question> object) throws IOException {

                QuestionDatabase.getInstance(getContext()).synchroniseDatabaseWithServerItems(object);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (adapter != null) {
                            adapter.updateListWithQuestions(object);
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            listener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    // Implémentation de l'interface pour communication avec activity "parente

    public interface OnListFragmentInteractionListener {
        void onListQuestionClick(Question item);
        void onListQuestionLongClick(Question item);
    }
}
