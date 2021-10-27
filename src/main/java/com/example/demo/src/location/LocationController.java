package com.example.demo.src.location;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.location.model.*;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.io.*;
import org.springframework.http.HttpHeaders;
import org.json.simple.parser.JSONParser;
import org.springframework.http.MediaType;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import static com.example.demo.config.BaseResponseStatus.JSON_PARSE_ERROR;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpMethod;
import org.json.simple.JSONObject;
import java.net.URI;
import org.json.simple.JSONArray;

import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.isRegexEmail;

@RestController
@RequestMapping("/locations")   //이 주소로 요청이 오면 일단 여기로 연결이 된다.
public class LocationController {   //유저 컨트롤러 실행!
    final Logger logger = LoggerFactory.getLogger(this.getClass()); //로거는 잘 모르겠음..

    @Autowired
    private final LocationProvider locationProvider;
    @Autowired
    private final LocationService locationService;
    @Autowired
    private final JwtService jwtService;    //필요한 객체들을 정의하고 밑의 생성자에서 받아와서 생성해준다.




    public LocationController(LocationProvider locationProvider, LocationService locationService, JwtService jwtService){
        this.locationProvider = locationProvider;
        this.locationService = locationService;
        this.jwtService = jwtService;
    }   //IoC 기능. 일반적으로는 클래스를 만들때 필요한 객체가 있으면 스스로 만들어 써야했는데, IoC의 경우 필요한 객체가 있으면 `선언만 해놓고 외부에서 받아서 쓸 수 있다.`



    /**
     * 특정 유저 주소 조회 API
     * [GET] /locations/:userId
     * @return BaseResponse<GetLocation>
     */

    // Path-variable
    @ResponseBody
    @GetMapping("/{userId}") // (GET) 127.0.0.1:9000/location/:userId 이런 형식으로 입력이 된경우
    public BaseResponse<DeliveryLocations> getLocation(@PathVariable("userId") int userId) { //API 통신 결과에 의한 성공 및 실패를 BaseResponse를 사용해 처리해줍니다. 입력된 인덱스를 PathVariable로 받는다.
        // Get Location

        try{
            //jwt에서 idx 추출.
            int userIdByJwt = jwtService.getUserId();
            //userIdx와 접근한 유저가 같은지 확인
            if(userId != userIdByJwt){
               return new BaseResponse<>(INVALID_USER_JWT);
            }

            DeliveryLocations getLocation = locationProvider.getLocation(userId);//getUser(userIdx)를 받아와서 반환한다.
            return new BaseResponse<>(getLocation);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }

    }

////////////////////////////////////////////////////여기까지 체크함


    /**
     * 배송지 추가 API
     * [POST] /locations
     * @return BaseResponse<PostUserRes>
     */
    // Body

    @ResponseBody
    @PostMapping("")
    public BaseResponse<PostLocationRes> createLocation(@RequestBody PostLocationReq postLocationReq) {
        // TODO: email 관련한 짧은 validation 예시입니다. 그 외 더 부가적으로 추가해주세요!

        try{
            if(postLocationReq.getUserId() == 0){
                return new BaseResponse<>(POST_LOCATIONS_EMPTY_USERID);
            }
            //탈퇴한 유저인지 체크
            int checkStatus = locationProvider.checkStatus(postLocationReq.getUserId());
            if(checkStatus == 0){//탈퇴한 유저라면
                return new BaseResponse<>(POST_USERS_INVALID_USER);
            }
            //주소가 입력이 안된 경우
            if(postLocationReq.getLocation() == null){
                return new BaseResponse<>(POST_LOCATIONS_EMPTY_LOCATION);
            }

            float []arr = kakaoLocation(postLocationReq.getLocation());
            postLocationReq.setX(arr[0]);
            postLocationReq.setY(arr[1]);

            PostLocationRes postLocationRes = locationService.createLocation(postLocationReq);
            return new BaseResponse<>(postLocationRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }


    /**
     * 배송지 주소 변경 API
     */

    @ResponseBody
    @PatchMapping("/{locationId}")
    public BaseResponse<String> modifyLocationName(@PathVariable("locationId") int locationId, @RequestBody Location location){
        try {
            //jwt에서 idx 추출.
            int userIdByJwt = jwtService.getUserId();
            //locationId로 유저 인덱스 조회
            int userId = locationProvider.checkUserId(locationId);
            //userIdx와 접근한 유저가 같은지 확인
            if(userId != userIdByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }

            //같다면 배송지 변경
            //좌표도 같이 수정해줘야한다.
            float []arr = kakaoLocation(location.getLocation());

            PatchLocation patchLocation = new PatchLocation(locationId,location.getLocation(), arr[0], arr[1]);
            locationService.modifyLocationName(patchLocation);

            String result = "";
        return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    //배송지 삭제
    @ResponseBody
    @PatchMapping("/{locationId}/status")
    public BaseResponse<String> modifyLocationStatus(@PathVariable("locationId") int locationId, @RequestBody Location location){
        try {
            //jwt에서 idx 추출.
            int userIdByJwt = jwtService.getUserId();
            //userIdx와 접근한 유저가 같은지 확인
            int userId = locationProvider.checkUserId(locationId);
            if(userId != userIdByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            if(location.getStatus() == null){
                return new BaseResponse<>(POST_LOCATIONS_EMPTY_STATUS);
            }
            if(location.getStatus().equals("Invalid")){
                //현재 배송지 상태가 Valid 인지 확인해야함 ->service에서 처리해줄것.


                //같다면 배송 상태 변경
                StatusLocation statusLocation = new StatusLocation(locationId,location.getStatus());
                locationService.modifyLocationStatus(statusLocation);

                String result = "";
                return new BaseResponse<>(result);
            } else{
                return new BaseResponse<>(POST_LOCATIONS_STATUS);
            }


        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    public float[] kakaoLocation(String location) throws BaseException {
        try{
            RestTemplate restTemplate = new RestTemplate();
            String appKey = "KakaoAK " + "457e9054ba0d5d4ce63d778a4c052a38";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);//JSON 변환
            headers.set("Authorization", appKey); //appKey 설정 ,KakaoAK kkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk 이 형식 준수


            HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
            String tempurl = "https://dapi.kakao.com/v2/local/search/address.json?query=" + URLEncoder.encode(location, "UTF-8");
            URI url=URI.create(tempurl);

            ResponseEntity<String> response= restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
//String 타입으로 받아오면 JSON 객체 형식으로 넘어옴
            JSONParser jsonParser = new JSONParser();

            JSONObject jsonObject = (JSONObject) jsonParser.parse(response.getBody().toString());
//저는 Body 부분만 사용할거라 getBody 후 JSON 타입을 인자로 넘겨주었습니다
//헤더도 필요하면 getBody()는 사용할 필요가 없겠쥬
            JSONArray docuArray = (JSONArray) jsonObject.get("documents");
            JSONObject docuObject = (JSONObject) docuArray.get(0);//배열 0번째 요소 불러오고
            //좌표를 배열에 담아 리턴한다.
            float []arr = new float[2];
            arr[0] = Float.parseFloat(docuObject.get("x").toString());
            arr[1] = Float.parseFloat(docuObject.get("y").toString());
            return arr;

        }catch(Exception ignored) {
            throw new BaseException(JSON_PARSE_ERROR);
        }

    }


}
