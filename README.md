# Open Source Project - DustSensor (오공파)

블루투스 기반 미세먼지 센서 데이터를 수집하고, 실시간 수치/웹 대시보드/지도로 확인할 수 있는 Android 앱 프로젝트입니다.

## 프로젝트 개요

- **앱 이름**: 오공파
- **플랫폼**: Android (Java)
- **목적**: 센서 측정값(PM1.0, PM2.5, PM10)과 위치 정보를 사용자에게 직관적으로 제공
- **핵심 흐름**
  1. 블루투스로 센서 디바이스 연결
  2. 센서 트리거 후 서버 데이터 조회
  3. 결과 화면/웹 화면/지도 화면에서 상태 확인

## 주요 기능

- **Bluetooth 연결/검색**
  - 페어링된 디바이스 목록 조회
  - 주변 블루투스 디바이스 검색 및 선택 연결
- **미세먼지 수치 표시**
  - PM1.0 / PM2.5 / PM10 수치 표시
  - 수치에 따른 상태 이미지 및 메시지 출력
- **웹 대시보드 연동**
  - 두 개의 웹 페이지를 앱 내 WebView로 제공
  - Swipe Refresh를 통한 새로고침
- **지도 표시**
  - Naver Map 기반 위치 표시
  - 미리 정의된 측정 지점 마커 표시

## 기술 스택

- Java
- Android SDK (minSdk 24, compileSdk 34)
- Retrofit2, Gson
- ViewPager2, Material Components
- Naver Maps SDK, Google Play Services Maps

## 프로젝트 구조

```text
Open_Source_Project_DustSensor/
├─ app/
│  ├─ src/main/java/com/example/myapplication/
│  │  ├─ MainActivity.java          # 탭/권한/네트워크 초기화
│  │  ├─ ConnectionFragment.java    # 블루투스 검색/연결
│  │  ├─ SensingFragment.java       # 센싱 결과 표시
│  │  ├─ WebViewFragment.java       # 웹 대시보드 표시
│  │  └─ MapFragment.java           # 지도/마커 표시
│  └─ src/main/res/                 # 레이아웃, 이미지, 사운드, 문자열
├─ gradle/
└─ build.gradle.kts
```

## 개발 환경 및 실행 방법

### 요구 사항

- Android Studio (최신 안정 버전 권장)
- JDK 17
- Android SDK / Emulator 또는 실제 Android 기기
- 인터넷 연결 (서버 API 및 지도/웹 연동 필요)

### 실행

1. Android Studio에서 프로젝트 열기
2. Gradle Sync 수행
3. 실행 디바이스(에뮬레이터/실기기) 선택
4. 앱 실행

## 사용 방법

1. 앱 실행 후 권한(위치/블루투스/네트워크 등) 허용
2. **Connection** 탭에서 디바이스 검색 또는 페어링 목록 확인
3. 기기 선택 후 연결
4. 센싱 전송 버튼으로 측정 요청
5. **Advertising(Result)** 탭에서 수치 및 상태 확인
6. **CheckingPage** 탭에서 웹 대시보드 확인
7. **Location** 탭에서 측정 지점 위치 확인

## 테스트

로컬에서 아래 명령으로 단위 테스트를 실행할 수 있습니다.

```bash
./gradlew test
```

> 참고: `com.android.application` 플러그인 해석 오류가 발생하면 네트워크 상태를 확인하고, Android Studio에서 **Gradle Sync**를 다시 실행한 뒤 재시도하세요.

## 권한 및 유의사항

- 앱은 블루투스, 위치, 네트워크, 저장소 권한을 사용합니다.
- 서버/웹 URL은 현재 코드에 하드코딩되어 있으므로, 운영 환경에서는 반드시 빌드 설정(`BuildConfig`/`gradle.properties`) 등 외부 설정으로 분리해 관리하는 것을 권장합니다.
- `android:usesCleartextTraffic="true"` 설정을 사용하므로 운영 환경에서는 HTTPS 전환을 권장합니다.

## 라이선스

이 프로젝트는 **Apache-2.0** 라이선스를 따릅니다.
