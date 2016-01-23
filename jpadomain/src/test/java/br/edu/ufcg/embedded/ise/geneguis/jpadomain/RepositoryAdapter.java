package br.edu.ufcg.embedded.ise.geneguis.jpadomain;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

public class RepositoryAdapter<T, ID extends Serializable> implements JpaRepository<T, ID> {

	public Page<T> findAll(Pageable arg0) {
		return null;
	}

	public long count() {
		return 0;
	}

	public void delete(ID arg0) {
	}

	public void delete(T arg0) {
	}

	public void delete(Iterable<? extends T> arg0) {
	}

	public void deleteAll() {
	}

	public boolean exists(ID arg0) {
		return false;
	}

	public T findOne(ID arg0) {
		return null;
	}

	public <S extends T> S save(S arg0) {
		return null;
	}

	public List<T> findAll() {
		return null;
	}

	public List<T> findAll(Sort sort) {
		return null;
	}

	public List<T> findAll(Iterable<ID> ids) {
		return null;
	}

	public <S extends T> List<S> save(Iterable<S> entities) {
		return null;
	}

	public void flush() {
	}

	public <S extends T> S saveAndFlush(S entity) {
		return null;
	}

	public void deleteInBatch(Iterable<T> entities) {
	}

	public void deleteAllInBatch() {
	}

	public T getOne(ID id) {
		return null;
	}

}
