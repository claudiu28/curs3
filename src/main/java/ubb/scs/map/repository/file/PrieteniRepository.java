package ubb.scs.map.repository.file;

import ubb.scs.map.domain.Prietenie;
import ubb.scs.map.domain.Utilizator;
import ubb.scs.map.domain.validators.ValidationException;
import ubb.scs.map.domain.validators.Validator;
import ubb.scs.map.repository.Repository;

public class PrieteniRepository extends AbstractFileRepository<Long, Prietenie> {
    private final Repository<Long, Utilizator> repository;

    public PrieteniRepository(Validator<Prietenie> validator, Repository<Long, Utilizator> repository, String fileName) {
        super(validator, fileName);
        this.repository = repository;
        loadPrieteni();
    }

    private void loadPrieteni() {
        for (var prietenie : findAll()) {
            Utilizator u1 = repository.findOne(prietenie.getNodPrietenie1());
            Utilizator u2 = repository.findOne(prietenie.getNodPrietenie2());

            if (u1 == null && u2 == null) {
                throw new IllegalArgumentException("Nu s-a putut incarca corect din fisier!");
            } else if (u1 != null && u2 != null) {
                u1.addFriends(u2);
                u2.addFriends(u1);
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
