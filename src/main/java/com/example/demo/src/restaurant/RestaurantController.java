package com.example.demo.src.restaurant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.restaurant.model.*;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.isRegexEmail;

@RestController
@RequestMapping("/restaurants")   //이 주소로 요청이 오면 일단 여기로 연결이 된다.
public class RestaurantController {   //유저 컨트롤러 실행!
    final Logger logger = LoggerFactory.getLogger(this.getClass()); //로거는 잘 모르겠음..

    @Autowired
    private final RestaurantProvider restaurantProvider;
    @Autowired
    private final RestaurantService restaurantService;
    @Autowired
    private final JwtService jwtService;    //필요한 객체들을 정의하고 밑의 생성자에서 받아와서 생성해준다.




    public RestaurantController(RestaurantProvider restaurantProvider, RestaurantService restaurantService, JwtService jwtService){
        this.restaurantProvider = restaurantProvider;
        this.restaurantService = restaurantService;
        this.jwtService = jwtService;
    }   //IoC 기능. 일반적으로는 클래스를 만들때 필요한 객체가 있으면 스스로 만들어 써야했는데, IoC의 경우 필요한 객체가 있으면 `선언만 해놓고 외부에서 받아서 쓸 수 있다.`


    /**
     * 특정 음식점 영업시간 조회 API
     * [GET] /open-hours/:restaurantId
     * @return BaseResponse<GetLocation>
     */

    @ResponseBody
    @GetMapping("")
    public BaseResponse<DeliveryRestaurant> deliveryRestaurant(@RequestParam(defaultValue="-1") int categoryId, @RequestParam(required = false) String cheetah) {
        try{//함수가 있는데, categoryId값을 넣으면 그 이메일의 유저를 조회해주고 안넣으면 전체 유저를 조회해준다.
            if(categoryId == -1){//카테고리 아이디가 없으면
                if(cheetah == null){//치타 값도 없으면
                    DeliveryRestaurant deliveryRestaurant = restaurantProvider.deliveryRestaurant();//restaurantProvider의 deliveryRestaurant의 결과값을 리스트에 담아서
                    return new BaseResponse<>(deliveryRestaurant);//리스트를 반환하는듯
                }
                DeliveryRestaurant deliveryRestaurant = restaurantProvider.deliveryRestaurantByCheetah(cheetah);//restaurantProvider의 deliveryRestaurant의 결과값을 리스트에 담아서
                return new BaseResponse<>(deliveryRestaurant);
            }
            // Get Users
            DeliveryRestaurant deliveryRestaurant = restaurantProvider.deliveryRestaurantBycategoryId(categoryId);//쿼리 스트링으로 이메일 값을 받은 경우엔, 입력한 이메일과 같은 유저를 찾아 반환한다.
            return new BaseResponse<>(deliveryRestaurant);
        } catch(BaseException exception){//예외처리
            return new BaseResponse<>((exception.getStatus()));
        }
    }



    // Path-variable
    @ResponseBody
    @GetMapping("/{restaurantId}/detail")
    public BaseResponse<DeliveryRestaurantDetail> deliveryRestaurantDetail(@PathVariable("restaurantId") int restaurantId) { //API 통신 결과에 의한 성공 및 실패를 BaseResponse를 사용해 처리해줍니다. 입력된 인덱스를 PathVariable로 받는다.
        //
        try{
            DeliveryRestaurantDetail deliveryRestaurant = restaurantProvider.deliveryRestaurantDetail(restaurantId);//getUser(userIdx)를 받아와서 반환한다.
            return new BaseResponse<>(deliveryRestaurant);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }

    }




}
