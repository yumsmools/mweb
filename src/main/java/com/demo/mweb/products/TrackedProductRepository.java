package com.demo.mweb.products;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface TrackedProductRepository  extends CrudRepository<TrackedProduct, Long> {
	List<TrackedProduct> findByEmail(String email);
}