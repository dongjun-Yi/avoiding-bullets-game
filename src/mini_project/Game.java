package mini_project;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
//�Ѿ� ���� Ŭ����(�Ѿ� ��ǥ�� �ӵ� ���� ����)
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
	ImageIcon ib= new ImageIcon("images/back.png");//����̹���
	Image imgb =ib.getImage();
	
	ImageIcon ia= new ImageIcon("images/airplane.png"); //�÷��̾� �̹���(�����)
	Image ap =ia.getImage();
	//�÷��̾� ��ǥ
	int xpos=100;
	int ypos=100;
	
	Image bufferImage;//ȭ���� �����Ÿ��� ���� ���ֱ����� ���� ������ �����̹��������� �׸��� ���� �̹��� ��ü
	Graphics ge;
	ArrayList balls = new ArrayList();//�Ѿ��� �����ϱ� ���� �÷���
	
	
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
		bufferImage = createImage(500,400); //������ �̹��� �����
		ge=bufferImage.getGraphics(); //������ �̹����� �׷� repaint()�� ���� ȭ�� �������� ���ش�
		ge.drawImage(imgb,0,0,null);
		ge.drawImage(ap,xpos,ypos,this);
		
		ge.setColor(Color.RED);
        int size = balls.size();
        for (int i = 0; i < size; i++) {
            Ball b = (Ball) balls.get(i); 
            ge.fillOval(b.x, b.y,10, 10); //�Ѿ���  bufferimage�� �׸���
            
        }
        
        update(g);
     
	}
	 //repaint()�� ȣ���ϰ� �Ǹ� update()->paint()������ ó�� �Ǳ� ������ update�� �������̵� ���ش�.
	@Override
	public void update(Graphics g) {
		g.drawImage(bufferImage, 0, 0, this);
	}
	
	
	@Override
	public void run() {
		new BallGenerator().start(); //�Ѿ��� ��ġ�� �������� �������ִ� ������ ����
		while(isRunning) {
			try {
                Thread.sleep(15);
	            
	        } catch (Exception e){
	            e.printStackTrace();
	        }
			
			takeBallsOutFromArray(); //�Ѿ��� ������ �ٱ����� ���������� ó���� �ӵ� ����
			movePlayer(); // �÷��̾� ������
			PlayerBallBumped(); //�÷��̾�� �Ѿ��� �ε������� ó��
			repaint(); //�ٽñ׸���
			count++; //���������� ���� count������ ����
        }
			
			
	}


	//�Ѿ��� ��ġ�� �������� �������ִ� ������
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
	
	//�Ѿ��� ������ �ٱ����� ���������� ��ġ ������ �Ѿ��� �ӵ� ����
	 private void takeBallsOutFromArray() {
	        int size = balls.size();
	        for (int i = 0; i < size; i++) {
	            Ball b = (Ball) balls.get(i); 

	            b.x += b.xSp;
	            b.y += b.ySp;

	            if (b.y <= TOP) {
	                b.y = TOP;
	                b.ySp = -b.ySp; //�ӵ��� -�� �ٲ�
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
	 //Ű���� ����Ű ������ �� ������ ��ǥ 5��ŭ �̵� 
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
	 
	 //�Ѿ��̶� �÷��̾ �ε��������� ó�� �޼ҵ�
	 private void PlayerBallBumped() {
	        int size = balls.size();  
	        
	        for (int i = 0; i < size; i++) { 
	            Ball b = (Ball) balls.get(i);
	            int Circlex=b.x;
	            int Circley=b.y;
	            
	            //�Ѿ��� ������ 10*10 �簢���̶�� �����ϰ� �� ��ǥ�� �÷��̾ �ִٸ� �浹�ߴٰ� �ۼ�
	            if(xpos<=Circlex+10 && Circlex-10<=xpos && ypos<=Circley+10 && Circley-10<=ypos) {
	            	isRunning = false;
	                this.setVisible(false);
	                this.dispose(); //�ش������Ӹ�����
	                JOptionPane.showMessageDialog(this, "GameOver", "����: "+count+"��", JOptionPane.ERROR_MESSAGE);
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
