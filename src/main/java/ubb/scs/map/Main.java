package ubb.scs.map;

import ubb.scs.map.domain.Prietenie;
import ubb.scs.map.domain.Utilizator;
import ubb.scs.map.domain.validators.PrietenieValidator;
import ubb.scs.map.domain.validators.UtilizatorValidator;
import ubb.scs.map.domain.validators.ValidationException;
import ubb.scs.map.repository.Repository;
import ubb.scs.map.repository.file.PrieteniRepository;
import ubb.scs.map.repository.file.UtilizatorRepository;
import ubb.scs.map.service.ServiceApp;
import ubb.scs.map.ui.UI;


public class Main {
    public static void main(String[] args) {
        try {
            Repository<Long, Utilizator> repoFileUtilizatori = new UtilizatorRepository(new UtilizatorValidator(), "./data/utilizatori.txt");
            Repository<Long, Prietenie> repoPrietenie = new PrieteniRepository(new PrietenieValidator(), repoFileUtilizatori, "./data/prieteni.txt");
            ServiceApp service = new ServiceApp(repoFileUtilizatori, repoPrietenie);
            UI ui = new UI(service);
            ui.menu();
        } catch (IllegalArgumentException | ValidationException e) {
            System.out.println(e.getMessage());
        }
        System.out.println();
    }
}