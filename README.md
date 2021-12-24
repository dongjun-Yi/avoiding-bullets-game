# 자바 GUI 그래픽과 스레드를 이용한 avoiding-bullets-game
 무작위로 날라다니는 총알을 키보드 방향키로 총알을 피해 살아남는 게임

## game description
 * 게임이 시작되면 총알들이 무작위로 날라다닌다.
 * 플레이어는 키보드 방향키로 상하좌우 움직일 수 있으며 이를 이용해 총알을 피할 수 있다.
 * 총알을 피하면서 계속 생존하면 점수가 오른다.
 * 만약 플레이어가 총알과 부딪힌다면 게임이 종료된다는 메세지와 함께 메세지상단에 점수가 표시된다.
 * 확인버튼을 누르게 되면 다시 게임이 시작되며 사용자가 원할때 x를 눌러 게임을 종료할 수 있다.
 
 
 ![게임 실행 화면](https://user-images.githubusercontent.com/90665186/147353837-47ba93c4-485f-4e33-8fe4-e537c258976b.png)
 
 ## 각 클래스와 메소드의 역할
  #### Ball class
   * 총알의 좌표와 속도를 나타내는 클래스
   
  #### Game class
  * paint 메소드 : 배경, 플레이어(비행기), 총알 그리는 메소드
  * update 메소드 : 버퍼 이미지 그리는 메소드
  * run 메소드 : repaint()를 사용하여 프레임 화면을 갱신해주는 메소드 
  * BallGenerator 클래스(스레드) : 총알의 위치를 랜덤으로 지정해주는 스레드
  * TakeBallsOutFromArray 메소드 : 총알이 프레임 바깥으로 나갔을 경우의 처리와 총알의 속도를 제어하는 메서드
  * moveplayer 메소드 : 키보드 입력을 받아 플레이어 위치 변경해주는 메소드
  * PlayerBallBumped 메소드 : 총알과 플레이어가 충돌했을 경우 게임을 끝나게 하는 메소드
  * main 메소드 : 게임을 실행시키는 메소드

 
