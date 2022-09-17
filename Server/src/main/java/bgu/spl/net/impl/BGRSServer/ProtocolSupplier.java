package bgu.spl.net.impl.BGRSServer;

import java.util.function.Supplier;

import bgu.spl.net.api.MessagingProtocol;

public class ProtocolSupplier implements Supplier<MessagingProtocol> {
	/*
	 * this is a supplier for the Messaging protocol we implemented 
	 */

	public ProtocolSupplier() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public MessagingProtocol get() {
		MessagingProtocolImpl instance = new MessagingProtocolImpl();
		return instance;
	}

}
