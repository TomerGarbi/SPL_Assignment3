package bgu.spl.net.impl.BGRSServer;

import java.io.IOException;

import bgu.spl.net.srv.ThreadPerClientServer;

public class TPCMain {
	/*
	 * this is the main class that runs the Thread Per Client server
	 */
	
	
	public static void main (String[] args) {
		int port = Integer.parseInt(args[0]);
		ThreadPerClientServer server = new ThreadPerClientServer(port, new ProtocolSupplier(), new EnDecSupplier());
		Database db = Database.getInstance();//just for initialing the data base before the server start to serve
		server.serve();
		try {
			server.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
