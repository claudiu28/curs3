package ubb.scs.map.domain.validators;

import ubb.scs.map.domain.Prietenie;
import ubb.scs.map.domain.Utilizator;
import ubb.scs.map.repository.Repository;

public class PrietenieValidator implements Validator<Prietenie> {
    private final Repository<Long, Utilizator> repository;

    public PrietenieValidator(Repository<Long, Utilizator> repository) {
        this.repository = repository;
    }

    @Override
    public void validate(Prietenie prietenie) throws ValidationException {
        Long u1Id = prietenie.getNodPrietenie1();
        Long u2Id = prietenie.getNodPrietenie2();

        if (repository.findOne(u1Id) == null) {
            throw new ValidationException("Utilizatorul cu ID-ul " + u1Id + " nu exista!");
        }
        if (repository.findOne(u2Id) == null) {
            throw new ValidationException("Utilizatorul cu ID-ul " + u2Id + " nu exista!");
        }
        if (u1Id.equals(u2Id)) {
            throw new ValidationException("Nu poti fi prieten cu tine insuti!");
        }
    }
}
