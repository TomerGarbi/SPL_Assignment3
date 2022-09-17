package bgu.spl.net.impl.BGRSServer;

import java.util.function.Supplier;

import bgu.spl.net.api.MessageEncoderDecoder;

public class EnDecSupplier implements Supplier<MessageEncoderDecoder>{
	/*
	 * this is a supplier of the Message Encoder Decoder we implemented(MesageEncouderDecouderImpl)
	 */

	public EnDecSupplier() {
		
	}

	@Override
	public MessageEncoderDecoder get() {
		MessageEncoderDecoderImpl instance = new MessageEncoderDecoderImpl();
		return instance;
	}

}
