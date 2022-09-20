![인증관련 architecture.png](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/e61ab37b-1b33-4727-a964-a6ca12ae2697/%EC%9D%B8%EC%A6%9D%EA%B4%80%EB%A0%A8_architecture.png)


## 👉 JWT를 쓰는 이유
사용자 인증에 필요한 모든 정보는 토큰 자체에 포함하기 때문에 별도의 인증 저장소가 필요없다는 것
분산 마이크로 서비스 환경에서 중앙 집중식 인증 서버와 데이터베이스에 의존하지 않는 쉬운 인증 및 인가 방법을 제공
<br>

<br>
## AccessToken, RefreshToken을 탈취 당한다면?
<aside>
💡 **먼저, 이해를 돕기위해 아래와 같은 상태이다.**

1. 클라이언트가 Access Token, Refresh Token 둘다 들고있는다.
2. DB에 두 값을 모두 저장한다.

**Q1. AccessToken을 탈취당하면?**

AccessToken 은 짧은 주기를 가지므로 큰 위험이 없고 Refresh Token을 들고있지않은 클라이언트는 재발급을 받을수 없다.

**Q2. RefreshToken을 탈취당하면?**

정상적인 사용자일 경우 기존의 AccessToken으로 접근하면 DB에 저장된 AccessToken과 비교하여 검증한다.

공격자는 탈취한 RefreshToken으로 새로 AccesToken을 생성하여 서버측에 전송하면 서버는 DB에 저장된
AccesToken과 공격자에게 받은 AccesToken이 다른것을 확인한다.

만약 DB에 저장된 토큰이 만료되지 않았을 경우, (굳이 AccessToken을 새로 생성할 이유가 없는 경우)
이는 서버가 RefreshToken을 탈취 당했다고 생각하고 두 토큰을 폐기 시킨다.

</aside>

<img width="920" alt="인증관련_architecture" src="https://user-images.githubusercontent.com/56526225/191224926-8d699e20-cd5a-4cb2-8ea1-45a795e8ff15.png">
