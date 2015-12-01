package com.pavelsklenar.service;

import java.util.List;

import com.pavelsklenar.domain.SearchResult;

/**
 * Processor for comparing {@link SearchResult} saved in DB and new incoming
 * @author pavel.sklenar
 *
 */
public interface SearchResultProcessor {

	/**
	 * Generally it processes incoming list of {@link SearchResult} and return only filtered {@link SearchResult}
	 * @param resultsToCompare list of all {@link SearchResult}
	 * @return compared list with filtered {@link SearchResult}
	 */
	public abstract List<SearchResult> processCompare(
			List<SearchResult> resultsToCompare);

}