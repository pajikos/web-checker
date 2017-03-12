package com.pavelsklenar.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.pavelsklenar.domain.SearchPage;
import com.pavelsklenar.domain.SearchResult;
import com.pavelsklenar.repository.SearchPageRepository;
import com.pavelsklenar.repository.SearchResultRepository;
import com.pavelsklenar.service.SearchRepoService;

/**
 * Single point of contract service for for manipulating with repositories
 * 
 * @author pajik
 *
 */
@Component
@Transactional
class SearchRepoServiceImpl implements SearchRepoService {

	private final SearchPageRepository searchPageRepository;

	private final SearchResultRepository searchResultRepository;

	@Autowired
	public SearchRepoServiceImpl(SearchPageRepository searchPageRepository,
			SearchResultRepository searchResultRepository) {
		this.searchPageRepository = searchPageRepository;
		this.searchResultRepository = searchResultRepository;
	}

	public SearchPage getSearchPage(String name) {
		Assert.notNull(name, "Name must not be null");
		return this.searchPageRepository.findByName(name);
	}

	public Page<SearchResult> getSearchResults(SearchPage searchPage, Pageable pageable) {
		Assert.notNull(searchPage, "searchPage must not be null");
		return this.searchResultRepository.findBySearchPage(searchPage, pageable);
	}

	public List<SearchPage> getAllSearchPages() {
		return this.searchPageRepository.findAll();
	}

	public List<SearchResult> findByUrl(String url) {
		Assert.notNull(url, "URL must not be null");
		return searchResultRepository.findByUrl(url);
	}

	public void saveAllSearchResults(List<SearchResult> resultsToSave) {
		if (resultsToSave != null && !resultsToSave.isEmpty()) {
			searchResultRepository.save(resultsToSave);
		}
	}
}
