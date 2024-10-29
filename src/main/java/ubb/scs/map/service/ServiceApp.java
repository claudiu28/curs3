package ubb.scs.map.service;

import ubb.scs.map.domain.Entity;
import ubb.scs.map.domain.Prietenie;
import ubb.scs.map.domain.Utilizator;
import ubb.scs.map.domain.validators.ValidationException;
import ubb.scs.map.repository.Repository;


import java.util.List;
import java.util.Objects;
import java.util.Optional;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ServiceApp {
    protected final Repository<Long, Utilizator> repoUsers;
    protected final Repository<Long, Prietenie> repoPrietenie;

    public ServiceApp(Repository<Long, Utilizator> repoUsers, Repository<Long, Prietenie> repoPrietenie) {
        this.repoUsers = repoUsers;
        this.repoPrietenie = repoPrietenie;
    }

    public Utilizator findUtilizatoriById(Long Id) {
        Optional<Utilizator> utilizator = repoUsers.findOne(Id);
        return utilizator.orElseThrow(() -> new ValidationException("Utilizator not found"));
    }

    public Long findPrietenieById(Long Id1, Long Id2) {
        var prietenii = repoPrietenie.findAll();
        return StreamSupport.stream(prietenii.spliterator(), false).
                filter(prietenie -> (Objects.equals(prietenie.getNodPrietenie1(), Id1) && Objects.equals(prietenie.getNodPrietenie2(), Id2)) ||
                        (Objects.equals(prietenie.getNodPrietenie1(), Id2) && Objects.equals(prietenie.getNodPrietenie2(), Id1)))
                .map(Entity::getId)
                .findFirst()
                .orElseThrow(() -> new ValidationException("Prietenie not found"));
    }

    private Long generateIdUtilizator() {
        var users = repoUsers.findAll();
        return StreamSupport.stream(users.spliterator(), false).mapToLong(Utilizator::getId).max()
                .orElse(0L) + 1;

    }

    private Long generateIdPrietenii() {
        var prietenii = repoPrietenie.findAll();
        return StreamSupport.stream(prietenii.spliterator(), false).mapToLong(Prietenie::getId).max().orElse(0L) + 1;
    }


    public Optional<Utilizator> adaugaUtilizator(Utilizator utilizator) {
        utilizator.setId(generateIdUtilizator());
        if (utilizator.getId() != null) {
            return repoUsers.save(utilizator);
        } else {
            throw new IllegalArgumentException("Id user doesn't exist");
        }
    }

    public Optional<Utilizator> removerUtilizator(Long id) {
        if (repoUsers.findOne(id).isEmpty()) {
            throw new IllegalArgumentException("Id user doesn't exist");
        }
        List<Prietenie> prieteniiDeSters = StreamSupport.stream(repoPrietenie.findAll().spliterator(), false)
                .filter(prietenie -> Objects.equals(prietenie.getNodPrietenie1(), id) || Objects.equals(prietenie.getNodPrietenie2(), id))
                .toList();

        prieteniiDeSters.forEach(prietenie -> removePrietenie(prietenie.getNodPrietenie1(), prietenie.getNodPrietenie2()));

        return repoUsers.delete(id);
    }

    public Iterable<Utilizator> getUtilizatori() {
        return repoUsers.findAll();
    }


    private boolean verificaPrietenie(Iterable<Prietenie> prietenii, Long u1Id, Long u2Id) {
        return StreamSupport.stream(prietenii.spliterator(), false)
                .anyMatch(prietenie ->
                        (Objects.equals(prietenie.getNodPrietenie1(), u1Id) && Objects.equals(prietenie.getNodPrietenie2(), u2Id))
                                ||
                                (Objects.equals(prietenie.getNodPrietenie2(), u1Id) && Objects.equals(prietenie.getNodPrietenie1(), u2Id))
                );
    }

    public void adaugaPrietenie(Utilizator u1, Utilizator u2) {
        Long u1Id = u1.getId();
        Long u2Id = u2.getId();

        Prietenie prietenie = new Prietenie(u1Id, u2Id);
        prietenie.setId(generateIdPrietenii());

        Iterable<Prietenie> prietenii = repoPrietenie.findAll();
        if (verificaPrietenie(prietenii, u1Id, u2Id)) {
            throw new IllegalArgumentException("Prietenie deja exista!");
        }

        repoPrietenie.save(prietenie);
        u1.addFriends(u2);
        u2.addFriends(u1);
    }

    public void removePrietenie(Long idU1, Long idU2) {
        Iterable<Prietenie> prietenii = repoPrietenie.findAll();
        if (!verificaPrietenie(prietenii, idU1, idU2)) {
            throw new IllegalArgumentException("Prietenia nu exista!");
        }

        Long idPrietenie = findPrietenieById(idU1, idU2);
        repoPrietenie.delete(idPrietenie);

        Optional<Utilizator> u1 = repoUsers.findOne(idU1);
        Optional<Utilizator> u2 = repoUsers.findOne(idU2);

        Utilizator u11 = u1.orElseThrow(() -> new ValidationException("Utilizator not found"));
        Utilizator u22 = u2.orElseThrow(() -> new ValidationException("Utilizator not found"));

        u11.removeFriends(u22);
        u22.removeFriends(u11);
    }
}
