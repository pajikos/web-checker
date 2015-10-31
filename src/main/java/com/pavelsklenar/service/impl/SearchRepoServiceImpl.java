/*
 * Copyright 2012-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
import com.pavelsklenar.service.SearchPageRepository;
import com.pavelsklenar.service.SearchRepoService;
import com.pavelsklenar.service.SearchResultRepository;

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

	public Page<SearchResult> getSearchResults(SearchPage searchPage,
			Pageable pageable) {
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
