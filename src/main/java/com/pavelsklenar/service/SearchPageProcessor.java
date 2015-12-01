package com.pavelsklenar.service;

import java.util.List;

import com.pavelsklenar.domain.SearchPage;
import com.pavelsklenar.domain.SearchResult;

/**
 * Interface for {@link SearchPage} processors
 * @author pavel.sklenar
 *
 */
public interface SearchPageProcessor {

	/**
	 * Process {@link SearchPage} and return list of found {@link SearchResult}
	 * @param searchPageToProcess
	 * @return
	 * @throws Exception
	 */
	public abstract List<SearchResult> processSearch(SearchPage searchPageToProcess)
			throws Exception;

}