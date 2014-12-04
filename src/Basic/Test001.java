package Basic;

public class Test001 implements Runnable {

	int i = 0;
	
	@Override
	public void run() {
		while (i < 100)
			System.out.println(i++);
	}

	public static void main(String[] args) {
		Thread t = new Thread(new Test001());
		t.start();
	}
}
