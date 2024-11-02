package ubb.scs.map.repository.database;

import ubb.scs.map.domain.Utilizator;
import ubb.scs.map.domain.validators.Validator;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;


public class UtilizatoriDB extends AbstractDBRepository<Long, Utilizator> {

    public UtilizatoriDB(Validator<Utilizator> validator) {
        super(validator, TableName.UTILIZATORI);
    }

    @Override
    protected Utilizator toEntity(ResultSet resultSet) throws SQLException {
        String name = resultSet.getString("NUME");
        String prenume = resultSet.getString("PRENUME");
        Utilizator utilizator = new Utilizator(name, prenume);
        utilizator.setId(resultSet.getLong("ID"));
        return utilizator;
    }

    @Override
    protected Map<String, Object> getEntityValues(Utilizator user) {
        Map<String, Object> valsUsers = new HashMap<>();
        valsUsers.put("ID", user.getId());
        valsUsers.put("NUME", user.getFirstName());
        valsUsers.put("PRENUME", user.getLastName());
        return valsUsers;
    }
}
