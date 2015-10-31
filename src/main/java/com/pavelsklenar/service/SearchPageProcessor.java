package com.pavelsklenar.service;

import java.util.List;

import com.pavelsklenar.domain.SearchPage;
import com.pavelsklenar.domain.SearchResult;

public interface SearchPageProcessor {

	public abstract List<SearchResult> processSearch(SearchPage searchPageToProcess)
			throws Exception;

}