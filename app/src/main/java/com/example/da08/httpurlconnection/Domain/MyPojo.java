package com.example.da08.httpurlconnection.Domain;

/**
 * Created by Da08 on 2017. 6. 13..
 */

// json 내용을 각 객체로 만들어줘야 하는데 http://pojo.sodhanalibrary.com/ 에서 자동으로 바꿔 줌
public class MyPojo {  // 데이터
    private SearchPublicToiletPOIService SearchPublicToiletPOIService;  // Point Of Interest
    // json은 변수명이 오브젝트 이름임 , 변수가 하나!

    public SearchPublicToiletPOIService getSearchPublicToiletPOIService () {
        return SearchPublicToiletPOIService;
    }

    public void setSearchPublicToiletPOIService (SearchPublicToiletPOIService SearchPublicToiletPOIService) {
        this.SearchPublicToiletPOIService = SearchPublicToiletPOIService;
    }

    @Override
    public String toString() {
        return "ClassPojo [SearchPublicToiletPOIService = "+SearchPublicToiletPOIService+"]";
    }
}