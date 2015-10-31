package com.pavelsklenar.service;

import java.util.List;

import com.pavelsklenar.domain.SearchResult;

public interface SearchResultProcessor {

	public abstract List<SearchResult> processCompare(
			List<SearchResult> resultsToCompare);

}