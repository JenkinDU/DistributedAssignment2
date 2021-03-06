package dfrs.test;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestBookEdit extends dfrs.test.Test {
	private static final int THREAD = 3;
	
	@Before
	public void setUp() throws Exception {
		initConnection();
	}


	public static void main(String args[]) {
		TestBookEdit test = new TestBookEdit();
		try {
			test.setUp();
			test.test();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		for(int i=0;i<9;i++) {
			if(i%3==0) {
				if(i/3 == 2) {
					startEditTest(dfrs[i], i, 0, false);
				} else {
					startBookTest(dfrs[i], i, 0, false);
				}
			} else if(i%3==1) {
				if((i-1)/3 == 2) {
					startEditTest(dfrs[i], i, 1, false);
				} else {
					startBookTest(dfrs[i], i, 1, false);
				}
			} else if(i%3==2) {
				if((i-2)/3 == 2) {
					startEditTest(dfrs[i], i, 2, false);
				} else {
					startBookTest(dfrs[i], i, 2, false);
				}
			}
		}
		System.out.println("\nBook and Edit Testing...");
	}

	@Override
	protected int getThreadNum() {
		return THREAD;
	}
}
