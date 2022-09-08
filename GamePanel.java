package typingTutor;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Font;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable {
		private AtomicBoolean done ; //REMOVE
		private AtomicBoolean started ; //REMOVE
		private AtomicBoolean won ; //REMOVE

		private FallingWord[] words;
		private int noWords;
		private final static int borderWidth=25; //appearance - border
                private final static int borderHeight=25; //appearance - border added
                
		GamePanel(FallingWord[] words, int maxY,	
				 AtomicBoolean d, AtomicBoolean s, AtomicBoolean w) {
			this.words=words; //shared word list
			noWords = words.length; //only need to do this once
			done=d; //REMOVE
			started=s; //REMOVE
			won=w; //REMOVE  
		}
		
		public void paintComponent(Graphics g) {
		    int width = getWidth()-borderWidth*2;
		    int height = getHeight()-borderWidth*2;
		    g.clearRect(borderWidth,borderWidth,width,height);//the active space
                    g.setColor(Color.lightGray);
                    g.fillRect(0,borderWidth,borderWidth,height); //left panel
                    g.fillRect(width+borderWidth,borderWidth,borderWidth,height-borderWidth);//right panel
		    g.setColor(Color.pink); //change colour of pen
		    g.fillRect(0,height,width+borderWidth*2,borderWidth); //draw danger zone
                    
		    g.setColor(Color.black);
		    g.setFont(new Font("Arial", Font.PLAIN, 26));
		   //draw the words
		    if (!started.get()) {
		    	g.setFont(new Font("Arial", Font.BOLD, 26));
				g.drawString("Type all the words before they hit the red zone,press enter after each one.",borderWidth*2,height/2);	
		    }
		    else if (!done.get()) {
		    	for (int i=0;i<noWords;i++){	    
                            if (!words[i].getIsHungryWord()){  //regular falling word
                                g.setColor(Color.black);
		    		g.drawString(words[i].getWord(),words[i].getX()+borderWidth,words[i].getY());
                            }
                            else{ //Hungry word
                                g.setColor(Color.green);
                                g.drawString(words[i].getWord(),words[i].getX()+borderWidth,words[i].getY());
                                g.setColor(Color.lightGray);
                                g.fillRect(0,borderWidth,borderWidth,height-borderWidth); //left panel -refilled to stop parts of words beiing left onscreen
                                g.fillRect(width+borderWidth,borderWidth,borderWidth,height-borderWidth); //right panel
                                int hwLength=words[i].getWord().length()*26;
                                int hwx=words[i].getX()+hwLength/2; //middle x value of hungry word
                                int hwy=words[i].getY()+13; //middle y value of hungry word
                                //Code for checking collisions here
                                for (int j=0;j<noWords;j++){
                                    if (!words[j].getIsHungryWord()){
                                        int wordLength=words[j].getWord().length()*26; //length of falling word
                                        int compareX=hwx-(words[j].getX()+wordLength/2);//comparing middle of 2 words
                                        int compareY=hwy-(words[j].getY()+13);//comparing middle of two words
                                        if(compareX<0) compareX=-compareX; //find absolute value
                                        if(compareY<0) compareY=-compareY; //find absolute value
                                        if(compareY<26&&compareX<wordLength/2+hwLength/2){ //checks if words are touching
                                            words[j].setSpeed(0);
                                            words[j].setDropped(true);
                                        }
                                        
                                    }
                                }
                            }
		    	}
                        g.setColor(Color.lightGray); //change colour of pen
		    	g.fillRect(borderWidth,0,width+borderWidth,borderWidth); //fillinf in top panel
		   }
		   else { if (won.get()) {
			   g.setFont(new Font("Arial", Font.BOLD, 36));
			   g.drawString("Well done!",width/3,height/2);	
		   } else {
			   g.setFont(new Font("Arial", Font.BOLD, 36));
			   g.drawString("Game over!",width/2,height/2);	
		   }
		   }
		}
		
		public int getValidXpos() {
			int width = getWidth()-borderWidth*5;
			int x= (int)(Math.random() * width);
			return x;
		}
                //This method was added for implimentation of HungryWord
                public int getValidYpos() {
			int height = getHeight()-borderHeight*4;
			int y=(int)(Math.random() * height)+borderWidth;
			return y;
		}
                
		public void run() {
			while (true) {
				repaint();
				try {
					Thread.sleep(10); 
				} catch (InterruptedException e) {
					e.printStackTrace();
				};
			}
		}

	}


