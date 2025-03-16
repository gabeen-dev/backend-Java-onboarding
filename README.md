# *바로인턴 온보딩 과제 -백엔드(Java)*
  
---  

# 기술 스택
- Language: Java 17
- Framework: Spring Boot 3.4.3
- Security: JWT
- Database: H2 Database
- Test: JUnit5
- Build Tool: Gradle
- Deployment: AWS EC2

---  

- # API 명세서

## 회원가입 API
**Endpoint:** `POST http://43.201.113.12:8080/signup`

### 요청값
```json  
{  
  "username": "JIN HO",  
  "password": "12341234",  
  "nickname": "Mentos"  
}  
``` 

### 회원가입 성공 응답
```json  
{  
  "username": "JIN HO",  
  "password": "12341234",  
  "nickname": "Mentos"  
}  
```  

### 회원가입 실패 (이미 가입된 사용자)
```json  
{  
  "error": {  
    "code": "USER_ALREADY_EXISTS",  
    "message": "이미 가입된 사용자입니다."  
  }  
}  
```  

## 로그인 API
**Endpoint:** `POST http://43.201.113.12:8080/login`

### 요청값
```json  
{  
  "username": "JIN HO",  
  "password": "12341234"  
}  
```  

### 로그인 성공 응답
```json  
{  
  "token": "eKDIkdfjoakIdkfjpekdkcjdkoIOdjOKJDFOlLDKFJKL"  
}  
```  

### 로그인 실패 (잘못된 계정 정보)
```json  
{  
  "error": {  
    "code": "INVALID_CREDENTIALS",  
    "message": "아이디 또는 비밀번호가 올바르지 않습니다."  
  }  
}  
```  

## 관리자 권한 부여 API
**Endpoint:** `PATCH http://43.201.113.12:8080/admin/users/{id}/roles`

### Path Variable
- `{id}`: 권한을 부여할 사용자의 ID

### 관리자 권한 부여 성공 응답
```json  
{  
  "username": "JIN HO",  
  "nickname": "Mentos",  
  "roles": [  
    {  
      "role": "Admin"  
    }  
  ]  
}  
```  

### 권한이 부족한 경우 (접근 제한)
```json  
{  
  "error": {  
    "code": "ACCESS_DENIED",  
    "message": "관리자 권한이 필요한 요청입니다. 접근 권한이 없습니다."  
  }  
}  
```  

### 토큰이 유효하지 않거나 만료된 경우
```json  
{  
  "error": {  
    "code": "INVALID_TOKEN",  
    "message": "유효하지 않은 인증 토큰입니다."  
  }  
}  
```  

### 권한이 부족한 경우
```json  
{  
  "error": {  
    "code": "ACCESS_DENIED",  
    "message": "접근 권한이 없습니다."  
  }  
}  
```  

---   

# EC2 배포
#### 인스턴스 유형: t2.micro
#### 배포 ip: 43.201.113.12
#### 빌드 
```shell
./gradlew clean build 
```
  
---  
# 프로젝트 실행 시나리오

### 회원가입

회원가입 시, **JWT 토큰이 필요하지 않으며** 인증 필터를 통과하지 않습니다.

1. 사용자가 **회원가입 요청**을 보냅니다.

    - 요청 시 `username`, `password`, `nickname`을 입력해야 합니다.

2. 입력한 정보가 유효하면 회원가입이 완료됩니다.

3. 만약 같은 `username`이 이미 존재한다면, 회원가입이 실패하며 `USER_ALREADY_EXISTS` 오류가 반환됩니다.
 
<br>

### 로그인

회원가입이 완료된 사용자는 로그인하여 **JWT 토큰을 발급**받을 수 있습니다.

1. 사용자가 **로그인 요청**을 보냅니다.

    - 요청 시 `username`과 `password`를 입력해야 합니다.

2. 입력한 정보가 올바르면 로그인에 성공하며 **JWT 토큰이 발급**됩니다.

3. 잘못된 아이디 또는 비밀번호를 입력 시 로그인에 실패하며 `INVALID_CREDENTIALS` 오류가 반환됩니다.

4. 로그인 시 **JWT 필터를 통과하지 않으며**, 인증이 필요하지 않습니다.

<br>

### 관리자 권한 부여

관리자는 특정 사용자에게 **관리자(Admin) 권한을 부여할 수 있습니다.** 이 기능을 사용하기 위해서는 **인증된 관리자 계정의 JWT 토큰이 필요**합니다.

#### 초기 관리자 계정 설정

- 초기 데이터 설정을 위해 `DataInitializer` 클래스를 통해 프로텍트가 구동될때 `Admin` 권한을 가진 `user1` 계정이 자동으로 생성 됩니다.
-
- 비밀번호는 `1234`로 설정되며, 암호화되어 저장됩니다.

#### 관리자 권한 부여 과정

1. **관리자가 특정 사용자에게 관리자 권한을 부여하려는 요청을 보냅니다.**

    - 요청 시 대상 사용자의 `userId`를 포함해야 합니다.

2. 요청을 보낸 사용자가 **관리자 권한을 가지고 있다면**, 대상 사용자의 권한이 `Admin`으로 변경됩니다.

3. 만약 요청을 보낸 사용자가 관리자 권한이 없다면, `ACCESS_DENIED` 예외가 반환됩니다.

4. 제공된 JWT 토큰이 유효하지 않거나 만료되었으면, `INVALID_TOKEN` 예외가 반환됩니다.

5. 권한이 없는 사용자가 접근하려고 하면, `ACCESS_DENIED` 예외코드가 반환됩니다.

  
---