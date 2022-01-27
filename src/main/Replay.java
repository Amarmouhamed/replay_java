package main;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import umontreal.ssj.simevents.*;
import umontreal.ssj.simevents.Sim;

public class Replay {
	static String path="C:/Users/Amar/Desktop/M2BI/Performences des systemes decisionnels/donnees/data Vanad/calls/";
    List<Client> les_clients;
    HashMap<Integer, Service> les_services;
    
    public Replay(List<Client> les_clients,HashMap<Integer, Service> les_services) {
        this.les_clients = les_clients;
        this.les_services=les_services;
    }
    public void simulate(double timeHorizon) {
        Sim.init();
        new EndOfSim().schedule(timeHorizon);
        // appel des arrives des clients
        this.events_client();

        Sim.start();
    }
    public void events_client(){
    	Date date_premier_client=les_clients.get(0).arrival_time;
        for (Client client : les_clients) {
			new Arrival(client).schedule(client.arrival_time_relative(date_premier_client)); // on prévoit l'arrivé du client
			if(client.response_time==null) {// pas de réponse pour le client
				
			}else {
				new Response(client).schedule(client.response_time_relative(date_premier_client));// on prévoit la reponse du client
			}
			new Departure(client).schedule(client.departure_time_relative(date_premier_client));// on prévoit le départ du client
		}
    }

    class Arrival extends Event {
        Client client ;
        public Arrival(Client Client){
        	this.client=Client;
        }
        public void actions() {
        	Service s=les_services.get(client.service_id);// retrouver le service du client
        	client.setQt(s.waitList.size());// recuperer la taille de la file d'attente
        	client.nb_agent=s.nombre_agent;

        	les_services.get(client.service_id).waitList.add(client);// le client est rentré dans la file d'attente du service demandé
            System.out.println(client.arrival_toString(les_clients.indexOf(client)));
        }
    }
    
    class Response extends Event {
        Client client ;
        public Response(Client Client){this.client=Client;}
        public void actions() {
        	Service s=les_services.get(client.service_id);// retrouver le service du client
        	client.setLES(s.LES);  // le temps d'attente du dernier client a entrer en service
        	
        	les_services.get(client.service_id).waitList.remove(client);// on l'enleve de la file d'attente du service
        	
            System.out.println(client.response_toString(les_clients.indexOf(client)));
        }
    }

    class Departure extends Event {
        Client client ;
        public Departure(Client Client){this.client=Client;}
        public void actions() {
        	Date date_premier_client=les_clients.get(0).arrival_time;
        	
        	System.out.println(client.departure_toString(les_clients.indexOf(client)));
        	if(client.response_time==null) {// pas de réponse pour le client
        		les_services.get(client.service_id).waitList.remove(client);// on l'enleve de la file d'attente du service
			}else {// il vient du service
				les_services.get(client.service_id).LES=client.response_time_relative(date_premier_client) - client.arrival_time_relative(date_premier_client);
			}
        }
    }

    class EndOfSim extends Event {
        public void actions() {
        	save(les_clients);
            Sim.stop();
        }
    }

    public static void main(String[] args) throws IOException {
        System.out.println("Début du programme");
        List<Client> les_clients=readData();
        HashMap<Integer, Service>  les_services=read_services();
        
        Replay queue = new Replay(les_clients,les_services);
        Date date_premier_appel=les_clients.get(0).arrival_time;
        Client dernier_client=les_clients.get(les_clients.size()-1);
        double temps_simulation= dernier_client.departure_time_relative(date_premier_appel); // difference entre le premier et le dernier appel (en seconde)
        queue.simulate(temps_simulation);
        System.out.println("Fin du programme");
    }
    static List<Client> readData() throws IOException { 
        String file = path+"data/calls-2014-01.csv";
        //String file = path+"csv/test.csv";
        List<Client> content = new ArrayList<>();
        try(BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = "";
            while ((line = br.readLine()) != null) {
                String[] tab= line.split(",");
                
                if (castDate(tab[0])!=null) {
                    Date arrival_time=castDate(tab[0]);// heure d'arrive
                    int service_id=castInt(tab[1]); // type de client
                    int	agent_id=castInt(tab[2]);// id_agent
                    Date response_time=castDate(tab[3]);// heure de réponse de l'agent
                    Date consult=castDate(tab[4]);
                    Date transfer=castDate(tab[5]);// heure de transfert de l'appel
                    Date departure_time=castDate(tab[6]);// fin d'appel
                	
                    Client c= new Client(arrival_time,service_id,agent_id,response_time,consult,transfer,departure_time);
                    content.add(c);
                    
                	//System.out.println("line= "+c.toString(content.get(0).getArrival_time()));
				} else {
					System.out.println("Premiere ligne ou date invalide");
				}
                
            }
        } catch (FileNotFoundException e) {
          System.out.println("Probleme de lecture du fichier");
        }
        return content;
    }

    static HashMap<Integer, Service> read_services() throws IOException {
        String file = path+"csv/service_nb_agent.csv";
        HashMap<Integer, Service>  content = new HashMap<Integer, Service> ();
        try(BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = "";
            while ((line = br.readLine()) != null) {
                String[] tab= line.split(",");
                
                if (castInt(tab[0])!=0) {
                    int service_id=castInt(tab[0]); // type de client
                    int	nb_agent=castInt(tab[1]);// id_agent
                	
                    Service s=new Service(service_id,nb_agent);
                    content.put(service_id, s);
				} else {
					System.out.println("lecture services Premiere ligne ou int invalide");
				}
                
            }
        } catch (FileNotFoundException e) {
          System.out.println("Probleme de lecture du fichier");
        }
        return content;
    }
    public static Date castDate(String dateStr) {
    	if(dateStr==null || dateStr=="null") {
    		return null;
    	}
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setLenient(false);
        try {
            Date d= (Date) sdf.parse(dateStr);
            return d;
        } catch (ParseException e) {
            return null;
        }
    }
    public static int castInt(String s) {
    	try {
			return Integer.parseInt(s);
		} catch (Exception e) {
			return 0;
		}
    }
    public static void save(List<Client> liste) {
      FileWriter writer;
      try {
			  writer = new FileWriter(path+"data_replayed_csv/mois_1.csv");
			  //writer.write("numero,queue_name,agent_number , date_received,response,hangup,longueur_file_attente,LES,arrival_time_relative,response_time_relative,departure_time_relative");
			  writer.write("numero,queue_name,agent_number , date_received,response,hangup,longueur_file_attente,LES,nb_agent");
	          writer.write("\n"); // newline

	          Date date_premier_appel=liste.get(0).arrival_time;
	          
		      for(Client c : liste) {
		    	  DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		    	  /*String ligne= liste.indexOf(c) +","+c.service_id+","+c.agent_id+","+sdf.format(c.arrival_time)+","+c.getResponse_time_string()
		    	  +","+sdf.format(c.departure_time)+","+c.qt+","+c.LES+","+c.arrival_time_relative(date_premier_appel)
		    	  +","+c.response_time_relative(date_premier_appel)+","+c.departure_time_relative(date_premier_appel);*/
		    	  String ligne= liste.indexOf(c) +","+c.service_id+","+c.agent_id+","+sdf.format(c.arrival_time)+","+c.getResponse_time_string()
		    	  +","+sdf.format(c.departure_time)+","+c.qt+","+c.LES+","+c.nb_agent;
		          writer.write(ligne);
		          writer.write("\n"); // newline
		      }
		      writer.close();
	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
