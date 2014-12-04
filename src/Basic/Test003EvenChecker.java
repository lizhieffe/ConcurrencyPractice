package Basic;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Test003EvenChecker implements Runnable {
	private IntGenerator generator;
	private final int id;
	public Test003EvenChecker (IntGenerator g, int ident) {
		this.generator = g;
		this.id = ident;
	}
	
	public void run() {
		while (!generator.isCanceled()) {
			int val = generator.next();
//			System.out.println("val = " + val + ", in thread id = " + id);
			if (val % 2 != 0) {
				System.out.println(val + " not even! (thread id = " + id + ")");
				generator.cancel();
			}
		}
	}
	
	public static void test(IntGenerator gp, int count) {
		System.out.println("Press Control-C to exit");
		ExecutorService es = Executors.newCachedThreadPool();
		for (int i = 0; i < count; i++) {
			es.execute(new Test003EvenChecker(gp, i));
		}
		es.shutdown();
	}
	
	public static void test(IntGenerator gp) {
		test(gp, 10);
	}
	
	public static void main(String[] args) {
		test(new EvenGenerator(), 50);
	}
}

class EvenGenerator extends IntGenerator {
	private int currentEvenValue = 0;
	
	@Override
	public int next() {
		synchronized(this) {
			++currentEvenValue;
			++currentEvenValue;
		}
		synchronized(this) {
			return currentEvenValue;
		}
	}
	
}

abstract class IntGenerator {
	private volatile boolean canceled = false;
	public abstract int next();
	public void cancel() {
		canceled = true;
	}
	public boolean isCanceled() {
		return canceled;
	}
}