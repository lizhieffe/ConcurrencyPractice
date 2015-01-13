package Basic;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Philosopher implements Runnable {
	private Chopstick left;
	private Chopstick right;
	private final int id;
	private final int ponderFactor;
	private Random rand = new Random(47);
	public Philosopher(Chopstick left, Chopstick right, int id, int ponderFactor) {
		this.left = left;
		this.right = right;
		this.id = id;
		this.ponderFactor = ponderFactor;
	}
	
	private void pause() throws InterruptedException {
	    if(ponderFactor == 0) return;
	    TimeUnit.MILLISECONDS.sleep(
	      rand.nextInt(ponderFactor * 250));
	}
	
	public void run() {
		try {
			System.out.println(this + " is thinking!");
			pause();
			
			left.take();
			System.out.println(this + " picked the left stick: " + left);
			right.take();
			System.out.println(this + " picked the right stick: " + right);
			
			pause();
			left.drop();
			System.out.println(this + " droped the left stick: " + left);
			right.drop();
			System.out.println(this + " droped the right stick: " + right);
		}
		catch (Exception e) {
			System.out.println(this + " exiting vie interrupt");
		}
	}
	
	public static void main(String[] args) throws Exception {
	    int ponder = 5;
	    if(args.length > 0)
	      ponder = Integer.parseInt(args[0]);
	    int size = 5;
	    if(args.length > 1)
	      size = Integer.parseInt(args[1]);
	    ExecutorService exec = Executors.newCachedThreadPool();
	    Chopstick[] sticks = new Chopstick[size];
	    for(int i = 0; i < size; i++)
	      sticks[i] = new Chopstick();
	    for(int i = 0; i < size; i++)
	      exec.execute(new Philosopher(
	        sticks[i], sticks[(i+1) % size], i, ponder));
	    TimeUnit.SECONDS.sleep(5);
	    exec.shutdownNow();
	  }
}

class Chopstick {
	private boolean taken = false;
	public synchronized void take() throws InterruptedException {
		while (taken == true)
			wait();
		taken = true;
	}
	public synchronized void drop() {
		taken = false;
		notifyAll();
	}
}