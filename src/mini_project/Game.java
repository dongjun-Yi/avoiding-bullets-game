package mini_project;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
//총알 구현 클래스(총알 좌표와 속도 변수 생성)
class Ball {
    public static final int SIZE = 0;
	int x = 0;
    int y = 0;
    int[] speed = {1, 2, 3, 4, 5};
    int xSp, ySp;
    Random rd;
    
    Ball(int x, int y) {
        this.x = x;
        this.y = y;

        rd = new Random();

        int s = rd.nextInt(5);
        xSp = speed[s];
        ySp = speed[s];
    }
}


public class Game extends JFrame implements Runnable{
	ImageIcon ib= new ImageIcon("images/back.png");//배경이미지
	Image imgb =ib.getImage();
	
	ImageIcon ia= new ImageIcon("images/airplane.png"); //플레이어 이미지(비행기)
	Image ap =ia.getImage();
	//플레이어 좌표
	int xpos=100;
	int ypos=100;
	
	Image bufferImage;//화면이 깜빡거리는 것을 없애기위해 따로 별도의 가상이미지공간에 그리기 위한 이미지 객체
	Graphics ge;
	ArrayList balls = new ArrayList();//총알을 저장하기 위한 컬렉션
	
	
	int TOP=0;
    int BOTTOM = 400;
    int LEFT=0;
    int RIGHT = 500;
    
    boolean isRunning= true;
    
    private boolean isKeyUp, isKeyDown, isKeyLeft, isKeyRight = false;
    
    int count=0;
    
	public Game() {
		setTitle("mini_project");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(500,400);
		setVisible(true);
		
		addKeyListener(new KeyAdapter() {
			
			@Override
			public void keyPressed(KeyEvent e) {
				switch(e.getKeyCode()) {
				 	case KeyEvent.VK_UP:
		                isKeyUp = true;
		                break;

		            case KeyEvent.VK_DOWN:
		                isKeyDown = true;
		                break;

		            case KeyEvent.VK_LEFT:
		                isKeyLeft = true;
		                break;

		            case KeyEvent.VK_RIGHT:
		                isKeyRight = true;
		                break;
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
				switch (e.getKeyCode()) { 
	            	case KeyEvent.VK_UP:
	            		isKeyUp = false;
	            		break;

	            	case KeyEvent.VK_DOWN:
	                	isKeyDown = false;
	                	break;

	            	case KeyEvent.VK_LEFT:
	            		isKeyLeft = false;
	            		break;

	            	case KeyEvent.VK_RIGHT:
	            		isKeyRight = false;
	            		break;
				}
			}
		});
	}
	
	@Override
	public void paint(Graphics g) {
		bufferImage = createImage(500,400); //가상의 이미지 만들기
		ge=bufferImage.getGraphics(); //가상의 이미지에 그려 repaint()로 인한 화면 깜빡임을 없앤다
		ge.drawImage(imgb,0,0,null);
		ge.drawImage(ap,xpos,ypos,this);
		
		ge.setColor(Color.RED);
        int size = balls.size();
        for (int i = 0; i < size; i++) {
            Ball b = (Ball) balls.get(i); 
            ge.fillOval(b.x, b.y,10, 10); //총알을  bufferimage에 그린다
            
        }
        
        update(g);
     
	}
	 //repaint()를 호출하게 되면 update()->paint()순으로 처리 되기 떄문에 update를 오버라이드 해준다.
	@Override
	public void update(Graphics g) {
		g.drawImage(bufferImage, 0, 0, this);
	}
	
	
	@Override
	public void run() {
		new BallGenerator().start(); //총알의 위치를 랜덤으로 지정해주는 쓰레드 실행
		while(isRunning) {
			try {
                Thread.sleep(15);
	            
	        } catch (Exception e){
	            e.printStackTrace();
	        }
			
			takeBallsOutFromArray(); //총알이 프레임 바깥으로 나갔을때의 처리와 속도 조정
			movePlayer(); // 플레이어 움직임
			PlayerBallBumped(); //플레이어와 총알이 부딪힐떄의 처리
			repaint(); //다시그리기
			count++; //점수측정을 위해 count변수로 측정
        }
			
			
	}


	//총알의 위치를 랜덤으로 지정해주는 쓰레드
	class BallGenerator extends Thread { 

		@Override
		public void run() {
			 int x, y;
		        while (true) {
		            x = (int) (Math.random()* (RIGHT - Ball.SIZE));
		            y = (int) (Math.random()* (BOTTOM - Ball.SIZE));
		            
		            
		            
		            balls.add(new Ball(x, y));

		            try {
		                Thread.sleep(700);
		            } catch (Exception e) {
		            		
		            }
		        }
		}
	    
	}
	
	//총알이 프레임 바깥으로 나갔을떄의 위치 조정과 총알의 속도 조정
	 private void takeBallsOutFromArray() {
	        int size = balls.size();
	        for (int i = 0; i < size; i++) {
	            Ball b = (Ball) balls.get(i); 

	            b.x += b.xSp;
	            b.y += b.ySp;

	            if (b.y <= TOP) {
	                b.y = TOP;
	                b.ySp = -b.ySp; //속도를 -로 바꿈
	            }

	            if (b.y >= BOTTOM) {
	                b.y = BOTTOM;
	                b.ySp = -b.ySp;
	            }

	            if (b.x <= LEFT) {
	                b.x = LEFT;
	                b.xSp = -b.xSp;
	            }

	            if (b.x >= RIGHT) {
	                b.x = RIGHT;
	                b.xSp = -b.xSp;
	            }
	        }
	    }
	 //키보드 방향키 누릉때 각 방향의 좌표 5만큼 이동 
	 private void movePlayer() {
	        if (isKeyUp) {
	            ypos -= 5;
	        }

	        if (isKeyDown) {
	            ypos += 5;
	        }

	        if (isKeyLeft) {
	            xpos -= 5;
	        }

	        if (isKeyRight) {
	            xpos += 5;
	        }
	    }
	 
	 //총알이랑 플레이어가 부딪혔을떄의 처리 메소드
	 private void PlayerBallBumped() {
	        int size = balls.size();  
	        
	        for (int i = 0; i < size; i++) { 
	            Ball b = (Ball) balls.get(i);
	            int Circlex=b.x;
	            int Circley=b.y;
	            
	            //총알의 면적을 10*10 사각형이라고 가정하고 이 좌표에 플레이어가 있다면 충돌했다고 작성
	            if(xpos<=Circlex+10 && Circlex-10<=xpos && ypos<=Circley+10 && Circley-10<=ypos) {
	            	isRunning = false;
	                this.setVisible(false);
	                this.dispose(); //해당프레임만종료
	                JOptionPane.showMessageDialog(this, "GameOver", "점수: "+count+"점", JOptionPane.ERROR_MESSAGE);
	                new Game();
	                Thread th= new Thread(new Game());
	        		th.start();
            	}    
        	}
	    }
	
	 
	
	
	public static void main(String[] args) {
		new Game();
		Thread th= new Thread(new Game());
		th.start();
		
	}

}
