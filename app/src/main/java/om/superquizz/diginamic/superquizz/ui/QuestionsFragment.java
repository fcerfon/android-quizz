package om.superquizz.diginamic.superquizz.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import om.superquizz.diginamic.superquizz.R;
import om.superquizz.diginamic.superquizz.model.Question;
import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */

public class QuestionsFragment extends Fragment {

    private List<Question> questionList;

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public QuestionsFragment() {
    }

    private int getNextInt() {
        Question lastQuestion = questionList.get(questionList.size() - 1);

        if (lastQuestion != null) {
            return lastQuestion.getId() + 1;
        }
        return 1;
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static QuestionsFragment newInstance(int columnCount) {
        QuestionsFragment fragment = new QuestionsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    private void initializeQuestionList() {

        // Initialisation des questions par défaut

        questionList = new ArrayList<>();
        questionList.add(new Question(1,"Quelle est la capitale de la France",
                "Londres", "Paris", "Bruxel",
                "Strasbourg", 1));

        questionList.add(new Question(2,"Quel type de chat est le plus moche ?",
                "Chat de gouttière", "Chat siamois",
                "Chat breton","Chat sans poil", 3));

        questionList.add(new Question(3,"Ils ont des chapeaux ronds vive ...",
                "les bretons", "la pluie", "les parigos",
                "les nantais", 0));

        questionList.add(new Question(4,"Le délicieux gateau au beurre s'écrit ...",
                "Kouign amant", "Kouignaman", "Kouign amann",
                "Kouignamant", 2));
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

            initializeQuestionList();
            recyclerView.setAdapter(new MyQuestionsFragmentRecyclerViewAdapter(questionList, mListener));
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Question item);
    }
}
