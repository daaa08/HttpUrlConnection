package com.example.da08.httpurlconnection;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Da08 on 2017. 6. 12..
 */

public class Remote {

    // 인자로 받은 url로 네트워크를 통해 데이터를 가져오는 함수
    public static String getData(String url) throws Exception{ // 데이터를 가져온 순간은 무조건 String , 에러를 호출한 측에서 Exception 처리
        String result = "";
        // 네트워크 처리
        // 1 요청처리 request
        // 1.1 url 객체 만들기
        URL serverUrl = new URL(url);
        // 1.2 연결객체 생성 - 서버가 https면 https커넥션을 해줘야 함
        HttpURLConnection con = (HttpURLConnection)serverUrl.openConnection(); //url 객체에서 연결을 꺼냄
        // 1.3 http 메소드 결정
        con.setRequestMethod("GET"); // post, get 호출
        // 2 응답처리 reponse
        // 2.1 응답 코드 분석
        int responseCode = con.getResponseCode();
        // 2.2 정상적인 응답 처리
        if(responseCode == HttpURLConnection.HTTP_OK){  // 정상적인 코드처리
            BufferedReader br = new BufferedReader( new InputStreamReader(con.getInputStream()));
            String temp = null;
            while((temp = br.readLine()) != null){  // 한줄씩 읽기
                result += temp;
            }
        }else{
            // 각자 호출측으로 Exception 을 만들어서 오류 처리
            Log.e("Network", "error_code"+responseCode);
        }
        return result;
    }

    // thread를 생성
    public static void newTask(final TaskInterface taskInterface){  // taskInterface 로 구현
        new AsyncTask<String,Void, String>(){
            // 백그라운드 처리를 할 함수 오버라이드
            @Override
            protected String doInBackground(String... params) {  // excute로 실행
                String result = "";
                try {
                    // getData함수로 데이터를 가져옴
                    result = getData(params[0]);
                    Log.i("Network", result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return result;
            }
            @Override
            protected void onPostExecute(String result) {
                // 결과처리
                taskInterface.postExecute(result);
            }
        }.execute(taskInterface.getUrl());
    }
}
