package ubb.scs.map.repository.database;

import ubb.scs.map.domain.Prietenie;
import ubb.scs.map.domain.validators.Validator;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class PrietenieDB extends AbstractDBRepository<Long, Prietenie> {

    public PrietenieDB(Validator<Prietenie> validator) {
        super(validator, TableName.PRIETENII);
    }

    @Override
    protected Prietenie toEntity(ResultSet resultSet) throws SQLException {
        Long userId1 = resultSet.getLong("UTILIZATOR1");
        Long userId2 = resultSet.getLong("UTILIZATOR2");
        Long Id = resultSet.getLong("ID");
        Prietenie prietenie = new Prietenie(userId1, userId2);
        prietenie.setId(Id);
        return prietenie;
    }

    @Override
    protected Map<String, Object> getEntityValues(Prietenie entity) {
        Map<String, Object> values = new HashMap<>();
        values.put("ID", entity.getId());
        values.put("UTILIZATOR1", entity.getNodPrietenie1());
        values.put("UTILIZATOR2", entity.getNodPrietenie2());
        return values;
    }
}
