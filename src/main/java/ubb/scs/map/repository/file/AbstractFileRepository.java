package ubb.scs.map.repository.file;

import ubb.scs.map.domain.Entity;
import ubb.scs.map.domain.validators.Validator;
import ubb.scs.map.repository.memory.InMemoryRepository;

import java.io.*;
import java.util.Optional;

public abstract class AbstractFileRepository<ID, E extends Entity<ID>> extends InMemoryRepository<ID, E> {
    private final String filename;

    public AbstractFileRepository(Validator<E> validator, String fileName) {
        super(validator);
        filename = fileName;
        loadFromFile();
    }


    public abstract E createEntity(String line);

    public abstract String saveEntity(E entity);

    private void loadFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                E entity = createEntity(line);
                super.save(entity);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error loading data from file: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<E> save(E entity) {
        Optional<E> e = super.save(entity);
        if(e.isEmpty()) {
            writeToFile();
        }
        return e;
    }

    private void writeToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (E entity : entities.values()) {
                String ent = saveEntity(entity);
                writer.write(ent);
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<E> delete(ID id) {
        Optional<E> e = super.delete(id);
        if(e.isPresent()) {
            writeToFile();
        }
        return e;
    }

    @Override
    public Optional<E> update(E entity) {
        Optional<E> entityToUpdate = super.update(entity);
        if(entityToUpdate.isEmpty()) {
            writeToFile();
        }
        return entityToUpdate;
    }
}
