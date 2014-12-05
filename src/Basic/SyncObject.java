package Basic;

public class SyncObject {
	public static void main(String[] args) {
		final DueSync ds = new DueSync();
		new Thread() {
			public void run() {
				ds.f();
			};
		}.start();
		ds.g();
	}
}

class DueSync {
	private Object syncObjc = new Object();
	public synchronized void f() {
		for (int i = 0; i < 100; i++) {
			System.out.println("f()");
			Thread.yield();
		}
	}
	public void g() {
		synchronized(syncObjc) {
			for (int i = 0; i < 100; i++) {
				System.out.println("g()");
				Thread.yield();
			}
		}
	}
}