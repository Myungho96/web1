package com.example.demo.src.menu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.menu.model.*;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.isRegexEmail;

@RestController
@RequestMapping("/menu")   //이 주소로 요청이 오면 일단 여기로 연결이 된다.
public class MenuController {   //유저 컨트롤러 실행!
    final Logger logger = LoggerFactory.getLogger(this.getClass()); //로거는 잘 모르겠음..

    @Autowired
    private final MenuProvider menuProvider;
    @Autowired
    private final MenuService menuService;
    @Autowired
    private final JwtService jwtService;    //필요한 객체들을 정의하고 밑의 생성자에서 받아와서 생성해준다.




    public MenuController(MenuProvider menuProvider, MenuService menuService, JwtService jwtService){
        this.menuProvider = menuProvider;
        this.menuService = menuService;
        this.jwtService = jwtService;
    }   //IoC 기능. 일반적으로는 클래스를 만들때 필요한 객체가 있으면 스스로 만들어 써야했는데, IoC의 경우 필요한 객체가 있으면 `선언만 해놓고 외부에서 받아서 쓸 수 있다.`


    /**
     * 특정 음식점 영업시간 조회 API
     * [GET] /open-hours/:restaurantId
     * @return BaseResponse<GetLocation>
     */

    // Path-variable
    @ResponseBody
    @GetMapping("/{restaurantId}") // (GET) 127.0.0.1:9000/location/:userId 이런 형식으로 입력이 된경우
    public BaseResponse<DeliveryMenu> deliveryMenu(@PathVariable("restaurantId") int restaurantId) { //API 통신 결과에 의한 성공 및 실패를 BaseResponse를 사용해 처리해줍니다. 입력된 인덱스를 PathVariable로 받는다.
        //Valid한 음식점인지 체크해야함.
        
        try{
            DeliveryMenu deliveryMenu = menuProvider.deliveryMenu(restaurantId);//getUser(userIdx)를 받아와서 반환한다.
            return new BaseResponse<>(deliveryMenu);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }

    }

////////////////////////////////////////////////////이 밑의 api는 사용하지 않음.

    @ResponseBody
    @PostMapping("")
    public BaseResponse<PostMenuRes> createMenu(@RequestBody PostMenuReq postMenuReq) {
        try{
            PostMenuRes postMenuRes = menuService.createMenu(postMenuReq);
            return new BaseResponse<>(postMenuRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    

    //메뉴 이름 수정
    @ResponseBody
    @PatchMapping("/{menuId}")
    public BaseResponse<String> modifyMenuName(@PathVariable("menuId") int menuId, @RequestBody Menu menu){
        try {
            //jwt에서 idx 추출.
            //int userIdByJwt = jwtService.getUserId();
            //userIdx와 접근한 유저가 같은지 확인
            //if(userId != userIdByJwt){
            //    return new BaseResponse<>(INVALID_USER_JWT);
            //}
            //같다면 유저네임 변경
            PatchMenu patchMenu = new PatchMenu(menuId,menu.getName());
            menuService.modifyMenuName(patchMenu);

            String result = "";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }





}
