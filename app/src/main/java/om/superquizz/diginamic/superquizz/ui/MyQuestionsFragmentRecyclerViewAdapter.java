package om.superquizz.diginamic.superquizz.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import om.superquizz.diginamic.superquizz.ui.QuestionsFragment.OnListFragmentInteractionListener;
import om.superquizz.diginamic.superquizz.R;

import java.util.List;
import om.superquizz.diginamic.superquizz.model.Question;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Question} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 */
public class MyQuestionsFragmentRecyclerViewAdapter extends RecyclerView.Adapter<MyQuestionsFragmentRecyclerViewAdapter.ViewHolder> {

    private  List<Question> questions;
    private final OnListFragmentInteractionListener mListener;

    public MyQuestionsFragmentRecyclerViewAdapter(List<Question> items, OnListFragmentInteractionListener listener) {
        questions = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_questionsfragment, parent, false);
        return new ViewHolder(view);
    }

    public void updateListWithQuestions(List<Question> newValues) {
        questions = newValues;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = questions.get(position);
        holder.mIdView.setText(String.valueOf(questions.get(position).getId()));

        String questionEntitled = holder.mItem.getIntitule();

        holder.mContentView.setText(questionEntitled);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onListQuestionClick(holder.mItem);
                }
            }
        });

        holder.mView.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {

                if (mListener != null) {
                    mListener.onListQuestionLongClick(holder.mItem);
                }
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final TextView mIdView;
        final TextView mContentView;
        Question mItem;

        ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.item_number);
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
