package com.example.demo.src.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.isRegexEmail;

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
            GetUserRes getUserRes = userProvider.getUser(userId);//getUser(userIdx)를 받아와서 반환한다.
            return new BaseResponse<>(getUserRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }

    }

    //특정 유저 address 조회
    @ResponseBody
    @GetMapping("/{userId}/location") // (GET) 127.0.0.1:9000/users/:userIdx/location 이런 형식으로 입력이 된경우
    public BaseResponse<List<GetLocation>> getLocation(@PathVariable("userId") int userId) { //API 통신 결과에 의한 성공 및 실패를 BaseResponse를 사용해 처리해줍니다. 입력된 인덱스를 PathVariable로 받는다.
        // Get Location
        try{
            List<GetLocation> getLocation = userProvider.getLocation(userId);//getUser(userIdx)를 받아와서 반환한다.
            return new BaseResponse<>(getLocation);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }

    }

    //특정 유저 즐겨찾기 조회
    @ResponseBody
    @GetMapping("/{userId}/favorites") // (GET) 127.0.0.1:9000/users/:userIdx/favorites 이런 형식으로 입력이 된경우
    public BaseResponse<List<GetFavorites>> getFavorites(@PathVariable("userId") int userId) { //API 통신 결과에 의한 성공 및 실패를 BaseResponse를 사용해 처리해줍니다. 입력된 인덱스를 PathVariable로 받는다.
        //  Get Favorites
        try{
            List<GetFavorites> getFavorites = userProvider.getFavorites(userId);//getUser(userIdx)를 받아와서 반환한다.
            return new BaseResponse<>(getFavorites);
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

    @ResponseBody
    @PostMapping("/logIn")
    public BaseResponse<PostLoginRes> logIn(@RequestBody PostLoginReq postLoginReq){
        try{
            // TODO: 로그인 값들에 대한 형식적인 validatin 처리해주셔야합니다!
            // TODO: 유저의 status ex) 비활성화된 유저, 탈퇴한 유저 등을 관리해주고 있다면 해당 부분에 대한 validation 처리도 해주셔야합니다.
            PostLoginRes postLoginRes = userProvider.logIn(postLoginReq);
            return new BaseResponse<>(postLoginRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }*/

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
            //int userIdByJwt = jwtService.getUserId();
            //userIdx와 접근한 유저가 같은지 확인
            //if(userId != userIdByJwt){
            //    return new BaseResponse<>(INVALID_USER_JWT);
            //}
            //같다면 유저네임 변경
            PatchUserReq patchUserReq = new PatchUserReq(userId,user.getName());
            userService.modifyUserName(patchUserReq);

            String result = "";
        return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }


}
