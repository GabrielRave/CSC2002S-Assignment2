//This class was modified: impliments comparable and has a compareTo method based on height
package typingTutor;

public class FallingWord implements Comparable<FallingWord>{
	private String word; // the word
	private int x; //position - width
	private int y; // postion - height
	private int maxY; //maximum height
        private int maxX; //added
	private boolean dropped; //flag for if user does not manage to catch word in time
	private boolean isHungryWord; //used to destinguish if falling word is hungryWord or regular
	private int fallingSpeed; //how fast this word is
	private static int maxWait=1000;
	private static int minWait=100;

	public static WordDictionary dict;
	
	FallingWord() { //constructor with defaults
		word="computer"; // a default - not used
		x=0;
		y=0;	
		maxY=300;
		dropped=false;
		fallingSpeed=(int)(Math.random() * (maxWait-minWait)+minWait); 
	}
	
	FallingWord(String text) { 
		this();
		this.word=text;
	}
	
	FallingWord(String text, int x, int y, int maxX, int maxY) { //most commonly used constructor - sets it all. changed to have more informaation set
		this(text);
		this.x=x; //only need to set x, word is at top of screen at start
		this.maxY=maxY;
                this.maxX=maxX; //added
                this.y=y; //added
	}
	
	public static void increaseSpeed( ) {
		minWait+=50;
		maxWait+=50;
	}
	
	public static void resetSpeed( ) {
		maxWait=1000;
		minWait=100;
	}
	
        @Override //this method was added and compares falling words based on height
        public int compareTo(FallingWord fw){
            synchronized(this){
                Integer x = getY();
                Integer y = fw.getY();
                return y.compareTo(x);
            }
        }

// all getters and setters must be synchronized
	public synchronized  void setY(int y) {
		if (y>maxY) {
			y=maxY;
			dropped=true; //user did not manage to catch this word
		}
		this.y=y;
	}
	
	public synchronized  void setX(int x) {
		if (x>maxX) {
			x=maxX;
			dropped=true; //user did not manage to catch this word
		}
		this.x=x;
	}
        
        public synchronized void setSpeed(int speed){
            this.fallingSpeed=speed;
        }
	
	public synchronized  void setWord(String text) {
		this.word=text;
	}

	public synchronized  String getWord() {
		return word;
	}
	
	public synchronized  int getX() {
		return x;
	}	
	
	public synchronized  int getY() {
		return y;
	}
	
	public synchronized  int getSpeed() {
		return fallingSpeed;
	}

	public synchronized void setPos(int x, int y) {
		setY(y);
		setX(x);
	}
	public synchronized void resetPos() {
		setY(0);
	}

	public synchronized void resetWord() {
		resetPos();
		word=dict.getNewWord();
		dropped=false;
		fallingSpeed=(int)(Math.random() * (maxWait-minWait)+minWait); 
		//System.out.println(getWord() + " falling speed = " + getSpeed());
	}
	
	public synchronized boolean matchWord(String typedText) {
		//System.out.println("Matching against: "+text);
		if (typedText.equals(this.word)) {
			resetWord();
			return true;
		}
		else
			return false;
	}

	public synchronized  void drop(int inc) {
		setY(y+inc);
	}
        
        public synchronized  void left(int inc) { //method added for hungryWord
		setX(x+inc);
	}
        
	public synchronized  boolean dropped() {
		return dropped;
	}
        
        public void setIsHungryWordTrue(){
            isHungryWord=true;
        }

    public synchronized void setDropped(boolean dropped) {
        this.dropped = dropped;
    }
        
        public boolean getIsHungryWord(){ //method added
            return isHungryWord;
        }
}
