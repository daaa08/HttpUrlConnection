package com.example.da08.httpurlconnection;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
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

    int pageBegin = 1;
    int pageEnd = 10;


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

        listView = (ListView)findViewById(R.id.listView);
        textView = (TextView)findViewById(R.id.textView);

        // data - 위에서 공간 할당
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,datas);
        listView.setAdapter(adapter);  // adapter 연결


        // map setting
        FragmentManager manager = getSupportFragmentManager();  // map 불러오기
        SupportMapFragment mapFragment = (SupportMapFragment)manager.findFragmentById(R.id.mapView);

        // 로드되면 onReady를 호출하도록
        mapFragment.getMapAsync(this);



    }

    private void setPage(int page){   // setPage가 어디서 호출될지 모르기때문에 함수로
        pageBegin = pageEnd - OFF_SET +1;

        pageEnd = page * OFF_SET;   // 1 *10 = 10, 2* 10 = 20...
    }

    private void setUrl(int begin , int end){   // Remote에서 getUrl을 호출하여 사용

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

        Gson gson = new Gson(); // gson : jsonString을 Domain 폴더 파일들에 맞는 객체로 자동 변환해주는 툴

        // 1 json String을 class로 변환
        MyPojo mp = gson.fromJson(jsonString, MyPojo.class); // json은 String 형태로 날아옴

        // 2 class를 json String으로 변환 - 총 개수 화면에 세팅
        textView.setText("총 개수" + mp.getSearchPublicToiletPOIService().getList_total_count());
        // 건물의 이름을  listView에 세팅
        Row rows[] = mp.getSearchPublicToiletPOIService().getRow(); // json데이터에서 Row파일에 건물 이름이 있음

        // 네트워크에서 가져온 데이터를 꺼내서 datas에 담아줌
        for(Row row : rows){
            datas.add(row.getFNAME());

            // row를 돌면서 화장실 하나하나에 좌표 생성
            MarkerOptions marker = new MarkerOptions();  // 좌표를 마커에 담아 줌
            LatLng tempCount = new LatLng(row.getY_WGS84(), row.getX_WGS84());
            marker.position(tempCount);
            marker.title(row.getFNAME());

            myMap.addMarker(marker);
        }

        // 지도 컨트롤
        LatLng latLng = new LatLng(37.516068, 127.019361);  // 신사동으로 잡아 줌
        myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));  // 좌표 , 줌 레벨

        // 그리고 adapter를 갱신
        adapter.notifyDataSetChanged();



    }

    GoogleMap myMap;
    @Override
    public void onMapReady(GoogleMap googleMap) {  // google이 알아서 호출해 줌

        myMap = googleMap;  // 지도를 컨트롤하다보니 해당 함수에 지도를 불러오는게 없으므로 생성해 줌

        // map이 불러와지고 호출
        setPage(1);
        setUrl(pageBegin, pageEnd);
        Remote.newTask(this);


    }
}