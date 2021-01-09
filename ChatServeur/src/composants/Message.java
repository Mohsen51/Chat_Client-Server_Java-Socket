package composants;

public class Message {
	public Client_info client_info;
	public String message;
	
	public Message (Client_info ci, String msg) {
		this.client_info = ci;
		this.message = msg;
	}

	public String toString() {
		if (this.client_info == null)
			return ("gestion" + " : "+ this.message);
		return (this.client_info.pseudo +" : "+ this.message);
	}
}
