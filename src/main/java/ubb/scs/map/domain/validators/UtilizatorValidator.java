package ubb.scs.map.domain.validators;


import ubb.scs.map.domain.Utilizator;

public class UtilizatorValidator implements Validator<Utilizator> {
    @Override
    public void validate(Utilizator entity) throws ValidationException {
        if (entity.getFirstName().isEmpty())
            throw new ValidationException("Utilizatorul nu este valid!");
        if (entity.getLastName().isEmpty()) {
            throw new ValidationException("Utilizatorul nu este valid!");
        }
    }
}
