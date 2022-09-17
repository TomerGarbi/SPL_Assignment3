package bgu.spl.net.srv;

import java.util.function.Supplier;

import bgu.spl.net.api.MessageEncoderDecoder;
import bgu.spl.net.api.MessagingProtocol;

public class ThreadPerClientServer extends BaseServer {
    
    public ThreadPerClientServer(
            int port,
            Supplier<MessagingProtocol> protocolFactory,
            Supplier<MessageEncoderDecoder> encoderDecoderFactory) {
 
        super(port, protocolFactory, encoderDecoderFactory);
    }
 
    @Override
    protected void execute(BlockingConnectionHandler handler) {
        new Thread(handler).start();
    }

	
 
}