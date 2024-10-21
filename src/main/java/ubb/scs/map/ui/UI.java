package ubb.scs.map.ui;

import ubb.scs.map.domain.Utilizator;
import ubb.scs.map.service.ServiceApp;

import java.util.Objects;
import java.util.Scanner;

public class UI {
    private static ServiceApp serviceApp;

    public UI(ServiceApp service) {
        serviceApp = service;
    }

    public void menu() {
        Scanner scanner = new Scanner(System.in);
        boolean ok = true;
        while (ok) {
            System.out.println("1. Adauga utilizator!\n");
            System.out.println("2. Sterge utilizator!\n");
            System.out.println("3. Vezi utilizatori!\n");
            System.out.println("4. Adauga prietenie utilizatori!\n");
            System.out.println("5. Sterge prietenie utilizatori!\n");
            System.out.println("6. Vezi prietenie utilizatori!\n");
            System.out.println("0. Termina utilizare!\n");
            System.out.println("Optiunea dorita este:\n");
            int numar = scanner.nextInt();
            switch (numar) {
                case 1:
                    adaugaUtilizator();
                    break;
                case 2:
                    removeUtilizator();
                    break;
                case 3:
                    getUtilizator();
                    break;
                case 4:
                    adaugaPrietenie();
                    break;
                case 5:
                    stergePrietenie();
                    break;
                case 6:
                    veziPrietenii();
                    break;
                case 0:
                    ok = false;
                    break;
            }
        }
    }
    private static void veziPrietenii(){
        var Users = serviceApp.getUtilizatori();
        var Prietenii = serviceApp.getPrietenii();
        for(var user : Users) {
            System.out.println("Pritenii lui " + user.getLastName() + " " + user.getFirstName() + " " + "sunt:" + user.getFriends());
        }
    }

    private static void adaugaPrietenie(){
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("ID utilizator 1:");
            Long id1 = scanner.nextLong();
            Utilizator utilizator1 = serviceApp.findUtilizatoriById(id1);
            if (utilizator1 == null) {
                System.out.println("Utilizatorul 1 nu exista!");
                return;
            }
            System.out.println("ID utilizator 2:");
            Long id2 = scanner.nextLong();
            Utilizator utilizator2 = serviceApp.findUtilizatoriById(id2);
            if (utilizator2 == null) {
                System.out.println("Utilizatorul 2 nu exista.");
                return;
            }
            serviceApp.adaugaPrietenie(utilizator1, utilizator2);
            System.out.println("Prietenia a fost adaugata cu succes!");

        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    private static void stergePrietenie(){
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Id Prietenie:");
            Long id = scanner.nextLong();
            serviceApp.removePrietenie(id);
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void adaugaUtilizator() {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Nume utilizator nou:");
            String nume = scanner.nextLine();
            System.out.println("Prenume utilizator nou:");
            String prenume = scanner.nextLine();
            Utilizator utilizator = new Utilizator(nume, prenume);
            if(serviceApp.adaugaUtilizator(utilizator) == null){
                System.out.println("Utilizator adaugat cu succes!");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void removeUtilizator() {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Id -ul de utilizator pe care il stergeti:");
            Long id = scanner.nextLong();
            Utilizator utilizator = serviceApp.removerUtilizator(id);
            if(Objects.equals(utilizator.getId(), id)){
                System.out.println("Utilizatorul " + utilizator.getLastName() + " "+ utilizator.getFirstName() + " este sters succes!");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void getUtilizator() {
        System.out.println("Utilizatori:\n");
        var utilizatori = serviceApp.getUtilizatori();
        for (var utilizator : utilizatori) {
            System.out.println(utilizator);
        }
    }
}
