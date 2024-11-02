package ubb.scs.map;

import ubb.scs.map.domain.Prietenie;
import ubb.scs.map.domain.Utilizator;
import ubb.scs.map.domain.validators.PrietenieValidator;
import ubb.scs.map.domain.validators.UtilizatorValidator;
import ubb.scs.map.domain.validators.ValidationException;
import ubb.scs.map.repository.Repository;
import ubb.scs.map.repository.database.PrietenieDB;
import ubb.scs.map.repository.database.UtilizatoriDB;
import ubb.scs.map.service.ServiceApp;
import ubb.scs.map.ui.UI;


public class Main {
    public static void main(String[] args) {
        try {
            Repository<Long, Utilizator> Utilizatori = new UtilizatoriDB(new UtilizatorValidator());
            Repository<Long, Prietenie> Prietenie = new PrietenieDB(new PrietenieValidator());
            ServiceApp service = new ServiceApp(Utilizatori, Prietenie);
            UI ui = new UI(service);
            ui.menu();
        } catch (IllegalArgumentException | ValidationException e) {
            System.out.println(e.getMessage());
        }
        System.out.println();
    }
}