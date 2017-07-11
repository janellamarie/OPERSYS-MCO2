package using_locks;

public class Driver {

	private static void log(final String message) {
		System.out.printf("%tT [%s] %s%n", System.currentTimeMillis(), Thread.currentThread().getName(), message);
	}
	
		
	public static void main(String[] args){

		Driver.log("Start");

		final Thread t = new Thread("My Thread") {
			@Override
			public void run() {
				Driver.log("Hello from thread");
			}
		};
		t.start();

		final Thread j = new Thread("Fuck"){
		    public void run(){
		        Driver.log("asdfasdfadsf");
            }
        };
		j.start();

		Driver.log("Waiting for thread to finish");
        try {
            t.join();
            j.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Driver.log("Done");

		
		CalTrain calTrain = new CalTrain();	
		Station x = new Station();
		
		calTrain.station_load_train(x, 10);
	}
}
