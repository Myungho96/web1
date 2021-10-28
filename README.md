# Spring Boot CoupangEats DB 모의 설계

### **Folder Structure**

- `src`: 도메인 별로 폴더를 분리해 놓았다.
- `config` 및 `util` 폴더:  `src` 에서 필요한 부차적인 파일들을 모아놓은 폴더이다. 필요한 경우 import 하여 사용한다.
- 도메인 폴더 구조
    
    > controller → service || provider → Dao
- Route:. get,post 등중에 정해야하며 restful하게 /users/:useridx 처럼 설계해줘야한다.
- Controller:
    
     Request에서 보낸 라우팅을 처리한다. restful하게 설계해야함. 리퀘스트값을 라우트에게 받으면 받은 걸 service와 provider로 넘겨준다.
    
    라우트에서 받은 path변수나 쿼리 스트링,바디 등을 받아줘서 필요하다면 파라미터 값으로 밑으로 넘겨준다.
    
    벨리데이션은 컨트롤러에서 처리해야함.
    
- Provider/Service: 비즈니스 로직 처리, 의미적 Validation
    
    Service -  db 커넥션이 일어나야하며, 트렌젝션도 일어나야한다. 인서트,딜리트,업데이트 등을 할때 이용(db를 사용하는 경우)
    
    Provider - 셀렉트 등을 실행할때 이용.
    
- DAO: Data Access Object의 줄임말. Query가 작성되어 있는 곳. 대규모 트래픽을 고려하였을 때 단일 쿼리로 구성하여야 한다.

## **Structure**

앞에 (*)이 붙어있는 파일(or 폴더)은 추가적인 과정 이후에 생성된다.

```
api-server-spring-boot
  > * build
  > gradle
  > * logs
    | app.log // warn, error 레벨에 해당하는 로그가 작성 되는 파일
    | app-%d{yyyy-MM-dd}.%i.gz
    | error.log // error 레벨에 해당하는 로그가 작성 되는 파일
    | error-%d{yyyy-MM-dd}.%i.gz
  > src.main.java.com.example.demo
    > config
      > secret
        | Secret.java // git에 추적되지 않아야 할 시크릿 키 값들이 작성되어야 하는 곳
      | BaseException.java // Controller, Service, Provider 에서 Response 용으로 공통적으로 사용 될 익셉션 클래스
      | BaseResponse.java // Controller 에서 Response 용으로 공통적으로 사용되는 구조를 위한 모델 클래스
      | BaseResponseStatus.java // Controller, Service, Provider 에서 사용 할 Response Status 관리 클래스
      | Constant.java // 공통적으로 사용될 상수 값들을 관리하는 곳
    > src
      > test
        | TestController.java
      > user
        > model
          | GetUserRes.java
          | KakaoLoginReq.java
          | PatchUserReq.java
          | PostLoginReq.java
          | PostLoginRes.java
          | PostUserReq.java
          | PostUserRes.java
          | StatusUserReq.java
          | User.java
        | UserController.java
        | UserProvider.java
        | UserService.java
        | UserDao.java
      > restaurant
        > model
          | DeliveryRestaurant.java
          | DeliveryRestaurantDetail.java
          | DetailList.java
          | ResultList.java
        | RestaurantController.java
        | RestaurantProvider.java
        | RestaurantService.java
        | RestaurantDao.java
      > orderHistory
        > model
          | DeliveryOrderHistory.java
          | ResultList.java
        | OrderHistoryController.java
        | OrderHistoryProvider.java
        | OrderHistoryService.java
        | OrderHistoryDao.java
      > openHour
        > model
          | DeliveryOpenHour.java
          | ResultList.java
        | OpenHourController.java
        | OpenHourProvider.java
        | OpenHourService.java
        | OpenHourDao.java
      > menu
        > model
          | PatchMenu.java
          | PostMenuReq.java
          | PostMenuRes.java
          | DeliveryMenu.java
          | Menu.java
          | ResultList.java
        | MenuController.java
        | MenuProvider.java
        | MenuService.java
        | MenuDao.java
      > location
        > model
          | PatchLocation.java
          | PostLocationReq.java
          | PostLocationRes.java
          | DeliveryLocations.java
          | Location.java
          | StatusLocation.java
        | LocationController.java
        | LocationProvider.java
        | LocationService.java
        | LocationDao.java
      > favorite
        > model
          | NameImage.java
          | PostFavoriteReq.java
          | PostFavoriteRes.java
          | DeliveryFavorite.java
          | Favorite.java
          | StatusFavorite.java
        | FavoriteController.java
        | FavoriteProvider.java
        | FavoriteService.java
        | FavoriteDao.java
      > coupon
        > model
          | ResultList.java
          | DeliveryCoupon.java
          | Coupon.java
          | StatusCoupon.java
        | CouponController.java
        | CouponProvider.java
        | CouponService.java
        | CouponDao.java
      > category
        > model
          | DeliveryCategory.java
          | ResultList.java
        | CategoryController.java
        | CategoryProvider.java
        | CategoryService.java
        | CategoryDao.java
      | WebSecurityConfig.java // spring-boot-starter-security, jwt 를 사용하기 위한 클래스
    > utils
      | AES128.java // 암호화 관련 클래스
      | JwtService.java // jwt 관련 클래스
      | ValidateRegex.java // 정규표현식 관련 클래스
    | DemoApplication // SpringBootApplication 서버 시작 지점
  > resources
    | application.yml // Database 연동을 위한 설정 값 세팅 및 Port 정의 파일
    | logback-spring.xml // logger 사용시 console, file 설정 값 정의 파일
build.gradle // gradle 빌드시에 필요한 dependency 설정하는 곳
.gitignore // git 에 포함되지 않아야 하는 폴더, 파일들을 작성 해놓는 곳

```

API 명세서 : https://docs.google.com/spreadsheets/d/1NxfF2rOpb41iBRTjDviQ_ioYq3o61oSqXTj6iZk8oIo/edit?usp=sharing
