package ubb.scs.map.domain.validators;

import ubb.scs.map.domain.Prietenie;

import java.util.Objects;


public class PrietenieValidator implements Validator<Prietenie> {

    @Override
    public void validate(Prietenie entity) throws ValidationException {
        if (entity.getNodPrietenie1() <= 0)
            throw new ValidationException("Id primului utilizator nu este valid!");
        if (entity.getNodPrietenie2() <= 0) {
            throw new ValidationException("Id al doilea utilizator nu este valid!");
        }
        if(Objects.equals(entity.getNodPrietenie1(), entity.getNodPrietenie2())){
            throw new ValidationException("Nu poti fi prieten cu tine!");
        }
    }
}


