package edu.android.chatting_game;

/**
 * Created by stu on 2017-03-25.
 */

public class MessageVO {

        private String sender, msg, chat_date;

        public MessageVO() {
        }

        public MessageVO(String sender, String msg, String chat_date) {
            this.sender = sender;
            this.msg = msg;
            this.chat_date = chat_date;
        }

        public String getSender() {
            return sender;
        }

        public void setSender(String sender) {
            this.sender = sender;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getChat_date() {
            return chat_date;
        }

        public void setChat_date(String chat_date) {
            this.chat_date = chat_date;
        }

        @Override
        public String toString() {
            String str = "sender:" + sender + "|msg:" + msg + "|chat_date:" + chat_date;
            return str;
        }

    }




