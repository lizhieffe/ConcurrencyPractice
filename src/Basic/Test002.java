package Basic;

public class Test002 extends Thread {
	int i = 0;
	
	public void run() {
		while (i < 100)
			System.out.println(i++);
	}
	
	public static void main() {
		Test002 t = new Test002();
		t.start();
	}
}
