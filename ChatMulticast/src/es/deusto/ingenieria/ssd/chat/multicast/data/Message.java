package es.deusto.ingenieria.ssd.chat.multicast.data;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Message {
	//Client message types
	public static final int CLIENT_MESSAGE_LOGIN=101;
	public static final int CLIENT_MESSAGE_ESTABLISH_CONNECTION=102;
	public static final int CLIENT_MESSAGE_ACCEPT_INVITATION=103;
	public static final int CLIENT_MESSAGE_REJECT_INVITATION=104;
	public static final int CLIENT_MESSAGE_CLOSE_CONVERSATION=105;
	public static final int CLIENT_MESSAGE_CLOSE_CONNECTION=106;
	public static final int CLIENT_MESSAGE_GET_USERS=107;
	public static final int CLIENT_MESSAGE=108;
	
	//Server message types
	public static final int SERVER_MESSAGE_CONNECTED=201;
	public static final int SERVER_MESSAGE_INVITATTION=202;
	public static final int SERVER_MESSAGE_INVITATTION_ACCEPTED=203;
	public static final int SERVER_MESSAGE_INVITATTION_REJECTED=204;
	public static final int SERVER_MESSAGE_CONVERSATION_CLOSED=205;
	public static final int SERVER_MESSAGE_DISCONNECTED=206;
	public static final int SERVER_MESSAGE_LIST_OF_USERS=207;
	public static final int SERVER_MESSAGE=208;
	
	//ERROR MESSAGES
	public static final int ERROR_MESSAGE_EXISTING_NICK=301;
	public static final int ERROR_MESSAGE_CONNECTION_FAILED=302;
	public static final int ERROR_MESSAGE_USER_ALREADY_CHATTING=303;
	public static final int ERROR_MESSAGE_USER_IS_DISCONNECTED=304;
	public static final int ERROR_MESSAGE_MESSAGE_ERROR=305;
	
	//Messages that are received by the server and this only responds with a simple message
	public static int[]CROSS_MESSAGES={CLIENT_MESSAGE_ESTABLISH_CONNECTION,CLIENT_MESSAGE_ACCEPT_INVITATION,CLIENT_MESSAGE_REJECT_INVITATION,CLIENT_MESSAGE_CLOSE_CONVERSATION,CLIENT_MESSAGE,ERROR_MESSAGE_USER_ALREADY_CHATTING};
	private static int[]RESPONSES_TO_CROSS_MESSAGES={SERVER_MESSAGE_INVITATTION,SERVER_MESSAGE_INVITATTION_ACCEPTED,SERVER_MESSAGE_INVITATTION_REJECTED,SERVER_MESSAGE_CONVERSATION_CLOSED, SERVER_MESSAGE,ERROR_MESSAGE_USER_ALREADY_CHATTING};
	
	private long timestamp;
	public int getMessageType() {
		return messageType;
	}
	public void setMessageType(int messageType) {
		this.messageType = messageType;
	}
	private String text;
	private int messageType;
	private User from;
	private User to;
	
	private static SimpleDateFormat dateFormatter = new SimpleDateFormat("HH:mm:ss");
	
	public Message(){
		
	}
	public long getTimestamp() {
		return timestamp;
	}
	
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public User getFrom() {
		return from;
	}
	
	public void setFrom(User from) {
		this.from = from;
	}
	
	public User getTo() {
		return to;
	}
	
	public void setTo(User to) {
		this.to = to;
	}
	
	public boolean equals(Object obj) {
		if (obj != null && obj.getClass().equals(this.getClass())) {
			Message otherMsg = (Message)obj;
			
			return this.timestamp == otherMsg.timestamp &&
				   this.text.equals(otherMsg.text) &&
				   this.from.equals(otherMsg.from) &&
				   this.to.equals(otherMsg.to);
		} else {
			return false;
		}
	}
	
	public String toString() {
		return "[" + dateFormatter.format(new Date(this.timestamp)) + "] '" + 
	           this.from + " -> " + this.to + " : " + this.text; 
				
	}
	public static boolean hasDestination(int code){
		
		for(int i:Message.CROSS_MESSAGES){
			if(i==code)return true;
		}
		
		return false;
	}
	public boolean isCrossMessage(){
		for(int i:Message.CROSS_MESSAGES){
			if(i==this.messageType)return true;
		}
		return false;
	}
	
	/**
	 * For the cross messages, returns the message type of the correct response to make.
	 * @return 0 if no response found or the number type if found
	 */
	public String getSimpleResponse(){
		if(this.to!=null){
			for (int i=0, ii=Message.CROSS_MESSAGES.length;i<ii;i++){
				if(Message.CROSS_MESSAGES[i]==this.messageType){
					if(this.messageType==Message.CLIENT_MESSAGE_ESTABLISH_CONNECTION){
						return new Integer(Message.RESPONSES_TO_CROSS_MESSAGES[i]).toString()+'&'+this.from.getNick();
					}else if(this.messageType==Message.CLIENT_MESSAGE){
						System.out.println("Client message: "+Message.RESPONSES_TO_CROSS_MESSAGES[i]+"&"+this.text);
						return new Integer(Message.RESPONSES_TO_CROSS_MESSAGES[i]).toString()+'&'+this.from.getNick()+'&'+this.text;
					}else{
						return new Integer(Message.RESPONSES_TO_CROSS_MESSAGES[i]).toString();
					}
				}
			}
		}
		return null;
	}
}