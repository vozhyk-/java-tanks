package re.neutrino.java_tanks.types.commands;

import java.io.IOException;

import re.neutrino.java_tanks.types.CommunicationStream;
import re.neutrino.java_tanks.types.basic.NetString;

public class SendChatMsgCommand extends CommandWithArgument<NetString> {
	@Override
	public Type getType() {
		return Type.SendChatMsg;
	}

	public SendChatMsgCommand(NetString msg) {
		super(msg);
	}

	public SendChatMsgCommand(String msg) {
		this(new NetString(msg));
	}

	public String getMsg() {
		return arg.getValue();
	}

	public static SendChatMsgCommand recvRest(CommunicationStream comm)
			throws IOException {
		return new SendChatMsgCommand(NetString.recv(comm));
	}
}
