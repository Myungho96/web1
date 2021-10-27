package com.example.demo.config;

import lombok.Getter;

/**
 * 에러 코드 관리
 */
@Getter
public enum BaseResponseStatus {
    /**
     * 1000 : 요청 성공
     */
    SUCCESS(true, 1000, "요청에 성공하였습니다."),


    /**
     * 2000 : Request 오류
     */
    // Common
    REQUEST_ERROR(false, 2000, "입력값을 확인해주세요."),
    EMPTY_JWT(false, 2001, "JWT를 입력해주세요."),
    INVALID_JWT(false, 2002, "유효하지 않은 JWT입니다."),
    INVALID_USER_JWT(false,2003,"권한이 없는 유저의 접근입니다."),
    JSON_PARSE_ERROR(false,2004,"json 데이터 parse 오류입니다."),

    // users
    USERS_EMPTY_USER_ID(false, 2010, "유저 아이디 값을 확인해주세요."),

    // [POST] /users, [POST] /users/logIn
    POST_USERS_EMPTY_EMAIL(false, 2015, "이메일을 입력해주세요."),
    POST_USERS_INVALID_EMAIL(false, 2016, "이메일 형식을 확인해주세요."),
    POST_USERS_EXISTS_EMAIL(false,2017,"중복된 이메일입니다."),
    POST_USERS_EMPTY_NAME(false, 2018, "이름을 입력해주세요."),
    POST_USERS_EMPTY_IDUSER(false, 2019, "아이디를 입력해주세요."),
    POST_USERS_EMPTY_PASSWD(false, 2020, "비밀번호를 입력해주세요."),
    POST_USERS_EMPTY_PHONE(false, 2021, "휴대폰번호를 입력해주세요."),
    POST_USERS_INVALID_PHONE(false, 2022, "휴대폰번호 형식을 확인해주세요."),
    POST_USERS_INVALID_USER(false, 2024, "탈퇴 또는 휴면 유저입니다. 확인해주세요."),
    // [POST] /users/logIn/kakao
    POST_USERS_NOT_CREATED(false, 2023, "회원이 아닙니다. 회원가입을 진행해주세요."),
    //[POST] /locations/:userId
    POST_LOCATIONS_EMPTY_LOCATION(false, 2025, "주소가 입력되지 않았습니다."),
    POST_LOCATIONS_EMPTY_USERID(false, 2026, "유저 인덱스 입력되지 않았습니다."),
    POST_LOCATIONS_EMPTY_RESTAURANTID(false, 2027, "음식점 인덱스가 입력되지 않았습니다."),
    POST_LOCATIONS_EMPTY_RESTAURANTNAME(false, 2028, "음식점 이름이 입력되지 않았습니다."),
    POST_LOCATIONS_EMPTY_IMAGE(false, 2029, "음식점 이미지 주소가 입력되지 않았습니다."),
    POST_FAVORITE_INVALID_RESTAURANTID(false, 2030, "존재하지 않는 음식점입니다. 확인해주세요."),
    POST_FAVORITE_INVALID_RESTAURANTNAME(false, 2031, "입력한 음식점 인덱스와 음식점이 다릅니다. 확인해주세요."),
    POST_FAVORITE_INVALID_IMAGE(false, 2032, "음식점 이미지 경로가 형식에 맞지 않습니다."),
    POST_LOCATIONS_EMPTY_STATUS(false, 2033, "배송지 상태가 입력되지 않았습니다."),
    POST_FAVORITE_EMPTY_STATUS(false, 2034, "즐겨찾기 상태가 입력되지 않았습니다."),
    POST_COUPON_EMPTY_STATUS(false, 2035, "쿠폰 상태가 입력되지 않았습니다."),
    POST_LOCATIONS_STATUS(false, 2036, "배송지 상태가 잘못 입력되었습니다."),
    POST_FAVORITE_STATUS(false, 2037, "즐겨찾기 상태가 잘못 입력되었습니다."),
    POST_COUPON_STATUS(false, 2038, "쿠폰 상태가 잘못 입력되었습니다."),
    POST_USERS_STATUS(false, 2039, "유저 상태가 잘못 입력되었습니다."),
    DB_FAVORITE_INVALID_STATUS(false, 2040, "삭제된 즐겨찾기에 접근하였습니다."),
    DB_LOCATIONS_INVALID_STATUS(false, 2041, "삭제된 주소에 접근하였습니다."),
    DB_COUPON_INVALID_STATUS(false, 2042, "삭제된 쿠폰에 접근하였습니다."),
    /**
     * 3000 : Response 오류
     */
    // Common
    RESPONSE_ERROR(false, 3000, "값을 불러오는데 실패하였습니다."),

    // [POST] /users
    DUPLICATED_EMAIL(false, 3013, "중복된 이메일입니다."),
    FAILED_TO_LOGIN(false,3014,"없는 아이디거나 비밀번호가 틀렸습니다."),



    /**
     * 4000 : Database, Server 오류
     */
    DATABASE_ERROR(false, 4000, "데이터베이스 연결에 실패하였습니다."),
    SERVER_ERROR(false, 4001, "서버와의 연결에 실패하였습니다."),
    CART_ERROR(false, 4002, "카트가 비어있습니다."),
    COUPON_ERROR(false, 4003, "쿠폰함이 비어있습니다."),
    OPENHOUR_ERROR(false, 4004, "영업 시간 정보가 없습니다."),
    MENU_ERROR(false, 4005, "메뉴 정보가 없습니다."),
    ORDERHISTORY_ERROR(false, 4006, "주문 정보가 없습니다."),
    RESTAURANT_ERROR(false, 4007, "해당하는 식당이 없습니다."),
    LOCATION_ERROR(false, 4008, "해당하는 주소가 없습니다."),
    FAVORITE_ERROR(false, 4013, "해당하는 즐겨찾기가 없습니다."),
    COUPON_EMPTY_ERROR(false, 4009, "해당하는 쿠폰이 없습니다."),
    CATEGORY_ERROR(false, 4010, "카테고리가 없습니다."),
    //[PATCH] /users/{userIdx}
    MODIFY_FAIL_USERNAME(false,4014,"유저네임 수정 실패"),
    MODIFY_FAIL_USERID(false,4018,"유저아이디 수정 실패"),
    MODIFY_FAIL_USERPHONE(false,4019,"유저 휴대폰번호 수정 실패"),
    MODIFY_FAIL_USERPASSWD(false,4020,"유저 비밀번호 수정 실패"),
    MODIFY_FAIL_USERSTATUS(false,4021,"유저 탈퇴 처리 실패"),
    MODIFY_FAIL_FAVORITE_STATUS(false,4022,"즐겨찾기 삭제 처리 실패"),

    MODIFY_FAIL_COUPON(false,4015,"쿠폰 상태 수정 실패"),
    MODIFY_FAIL_LOCATION(false,4016,"주소 수정 실패"),
    MODIFY_FAIL_LOCATION_STATUS(false,4017,"주소 삭제 실패"),

    PASSWORD_ENCRYPTION_ERROR(false, 4011, "비밀번호 암호화에 실패하였습니다."),
    PASSWORD_DECRYPTION_ERROR(false, 4012, "비밀번호 복호화에 실패하였습니다.");


    // 5000 : 필요시 만들어서 쓰세요
    // 6000 : 필요시 만들어서 쓰세요


    private final boolean isSuccess;
    private final int code;
    private final String message;

    private BaseResponseStatus(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}
