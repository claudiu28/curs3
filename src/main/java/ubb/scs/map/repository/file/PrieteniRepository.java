package ubb.scs.map.repository.file;

import ubb.scs.map.domain.Prietenie;
import ubb.scs.map.domain.Utilizator;
import ubb.scs.map.domain.validators.Validator;
import ubb.scs.map.repository.Repository;

public class PrieteniRepository extends AbstractFileRepository<Long, Prietenie> {
    Repository <Long, Utilizator> repository;

    public PrieteniRepository(Validator<Prietenie> validator, Repository <Long,Utilizator> repository,String fileName) {
        super(validator, fileName);
        this.repository = repository;
    }

    @Override
    public Prietenie createEntity(String line) {
        String[] splited = line.split(";");
        Long nodPrietenie1 = Long.parseLong(splited[1]);
        Long nodPrietenie2 = Long.parseLong(splited[2]);

        Prietenie prietenie = new Prietenie(nodPrietenie1, nodPrietenie2);
        prietenie.setId(Long.parseLong(splited[0]));


        Utilizator u1 = repository.findOne(prietenie.getNodPrietenie1());
        Utilizator u2 = repository.findOne(prietenie.getNodPrietenie2());

        if (u1 != null && u2 != null) {
            u1.addFriends(u2);
            u2.addFriends(u1);
        }

        return prietenie;
    }
    @Override
    public String saveEntity(Prietenie entity) {
        return entity.getId() + ";" + entity.getNodPrietenie1() + ";" + entity.getNodPrietenie2();
    }

}
