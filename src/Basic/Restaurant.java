package Basic;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Restaurant {
	Meal meal;
	ExecutorService exec = Executors.newCachedThreadPool();
	WaitPerson waitPerson;
	Chef chef;
	public static void main(String[] args) {
		Restaurant r = new Restaurant();
		r.waitPerson = new WaitPerson(r);
		r.chef = new Chef(r);
		r.exec.execute(r.waitPerson);
		r.exec.execute(r.chef);
	}
}

class Meal {
	private final int orderNum;
	public Meal(int orderNum) {
		this.orderNum = orderNum;
	}
	public String toString() {
		return "Meal " + orderNum;
	}
}

class WaitPerson implements Runnable {
	private Restaurant restaurant;
	WaitPerson (Restaurant restaurant) {
		this.restaurant = restaurant;
	}
	public void run() {
		try {
			while (!Thread.interrupted()) {
				synchronized(this) {
					while(restaurant.meal == null) {
						System.out.println("people is waiting...\nmeal is null = " + (restaurant.meal == null));
						wait();
					}
				}
				System.out.println("Waitperson got " + restaurant.meal);
				synchronized(restaurant.chef) {
					restaurant.meal = null;
					restaurant.chef.notifyAll();
				}
			}
			
		}
		catch (Exception e) {
			System.out.println("WaitPerson interrupted");
		}
	}
}

class Chef implements Runnable {
	Restaurant restaurant;
	int count = 0;
	Chef (Restaurant restaurant) {
		this.restaurant = restaurant;
	}
	public void run() {
		try {
			while (!Thread.interrupted()) {
				synchronized(this) {
					while (restaurant.meal != null) {
						System.out.println("chef is waiting...\nmeal is null = " + (restaurant.meal == null));
						wait();
					}
				}
				if (++count == 10) {
					System.out.println("Out of food, closing!");
					restaurant.exec.shutdownNow();
				}
				System.out.println("Order up!");
				synchronized(restaurant.waitPerson) {
					restaurant.meal = new Meal(count);
					restaurant.waitPerson.notifyAll();
				}
				TimeUnit.MILLISECONDS.sleep(100);
			}
		}
		catch(Exception e) {
			System.out.println("Chef interrupted");
		}
	}
	
}
