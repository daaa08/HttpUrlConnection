package com.example.da08.httpurlconnection;

/**
 * Created by Da08 on 2017. 6. 12..
 */

public interface TaskInterface {  // 효율성을 높이기위해 interface 사용
    public String getUrl();   // interface 라서 public을 쓸 필요가 없음
    public void postExecute(String result);
}
