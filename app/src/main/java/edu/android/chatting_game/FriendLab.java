package edu.android.chatting_game;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by stu on 2017-03-06.
 */
public class FriendLab {
    //    private static final int[] IDS = {
//            R.drawable.p1, R.drawable.p2, R.drawable.p3, R.drawable.p4, R.drawable.p5, R.drawable.p6, R.drawable.p7, R.drawable.p8, R.drawable.p9,
//    };
    private static final String TAG = "edu.android.chatting";
    private ArrayList<Friend> friendList;
    private static FriendLab instance;

    private FriendLab() {
        friendList = new ArrayList<Friend>();
//        createFriendsList();
        Log.i(TAG, "FriendLab\t________\tcreateFriendsList()");
    }

    public static FriendLab getInstance() {
        if (instance == null) {
            instance = new FriendLab();
        }
        return instance;
    }

    public void setFriendList(ArrayList<Friend> friendList) {
        this.friendList = friendList;
    }

    public ArrayList<Friend> getFriendList() {
        return friendList;
    }

    private void createFriendsList() {

//        StartAppActivity startapp = new StartAppActivity();
//        String my_phone = startapp.readFromFile(StartAppActivity.MY_PHONE_FILE);
/**
        HttpSelectFriendAsyncTask task = new HttpSelectFriendAsyncTask();
        task.execute("010");
 */
    }

/**
    private class HttpSelectFriendAsyncTask
            extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            Log.i(TAG, "doInBackground()\tparams[0]: " + params[0]);
            String result = selectProfile(params[0]);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.i(TAG, "onPostExecute()\tString s: " + s);
            Gson gson = new Gson();
            TypeToken<ArrayList<Friend>> typeToken =
                    new TypeToken<ArrayList<Friend>>() {
                    };
            Type type = typeToken.getType();
            ArrayList<Friend> list = gson.fromJson(s, type);;


            if (list != null) {
                Log.i(TAG, "list != null");
            }
//            for (Friend f : list) {
//                String name = f.getName();
//                String phone = f.getPhoneNumber();
//                String imageId = f.getImageId();
//                String msg = f.getStatusMessage();
//                int count = f.getFriendCount();
//            }
        }
    } // end class HttpSelectFriendAsyncTask

    public String selectProfile(String s) {
        String requestURL = "http://192.168.11.11:8081/Test3/SelectProfile";
        String result = "";
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);


        builder.addTextBody("phone", s, ContentType.create("Multipart/related", "UTF-8"));
        Log.i(TAG, "selectProfile()\tString s: " + s);

        InputStream inputStream = null;
        HttpClient httpClient = null;
        HttpPost httpPost = null;
        HttpResponse httpResponse = null;
        try {
            // send
            httpClient = AndroidHttpClient.newInstance("Android");
            httpPost = new HttpPost(requestURL);
            httpPost.setEntity(builder.build());

            httpResponse = httpClient.execute(httpPost);
            Log.i(TAG, "연결됨");

            // receive
            HttpEntity httpEntity = httpResponse.getEntity();
            inputStream = httpEntity.getContent();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            StringBuffer stringBuffer = new StringBuffer();
            String line = bufferedReader.readLine();
            while (line != null) {
                Log.i(TAG, "\n읽고있음\tline: " + line);
                stringBuffer.append(line);
                line = bufferedReader.readLine();
            }
            result = stringBuffer.toString();
            Log.i(TAG, "다 읽음\tresult: " + result);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
                httpPost.abort();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }// end selectProfile()
*/
}// end class FriendLab



