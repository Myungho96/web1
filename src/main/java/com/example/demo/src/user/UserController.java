package com.example.demo.src.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
//카카오 api를 위해 추가
import org.springframework.http.HttpHeaders;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.http.MediaType;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import static com.example.demo.config.BaseResponseStatus.JSON_PARSE_ERROR;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpMethod;
import java.net.URI;
import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.*;

@RestController
@RequestMapping("/users")   //이 주소로 요청이 오면 일단 여기로 연결이 된다.
public class UserController {   //유저 컨트롤러 실행!
    final Logger logger = LoggerFactory.getLogger(this.getClass()); //로거는 잘 모르겠음..

    @Autowired
    private final UserProvider userProvider;
    @Autowired
    private final UserService userService;
    @Autowired
    private final JwtService jwtService;    //필요한 객체들을 정의하고 밑의 생성자에서 받아와서 생성해준다.




    public UserController(UserProvider userProvider, UserService userService, JwtService jwtService){
        this.userProvider = userProvider;
        this.userService = userService;
        this.jwtService = jwtService;
    }   //IoC 기능. 일반적으로는 클래스를 만들때 필요한 객체가 있으면 스스로 만들어 써야했는데, IoC의 경우 필요한 객체가 있으면 `선언만 해놓고 외부에서 받아서 쓸 수 있다.`

    /**
     * 회원 조회 API
     * [GET] /users
     * 회원 번호 및 이메일 검색 조회 API
     * [GET] /users? Email=
     * @return BaseResponse<List<GetUserRes>>
     */
    //Query String
    @ResponseBody
    @GetMapping("") // (GET) 127.0.0.1:9000/users
    public BaseResponse<List<GetUserRes>> getUsers(@RequestParam(required = false) String email) {
        try{//함수가 있는데, email값을 넣으면 그 이메일의 유저를 조회해주고 안넣으면 전체 유저를 조회해준다.
            if(email == null){//이메일이 없으면,
                List<GetUserRes> getUsersRes = userProvider.getUsers();//userProvider의 getUsers의 결과값을 리스트에 담아서
                return new BaseResponse<>(getUsersRes);//BaseResponse가 뭔지 모르겠다. 리스트를 반환하는듯
            }
            // Get Users
            List<GetUserRes> getUsersRes = userProvider.getUsersByEmail(email);//쿼리 스트링으로 이메일 값을 받은 경우엔, 입력한 이메일과 같은 유저를 찾아 반환한다.
            return new BaseResponse<>(getUsersRes);
        } catch(BaseException exception){//예외처리
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 회원 1명 조회 API
     * [GET] /users/:userId
     * @return BaseResponse<GetUserRes>
     */

    // Path-variable
    @ResponseBody
    @GetMapping("/{userId}") // (GET) 127.0.0.1:9000/users/:userId 이런 형식으로 입력이 된경우
    public BaseResponse<GetUserRes> getUser(@PathVariable("userId") int userId) { //API 통신 결과에 의한 성공 및 실패를 BaseResponse를 사용해 처리해줍니다. 입력된 인덱스를 PathVariable로 받는다.
        // Get Users


        try{
            //jwt에서 idx 추출.
            int userIdByJwt = jwtService.getUserId();
            //userIdx와 접근한 유저가 같은지 확인
            if(userId != userIdByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            GetUserRes getUserRes = userProvider.getUser(userId);//getUser(userIdx)를 받아와서 반환한다.
            return new BaseResponse<>(getUserRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }

    }




    /**
     * 회원가입 API
     * [POST] /users
     * @return BaseResponse<PostUserRes>
     */
    // Body
    @ResponseBody
    @PostMapping("")
    public BaseResponse<PostUserRes> createUser(@RequestBody PostUserReq postUserReq) {
        // TODO: email 관련한 짧은 validation 예시입니다. 그 외 더 부가적으로 추가해주세요!
        if(postUserReq.getEmail() == null){
            return new BaseResponse<>(POST_USERS_EMPTY_EMAIL);
        }
        //이메일 정규표현
        if(!isRegexEmail(postUserReq.getEmail())){
            return new BaseResponse<>(POST_USERS_INVALID_EMAIL);
        }

        //이름 입력 체크
        if(postUserReq.getName() == null){
            return new BaseResponse<>(POST_USERS_EMPTY_NAME);
        }
        //아이디 입력 체크
        if(postUserReq.getIdUser() == null){
            return new BaseResponse<>(POST_USERS_EMPTY_IDUSER);
        }
        //비밀번호 입력 체크
        if(postUserReq.getPasswd() == null){
            return new BaseResponse<>(POST_USERS_EMPTY_PASSWD);
        }
        //휴대폰번호 입력 체크
        if(postUserReq.getPhone() == null){
            return new BaseResponse<>(POST_USERS_EMPTY_PHONE);
        }
        //휴대폰 정규표현
        if(!isRegexPhone(postUserReq.getPhone())){
            return new BaseResponse<>(POST_USERS_INVALID_PHONE);
        }

        try{
            PostUserRes postUserRes = userService.createUser(postUserReq);
            return new BaseResponse<>(postUserRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
    /**
     * 로그인 API
     * [POST] /users/logIn
     * @return BaseResponse<PostLoginRes>
     */
    @ResponseBody
    @PostMapping("/logIn")
    public BaseResponse<PostLoginRes> logIn(@RequestBody PostLoginReq postLoginReq){
        try{
            // TODO: 로그인 값들에 대한 형식적인 validatin 처리해주셔야합니다!
            if(postLoginReq.getEmail() == null){
                return new BaseResponse<>(POST_USERS_EMPTY_EMAIL);
            }
            //이메일 정규표현
            if(!isRegexEmail(postLoginReq.getEmail())){
                return new BaseResponse<>(POST_USERS_INVALID_EMAIL);
            }
            if(postLoginReq.getPasswd() == null){//비밀번호 입력 안했을경우
                return new BaseResponse<>(POST_USERS_EMPTY_PASSWD);
            }
            // TODO: 유저의 status ex) 비활성화된 유저, 탈퇴한 유저 등을 관리해주고 있다면 해당 부분에 대한 validation 처리도 해주셔야합니다.
            int checkStatus = userProvider.checkStatus(postLoginReq.getEmail());
            if(checkStatus == 0){//탈퇴한 유저라면
                return new BaseResponse<>(POST_USERS_INVALID_USER);
            }
            PostLoginRes postLoginRes = userProvider.logIn(postLoginReq);
            return new BaseResponse<>(postLoginRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 유저정보변경 API
     * [PATCH] /users/:userId
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/{userId}")
    public BaseResponse<String> modifyUserName(@PathVariable("userId") int userId, @RequestBody User user){
        try {
            //jwt에서 idx 추출.
            int userIdByJwt = jwtService.getUserId();
            //userIdx와 접근한 유저가 같은지 확인
            if(userId != userIdByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            //같다면 유저네임 변경
            PatchUserReq patchUserReq = new PatchUserReq(userId,user.getName(),user.getIdUser(),user.getPasswd(),user.getPhone(),user.getEmail());
            if(user.getName() != null){
                userService.modifyUserName(patchUserReq);
            }
            if(user.getIdUser() != null){
                userService.modifyUserId(patchUserReq);
            }
            if(user.getPhone() != null){
                if(!isRegexPhone(user.getPhone())){
                    return new BaseResponse<>(POST_USERS_INVALID_PHONE);
                }
                userService.modifyUserPhone(patchUserReq);
            }
            if(user.getPasswd() != null){
                userService.modifyUserPasswd(patchUserReq);
            }

            String result = "";
        return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @PatchMapping("/{userId}/status")
    public BaseResponse<String> modifyUserStatus(@PathVariable("userId") int userId, @RequestBody User user){
        try {
            //jwt에서 idx 추출.
            int userIdByJwt = jwtService.getUserId();
            //userIdx와 접근한 유저가 같은지 확인
            if(userId != userIdByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            if(user.getStatus().equals("Invalid")){
                //같다면 유저 탈퇴처리
                StatusUserReq statusUserReq = new StatusUserReq(userId,user.getStatus());
                userService.modifyUserStatus(statusUserReq);

                String result = "";
                return new BaseResponse<>(result);
            }else{
                return new BaseResponse<>(POST_USERS_STATUS);
            }

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    //private KakaoLogin kakao_restapi = new KakaoLogin();

    @GetMapping(value="/oauth")
    public String kakaoConnect() {

        String codeUrl = "https://kauth.kakao.com/oauth/authorize?client_id=" + "457e9054ba0d5d4ce63d778a4c052a38"
        +"&redirect_uri=http://hoyhoy.shop/users/callback&response_type=code";

        return "redirect:" + codeUrl;
    }

    @GetMapping(value="/callback")
    public String kakaoLoginCode(@RequestParam("code")String code)throws BaseException {
        String token = kakaoGetCode(code);
        return token;
    }
    //엑세스 토큰 받는 과정과 엑세스 토큰으로 조회하는 과정을 따로 분리해놓은 api
    @ResponseBody
    @GetMapping(value = "/logIn/kakao")
    public BaseResponse<PostUserRes> kakaoLogin(@RequestParam("token") String token) throws BaseException {
        try {
            //String token = kakaoGetCode(code);
            KakaoLoginReq kakaoLoginReq = kakaoGetInfo(token);
            PostUserRes postUserRes = userService.createKakaoLogin(kakaoLoginReq);
            return new BaseResponse<>(postUserRes);

        }catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }


    }


    public  String kakaoGetCode(String code) throws BaseException{
        try {
            RestTemplate restTemplate = new RestTemplate();
            String tokenUrl = "https://kauth.kakao.com/oauth/token?grant_type=authorization_code&client_id="
                    +"457e9054ba0d5d4ce63d778a4c052a38&redirect_uri=http://hoyhoy.shop/users/callback"
                    +"&code="+code;
            URI url=URI.create(tokenUrl);

            HttpHeaders tokenHeaders = new HttpHeaders();
            tokenHeaders.setContentType(MediaType.APPLICATION_JSON);//JSON 변환
            HttpEntity<String> tokenEntity = new HttpEntity<String>("parameters", tokenHeaders);

            ResponseEntity<String> tokenRes = restTemplate.exchange(url, HttpMethod.GET, tokenEntity, String.class);

            JSONParser jsonParser = new JSONParser();


            JSONObject tokenObject = (JSONObject) jsonParser.parse(tokenRes.getBody());
            return tokenObject.get("access_token").toString();
        }
        catch(Exception ignored){
            throw new BaseException(JSON_PARSE_ERROR);
        }
    }








    public KakaoLoginReq kakaoGetInfo(String token) throws BaseException{

        try {
            RestTemplate restTemplate = new RestTemplate();
            String infoUrl = "https://kapi.kakao.com/v2/user/me";
            URI url=URI.create(infoUrl);
            HttpHeaders infoHeaders = new HttpHeaders();
            infoHeaders.setContentType(MediaType.APPLICATION_JSON);
            infoHeaders.set("Authorization", "Bearer " + token);
            HttpEntity<String> infoEntity = new HttpEntity<String>("parameters", infoHeaders);
            ResponseEntity<String> infoRes = restTemplate.exchange(url, HttpMethod.GET, infoEntity, String.class);
            JSONParser jsonParser = new JSONParser();
            JSONObject profileObject = (JSONObject) jsonParser.parse(infoRes.getBody());
            JSONObject account = (JSONObject) profileObject.get("kakao_account");
            JSONObject profile = (JSONObject) account.get("profile");
            return new KakaoLoginReq(profile.get("nickname").toString(),account.get("email").toString());
        }
        catch(Exception ignored) {
            throw new BaseException(JSON_PARSE_ERROR);
        }

    }



}