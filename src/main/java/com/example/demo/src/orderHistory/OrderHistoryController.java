package com.example.demo.src.orderHistory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.orderHistory.model.*;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.isRegexEmail;

@RestController
@RequestMapping("/order-history")   //이 주소로 요청이 오면 일단 여기로 연결이 된다.
public class OrderHistoryController {   //유저 컨트롤러 실행!
    final Logger logger = LoggerFactory.getLogger(this.getClass()); //로거는 잘 모르겠음..

    @Autowired
    private final OrderHistoryProvider orderHistoryProvider;
    @Autowired
    private final OrderHistoryService orderHistoryService;
    @Autowired
    private final JwtService jwtService;    //필요한 객체들을 정의하고 밑의 생성자에서 받아와서 생성해준다.




    public OrderHistoryController(OrderHistoryProvider orderHistoryProvider, OrderHistoryService orderHistoryService, JwtService jwtService){
        this.orderHistoryProvider = orderHistoryProvider;
        this.orderHistoryService = orderHistoryService;
        this.jwtService = jwtService;
    }   //IoC 기능. 일반적으로는 클래스를 만들때 필요한 객체가 있으면 스스로 만들어 써야했는데, IoC의 경우 필요한 객체가 있으면 `선언만 해놓고 외부에서 받아서 쓸 수 있다.`


    /**
     * 특정 유저 카트 조회 API
     * [GET] /order-history/:userId
     * @return BaseResponse<GetLocation>
     */

    // Path-variable
    @ResponseBody
    @GetMapping("/{userId}") // (GET) 127.0.0.1:9000/location/:userId 이런 형식으로 입력이 된경우
    public BaseResponse<DeliveryOrderHistory> deliveryOrderHistory(@PathVariable("userId") int userId) { //API 통신 결과에 의한 성공 및 실패를 BaseResponse를 사용해 처리해줍니다. 입력된 인덱스를 PathVariable로 받는다.
        // Get Location
        try{
            //jwt에서 idx 추출.
            int userIdByJwt = jwtService.getUserId();
            //userIdx와 접근한 유저가 같은지 확인
            if(userId != userIdByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            DeliveryOrderHistory deliveryOrderHistory = orderHistoryProvider.deliveryOrderHistory(userId);//getUser(userIdx)를 받아와서 반환한다.
            return new BaseResponse<>(deliveryOrderHistory);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }

    }
    



}
