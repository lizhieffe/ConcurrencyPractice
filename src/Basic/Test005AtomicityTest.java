package Basic;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Test005AtomicityTest implements Runnable {
	private int i = 0;
	public int getValue() {
		return i;
	}
	private synchronized void evenIncrement() {
		i++;
		i++;
	}
	public void run() {
		while (true) {
			evenIncrement();
		}
	}
	public static void main(String[] args) {
		ExecutorService es = Executors.newCachedThreadPool();
		Test005AtomicityTest at = new Test005AtomicityTest();
		es.execute(at);
		while (true) {
			int val = at.getValue();
			if (val % 2 == 1) {
				System.out.println(val);
				System.exit(0);
			}
		}
	}
}
