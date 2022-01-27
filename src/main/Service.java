package main;
import java.util.LinkedList;

public class Service {
    int service_id; // id du service
    int nombre_agent;// le nombre d'agents s ayant les comp´etences de servir le client
    public LinkedList<Client> waitList;// la liste d'attente
    double LES; // le temps d'attente du dernier client `a entrer en service
    //LinkedList<Call> servList = new LinkedList<Call> ();// en cours d's
    public Service(int service_id, int nombre_agent) {
        this.service_id = service_id;
        this.nombre_agent = nombre_agent;
        this.waitList = new LinkedList<Client> ();
        this.LES=0;
    }
    
}
