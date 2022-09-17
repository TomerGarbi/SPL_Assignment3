package bgu.spl.net.impl.BGRSServer;

import bgu.spl.net.api.MessagingProtocol;
import bgu.spl.net.messages.*;

public class MessagingProtocolImpl implements MessagingProtocol<String>{
	private ClientSession user = new ClientSession();
	@Override
	public String process(String msg) {
		//pass this on to our specific implementation
		//user represents functions permissible to client and specific impelementation
		return user.act(msg);
	}

	@Override
	public boolean shouldTerminate() {
		// TODO Auto-generated method stub
		return false;
	}
}
