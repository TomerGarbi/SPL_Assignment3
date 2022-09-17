package bgu.spl.net.impl.BGRSServer;

import java.nio.charset.StandardCharsets;

import bgu.spl.net.api.MessageEncoderDecoder;

public class MessageEncoderDecoderImpl implements MessageEncoderDecoder<String>{
	private byte[] datastream = new byte[1000];
	int ind = 0;
	int zeroSignal = 0;
	String code = "1000";
	
	@Override
	public String decodeNextByte(byte nextByte) {
		if(byte2byte(nextByte).compareTo("")!=0 && byte2byte(nextByte)!=null && nextByte != 10) {
			datastream[ind] = nextByte;
			ind+=1;
		}
		//if we have at least 2 bytes check if there is message
		if(ind==2) {
			byte[] opcode = new byte[2];
			opcode[0] = datastream[0];
			opcode[1] = datastream[1];
			code = new String(opcode, StandardCharsets.UTF_8);
		}
		//after each byte check if it is time to send the message
		if(terminate(code, datastream, nextByte) & ind >= 1) {
			//send the message along
			String message = contentExtraction(datastream, ind);
			reset();
			return message;
		}
		return null;
	}
	//reset parameters of the decoder after receiving a complete message
	private void reset() {
		code = "1000";
		ind = 0;
		zeroSignal = 0;
		datastream = new byte[1000];
	}
	
	//encode the string received to a bytes array
	@Override
	public byte[] encode(String message) {
		return message.getBytes(StandardCharsets.UTF_8);
	}
	
	//check if the bytes so far can create a message. if they are, return the message. else return NULL
	private boolean terminate(String code, byte[] bytes, byte nextByte) {
		String nextByteArr = byte2byte(nextByte);
		
		if (nextByte==0) {
			zeroSignal += 1;
		}
		//admin register
		if(code.compareTo("01")==0) {
			return zeroSignal == 2;
		}
		//student register
		if(code.compareTo("02")==0) {
			return zeroSignal == 2;
		}
		//login request
		if(code.compareTo("03")==0) {
			return zeroSignal == 2;
		}
		//logout request
		if(code.compareTo("04")==0) {
			return true;
		}
		//course register
		if(code.compareTo("05")==0) {
			return zeroSignal == 1;
		}
		//kdam check
		if(code.compareTo("06")==0) {
			return zeroSignal == 1;
		}
		//admin course status
		if(code.compareTo("07")==0) {
			return zeroSignal == 1;
		}
		//admin student status
		if(code.compareTo("08")==0) {
			return zeroSignal == 1;
		}
		//check if registered
		if(code.compareTo("09")==0) {
			return zeroSignal == 1;
		}
		//unregister from course
		if(code.compareTo("10")==0) {
			return zeroSignal == 1;
		}
		//check current courses
		if(code.compareTo("11")==0) {
			return true;
		}
		return false;
	}
	
	private short bytesToShort(byte[] byteArr)
	{
	    short result = (short)((byteArr[0] & 0xff) << 8);
	    result += (short)(byteArr[1] & 0xff);
	    return result;
	}
	
	private byte[] shortToBytes(short num)
	{
	    byte[] bytesArr = new byte[2];
	    bytesArr[0] = (byte)((num >> 8) & 0xFF);
	    bytesArr[1] = (byte)(num & 0xFF);
	    return bytesArr;
	}
	private String byte2byte(byte val) {
		byte[] output = new byte[1];
		output[0] = val;
		return new String(output, StandardCharsets.UTF_8);
	}
	private String contentExtraction(byte[] input, int indx) {
		//send the message along
		byte[] bytemessage = new byte[indx];
		for(int i = 0; i < indx ; i++) {
			if(input[i]==0) {
				bytemessage[i] = 32;
			}
			else {
			bytemessage[i] = input[i];
			}
		}
		return new String(bytemessage,StandardCharsets.UTF_8);
	}
}
