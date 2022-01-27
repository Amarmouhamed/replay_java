package main;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Client {
	// ce qu'on a dèja
    Date arrival_time;// heure d'arrive
    int service_id; // type de client
    int	agent_id;// id_agent
    Date response_time;// heure de réponse de l'agent
    Date consult;
    Date transfer;// heure de transfert de l'appel
    Date departure_time;// fin d'appel
    // Ã  trouver
    int qt;//la longueur de sa file d'attente 
    double LES; // le temps d'attente du dernier client `a entrer en service
    int nb_agent;
    
	public Client(Date arrival_time, int service_id, int agent_id, Date response_time, Date consult, Date transfer,
			Date departure_time) {
		super();
		this.arrival_time = arrival_time;
		this.service_id = service_id;
		this.agent_id = agent_id;
		this.response_time = response_time;
		this.consult = consult;
		this.transfer = transfer;
		this.departure_time = departure_time;
	}
	@Override
	public String toString() {
		return "Client [arrival_time=" + arrival_time + ", service_id=" + service_id + ", agent_id=" + agent_id
				+ ", response_time=" + response_time + ", consult=" + consult + ", transfer=" + transfer
				+ ", departure_time=" + departure_time + "]";
	}
	public String arrival_toString(int index) {
		return index+". Arrivé  service_id:"+service_id+"  "+ arrival_time.getHours() + "H:" + arrival_time.getMinutes()+" , qt="+qt+" , LES="+LES+ "]";
	}
	public String response_toString(int index) {
		return index+". ####### Réponse [" + arrival_time.getHours() + "H:" + arrival_time.getMinutes()+ "]";
	}
	public String departure_toString(int index) {
		return index+". --------- Départ [" + arrival_time.getHours() + "H:" + arrival_time.getMinutes()+ "]";
	}

	public String toString(Date date_debut) {
		return "Client [arrival_time=" +arrival_time + ", date_debut=" + date_debut + ", arrival_time-date_debut="
			+ ((arrival_time.getTime()-date_debut.getTime())/1000) + "]";
	}
	public double arrival_time_relative(Date date_debut) {
		return ((arrival_time.getTime()-date_debut.getTime())/1000);
	}
	public double departure_time_relative(Date date_debut) {
		return ((departure_time.getTime()-date_debut.getTime())/1000);
	}
	public double response_time_relative(Date date_debut) {
		if(response_time==null) return 0;
		return ((response_time.getTime()-date_debut.getTime())/1000);
	}
	public double transfer_time_relative(Date date_debut) {
		return ((transfer.getTime()-date_debut.getTime())/1000);
	}
	public double consult_time_relative(Date date_debut) {
		return ((consult.getTime()-date_debut.getTime())/1000);
	}
	public Date getArrival_time() {
		return arrival_time;
	}
	public void setArrival_time(Date arrival_time) {
		this.arrival_time = arrival_time;
	}
	public int getService_id() {
		return service_id;
	}
	public void setService_id(int service_id) {
		this.service_id = service_id;
	}
	public int getAgent_id() {
		return agent_id;
	}
	public void setAgent_id(int agent_id) {
		this.agent_id = agent_id;
	}
	public Date getResponse_time() {
		
		return response_time;
	}
	public String getResponse_time_string() {
		if(response_time==null) return "";

  	  	DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(arrival_time);
	}
	public void setResponse_time(Date response_time) {
		this.response_time = response_time;
	}
	public Date getConsult() {
		return consult;
	}
	public void setConsult(Date consult) {
		this.consult = consult;
	}
	public Date getTransfer() {
		return transfer;
	}
	public void setTransfer(Date transfer) {
		this.transfer = transfer;
	}
	public Date getDeparture_time() {
		return departure_time;
	}
	public void setDeparture_time(Date departure_time) {
		this.departure_time = departure_time;
	}
	public int getQt() {
		return qt;
	}
	public void setQt(int qt) {
		this.qt = qt;
	}
	public double getLES() {
		return LES;
	}
	public void setLES(double lES) {
		LES = lES;
	}
    
}
