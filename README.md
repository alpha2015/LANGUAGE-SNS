#Application Info
사용자들을 대상으로 모국어와 배우고자하는 언어를 매칭시켜 찾아주고자 하는 Appication
<https://github.com/alpha2015/LANGUAGE-SNS>


## 프로젝트 기획 변경사항

### 7.24 일자
* 회원가입 Activity
  * 변경전 : 회원정보 한 항목당 fragment가 존재하여 다음버튼 클릭시 fragment들이 전환
  * 변경후 : 회원정보 항목들 모두 하나의 fragment에서 관리하며 view 숨김/보임 전환으로 회원정보 입력
  * 변경사유 : 불필요한 fragment들 추가로 코드 중복 발생 예상(하나의 fragment로 view visible/gone 조작)


### 8.6 일자
* 매칭결과 Activity
  * 변경전 : 1) 매칭결과 Activity에서 유저리스트 fragment와 유저 상세보기 fragment가 존재
           2) 리스트 중 유저 선택시 유저 상세보기 fragment로 교체되며 유저 상세보기 페이지 전환
  * 변경후 : 매칭결과 Activity에서 유저리스트 바로 뿌려주고 유저 선택시 상세보기 Activity 호출
  * 변경사유 : 1) 유저를 선택했을때 페이지 전환으로 완전히 다른 화면으로 변하는게 자연스럽다고 판단
             2) 매칭결과 Activity에서 fragment교체를 제외하고 기능이 없음

### 8.14 일자
* 매칭 Activity
  * 변경전 : 1) 언어 선택 fragment, 매칭 시작 fragment, 회원정보 fragment
  * 변경후 : 1) 언어선택 , 매칭시작을 하나의 fragment로 통합
           2) 새로가입 한 유저나 소개글 업데이트한 유저정보 보여주는 timeline fragment 추가(cross 매칭된 유저만 보여줌)
  * 변경사유 : 1) 불필요한 fragment merge
             2) 기획 내용 추가