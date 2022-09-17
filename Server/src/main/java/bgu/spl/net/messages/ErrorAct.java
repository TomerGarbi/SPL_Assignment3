package bgu.spl.net.messages;

import bgu.spl.net.api.*;

public class ErrorAct implements Message {
	
	private String value;
	
	public ErrorAct (String code){
		value = code;
	}
	@Override
	public String action(String input) {
		return "13"+value+'\n';
	}
}