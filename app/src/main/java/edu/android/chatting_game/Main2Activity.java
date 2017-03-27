package edu.android.chatting_game;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Main2Activity extends AppCompatActivity
        implements LongClick_Fragment.onItemSelectedListener {


    public static final int REQ_CODE = 1001;
    private String receiveSign;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    public String my_phone;
    public int Color;

    private static final String TAG = "edu.android.chatting";

    // TODO: 2017-03-20 서비스 바인딩
    private RestartService receiver;
    Intent intentMyService;

    // 데이터 수신용 value
    private String dataPassed = "noData";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        // 번호 저장하기
        my_phone = readFromFile(StartAppActivity.MY_PHONE_FILE);
        Log.i("my_phone", "Main2Activity// onCreate()// my_phone:" + my_phone);

        intentMyService = new Intent(this, MyService.class);
        receiver = new RestartService();

        // ????
        IntentFilter mainFilter = new IntentFilter("edu.android.chatting");
        mainFilter.addAction(MyService.MY_ACTION);

        // 리시버 저장
        registerReceiver(receiver, mainFilter);

        // 서비스 시작
        startService(intentMyService);


    }// end onCreate()

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(receiver);
    }

    //    public void OnDestroy() {
//
//        // 리시버 삭세를 하지 않으면 에러
//        Log.d("MpMainActivity", "Service Destroy");
//        unregisterReceiver(receiver);
//
//        super.onDestroy();
//    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void itemSelected(int which, String name, String phone) {
        switch (which) {
            case 0:
                nameUpdate(name, phone);
                break;
            case 1:
                itemDelete(name, phone);
                break;
        }
    }

    private void itemDelete(String name, String phone) {
        //TODO:DB 삭제 작업할 곳
        LongClickDeleteDialogFragment dlg = new LongClickDeleteDialogFragment();
        dlg.show(getSupportFragmentManager(), "dlg");
        Bundle bundle = new Bundle();
        bundle.putString("my_phone", my_phone);
        bundle.putString("f_phone", phone);
        dlg.setArguments(bundle);

//        Intent intent = new Intent(Main2Activity.this, LongClickDeleteDialogFragment.class);
//        intent.putExtra(FriendsRecyclerViewFragment.KEY_EXTRA_PHONENUMBER, name);
//
//        startActivityForResult(intent, REQ_CODE);

    }

    private void nameUpdate(String name, String phone) {

        Intent intent = new Intent(Main2Activity.this, Long_Click_name_Update.class);
        intent.putExtra(FriendsRecyclerViewFragment.KEY_EXTRA_NAME2, name);
        intent.putExtra(FriendsRecyclerViewFragment.KEY_EXTRA_PHONENUMBER, phone);

        startActivityForResult(intent, REQ_CODE);
    }

    // 앱이 실행 되었을 때 메시지가 오면 받아올 신호값
    public void SendSignListener(String sendSign){
        receiveSign = sendSign;
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main2, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).

            Fragment fragment = null;
            switch (position) {
                case 0:
                    fragment = new FriendsListFragment(my_phone);
                    break;
                case 1:
                    fragment = new ChatListFragment(my_phone);
                    break;
                case 2:
                    fragment = new SettingFragment();
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "친 구";
                case 1:
                    return "채 팅";
                case 2:
                    return "설 정";
            }
            return null;
        }
    }

    public String readFromFile(String filename) {
        // 파일에서 읽은 문자열을 append할 변수
        StringBuffer buffer = new StringBuffer();

        InputStream in = null; // file input stream
        InputStreamReader reader = null; // 인코딩된 문자열을 읽기 위해서
        BufferedReader br = null; //

        try {
            in = openFileInput(filename);
            reader = new InputStreamReader(in);
            br = new BufferedReader(reader);

            String line = br.readLine();
            while (line != null) {
                buffer.append(line);
                line = br.readLine();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Log.i("gg", buffer.toString());
        return buffer.toString();
    }

    class RestartService extends BroadcastReceiver {
        public static final String ACTION_RESTART_PERSISTENTSERVICE
                = "ACTION.Restart.PersistentService";

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("Service", "service called!");
            // 리시버가 뭔가를 받았을 때
            dataPassed = intent.getStringExtra("DATAPASSED");
            Log.d("Service", dataPassed);

            // 신호를 받았을 때 chatListFragment의 메소드를 불러옴
//            if(dataPassed.equals("receivedTrue")){
//                ChatListFragment chatListFragment = new ChatListFragment();
//                chatListFragment.callChatListFragment();
//
//            }


        /* 서비스 죽일때 알람으로 다시 서비스 등록 */
            if (intent.getAction().equals(ACTION_RESTART_PERSISTENTSERVICE)) {
                Log.d("Service", "Service dead, but resurrection");

                Intent i = new Intent(context, MyService.class);
                context.startService(i);
            }

        /* 폰 재부팅할때 서비스 등록 */
            if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
                Log.d("Service", "ACTION_BOOT_COMPLETED");

                Intent i = new Intent(context, MyService.class);
                context.startService(i);
            }
        }

    }


}
