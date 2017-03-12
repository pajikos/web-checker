package com.pavelsklenar.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pavelsklenar.domain.SearchResult;
import com.pavelsklenar.service.SearchRepoService;
import com.pavelsklenar.service.SearchResultProcessor;

/**
 * Processor of {@link SearchResult}s
 * 
 * @author pajik
 *
 */
@Component
public class SearchResultProcessorImpl implements SearchResultProcessor {

	SearchRepoService searchRepoService;

	private static final Logger LOG = LoggerFactory.getLogger(SearchResultProcessorImpl.class);

	@Autowired
	public SearchResultProcessorImpl(SearchRepoService searchRepoService) {
		this.searchRepoService = searchRepoService;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.pavelsklenar.service.impl.SearchResultProcessor#processCompare(java.
	 * util.List)
	 */
	public List<SearchResult> processCompare(List<SearchResult> resultsToCompare) {
		List<SearchResult> newSearchResults = new ArrayList<SearchResult>();
		for (SearchResult searchResult : resultsToCompare) {
			LOG.info("Trying to find SearchResult in DB: {}", searchResult);
			List<SearchResult> findByUrl = searchRepoService.findByUrl(searchResult.getUrl());
			for (SearchResult searchResultDB : findByUrl) {
				LOG.debug("DB contains this SearchResult (matched by URL): {}", searchResultDB);
			}
			if (findByUrl.isEmpty()) {
				LOG.info("SearchResult not found in DB: {}", searchResult);
				newSearchResults.add(searchResult);
			} else {
				LOG.info("The same SearchResult found in DB.");
			}
		}
		return newSearchResults;
	}

}
