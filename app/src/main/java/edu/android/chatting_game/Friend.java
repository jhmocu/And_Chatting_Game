package edu.android.chatting_game;

import android.graphics.Bitmap;

/**
 * Created by stu on 2017-03-06.
 */

public class Friend {
    private static final String TAG = "edu.adroid.chatting";
    private String phone, fName, pic_res, status_msg, relation_code;
    /***/
    private Bitmap pic_bitmap;



    public Friend() {
    }

    public Friend(String phone, String fName, String pic_res, String status_msg, String relation_code) {
        this.phone = phone;
        this.fName = fName;
        this.pic_res = pic_res;
        this.status_msg = status_msg;
        this.relation_code = relation_code;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getPic_res() {

        return pic_res;
    }

    public void setPic_res(String pic_res) {
        this.pic_res = pic_res;
    }

    public String getStatus_msg() {
        return status_msg;
    }

    public void setStatus_msg(String status_msg) {
        this.status_msg = status_msg;
    }

    public String getRelation_code() {
        return relation_code;
    }

    public void setRelation_code(String relation_code) {
        this.relation_code = relation_code;
    }

//    class

//    public Bitmap getPic_bitmap() {
//        Log.i(TAG, "getPhotoBitmap()\npic_res: " +pic_res);
//
////        String imageUrl = "http://192.168.11.11:8081/Test3/uploadDirectory/" + pic_res;
//        String imageUrl = "http://192.168.11.11:8081/Test3/uploadDirectory/IMG_20170222_04433646.jpg";
//        pic_bitmap = null;
//        URL url = null;
//        HttpURLConnection connection = null;
//        InputStream inputStream = null;
//        BufferedInputStream bis = null;
//        try {
//            url = new URL(imageUrl);
//            connection = (HttpURLConnection) url.openConnection();
//            connection.connect();
//            int resCode = connection.getResponseCode();
//            if (resCode == HttpURLConnection.HTTP_OK) {
//                inputStream = connection.getInputStream();
//                bis = new BufferedInputStream(inputStream);
//                pic_bitmap = BitmapFactory.decodeStream(bis);
//            }
//
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                bis.close();
//                connection.disconnect();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        return pic_bitmap;
//    }
//
//    public void setPic_bitmap(Bitmap pic_bitmap) {
//        this.pic_bitmap = pic_bitmap;
//    }

} // end class Friend
