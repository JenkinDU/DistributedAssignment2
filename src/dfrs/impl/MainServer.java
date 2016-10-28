package dfrs.impl;

public class MainServer {

	public static void main(String[] args) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				DFRSServerMTL.main(null);
			}
		}).start();
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		new Thread(new Runnable() {

			@Override
			public void run() {
				DFRSServerWST.main(null);
			}
		}).start();
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		new Thread(new Runnable() {

			@Override
			public void run() {
				DFRSServerNDL.main(null);
			}
		}).start();
	}

}
