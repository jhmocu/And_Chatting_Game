package edu.android.chatting_game;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FriendsRecyclerViewFragment
        extends Fragment {

    private static final String TAG = "edu.android.chatting";

    public static final String KEY_EXTRA_NAME = "key_name";
    public static final String KEY_EXTRA_PHONENUMBER = "key_phone";
    public static final String KEY_EXTRA_MESSAGE = "key_msg";
    public static final String KEY_EXTRA_IMAGEID = "key_image";
    public static final String KEY_EXTRA_NAME2 = "key_name2";


    private RecyclerView recyclerView;
    private ArrayList<Friend> list;

    class FriendViewHolder
            extends RecyclerView.ViewHolder {

        private ImageView photo;
        private TextView name, message;

        public FriendViewHolder(final View itemView) {
            super(itemView);

            photo = (ImageView) itemView.findViewById(R.id.imageView_list);
            name = (TextView) itemView.findViewById(R.id.textName_list);
            message = (TextView) itemView.findViewById(R.id.textMsg_list);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    startProfileActivity(position);
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position = getAdapterPosition();

                    DialogFragment longClickFragment = LongClick_Fragment.newInstance(name.getText().toString());
                    longClickFragment.show(getChildFragmentManager(), "longClick_dialog");

                    return true;
                }
            });
        }
    } // end class FriendViewHolder

    class FriendAdapter
            extends RecyclerView.Adapter<FriendViewHolder> {

        @Override
        public FriendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View itemView = inflater.inflate(R.layout.friend_item, parent, false);
            FriendViewHolder viewHolder = new FriendViewHolder(itemView);

            Log.i(TAG, "onCreateViewHolder()");

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(FriendViewHolder holder, int position) {
            Friend friend = list.get(position);
//            holder.photo.setImageResource(friend.getImageId());
            holder.name.setText(friend.getfName());
            holder.message.setText(friend.getStatus_msg());
            Log.i(TAG, "onBindViewHolder()");
        }

        @Override
        public int getItemCount() {
            return list.size();

        }
    }// end class FriendAdapter

    @Override
    public void onResume() {
        super.onResume();
//        HttpSelectFriendAsyncTask task = new HttpSelectFriendAsyncTask();
//        task.execute("010");
        Log.i(TAG, "onResume()");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        HttpSelectFriendAsyncTask task = new HttpSelectFriendAsyncTask();
//        task.execute("010");
        Log.i(TAG, "onCreate()");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.i(TAG, "FriendsRecyclerViewFragment\t//////////\tonCreateView()");
//        HttpSelectFriendAsyncTask task = new HttpSelectFriendAsyncTask();
//        task.execute("010");

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recycler_view_friends, container, false);
        list = FriendLab.getInstance().getFriendList();
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_container);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new FriendAdapter());
        for (Friend f : list) {
            Log.i(TAG, "fList에서 Friend 객체 확인\n" + f.getfName());
        }
        return view;
    }

    private void startProfileActivity(int position) {
        if (position == list.size() - 1) {
            Intent intent = new Intent(getContext(), Profile_My_info.class);
            intent.putExtra(KEY_EXTRA_IMAGEID, list.get(
                    position).getPic_res());
            intent.putExtra(KEY_EXTRA_NAME, list.get(position).getfName());
            intent.putExtra(KEY_EXTRA_MESSAGE, list.get(position).getStatus_msg());
            startActivity(intent);
//            intent.putExtra(KEY_EXTRA_PHONENUMBER, list.get(position).getPhoneNumber());
        } else {
            Intent intent = new Intent(getContext(), ProfileInfoActivity.class);
            intent.putExtra(KEY_EXTRA_IMAGEID, list.get(position).getPic_res());
            intent.putExtra(KEY_EXTRA_NAME, list.get(position).getfName());
            intent.putExtra(KEY_EXTRA_PHONENUMBER, list.get(position).getPhone());
            intent.putExtra(KEY_EXTRA_MESSAGE, list.get(position).getStatus_msg());
            startActivity(intent);
        }
    }

//    private class HttpSelectFriendAsyncTask
//            extends AsyncTask<String, String, String> {
//
//        @Override
//        protected String doInBackground(String... params) {
//            Log.i(TAG, "doInBackground()\tparams[0]: " + params[0]);
//            String result = selectProfile(params[0]);
//            return result;
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//            Log.i(TAG, "onPostExecute()\nString s:\n" + s);
//            Gson gson = new Gson();
//            TypeToken<ArrayList<Friend>> typeToken =
//                    new TypeToken<ArrayList<Friend>>() {
//                    };
//            Type type = typeToken.getType();
//            list = gson.fromJson(s, type);
//
//            /** 확인 */
//            if (list != null) {
//                Log.i(TAG, "fList != null");
//                for (Friend f : list) {
//                    Log.i(TAG, "fList에서 Friend 객체 확인\n" + f.getfName());
//                }
//            }
////            for (Friend f : list) {
////                String name = f.getName();
////                String phone = f.getPhoneNumber();
////                String imageId = f.getImageId();
////                String msg = f.getStatusMessage();
////                int count = f.getFriendCount();
////            }
//        }
//    } // end class HttpSelectFriendAsyncTask
//
//    public String selectProfile(String s) {
//        String requestURL = "http://192.168.11.11:8081/Test3/SelectProfile";
//        String result = "";
//        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
//        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
//
//        builder.addTextBody("phone", s, ContentType.create("Multipart/related", "UTF-8"));
//        Log.i(TAG, "selectProfile()\tString s: " + s);
//
//        InputStream inputStream = null;
//        HttpClient httpClient = null;
//        HttpPost httpPost = null;
//        HttpResponse httpResponse = null;
//        try {
//            // send
//            httpClient = AndroidHttpClient.newInstance("Android");
//            httpPost = new HttpPost(requestURL);
//            httpPost.setEntity(builder.build());
//
//            httpResponse = httpClient.execute(httpPost);
//            Log.i(TAG, "연결됨");
//
//            // receive
//            HttpEntity httpEntity = httpResponse.getEntity();
//            inputStream = httpEntity.getContent();
//
//            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
//            StringBuffer stringBuffer = new StringBuffer();
//            String line = bufferedReader.readLine();
//            while (line != null) {
////                Log.i(TAG, "읽고있음\tline: " + line);
//                stringBuffer.append(line);
//                line = bufferedReader.readLine();
//            }
//            result = stringBuffer.toString();
//            Log.i(TAG, "다 읽음\nresult: " + result +"\n");
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                inputStream.close();
//                httpPost.abort();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        return result;
//    }// end selectProfile()
}// class FriendsRecyclerViewFragment