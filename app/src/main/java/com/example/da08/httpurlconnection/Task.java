package com.example.da08.httpurlconnection;

import android.os.AsyncTask;
import android.util.Log;

import static com.example.da08.httpurlconnection.Remote.getData;

/**
 * Created by Da08 on 2017. 6. 12..
 */

public class Task {

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