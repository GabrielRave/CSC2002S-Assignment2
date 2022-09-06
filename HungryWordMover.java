package typingTutor;

import static java.lang.Thread.sleep;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class HungryWordMover extends WordMover {
	private FallingWord myWord;
	private AtomicBoolean done;
	private AtomicBoolean pause; 
	private Score score;
	CountDownLatch startLatch; //so all can start at once

	HungryWordMover( FallingWord word,WordDictionary dict, Score score,
			CountDownLatch startLatch, AtomicBoolean d, AtomicBoolean p) {
                super(word);
		this.myWord=word;
		this.startLatch = startLatch;
		this.score=score;
		this.done=d;
		this.pause=p;
	}
        
        
	HungryWordMover( FallingWord word) {
                super(word);
		myWord = word;
	}
	

        public FallingWord getMyWord() {
            return myWord;
        }   
        
        public void HungryWordScore(int score1){
            score.HungryWordScore(score1);
        }
	
	public void run() {
            //System.out.println("Word mover thread started");
		//System.out.println(myWord.getWord() + " falling speed = " + myWord.getSpeed());
		try {
			System.out.println(myWord.getWord() + " waiting to start " );
			startLatch.await();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} //wait for other threads to start
		System.out.println(myWord.getWord() + " started" );
		while (!done.get()) {				
			//animate the word
                        //System.out.println(myWord.getWord());
			while (!myWord.dropped() && !done.get()) {
				    myWord.left(10);
                                try {
                                        sleep(myWord.getSpeed());
                                } catch (InterruptedException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                };		
                                while(pause.get()&&!done.get()) {};
			}
			if (!done.get() && myWord.dropped()) {
				score.missedWord();
				myWord.resetWord();
			}
			myWord.resetWord();
		}
	}
	
}
