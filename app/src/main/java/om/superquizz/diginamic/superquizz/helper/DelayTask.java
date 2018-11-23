package om.superquizz.diginamic.superquizz.helper;

import android.os.AsyncTask;
import android.os.SystemClock;

import om.superquizz.diginamic.superquizz.QuestionActivity;

public class DelayTask extends AsyncTask<Void, Integer, String> {
    private final progressCallbackInterface listener;

    public DelayTask(QuestionActivity context) {
        super();
        listener = context;
    }

    @Override
    protected String doInBackground(Void... params) {
        int count = 0;
        while (count < 15) {
            SystemClock.sleep(1000);
            count++;
            publishProgress(count * 7);
        }
        publishProgress(100);
        listener.onTaskCompleted();
        return "Complete";
    }

    @Override
    protected void onProgressUpdate(Integer... values) {

        int progress = values[0];

        listener.onTaskProgress(progress);
    }

    @Override
    protected void onPreExecute() {
        listener.onTaskStarted();
    }

    public interface progressCallbackInterface {
        void onTaskStarted();
        void onTaskProgress(int progressValue);
        void onTaskCompleted();
    }
}
