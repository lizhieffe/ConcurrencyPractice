package Basic;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class WaxOMatic {
	public static void main(String[] args) throws Exception {
		Car car = new Car();
		ExecutorService es = Executors.newCachedThreadPool();
		es.execute(new WaxOn(car));
		es.execute(new WaxOff(car));
		TimeUnit.SECONDS.sleep(5);
		es.shutdownNow();
	}
}

class Car {
	private boolean waxOn = false;
	public synchronized void waxed() {
		waxOn = true;
		notifyAll();
	}
	public synchronized void buffed() {
		waxOn = false;
		notifyAll();
	}
	public synchronized void waitForWaxing() throws InterruptedException {
		while (waxOn == false)
			wait();
	}
	public synchronized void waitForBuffing() throws InterruptedException {
		while (waxOn == true)
			wait();
	}
}

class WaxOn implements Runnable {
	private Car car;
	public WaxOn(Car c) {
		car = c;
	}
	private int count = 0;
	@Override
	public void run() {
		try {
			while (!Thread.interrupted()) {
				synchronized(this) {
					count++;
					System.out.println("Wax On! count = " + count);
				}
				
				TimeUnit.MILLISECONDS.sleep(200);
				car.waxed();
				car.waitForBuffing();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}

class WaxOff implements Runnable {
	private Car car;
	public WaxOff(Car c) {
		car = c;
	}
	@Override
	public void run() {
		try {
			while (!Thread.interrupted()) {
				System.out.println("Wax Off! ");
				TimeUnit.MILLISECONDS.sleep(200);
				car.buffed();
				car.waitForWaxing();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}