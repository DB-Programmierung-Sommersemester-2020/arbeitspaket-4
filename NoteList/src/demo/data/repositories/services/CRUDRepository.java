package demo.data.repositories.services;

import java.util.List;

public interface CRUDRepository <T, ID> {
	
	List<T> getAll();
	T getById(ID id);
	boolean create(T entity);
	boolean update(T entity);
	boolean delete(T entity);
}
