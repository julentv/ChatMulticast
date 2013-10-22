package es.deusto.ingenieria.ssd.chat.multicast.data;

public class User {	
	private String nick;
	private String ip;
	private int port;
	
	public User(){};
	public User(String nick, String ip, int port){
		this.nick=nick;
		this.ip=ip;
		this.port=port;
	}
	
	public String getIp() {
		return ip;
	}

	public int getPort() {
		return port;
	}

	public String getNick() {
		return nick;
	}
	
	public void setNick(String nick) {
		this.nick = nick;
	}
		
	public boolean equals(Object obj) {
		if (obj != null && obj.getClass().equals(this.getClass())) {			
			return this.nick.equalsIgnoreCase(((User)obj).nick);				  
		} else {
			return false;
		}
	}
	public String toString(){
		return "Nick: "+this.nick+"/ IP: "+this.ip+"/ Puerto:"+this.port;
		
	}
}