package om.superquizz.diginamic.superquizz.api;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import om.superquizz.diginamic.superquizz.model.Question;

public class APIClient {

    static final String API_URL = "http://192.168.10.38";
    static final String API_PORT = "3000";
    static final String GET_QUEST_URI = "/questions";
    OkHttpClient client = new OkHttpClient();

    private static APIClient sInstance;

    static public APIClient getInstance() {
        if (sInstance == null) {
            sInstance = new APIClient();
        }
        return sInstance;
    }

    public void getQuestions(final APIResult<List<Question>> result) {

        Request req = new Request.Builder()
                .url(API_URL + ":" + API_PORT + GET_QUEST_URI)
                .build();

        client.newCall(req).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("failure", e.getMessage());
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        String responseData = response.body().string();
                        JSONObject answer = new JSONObject(responseData);
                        JSONArray jArray = answer.getJSONArray(responseData);

                        jArray.getJSONObject(0);

                        List<Question> questions = new ArrayList<Question>();

                        for (int i = 0 ; i < jArray.length() ; i++) {

                            JSONObject jsonQuestion = jArray.getJSONObject(i);

                            int id = jsonQuestion.getInt("id");
                            String intitule = jsonQuestion.getString("title");
                            String firstAnswer = jsonQuestion.getString("answer_1");
                            String secondAnswer = jsonQuestion.getString("answer_2");
                            String thirdAnswer = jsonQuestion.getString("answer_3");
                            String fourthAnswer = jsonQuestion.getString("answer_4");
                            int correctAnswer = jsonQuestion.getInt("correct_answer");
                            String authorImage = jsonQuestion.getString("author_img_url");
                            String author = jsonQuestion.getString("author");

                            questions.add(new Question(id, intitule, firstAnswer,
                                    secondAnswer, thirdAnswer, fourthAnswer, correctAnswer));
                        }

                    } catch (JSONException e) {
                        Log.e("apiclient", e.getMessage());
                    }
                }
            }
        });
    }

    public interface APIResult<T> {
        void onFailure(IOException e);
        void onSuccess(T object) throws  IOException;
    }
}
