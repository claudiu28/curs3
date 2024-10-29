package ubb.scs.map.repository.file;

import ubb.scs.map.domain.Prietenie;
import ubb.scs.map.domain.Utilizator;
import ubb.scs.map.domain.validators.ValidationException;
import ubb.scs.map.domain.validators.Validator;
import ubb.scs.map.repository.Repository;

import java.util.Optional;

public class PrieteniRepository extends AbstractFileRepository<Long, Prietenie> {
    private final Repository<Long, Utilizator> repository;

    public PrieteniRepository(Validator<Prietenie> validator, Repository<Long, Utilizator> repository, String fileName) {
        super(validator, fileName);
        this.repository = repository;
        loadPrieteni();
    }

    private void loadPrieteni() {
        for (var prietenie : findAll()) {
            Optional<Utilizator> u1 = repository.findOne(prietenie.getNodPrietenie1());
            Optional<Utilizator> u2 = repository.findOne(prietenie.getNodPrietenie2());

            if (u1.isEmpty() && u2.isEmpty()) {
                throw new IllegalArgumentException("Nu s-a putut incarca corect din fisier!");
            } else if (u1.isPresent() && u2.isPresent()) {
                Utilizator u11 = u1.get();
                Utilizator u22 = u2.get();
                u11.addFriends(u22);
                u22.addFriends(u11);
            }
        }
    }

    @Override
    public Prietenie createEntity(String line) throws ValidationException {
        String[] splited = line.split(";");
        if (splited.length != 3) {
            throw new ValidationException("Numar insuficent de argumente pt o prietenie!");
        }
        Long id = Long.parseLong(splited[0]);
        Long nod1 = Long.parseLong(splited[1]);
        Long nod2 = Long.parseLong(splited[2]);
        Prietenie prietenie = new Prietenie(nod1, nod2);
        prietenie.setId(id);
        return prietenie;
    }

    @Override
    public String saveEntity(Prietenie entity) {
        return entity.getId() + ";" + entity.getNodPrietenie1() + ";" + entity.getNodPrietenie2();
    }

}
