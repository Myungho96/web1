package com.example.demo.src.cart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.cart.model.*;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.isRegexEmail;

@RestController
@RequestMapping("/carts")   //이 주소로 요청이 오면 일단 여기로 연결이 된다.
public class CartController {   //유저 컨트롤러 실행!
    final Logger logger = LoggerFactory.getLogger(this.getClass()); //로거는 잘 모르겠음..

    @Autowired
    private final CartProvider cartProvider;
    @Autowired
    private final CartService cartService;
    @Autowired
    private final JwtService jwtService;    //필요한 객체들을 정의하고 밑의 생성자에서 받아와서 생성해준다.




    public CartController(CartProvider cartProvider, CartService cartService, JwtService jwtService){
        this.cartProvider = cartProvider;
        this.cartService = cartService;
        this.jwtService = jwtService;
    }   //IoC 기능. 일반적으로는 클래스를 만들때 필요한 객체가 있으면 스스로 만들어 써야했는데, IoC의 경우 필요한 객체가 있으면 `선언만 해놓고 외부에서 받아서 쓸 수 있다.`


    /**
     * 특정 유저 카트 조회 API
     * [GET] /carts/:userId
     * @return BaseResponse<GetLocation>
     */

    // Path-variable
    @ResponseBody
    @GetMapping("/{userId}") // (GET) 127.0.0.1:9000/location/:userId 이런 형식으로 입력이 된경우
    public BaseResponse<DeliveryCart> deliveryCart(@PathVariable("userId") int userId) { //API 통신 결과에 의한 성공 및 실패를 BaseResponse를 사용해 처리해줍니다. 입력된 인덱스를 PathVariable로 받는다.
        // Get Location
        try{
            DeliveryCart deliveryCart = cartProvider.deliveryCart(userId);//getUser(userIdx)를 받아와서 반환한다.
            return new BaseResponse<>(deliveryCart);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }

    }

////////////////////////////////////////////////////여기까지 체크함


    /**
     * 배송지 추가 API
     * [POST] /carts
     * @return BaseResponse<PostUserRes>
     */
    // Body

    @ResponseBody
    @PostMapping("")
    public BaseResponse<PostCartRes> createCart(@RequestBody PostCartReq postCartReq) {
        // TODO: email 관련한 짧은 validation 예시입니다. 그 외 더 부가적으로 추가해주세요!

        try{
            PostCartRes postCartRes = cartService.createCart(postCartReq);
            return new BaseResponse<>(postCartRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }



    /**
     * 배송지 주소 변경 API
     */

    //배송지 삭제

    @ResponseBody
    @PatchMapping("/{cartId}/status")
    public BaseResponse<String> modifyCartStatus(@PathVariable("cartId") int cartId, @RequestBody Cart cart){
        try {
            //jwt에서 idx 추출.
            //int userIdByJwt = jwtService.getUserId();
            //userIdx와 접근한 유저가 같은지 확인
            //if(userId != userIdByJwt){
            //    return new BaseResponse<>(INVALID_USER_JWT);
            //}
            //같다면 유저네임 변경
            StatusCart statusCart = new StatusCart(cartId,cart.getStatus());
            cartService.modifyCartStatus(statusCart);

            String result = "";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    //장바구니 수정
    @ResponseBody
    @PatchMapping("/{cartId}")
    public BaseResponse<String> modifyCartMenuNum(@PathVariable("cartId") int cartId, @RequestBody Cart cart){
        try {
            //jwt에서 idx 추출.
            //int userIdByJwt = jwtService.getUserId();
            //userIdx와 접근한 유저가 같은지 확인
            //if(userId != userIdByJwt){
            //    return new BaseResponse<>(INVALID_USER_JWT);
            //}
            //같다면 유저네임 변경
            PatchCart patchCart = new PatchCart(cartId,cart.getMenuNum());
            cartService.modifyCartMenuNum(patchCart);

            String result = "";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }



}
