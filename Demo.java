import java.util.*;
import java.io.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.*;


class Screen {
	int height;
	int width;
	Screen(int w, int h){
		width = w;
		height = h;
	}
}

class Spaceship {
	int myScore;
	int x, y; // my position in the screen
	Spaceship(int a, int b) { // to begin with, it appears at mid point bottom line
		x = a ;
		y = b;
	}

	void score() { // 
		myScore += 1 ;
	}

	void lifeCheck() { 

	}

	void crash() {

	}

}

class Rock{
	int x, y;
	Rock (int w, int h) {
		// randomly generated 
		this.x = w; //(w+2) / 2;
		this.y = h; //h / 2; 
	}

	boolean hit(int w, int h, Spaceship spaceship) {
		if ((spaceship.x == x) && (spaceship.y == y)) {
			return false;
		}
		return true;
	}
}

public class Demo extends JFrame {
	static char dir = '0';//
	//static char Predir = '0';
	static int score = 0;
	static boolean alive = true;
	private static JPanel jp = new JPanel();
	// ask for user input later ***************************************************************************
	static int width = 20; 
	static int height = 16;
	static int speed = 0;
	// static 
	static List<Integer> listX = new ArrayList<>();
	static Set<Integer> setX = new HashSet<>(); // keep track of generated x coordinates for rocks

	static List<List<Rock>> allRocks = new ArrayList<>();
	static List<Rock> rocks = initRocks(width, height);
	static int count = 0;
	//speed = ?

	public static void main (String[] args) throws IOException, InterruptedException{
		//System.out.println("START");
		new Demo();
		
		Screen screen = new Screen(width, height);
		Spaceship spaceship = new Spaceship(( width+2) / 2, height - 1); //(11, 15) Spaceship spaceship = new Spaceship(width, height);
		// to begin with, spaceship appears at mid point bottom line within screen. Consider that there are 4 # side bars that not in screen. 
		// >>Might remove side bars >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

		// Rock[] rocks // generate random rocks line by line 
		Rock rock = new Rock(width / 2, height / 2);

		// rocks.set(0, new Rock(11,0));
		// rocks.set(1, new Rock(12,1));
		// rocks.set(2, new Rock(11,2));
		// rocks.set(3, new Rock(1,3));

		//rocks = initRocks(width, height);//generateRocks(width);
		/* Test with a fixed timer*/
		// long start = System.currentTimeMillis();
		// long end = start + 50000; // 60 seconds * 1000 ms/sec
		// (System.currentTimeMillis() < end)
		while (alive) {
			if (count % 20 == 0 ) {
				rocks = initRocks(width, height);	
			}  
			cleanScreen();
			update(spaceship, rock, screen, dir, width, height);
			draw(spaceship, rock, rocks, screen);
			count++;
			if (count % 20 == 0 ) {
				rocks = initRocks(width, height);	
			}
			
			speed = updateSpeed(score);
			count++;
			
		}

		
		System.out.println("Game Over");
	} // end of main

	public static List<Rock> initRocks(int w, int h) {
		List<Rock> res = new ArrayList<>(); 
		Random rand = new Random();

		for (int i = 0; i < w/2 ; i++) {
			res.add(new Rock(Integer.MIN_VALUE, Integer.MIN_VALUE));
		}

		for (int i = 0; i < w/2 ; i++) {
			res.set(i, new Rock(rand.nextInt(w), i));
		}
		// res.set(0, new Rock(11,0));
		// res.set(1, new Rock(11,1));
		// res.set(2, new Rock(11,2));
		// res.set(3, new Rock(11,3));
		return res;
	}
	public static int updateSpeed(int score) throws InterruptedException {	
		if (score <= 200) {
			Thread.sleep(500 - score);
			return (int) (score / 2);
		}
		else {
			Thread.sleep(100);
			return 100;
		}
	}

	public static void cleanScreen() throws IOException, InterruptedException {
		new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();// 
	}

	public static void update(Spaceship spaceship, Rock rock, Screen screen, char direction, int w, int h) { //snake s, food f, map m, char dir, int W, int H
		// spaceship stays inside the screen. Whenever it touches the 4 side bars. it stays until we change direction
		switch (direction) {
		case 'w':
			spaceship.y -= 1;
			break;
		case 's':
			spaceship.y += 1;
			break;
		case 'a':
			spaceship.x -= 1;
			break;
		case 'd':
			spaceship.x += 1;
			break;
		}

		if (spaceship.x == 0) {
				//spaceship.x = 0;
				spaceship.x = 1; //screen.width; // walk through and start over from the opposite side along with the same direction
		} 
		else if (spaceship.x == screen.width + 1) {
			spaceship.x = screen.width ; // 
		} else if (spaceship.y == -1) { // too high up
			spaceship.y = 0;
			//spaceship.y = screen.height - 1;
		} else if (spaceship.y == height){ // s.y = m.height
			spaceship.y = height - 1;
		}

		// check for a rock for test
		if ( (spaceship.x == rock.x) && (spaceship.y == rock.y) ) {
			alive = false;
			return;
		}

		rock.y += 1;

		// check for all rocks from list
		for (Rock r: rocks) {
			if ( (spaceship.x == r.x) && (spaceship.y == r.y) ) {
				alive = false;
				return;
			}
		}

		for (Rock r: rocks) {
			r.y += 1;  
		} 

		if (alive) {
			score += 1;
		}

	}

	public static void draw(Spaceship spaceship, Rock rock, List<Rock> rocks, Screen screen) { // snake s, map m, food f, boolean Gameover
		int w = screen.width; 
		int h = screen.height;
		int spaceship_x = spaceship.x;
		int spaceship_y = spaceship.y;

		System.out.println("Rocks size: " + rocks.size());
		for (int i = 0; i < w+2; i++)
			System.out.print("#");
		System.out.print("\n");
		//System.out.println("Start Line");

		int size = rocks.size();
		int tempIndex = 0;


		if (alive) {
			for (int i = 0; i < h; i++) {
				for (int j = 0; j < w+2 ; j++) {
					if (j == 0) {
						System.out.print("#");
					} 
					else if (j == w + 1) {
						System.out.print("#");
					}
					else if ((i == spaceship_y) && (j == spaceship_x)) {
						System.out.print("*"); 
					}
					else if ( (i == rock.y) && (j == rock.x) ){ 
						System.out.print("O");
					}
					else if (tempIndex < size && i == rocks.get(tempIndex).y && j == rocks.get(tempIndex).x) {
						System.out.print("-");
						tempIndex++;
						// if (tempIndex == size) {
						// 	rocks = initRocks(width, height);
						// }
					}
					else {
						System.out.print(" "); 
					}
				}
				System.out.println();
			}
		}
		

		for (int i = 0; i < w+2 ; i++)
			System.out.print("#");
		System.out.println();
		System.out.println("Score: " + score);
		//System.out.println("Speed : " + speed + "%");
		System.out.println();
	}

		// }
			// try {
			// 	Thread.sleep(500);
			// }catch (InterruptedException ex) {
			// 	Thread.currentThread().interrupt();
			// }
		
		// System.out.print("abcdefg");
		// System.out.print("\r");
		// try{Thread.sleep(3000);}
		// catch (InterruptedException ex) {
		// 	Thread.currentThread().interrupt();
		// }

		//System.out.println("Final Score: " + score);
	//}


	public static void clearScreen(){
    //Clears Screen in java
	    try {
	        if (System.getProperty("os.name").contains("Windows"))
	            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
	        else
	            Runtime.getRuntime().exec("clear");
	    } catch (IOException | InterruptedException ex) {}
	}

	public Demo() { // Constructor
		this.setSize(250, 150);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Spaceship Game"); 
		this.setVisible(true);

		//this.addKeyListener(new KeyAdapter());
		this.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				dir = e.getKeyChar();		
			}
		});
	}

	public void keyTyped(KeyEvent e){

	}
	public void keyPressed(KeyEvent e){

	}
	public void keyReleased(KeyEvent e){
		//System.out.println("You Released:" + e.getKeyChar());
	}
}
