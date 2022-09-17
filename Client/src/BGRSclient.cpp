#include <stdlib.h>
#include <iostream>
#include <limits>
#include <ios>
#include <connectionHandler.h>
#include <boost/algorithm/string.hpp>

using namespace std;

bool is_null(const std::string& arg) {
    return arg.compare("") == 0;
}

string msgnum(const std::string& msg){
	std::string ans = "";
	//cout<<"Four digits "<<msg.substr(0,4)<< endl;
	if(msg.substr(2,1).compare("0")==0){
		//cout<<"Return command is less than 10"<<endl;
		ans = msg.substr(3,1);
	} else {
		//cout<<"Return command is 10 or greater"<<endl;
		ans = msg.substr(2,2);
	}
	return ans;
}

bool outputResponse(const std::string& msg) {
    //prints out response according to required format
    if (!is_null(msg)) {
	if(msg.substr(0,2).compare("12")==0){
		if(msg.substr(2,2).compare("06")==0|| 
		   msg.substr(2,2).compare("07")==0|| 
		   msg.substr(2,2).compare("08")==0|| 	
		   msg.substr(2,2).compare("11")==0){
			cout<<"ACK "<< msgnum(msg) << endl;
			int begin = 4;
			int end = msg.find("$");
			while(end!=-1){
				cout<<msg.substr(begin,end-begin)<<endl;
				begin = end +1;
				end = msg.find("$",begin);
			}
		}else{
		std::string remaining_msg = "";
		if(msg.length() > 5){
			remaining_msg = "\n"+msg.substr(4,msg.length()-5);
		}
        	cout << "ACK " << msgnum(msg) << remaining_msg << endl;
	}
}
	if(msg.substr(0,2).compare("13")==0)
	{
        	cout << "ERROR " << msgnum(msg) << " " << endl;
	}
        return true;
    }
    return false;
}

bool is_number(const std::string& s)
{
    std::string::const_iterator it = s.begin();
    while (it != s.end() && std::isdigit(*it)) ++it;
    return !s.empty() && it == s.end();
}

bool is_valid(const std::string& arg) {
    //check if username or password is valid - verify it does not contain 0?
    return true;
}

bool isCommand(const std::string &cmd, const std::string& arg1, const std::string& arg2) {
    //parses cmd to check if its a valid command
    //parse the string, the first segment needs to be one of the 11 command phrases
    //if the command phrase is present, we have to parse the remaining arguments
    //these commands have 3 arguments - user and pw are 2nd and 3rd arguments
    if (cmd.compare("ADMINREG") == 0 ||
        cmd.compare("STUDENTREG") == 0 ||
        cmd.compare("LOGIN") == 0) {
        //check if username and pw are valid format
        return is_valid(arg1)&is_valid(arg2)&!is_null(arg1)&!is_null(arg2);
    }
    //these commands have 1 argument
    else if ((cmd.compare("LOGOUT") == 0 ||
        cmd.compare("MYCOURSES") == 0) && 
	arg1.compare("") == 0 
	&& arg2.compare("") == 0){
        return true;
    }
    //these commands have 2 arguments - coursenumber is 2nd argument
    else if (cmd.compare("COURSEREG") == 0 ||
        cmd.compare("KDAMCHECK") == 0 ||
        cmd.compare("COURSESTAT") == 0 ||
        cmd.compare("ISREGISTERED") == 0 ||
        cmd.compare("UNREGISTER") == 0) {
        //is a valid command if 2nd arguement is a number and 3rd argument is null
        return is_number(arg1)&!is_null(arg1)& is_null(arg2);
    }
    //these commands have 2 arguments - username is 2nd argument
    else if (cmd.compare("STUDENTSTAT") == 0) {
        return is_valid(arg1)&!is_null(arg1)& is_null(arg2);
    }
    //if arg1 is not any of these exact phrases then command is invalid
    return false;
}

std::string convertToServerMessage(std::string arg1, std::string arg2, std::string arg3) {
    //takes a message sent by user from cin and converts to format readable by server
    //figure out which message we have, package and send info
    if (arg1.compare("ADMINREG") == 0) {
        return "01" + arg2 + '\0' + arg3 + '\0';
    }
    else if (arg1.compare("STUDENTREG") == 0) {
        return "02" + arg2 + '\0' + arg3 + '\0';
    }
    else if (arg1.compare("LOGIN") == 0) {
        return "03" + arg2 + '\0' + arg3 + '\0';
    }
    else if (arg1.compare("LOGOUT") == 0) {
        return "04";
    }
    else if (arg1.compare("COURSEREG") == 0) {
        return "05" + arg2 + '\0' + "";
    }
    else if (arg1.compare("KDAMCHECK") == 0) {
        return "06" + arg2 + '\0' + "";
    }
    else if (arg1.compare("COURSESTAT") == 0) {
        return "07" + arg2 + '\0' + "";
    }
    else if (arg1.compare("STUDENTSTAT") == 0) {
        return "08" + arg2 + '\0' + "";
    }
    else if (arg1.compare("ISREGISTERED") == 0) {
        return "09" + arg2 + '\0' + "";
    }
    else if (arg1.compare("UNREGISTER") == 0) {
        return "10" + arg2 + '\0' + "";
    }
    else if (arg1.compare("MYCOURSES") == 0) {
        return "11";
    }
    return "";
}

std::string convertToClientOutput(std::string servermsg) {
    //takes a message returned from the server and formats it to be output
    //parse the first ints from the message
    std::string opcode = servermsg.substr(0, 2);
    if (opcode.compare("12")==0) {
        //ack
        //return the rest of the message after the return opcode
        return servermsg;
    }
    else if (opcode.compare("13")==0) {
        //err
        //return the opcode the err was caused by
        return servermsg;
    }
    return servermsg;
}

/**
* This code assumes that the server replies the exact text the client sent it (as opposed to the practical session example)
*/
int main(int argc, char* argv[]) {
    if (argc < 3) {
        std::cerr << "Usage: " << argv[0] << " host port" << std::endl << std::endl;
        return -1;
    }
    std::string host = argv[1];
    short port = atoi(argv[2]);

    ConnectionHandler connectionHandler(host, port);
    if (!connectionHandler.connect()) {
        std::cerr << "Cannot connect to " << host << ":" << port << std::endl;
        return 1;
    }
	//std::cout << "Creating connection" << std::endl;
    //From here we will see the rest of the echo client implementation:
    while (1) {
        const short bufsize = 1024;
        char buf[bufsize];
	//std::cout << "Waiting for user input" << endl;
	std::cin.getline(buf, bufsize);
	std::string line(buf);
	std::vector<std::string> tokens;
	boost::split(tokens, line, boost::is_any_of("\t "));
        //get up to 3 inputs from cin.
	int len = tokens.size();
        std::string arg1;
        std::string arg2;
        std::string arg3;
        //parse 3 inputs from the new line
	if(len < 1){arg1 = "";}
	else{arg1 = tokens[0];}
	
	if(len < 2){arg2 = "";}
	else{arg2 = tokens[1];}
	
	if(len < 3){arg3 = "";}
	else{arg3 = tokens[2];}
        //now we have a string, we only want to send messages that constitute valid commands

        if (isCommand(arg1, arg2, arg3)) {
            //if line is valid then we need to change it to the proper form
            std::string convertedline = convertToServerMessage(arg1, arg2, arg3);
            if (!connectionHandler.sendLine(convertedline)) {
                std::cout << "Disconnected. Exiting...\n" << std::endl;
                break;
            }
            // 2. Read a line (up to the newline character using the getline() buffered reader
            std::string answer;
            // We could also use: connectionHandler.getline(answer) and then get the answer without the newline char at the end
            if (!connectionHandler.getLine(answer)) {
                std::cout << "Disconnected. Exiting...\n" << std::endl;
                break;
            }
            // A C string must end with a 0 char delimiter.  When we filled the answer buffer from the socket
            // we filled up to the \n char - we must make sure now that a 0 char is also present. So we truncate last character.
            std::string convertedanswer = convertToClientOutput(answer);
            //if we have a response it will be displayed properly and then we break out
            if (outputResponse(convertedanswer)&(convertedanswer.substr(2,2).compare("04")==0)) {
                break;
            }
        }
        else {
            std::cout << "Invalid command.\n" << std::endl;
        }
    }
    return 0;
}
