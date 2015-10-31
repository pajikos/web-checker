package com.pavelsklenar.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;

import com.pavelsklenar.domain.SearchPage;
import com.pavelsklenar.domain.SearchResult;

public interface SearchResultRepository extends Repository<SearchResult, Long>, CrudRepository<SearchResult, Long> {

	public Page<SearchResult> findBySearchPage(SearchPage searchPage, Pageable pageable);
	
	public List<SearchResult> findByUrl(String url);

}
