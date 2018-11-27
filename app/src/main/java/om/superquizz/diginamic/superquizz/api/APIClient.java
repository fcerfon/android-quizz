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
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import om.superquizz.diginamic.superquizz.model.Question;

public class APIClient {

    // API connectivity

    static final String API_LOCAL_URL = "http://192.168.10.189";
    static final String API_SERVER_URL = "http://192.168.10.38";
    static final String API_URL = API_LOCAL_URL;
    static final String API_PORT = "3000";
    static final String GET_QUEST_URI = "/questions";
    static final String POST_URI = "/questions";

    private final OkHttpClient client = new OkHttpClient();

    // POST values

    static final String AUTHOR_IMG_URL = "https://img.sheetmusic.direct/catalogue/contributor/e557d666-3595-4d82-8830-9cef343ab3a6/large.jpg";
    static final String AUTHOR_NAME = "Florent";

    // Singleton

    private static APIClient sInstance;

    static public APIClient getInstance() {
        if (sInstance == null) {
            sInstance = new APIClient();
        }
        return sInstance;
    }


    private String getValueIfExists(JSONObject jsonQuestion, String key) {
        try {
            if (jsonQuestion.has(key)) {
                return jsonQuestion.getString(key);
            }
        } catch (JSONException e) {
            Log.e("error", e.getMessage());
        }
        return "";
    }
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    String post(String url, String json) throws IOException {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public void addQuestion(Question q) {

        try {
            RequestBody formBody = new FormBody.Builder()
                    .add("title", q.getIntitule())
                    .add("answer_1", q.getFirstAnswer())
                    .add("answer_2", q.getSecondAnswer())
                    .add("answer_3", q.getThirdAnswer())
                    .add("answer_4", q.getFourthAnswer())
                    .add("correct_answer", Integer.toString(q.getGoodAnswer()))
                    .add("author_img_url", AUTHOR_IMG_URL)
                    .add("author", AUTHOR_NAME)
                    .build();

            OkHttpClient client = new OkHttpClient.Builder()
                    .build();
            Request request = new Request.Builder()
                    .url(API_URL + ":" + API_PORT + POST_URI)
                    .post(formBody)
                    .build();

            Callback loginCallback = new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e("error", e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        JSONObject responseObj = new JSONObject(response.body().string());
                        Log.i("plop", "responseObj: " + responseObj);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };

            client.newCall(request).enqueue(loginCallback);

        } catch (Exception e){
            Log.e("error", e.getMessage());
        }
    }

    public void getQuestions(final APIResult<List<Question>> listener) {

        Request req = new Request.Builder()
                .url(API_URL + ":" + API_PORT + GET_QUEST_URI)
                .build();

        client.newCall(req).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("failure", e.getMessage());
                listener.onAPIGetQuestionsFail(e);
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        String responseData = response.body().string();
                        //JSONObject answer = new JSONObject(responseData);
                        JSONArray jArray = new JSONArray(responseData);

                        jArray.getJSONObject(0);

                        List<Question> questions = new ArrayList<Question>();

                        for (int i = 0 ; i < jArray.length() ; i++) {

                            JSONObject jsonQuestion = jArray.getJSONObject(i);

                            String IdStr = getValueIfExists(jsonQuestion,"id");
                            int id = IdStr .equals("") ? 0 : Integer.parseInt(IdStr );
                            String intitule = getValueIfExists(jsonQuestion,"title");
                            String firstAnswer = getValueIfExists(jsonQuestion,"answer_1");
                            String secondAnswer = getValueIfExists(jsonQuestion,"answer_2");
                            String thirdAnswer = getValueIfExists(jsonQuestion,"answer_3");
                            String fourthAnswer = getValueIfExists(jsonQuestion,"answer_4");
                            String correctAnswerStr = getValueIfExists(jsonQuestion,"correct_answer");
                            int correctAnswer = correctAnswerStr.equals("") ? 0 : Integer.parseInt(correctAnswerStr);
                            String authorImage = getValueIfExists(jsonQuestion,"author_img_url");
                            String author = getValueIfExists(jsonQuestion,"author");

                            questions.add(new Question(id, intitule, firstAnswer,
                                    secondAnswer, thirdAnswer, fourthAnswer, correctAnswer));
                        }

                        listener.onAPIGetQuestions(questions);

                    } catch (JSONException e) {
                        Log.e("error", e.getMessage());
                        listener.onAPIGetQuestionsFail(e);
                    }
                }
            }
        });
    }

    public interface APIResult<T> {
        void onAPIGetQuestionsFail(Exception e);
        void onAPIGetQuestions(T object) throws  IOException;
    }
}
