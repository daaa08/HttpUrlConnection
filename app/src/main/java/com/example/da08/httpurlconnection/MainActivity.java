package com.example.da08.httpurlconnection;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.da08.httpurlconnection.Domain.MyPojo;
import com.example.da08.httpurlconnection.Domain.Row;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TaskInterface, OnMapReadyCallback{

    /*
    기초정보
    url : http://openAPI.seoul.go.kr:8088/48557578576461613130315141764666/json/SearchPublicToiletPOIService/1/1000
    인증키 : 48557578576461613130315141764666
     */

    // 고정되는 정보
    static final String URL_PREFIX = "http://openAPI.seoul.go.kr:8088/";  // 인증 키 전까지
    static final String URL_CERT   = "48557578576461613130315141764666";  // 인증 키
    static final String URL_MID    = "/json/SearchPublicToiletPOIService/";  // start와 end를 제외한 서비스명까지
    public static final int OFF_SET = 10;  // 한 페이지에 불러오는 데이터 수

    int page = 0;
//    int pageBegin = 1;
//    int pageEnd = 10;


    ListView listView;
    TextView textView;
    String url = "";

    // adapter
    ArrayAdapter<String> adapter;

    // 아답터에서 사용할 데이터 공간
    final List<String> datas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setViews();
        setListener();
        setMap();

    }
    private void setViews(){
        listView = (ListView) findViewById(R.id.listView);
        textView = (TextView) findViewById(R.id.textView);

        // data - 위에서 공간 할당
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,datas);
        listView.setAdapter(adapter);  // adapter 연결
    }

    private void setListener(){
        // 스크롤의 상태값을 체크해주는 리스너
        listView.setOnScrollListener(scrollListener);
    }

    private void setMap(){
        // map setting
        FragmentManager manager = getSupportFragmentManager();  // map 불러오기
        SupportMapFragment mapFragment = (SupportMapFragment) manager.findFragmentById(R.id.mapView);
        // 로드되면 onReady 호출하도록
        mapFragment.getMapAsync(this);
    }

    // 리스트에 마지막 item이 보이는지 여부
     Boolean lastItem = false;
    AbsListView.OnScrollListener scrollListener = new AbsListView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            if(scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                    && lastItem){

                loadPage();
            }
        }

            /*
             firstVisibleItem : 현재 화면에 보여지는 첫번째 아이템의 번호
             visibleItemCount : 현재 화면에 보여지는 아이템의 개수
             totalItemCount   : 리스트에 담겨있는 전체 아이템의 개수
             */
        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            if(totalItemCount <= firstVisibleItem + visibleItemCount){
                lastItem = true;
            }else{
                lastItem = false;
            }
        }
    };

    private void loadPage(){
        nextPage();
        setUrl();
        Remote.newTask(MainActivity.this);

    }
//        pageBegin = pageEnd - OFF_SET +1;
//        pageEnd = page * OFF_SET;   // 1 *10 = 10, 2* 10 = 20...
//    }


    private void nextPage(){
        page = page + 1;
    }

    private void setUrl(){   // Remote에서 getUrl을 호출하여 사용

        int end = page * OFF_SET;
        int begin = end - OFF_SET + 1;

        /*
        // String 연산
        // String result = "문자열" + "문자열"+ "문자열";
        //                 ----------------
        //                    메모리공간 할당
        //                  -----------------------
        //                        메모리공간 할당

        StringBuffer sb = new StringBuffer();  // 동기화 지원  - 지금은 쓰지 않음  > 자동으로 StringBuilder로 컴파일되기 때문임 (단순할때만)
        sb.append("문자열");
        sb.append("문자열");

        StringBuilder sbl = new StringBuilder();  // 동기화 미지원 (속도가 빠름)  - 지금은 쓰지 않음  > 자동으로 StringBuilder로 컴파일되기 때문임(단순할때만)
        sbl.append("문자열");
        */

        url = URL_PREFIX + URL_CERT + URL_MID +begin+"/"+end;
    }

    @Override
    public String getUrl(){
        return url;
    }

    @Override
    public void postExecute(String jsonString){

        MyPojo data = convertJson(jsonString);

        int totalCount = data.getSearchPublicToiletPOIService().getList_total_count();
        Row items[] = data.getSearchPublicToiletPOIService().getRow();

        setItemCount(totalCount);

        addDatas(items);

        addMarkers(items);

        LatLng sinsa = new LatLng(37.516066, 127.019361);
        moveMapPosition(sinsa);

        // 그리고 adapter 를 갱신해준다.
        adapter.notifyDataSetChanged();
    }

    // 지도 이동
    private void moveMapPosition(LatLng position){
        myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 10));
    }

    // datas 에 데이터 더하기
    private void addDatas(Row[] items){
        for(Row item : items){
            datas.add(item.getFNAME());
        }
    }


    // 지도에 마커 생성
    private void addMarkers(Row[] items){
        // 네트워크에서 가져온 데이터를 꺼내서 datas에 담아줌
        for(Row row : items){
            // row를 돌면서 화장실 하나하나의 좌표를 생성
            MarkerOptions marker = new MarkerOptions();
            LatLng tempCoord = new LatLng(row.getY_WGS84(), row.getX_WGS84());
            marker.position(tempCoord);
            marker.title(row.getFNAME());

            myMap.addMarker(marker);
        }
    }


    // 총개수를 화면에 출력
    private void setItemCount(int totalCount){
        textView.setText("총 개수 : "+ totalCount);
    }

    // json 스트링을 MyPojo 오브젝트로 변환
    public MyPojo convertJson(String jsonString){
        Gson gson = new Gson();  // gson : jsonString을 Domain 폴더 파일들에 맞는 객체로 자동 변환해주는 툴
        return gson.fromJson(jsonString, MyPojo.class);  // json은 String 형태로 날아옴
    }

    GoogleMap myMap;
    @Override
    public void onMapReady(GoogleMap googleMap) {  // google이 알아서 호출해 줌

        myMap = googleMap;  // 지도를 컨트롤하다보니 해당 함수에 지도를 불러오는게 없으므로 생성해 줌
        // map이 불러와지고 호출
//        setPage(1);
//        setUrl();
//        Remote.newTask(this);
        loadPage();
    }
}
