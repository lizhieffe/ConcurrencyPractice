package Basic;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class OrnamentalGarden {
	public static void main(String[] args) throws Exception {
		ExecutorService exec = Executors.newCachedThreadPool();
		for (int i = 0; i < 5; i++)
			exec.execute(new Entrance(i));
		TimeUnit.SECONDS.sleep(3);
		Entrance.cancel();
		exec.shutdown();
		if (!exec.awaitTermination(250, TimeUnit.MILLISECONDS))
			System.out.println("Some takes is not terminated!");
		System.out.println("Total: " + Entrance.getGlobalCount());
		System.out.println("Sum of entrances: " + Entrance.sumEntrances());
	}
}

class Entrance implements Runnable {

	private static Count globalCount = new Count();
	private static List<Entrance> entrances = new ArrayList<Entrance>();
	private int number = 0;
	private final int id;
	private volatile static boolean canceled = false;
	public static void cancel() {
		canceled = true;
	}
	public Entrance(int id) {
		this.id = id;
		entrances.add(this);
	}
	public void run() {
		while (!canceled) {
			synchronized(this) {
				number++;
			}
			System.out.println(this + " / Total: " + globalCount.increment());
			try {
				TimeUnit.MILLISECONDS.sleep(100);
			}
			catch(Exception e) {
				System.out.println("sleep interrupted");
			}
		}
		System.out.println("Stopping " + this);
	}
	public synchronized int getValue() {
		return number;
	}
	public String toString() {
		return "Entrance id: " + id + ": " + getValue();
	}
	public static int getGlobalCount() {
		return globalCount.value();
	}
	public static int sumEntrances() {
		int sum = 0;
		for (Entrance e : entrances)
			sum += e.getValue();
		return sum;
	}
}

class Count {
	private int count = 0;
	Random rand = new Random(47);
	public synchronized int increment() {
		return ++count;
	}
	public synchronized int value() {
		return count;
	}
}