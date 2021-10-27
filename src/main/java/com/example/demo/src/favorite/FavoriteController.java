package com.example.demo.src.favorite;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.favorite.model.*;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.isRegexImage;

@RestController
@RequestMapping("/favorites")   //이 주소로 요청이 오면 일단 여기로 연결이 된다.
public class FavoriteController {   //유저 컨트롤러 실행!
    final Logger logger = LoggerFactory.getLogger(this.getClass()); //로거는 잘 모르겠음..

    @Autowired
    private final FavoriteProvider favoriteProvider;
    @Autowired
    private final FavoriteService favoriteService;
    @Autowired
    private final JwtService jwtService;    //필요한 객체들을 정의하고 밑의 생성자에서 받아와서 생성해준다.




    public FavoriteController(FavoriteProvider favoriteProvider, FavoriteService favoriteService, JwtService jwtService){
        this.favoriteProvider = favoriteProvider;
        this.favoriteService = favoriteService;
        this.jwtService = jwtService;
    }   //IoC 기능. 일반적으로는 클래스를 만들때 필요한 객체가 있으면 스스로 만들어 써야했는데, IoC의 경우 필요한 객체가 있으면 `선언만 해놓고 외부에서 받아서 쓸 수 있다.`


    // Path-variable
    @ResponseBody
    @GetMapping("/{userId}") // (GET) 127.0.0.1:9000/location/:userId 이런 형식으로 입력이 된경우
    public BaseResponse<DeliveryFavorite> deliveryFavorite(@PathVariable("userId") int userId) { //API 통신 결과에 의한 성공 및 실패를 BaseResponse를 사용해 처리해줍니다. 입력된 인덱스를 PathVariable로 받는다.
        // Get Location
        try{
            //jwt에서 idx 추출.
            int userIdByJwt = jwtService.getUserId();
            //userIdx와 접근한 유저가 같은지 확인
            if(userId != userIdByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            DeliveryFavorite deliveryFavorite = favoriteProvider.deliveryFavorite(userId);//getUser(userIdx)를 받아와서 반환한다.
            return new BaseResponse<>(deliveryFavorite);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }

    }

////////////////////////////////////////////////////여기까지 체크함



    @ResponseBody
    @PostMapping("")
    public BaseResponse<PostFavoriteRes> createFavorite(@RequestBody PostFavoriteReq postFavoriteReq) {
        // TODO: email 관련한 짧은 validation 예시입니다. 그 외 더 부가적으로 추가해주세요!

        try{

            if(postFavoriteReq.getUserId() == 0){
                return new BaseResponse<>(POST_LOCATIONS_EMPTY_USERID);
            }
            //탈퇴한 유저인지 체크
            int checkStatus = favoriteProvider.checkStatus(postFavoriteReq.getUserId());

            if(checkStatus == 0){//탈퇴한 유저라면
                return new BaseResponse<>(POST_USERS_INVALID_USER);
            }
            //음식점이 입력이 안된 경우
            if(postFavoriteReq.getRestaurantId() == 0){
                return new BaseResponse<>(POST_LOCATIONS_EMPTY_RESTAURANTID);
            }
            //음식점이 존재하는지, 존재한다면 Valid한지 체크해야함.
            int checkRestaurantStatus = favoriteProvider.checkRestaurantStatus(postFavoriteReq.getRestaurantId());
            if(checkRestaurantStatus == 0){//존재하지 않는 음식점이라면
                return new BaseResponse<>(POST_FAVORITE_INVALID_RESTAURANTID);
            }


            if(postFavoriteReq.getRestaurantName() == null){
                return new BaseResponse<>(POST_LOCATIONS_EMPTY_RESTAURANTNAME);
            }
            //음식점 이름이 음식점 인덱스와 일치하는지 체크해야할듯
            int checkRestaurantName = favoriteProvider.checkRestaurantName(postFavoriteReq.getRestaurantId(), postFavoriteReq.getRestaurantName());

            if(checkRestaurantName == 0){//인덱스와 입력한 음식점 이름이 다르다면
                return new BaseResponse<>(POST_FAVORITE_INVALID_RESTAURANTNAME);
            }


            if(postFavoriteReq.getImage() == null){
                return new BaseResponse<>(POST_LOCATIONS_EMPTY_IMAGE);
            }
            //이미지 주소 정규식 체크
            if(!isRegexImage(postFavoriteReq.getImage())){
                return new BaseResponse<>(POST_FAVORITE_INVALID_IMAGE);
            }

            PostFavoriteRes postFavoriteRes = favoriteService.createFavorite(postFavoriteReq);
            return new BaseResponse<>(postFavoriteRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }


    //배송지 삭제
    @ResponseBody
    @PatchMapping("/{favoriteId}/status")
    public BaseResponse<String> modifyFavoriteStatus(@PathVariable("favoriteId") int favoriteId, @RequestBody Favorite favorite){
        try {
            //jwt에서 idx 추출.
            int userIdByJwt = jwtService.getUserId();
            //userIdx와 접근한 유저가 같은지 확인
            int userId = favoriteProvider.checkUserId(favoriteId);
            if(userId != userIdByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            if(favorite.getStatus() == null){
                return new BaseResponse<>(POST_FAVORITE_EMPTY_STATUS);
            }
            if(favorite.getStatus().equals("Invalid")){
                //같다면 유저네임 변경
                StatusFavorite statusFavorite = new StatusFavorite(favoriteId,favorite.getStatus());
                favoriteService.modifyFavoriteStatus(statusFavorite);

                String result = "";
                return new BaseResponse<>(result);
            }else{
                return new BaseResponse<>(POST_FAVORITE_STATUS);
            }

        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }


}
