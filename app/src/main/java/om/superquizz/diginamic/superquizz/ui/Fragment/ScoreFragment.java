package om.superquizz.diginamic.superquizz.ui.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.MPPointF;

import java.util.ArrayList;

import om.superquizz.diginamic.superquizz.R;
import om.superquizz.diginamic.superquizz.database.QuestionDatabase;

public class ScoreFragment extends Fragment {

    private PieChart chart;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_score, container, false);

        chart = v.findViewById(R.id.chart1);

        chart.setUsePercentValues(true);
        chart.getDescription().setEnabled(false);
        chart.setExtraOffsets(5, 10, 5, 5);

        chart.setDragDecelerationFrictionCoef(0.95f);

        chart.setDrawHoleEnabled(true);
        chart.setHoleColor(Color.WHITE);

        chart.setTransparentCircleColor(Color.WHITE);
        chart.setTransparentCircleAlpha(110);

        chart.setHoleRadius(10f);
        chart.setTransparentCircleRadius(61f);

        chart.setRotationAngle(0);
        chart.setRotationEnabled(true);

        chart.animateY(1400, Easing.EaseInOutQuad);

        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        chart.setEntryLabelColor(Color.BLACK);
        chart.setEntryLabelTextSize(12f);

        updateChart();

        return v;
    }

    private void updateChart() {

        long answeredQuestionsNumber = QuestionDatabase.getInstance(getContext()).getAnsweredQuestionNumber();
        long successfullyAnsweredQuestionsNumber = QuestionDatabase.getInstance(getContext()).getSuccessfulQuestionsNumber();
        long allQuestionCount = QuestionDatabase.getInstance(getContext()).getAllQuestions().size();
        long wronglyAnsweredQuestionsNumber = answeredQuestionsNumber - successfullyAnsweredQuestionsNumber;
        long unansweredQuestions = allQuestionCount - wronglyAnsweredQuestionsNumber - successfullyAnsweredQuestionsNumber;

        ArrayList<PieEntry> questionEntries = new ArrayList<>();

        questionEntries.add(new PieEntry((float)successfullyAnsweredQuestionsNumber/(float)(allQuestionCount),getResources().getString(R.string.good_answer_piechart)));
        questionEntries.add(new PieEntry((float)wronglyAnsweredQuestionsNumber/(float)(allQuestionCount),getResources().getString(R.string.bad_answer_piechart)));
        questionEntries.add(new PieEntry((float)unansweredQuestions/(float)(allQuestionCount), getResources().getString(R.string.unanswered_question_piechart)));

        PieDataSet dataSet = new PieDataSet(questionEntries, getResources().getString(R.string.score_piechart));

        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);

        ArrayList<Integer> colors = new ArrayList<>();

        colors.add(Color.BLUE);
        colors.add(Color.RED);
        colors.add(Color.GRAY);

        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.BLACK);
        chart.setData(data);

        chart.highlightValues(null);
        chart.invalidate();
    }

}