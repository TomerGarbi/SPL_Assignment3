package bgu.spl.net.impl.BGRSServer;

import java.io.IOException;

import bgu.spl.net.srv.Reactor;

public class ReactorMain {
	/*
	 * this is the Main class that runs the Reactor server
	 */

	public static void main(String[] args) {
		int port = Integer.parseInt(args[0]);
		int numberOfThreads = Integer.parseInt(args[1]);
		Reactor re = new Reactor(numberOfThreads,port, new ProtocolSupplier(), new EnDecSupplier());
		Database db = Database.getInstance();//just for initialing the data base before the server start to serve
		re.serve();
		try {
			re.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
