package ubb.scs.map.service;

import ubb.scs.map.domain.Prietenie;
import ubb.scs.map.domain.Utilizator;
import ubb.scs.map.repository.Repository;

import java.util.Objects;

public class ServiceApp {
    protected final Repository<Long, Utilizator> repoUsers;
    protected final Repository<Long, Prietenie> repoPrietenie;

    public ServiceApp(Repository<Long, Utilizator> repoUsers, Repository<Long, Prietenie> repoPrietenie) {
        this.repoUsers = repoUsers;
        this.repoPrietenie = repoPrietenie;
    }

    public Utilizator findUtilizatoriById(Long Id) {
        return repoUsers.findOne(Id);
    }

    private Long generateIdUtilizator() {
        Long maxim = 0L;
        for (var user : repoUsers.findAll()) {
            if (maxim < user.getId()) {
                maxim = user.getId();
            }
        }
        return maxim + 1;
    }

    private Long generateIdPrietenii() {
        Long maxim = 0L;
        for (var prietenie : repoPrietenie.findAll()) {
            if (maxim < prietenie.getId()) {
                maxim = prietenie.getId();
            }
        }
        return maxim + 1;
    }

    public Utilizator adaugaUtilizator(Utilizator utilizator) {
        utilizator.setId(generateIdUtilizator());
        if (utilizator.getId() != null) {
            return repoUsers.save(utilizator);
        } else {
            throw new IllegalArgumentException("Id user doesn't exist");
        }
    }

    public Utilizator removerUtilizator(Long id) {
        if (repoUsers.findOne(id) == null) {
            throw new IllegalArgumentException("Id user doesn't exist");
        }
        return repoUsers.delete(id);
    }

    public Iterable<Utilizator> getUtilizatori() {
        return repoUsers.findAll();
    }

    public Iterable<Prietenie> getPrietenii() {
        return repoPrietenie.findAll();
    }

    public void adaugaPrietenie(Utilizator u1, Utilizator u2) {
        Long u1Id = u1.getId();
        Long u2Id = u2.getId();
        Prietenie prietenie = new Prietenie(u1Id, u2Id);
        prietenie.setId(generateIdPrietenii());
        for (var u : repoPrietenie.findAll()) {
            if ((Objects.equals(u.getNodPrietenie1(), u1Id) && Objects.equals(u.getNodPrietenie2(), u2Id)) ||
                    (Objects.equals(u.getNodPrietenie2(), u1Id) && Objects.equals(u.getNodPrietenie1(), u2Id))) {
                throw new IllegalArgumentException("Prietenie deja exista!");
            }
        }
        repoPrietenie.save(prietenie);
        u1.addFriends(u2);
        u2.addFriends(u1);
    }

    public void removePrietenie(Long idU1, Long idU2) {
        Long idPrietenie = 0L;
        for(var u : repoPrietenie.findAll()) {
            if ((Objects.equals(u.getNodPrietenie1(), idU1) && Objects.equals(u.getNodPrietenie2(), idU2)) ||
                    (Objects.equals(u.getNodPrietenie2(), idU1) && Objects.equals(u.getNodPrietenie1(), idU2))) {
                idPrietenie = u.getId();
            }
        }
        if(idPrietenie == 0L){
            throw new IllegalArgumentException("Prietenia nu exista!");
        }
        repoPrietenie.delete(idPrietenie);
        Utilizator u1 = repoUsers.findOne(idU1);
        Utilizator u2 = repoUsers.findOne(idU2);
        if (u1 != null && u2 != null) {
            u1.removeFriends(u2);
            u2.removeFriends(u1);
        }
    }
}
