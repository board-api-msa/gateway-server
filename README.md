# **Gateway Server**

Gateway Server는 마이크로서비스 아키텍처에서 **모든 외부 요청의 진입점** 역할을 합니다.   
클라이언트로부터 들어오는 요청을 적절한 마이크로서비스로 라우팅하며, 리소스를 생성, 변경, 삭제하는 요청 시에는 요청 헤더의 토큰을 검증하여 보안을 강화합니다.
User Service와 토큰 비밀키를 공유하여 요청의 토큰이 User Service에서 발급된 토큰인지 검증합니다.

## **기능**

- **요청 라우팅**: 클라이언트 요청을 분석하여 해당 마이크로서비스로 전달합니다.
- **토큰 검증**: 리소스 생성(POST), 변경(PUT), 삭제(DELETE) 요청 시 요청 헤더에 포함된 토큰을 검증합니다.
- **로드 밸런싱**: 서비스 간 로드 밸런싱을 통해 성능을 향상시킵니다.
- **서비스 발견**: Eureka 등의 서비스 레지스트리와 연동하여 동적으로 서비스 주소를 결정합니다.

## **기술 스택**

- **Spring boot**
- **JSON Web Token (JWT)**
- **Spring Cloud GateWay**
- **Spring Cloud Netflix Eureka Client**
- **Spring Cloud Config Client**
- **Spring Cloud LoadBalancer**