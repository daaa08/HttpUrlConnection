## Json

- 데이터를 주고 받는 형식

~~~
1. json 기본형
json 오브젝트 = { 중괄호와 중괄호 사이 }
{ "변수명" : "값", "변수명2" : "값2", "변수명3" : "값3" }

json.변수명 < 값

2. json 서브트리
{ "변수명" : json 오브젝트 }
{ "변수명" : { "서브명" : "값", "서브명2" : "값2", "서브명3" : "값3" } }

json.변수명.서브명 < 값

3. json 배열
{ "변수명" : json 배열 }
{ "변수명" : [
              { "서브명" : "값", "서브명2" : "값2", "서브명3" : "값3" }
             ,{ "서브명" : "값", "서브명2" : "값2", "서브명3" : "값3" }
             ,{ "서브명" : "값", "서브명2" : "값2", "서브명3" : "값3" }
           ]
}
json.변수명[0].서브명
json.변수명[1].서브명
~~~
> 구글에서 연동 가능한 API는 WGS1984
====== RestAPI ======
> openAPI.seoul.go.kr?key=sample&type=json&gubun=Geo&start=1&end=5   -> 값의 나열
?key=sample  >  key
&type=json  >  type
&gubun=Geo  > 구분자, 서비스
&start=1  > start
&end=5  > end       >> &start=1&end=5 > 데이터 1번부터 5번까지 불러옴

## setRequestMethod

- HttpURLConnection의 GET, POST사용하여 Url요청방식을 설정
```java
 HttpURLConnection con = (HttpURLConnection)serverUrl.openConnection();
con.setRequestMethod("GET");
```
