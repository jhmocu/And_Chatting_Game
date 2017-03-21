package edu.android.chatting_game;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


/**
 * A simple {@link Fragment} subclass.
 */
public class LongClickDeleteDialogFragment extends DialogFragment {

//    final Bundle extra = getActivity().getIntent().getExtras();
    private static final String TAG = "edu.android.chatting";

    public LongClickDeleteDialogFragment() {
        // Required empty public constructor
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("확인");
        builder.setMessage("친구를 삭제하시겠습니까?");

        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FriendDeleteConnect();
//                Toast.makeText(getActivity(), "삭제되었습니다.", Toast.LENGTH_SHORT).show();

            }
        });
        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                Toast.makeText(getActivity(), "삭제가 취소되었습니다.", Toast.LENGTH_SHORT).show();

            }
        });

        return builder.create();
    }

    private void FriendDeleteConnect(){
        ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(getActivity().CONNECTIVITY_SERVICE);
        NetworkInfo info = connMgr.getActiveNetworkInfo();
        if (info != null && info.isAvailable()) {
            Bundle extraa = getArguments();
            String my_phone = extraa.getString("my_phone"); // TODO: 값 가져오기
//            StartAppActivity startAppActivity = new StartAppActivity();
//            String my_phone = startAppActivity.readFromFile(StartAppActivity.MY_PHONE_FILE);
            Log.i(TAG, my_phone + "과연 번호가 나올 것인가!!!!");
            Log.i(TAG, info.getTypeName() + "사용 가능");

//            String friend_phone= getActivity().getIntent().getExtras().getString(FriendsRecyclerViewFragment.KEY_EXTRA_PHONENUMBER); // TODO: 선택한 친구 번호 가져오기
            String friend_phone = extraa.getString("f_phone");

            FriendVO vo = new FriendVO(my_phone,friend_phone, null);
            HttpDeleteAsyncTask task = new HttpDeleteAsyncTask();
            task.execute(vo);
        }

        Intent intent = new Intent(getContext(), Main2Activity.class);


        startActivity(intent);
    }

    private class HttpDeleteAsyncTask extends AsyncTask<FriendVO, String, String> {


        @Override
        protected String doInBackground(FriendVO... params) {
            String result=sendData(params[0]);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }

    public String sendData(FriendVO vo) {

        String requestURL = "http://192.168.11.11:8081/Test3/DeleteFriend";
        String result = "";
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

        builder.addTextBody("my_phone", vo.getMy_phone(), ContentType.create("Multipart/related", "UTF-8"));
        builder.addTextBody("friend_phone", vo.getFriend_phone(), ContentType.create("Multipart/related", "UTF-8"));

        InputStream inputStream = null;
        HttpClient httpClient = null; //
        HttpPost httpPost = null; //new HttpPost(requestURL);
        HttpResponse httpResponse = null;

        try {
            // http 통신 send
            httpClient = AndroidHttpClient.newInstance("Android");
            httpPost = new HttpPost(requestURL);
            httpPost.setEntity(builder.build());

            httpResponse = httpClient.execute(httpPost); // 연결 실행

            // http 통신 receive
            HttpEntity httpEntity = httpResponse.getEntity();
            inputStream = httpEntity.getContent();

            Log.i("gg", "good");
            BufferedReader bufferdReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            StringBuffer stringBuffer = new StringBuffer();
            String line = null;

            while ((line = bufferdReader.readLine()) != null) {
                stringBuffer.append(line + "\n");
            }

            result = stringBuffer.toString();
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
    }


}
