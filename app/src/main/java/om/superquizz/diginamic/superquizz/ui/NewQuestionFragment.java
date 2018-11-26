package om.superquizz.diginamic.superquizz.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import om.superquizz.diginamic.superquizz.R;
import om.superquizz.diginamic.superquizz.helper.NetworkChangeReceiver;
import om.superquizz.diginamic.superquizz.model.Question;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewQuestionFragment.OnCreateQuestionListener} interface
 * to handle interaction events.
 * Use the {@link NewQuestionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewQuestionFragment extends Fragment {

    private NewQuestionFragment.OnCreateQuestionListener mListener;
    private CheckBox[] checkboxes;
    private TextView intitule;
    private EditText answer1;
    private EditText answer2;
    private EditText answer3;
    private EditText answer4;
    FloatingActionButton fab;

    // private OnFragmentInteractionListener mListener;

    public NewQuestionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewQuestionFragment.
     */
    public static NewQuestionFragment newInstance(String param1, String param2) {
        NewQuestionFragment fragment = new NewQuestionFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //registerReceiver();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_question, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        checkboxes = new CheckBox[4];

        checkboxes[0] = view.findViewById(R.id.new_question_check_1);
        checkboxes[1] = view.findViewById(R.id.new_question_check_2);
        checkboxes[2] = view.findViewById(R.id.new_question_check_3);
        checkboxes[3] = view.findViewById(R.id.new_question_check_4);

        for (final CheckBox checkBox : checkboxes) {
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    for (CheckBox singleCheckbox : checkboxes) {
                        singleCheckbox.setChecked(false);
                    }
                    buttonView.setChecked(isChecked);
                }
            });
        }

        intitule = view.findViewById(R.id.question_name);
        answer1 = view.findViewById(R.id.new_question_answer_1);
        answer2 = view.findViewById(R.id.new_question_answer_2);
        answer3 = view.findViewById(R.id.new_question_answer_3);
        answer4 = view.findViewById(R.id.new_question_answer_4);

        fab = view.findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (NetworkChangeReceiver.isOnline(getActivity())) {
                    // getting the good answer number

                    int i = 0;
                    while (i < 4) {
                        if (checkboxes[i].isChecked()) {
                            break;
                        }
                        i++;
                    }

                    if (answer1.getText().toString().equals("") || answer2.getText().toString().equals("")
                            || answer3.getText().toString().equals("") || answer4.getText().toString().equals("")) {
                        Toast.makeText(getActivity(), "Please put 4 propositions.",
                                Toast.LENGTH_LONG).show();
                    }
                    else if (i == 4) {
                        Toast.makeText(getActivity(), "Please select a good answer.",
                                Toast.LENGTH_LONG).show();
                    }
                    else if (intitule.getText().toString().equals("")) {
                        Toast.makeText(getActivity(), "Please put a question title.",
                                Toast.LENGTH_LONG).show();
                    }
                    else {
                        Question q = new Question(0, intitule.getText().toString(),
                                answer1.getText().toString(), answer2.getText().toString(),
                                answer3.getText().toString(), answer4.getText().toString(), i);

                        mListener.onQuestionCreated(q);
                    }
                }

                else {
                    Toast.makeText(getActivity(), "No connection.", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof OnCreateQuestionListener) {
            mListener = (OnCreateQuestionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnCreateQuestionListener {
        void onQuestionCreated(Question q);
    }

    @Override
    public void onResume() {
        super.onResume();
        registerReceiver();
        //getActivity().registerReceiver(this.internalNetworkChangeReceiver, new IntentFilter(NetworkChangeReceiver.NETWORK_CHANGE_ACTION));
    }

    private void registerReceiver() {
        try {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(NetworkChangeReceiver.NETWORK_CHANGE_ACTION);
            getActivity().registerReceiver(this.internalNetworkChangeReceiver, intentFilter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onPause() {
        try {
            getActivity().unregisterReceiver(internalNetworkChangeReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onPause();
    }

    InternalNetworkChangeReceiver internalNetworkChangeReceiver = new InternalNetworkChangeReceiver();
    class InternalNetworkChangeReceiver extends BroadcastReceiver
    {
        String connectionStatus = "";

        public void onReceive(Context context, Intent intent) {

            boolean isConnected = intent.getBooleanExtra("status", true);

            if (!isConnected) {
                if (!connectionStatus.equals("disconnected")) {
                    Toast.makeText(getActivity(), "Connection to server lost",
                            Toast.LENGTH_LONG).show();
                }
                connectionStatus = "disconnected";
            }
            else {
                if (!connectionStatus.equals("connected")) {
                    Toast.makeText(getActivity(), "Connection to server came back",
                            Toast.LENGTH_LONG).show();
                }
                connectionStatus = "connected";
            }
        }
    }
}
