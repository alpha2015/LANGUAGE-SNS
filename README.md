#Application Info
사용자들을 대상으로 모국어와 배우고자하는 언어를 매칭시켜 찾아주고자 하는 Appication
<https://github.com/alpha2015/LANGUAGE-SNS>


## 프로젝트 기획 변경사항

### 7.24 일자
* 회원가입 Activity
  * 변경전 : 회원정보 한 항목당 fragment가 존재하여 다음버튼 클릭시 fragment들이 전환
  * 변경후 : 회원정보 항목들 모두 하나의 fragment에서 관리하며 view 숨김/보임 전환으로 회원정보 입력
  * 변경사유 : 불필요한 fragment들 추가로 코드 중복 발생 예상(하나의 fragment로 view visible/gone 조작)
