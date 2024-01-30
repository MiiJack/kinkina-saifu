package mg.fita.kinkinasaifu.repositories;

import java.util.List;

public interface ReCrudOperations<T, ID> {
  List<T> findAll();

  T findById(ID id);

  T save(T t);

  List<T> saveAll(List<T> toSave);

  void delete(ID id);
}
