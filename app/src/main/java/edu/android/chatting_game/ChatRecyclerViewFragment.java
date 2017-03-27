package edu.android.chatting_game;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;


public class ChatRecyclerViewFragment extends Fragment {

    private static final String TAG = "edu.android.chatting";
    private static final String TASK_CYCLE = "task_cycle";
    private RecyclerView recyclerView;
    private ArrayList<ChatRoomVO> chatRoomList;
    private ArrayList<ChatMessageReceiveVO> chatMessageList;
    private ArrayList<MessageVO> messageList;

    private int listPosition;
    private String my_phone, chatroom_name;

    class ChattingViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView txtRoom, txtLastMsg, txtFriendCount, txtTime, txtMsgCount;

        public ChattingViewHolder(View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.imageRoom);
            imageView.setBackground(new ShapeDrawable(new OvalShape()));
            imageView.setClipToOutline(true);
            txtRoom = (TextView) itemView.findViewById(R.id.txtRoom);
            txtLastMsg = (TextView) itemView.findViewById(R.id.txtLastMsg);
            txtFriendCount = (TextView) itemView.findViewById(R.id.txtFriendCount);
            txtTime = (TextView) itemView.findViewById(R.id.txtTime);
            txtMsgCount = (TextView) itemView.findViewById(R.id.txtMsgCont);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listPosition = getAdapterPosition();
                    Log.i(TAG, "ChatRecyclerView// onClick()// position:" + listPosition);
                    ChatRoomVO vo = chatRoomList.get(listPosition);
                    Log.i(TAG, "ChatRecyclerView// onClick()// vo:" + vo.toString());

                    ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(getActivity().CONNECTIVITY_SERVICE);
                    NetworkInfo info = connMgr.getActiveNetworkInfo();
                    if (info != null && info.isAvailable()) {
                        HttpSelectMessageAsyncTask task = new HttpSelectMessageAsyncTask();
                        task.execute(vo);
                    }
                }
            });

            // 롱클릭시 삭제 Dialog
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    listPosition = getAdapterPosition();
                    DialogFragment chatLongClickFragment = ChatLongClickDialogFragment.newInstance(listPosition);
                    chatLongClickFragment.show(getChildFragmentManager(), "chatLongClickFragment");
                    return true;
                }
            });
        }
    } // end class ChattingViewHolder

    class ChattingAdapter extends RecyclerView.Adapter<ChattingViewHolder> {

        @Override
        public ChattingViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View view = inflater.inflate(R.layout.fragment_chat_recycler_item, null, false);
            ChattingViewHolder chattingViewHolder = new ChattingViewHolder(view);
            return chattingViewHolder;
        }

        @Override
        public void onBindViewHolder(ChattingViewHolder holder, int position) {
            ChatRoomVO vo = chatRoomList.get(position);
            holder.txtRoom.setText(vo.getChatroom_name());
            holder.txtLastMsg.setText(vo.getLast_msg());
            holder.txtTime.setText(vo.getChat_date());
            holder.txtMsgCount.setText(vo.getChecked_read());
            holder.txtFriendCount.setText(vo.getMember_count());
        }

        @Override
        public int getItemCount() {
            return chatRoomList.size();
        }
    }// end class ChattingAdapter

    public ChatRecyclerViewFragment() {
    }

    @SuppressLint("ValidFragment")
    public ChatRecyclerViewFragment(String my_phone) {
        this.my_phone = my_phone;
        chatMessageList = new ArrayList<>();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_recycler_view, container, false);
        chatRoomList = ChatRoomLab.getInstance().getChatRoomVOList();
        recyclerView = (RecyclerView) view.findViewById(R.id.chatlist_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new ChattingAdapter());
        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }


    private class HttpSelectMessageAsyncTask extends AsyncTask<ChatRoomVO, Void, String> {

        @Override
        protected String doInBackground(ChatRoomVO... params) {
            String result = selectMyMessage(params[0]);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.i(TASK_CYCLE, "ChatRecyclerView// selectTask// onPostExecute() String s:" + s);
            Gson gson = new Gson();
            TypeToken<ArrayList<MessageVO>> typeToken = new TypeToken<ArrayList<MessageVO>>() {
            };
            Type type = typeToken.getType();

            // TODO: 2017-03-25 (New) ChatMessageVO Array에 저장


//            chatMessageList = ChatMessageReceiveLab.getInstance().getChatMessageList();
                    messageList = gson.fromJson(s, type);
//            chatMessageList = gson.fromJson(s, type);
            MessageLab.getInstance().setMessageList(messageList);
            Log.i(TASK_CYCLE, "ChatRecyclerView// seletTask// onPostExecute()// msgList.size()=" + messageList.size());
//
//
            if (!messageList.isEmpty()) {
                for (MessageVO vo : messageList) {
// TODO: 2017-03-25 하나씩 꺼낼 필요 있을까.
                    Log.i(TASK_CYCLE, "ChatRecyclerView// selectTask// onPostExecute() vo:\n" + vo.toString());
                }
            }

            enterTheChatRoom();
        }
    }// end class HttpSelectMessageAsyncTask

    public void enterTheChatRoom() {
        Intent intent = new Intent(getContext(), ChatRoomActivity.class);
        intent.putExtra("key_my_phone", my_phone);
        intent.putExtra("key_room_name", chatroom_name);
        intent.putExtra("color",BackgroundChangeVO.getInstance().getColor());
        if (FontChangeVO.getInstance().getTextSize() != 0) {
            intent.putExtra("Size", FontChangeVO.getInstance().getTextSize());
        } else {
            intent.putExtra("Size", 15f);
        }

        startActivity(intent);
    }

    public String selectMyMessage(ChatRoomVO vo) {
        String result = "";
        String checked = "false";
//        String requestURL = "http://192.168.11.11:8081/Test3/SelectChatReceive";
        String requestURL = "http://192.168.11.11:8081/Test3/SelectChatTableData";
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        chatroom_name = vo.getChatroom_name();

        Log.i(TASK_CYCLE, "ChatRecyclerView// selectTask// selectMyMessage()// chatroom_name:" + chatroom_name);
        Log.i(TASK_CYCLE, "ChatRecyclerView// selectTask// selectMyMessage()// vo=" + vo.toString());

//        builder.addTextBody("my_phone", "01090429548"/**vo.getPhone()*/, ContentType.create("Multipart/related", "UTF-8")); // member = receiver
//        builder.addTextBody("checked", checked, ContentType.create("Multipart/related", "UTF-8"));
        builder.addTextBody("table_name", "a0109731942701090429548", ContentType.create("Multipart/related", "UTF-8"));

        InputStream inputStream = null;
        AndroidHttpClient androidHttpClient = null; //
        HttpPost httpPost = null; //new HttpPost(requestURL);
        HttpResponse httpResponse = null;
        try {
            // http 통신 sendㅌ
            androidHttpClient = AndroidHttpClient.newInstance("Android");
            httpPost = new HttpPost(requestURL);
            httpPost.setEntity(builder.build());

            httpResponse = androidHttpClient.execute(httpPost); // 연결 실행

            // http 통신 receive
            HttpEntity httpEntity = httpResponse.getEntity();
            inputStream = httpEntity.getContent();

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
                androidHttpClient.close();
                inputStream.close();
                httpPost.abort();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Log.i(TASK_CYCLE, "ChatRecyclerView// selectTask// selectMyMessage() result:" + result);
        return result;
    }// end selectMyMessage
}// end class ChatRecyclerViewFragment
