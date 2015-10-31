package com.pavelsklenar.service.impl;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.pavelsklenar.domain.SearchPage;
import com.pavelsklenar.domain.SearchResult;
import com.pavelsklenar.service.SearchPageProcessor;
import com.pavelsklenar.service.SearchRepoService;

public class SearchPageProcessorImplTest {

	private SearchPageProcessor searchPageProcessor;
	private static final Logger LOG = LoggerFactory
			.getLogger(SearchPageProcessorImplTest.class);

	@Before
	public void setUp() throws Exception {
		searchPageProcessor = new SearchPageProcessorImpl();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSeznam() throws Exception {
		SearchPage searchPage = new SearchPage("sreality",
				"http://www.sreality.cz/hledani/prodej/domy/ceske-budejovice");
		searchPage
				.setXpathToListOfResults(".//*[contains(concat(' ',normalize-space(@class),' '),' property ')]");
		searchPage.setXpathToImage(".//*[contains(concat(' ',normalize-space(@class),' '),' img ')]");
		searchPage.setXpathToDescription(".//*[contains(concat(' ',normalize-space(@class),' '),' locality ')]");
		searchPage.setXpathToUrl(".//*[contains(concat(' ',normalize-space(@class),' '),' title ')]");
		searchPage.setXpathToTitle(".//*[contains(concat(' ',normalize-space(@class),' '),' title ')]");
		searchPage.setXpathToPrice(".//*[contains(concat(' ',normalize-space(@class),' '),' price ')]");
		searchPage.setOmitClassesInSearchResult("paging");
		List<SearchResult> processed = searchPageProcessor.processSearch(searchPage);

		LOG.info("Number of processed result from page {}: {}",
				searchPage.getUrl(), processed.size());

		for (SearchResult searchResult : processed) {
			LOG.info("Found result: {}", searchResult);
		}
	}

	@Test
	public void testJihoceskeReality() throws Exception {
		SearchPage searchPage = new SearchPage(
				"jihoceskereality",
				"http://jiho.ceskereality.cz/prodej/rodinne-domy/ceske-budejovice/ceskobudejovicko/nejnovejsi");
		searchPage
				.setXpathToListOfResults(".//*[contains(concat(' ',normalize-space(@class),' '),' div_nemovitost ')]");
		searchPage.setXpathToImage("div[1]/div/a/img[1]");
		searchPage.setXpathToDescription("div[2]/div/small");
		searchPage.setXpathToUrl(".//*[contains(concat(' ',normalize-space(@class),' '),' nemo ')]");
		searchPage.setXpathToTitle(".//*[contains(concat(' ',normalize-space(@class),' '),' nemo ')]");
		searchPage.setXpathToPrice(".//*[contains(concat(' ',normalize-space(@class),' '),' cena ')]");
		searchPage
				.setOmitClassesInSearchResult("topovana, list_navigation, top_vypis2");
		List<SearchResult> processed = searchPageProcessor.processSearch(searchPage);

		LOG.info("Number of processed result from page {}: {}",
				searchPage.getUrl(), processed.size());

		for (SearchResult searchResult : processed) {
			LOG.info("Found result: {}", searchResult);
		}

	}
	
	@Test
	public void testRemax() throws Exception {
		SearchPage searchPage = new SearchPage(
				"Remax",
				"http://www.remax-czech.cz/reality/vyhledavani/?price_to=4700000&regions[35][3301]=on&regions[35][3308]=on&sale=1&types[6]=on&order_by_published_date=0");
		searchPage
				.setXpathToListOfResults("/html/body/div[1]/div/div/div[1]/div[1]/div/ul/li");
		searchPage.setXpathToImage(".//*[contains(concat(' ',normalize-space(@class),' '),' img-rounded ')]");
		
		searchPage.setXpathToDescription(".//*[contains(concat(' ',normalize-space(@class),' '),' estate-address ')]");
		searchPage.setXpathToUrl("div[1]/h3/a");
		searchPage.setXpathToTitle("div[1]/h3/a");
		searchPage.setXpathToPrice(".//*[contains(concat(' ',normalize-space(@class),' '),' price ')]");
		searchPage
				.setOmitClassesInSearchResult("btn-item");
		List<SearchResult> processed = searchPageProcessor.processSearch(searchPage);

		LOG.info("Number of processed result from page {}: {}",
				searchPage.getUrl(), processed.size());

		for (SearchResult searchResult : processed) {
			LOG.info("Found result: {}", searchResult);
		}

	}
	
	@Test
	public void testCentury21() throws Exception {
		SearchPage searchPage = new SearchPage(
				"Century21",
				"http://www.century21.cz/nemovitosti?search[ptype]=house&search[price_to]=5000000&search[locality][0]=36&search[locality][1]=29");
		searchPage
				.setXpathToListOfResults(".//*[contains(concat(' ',normalize-space(@class),' '),' item ')]");
		searchPage.setXpathToImage("div[1]/a/img");
		
		searchPage.setXpathToDescription("div[2]/p[1]");
		searchPage.setXpathToUrl("div[2]/h4/a");
		searchPage.setXpathToTitle(".//*[contains(concat(' ',normalize-space(@class),' '),' title ')]");
		searchPage.setXpathToPrice(".//*[contains(concat(' ',normalize-space(@class),' '),' amount ')]");
		//searchPage
		//		.setOmitClassesInSearchResult("btn-item");
		List<SearchResult> processed = searchPageProcessor.processSearch(searchPage);

		LOG.info("Number of processed result from page {}: {}",
				searchPage.getUrl(), processed.size());

		for (SearchResult searchResult : processed) {
			LOG.info("Found result: {}", searchResult);
		}

	}
	
	@Test
	public void testRkStejskal() throws Exception {
		SearchPage searchPage = new SearchPage(
				"RkStejskal",
				"http://rkstejskal.cz/nemovitosti/?cena_do=5000000&kraj=kraj35&okres=okres3301&typ=1&druh=2&radit=datum");
		searchPage
				.setXpathToListOfResults(".//*[contains(concat(' ',normalize-space(@class),' '),' list-group-item ')]");
		searchPage.setXpathToImage("div/div[1]/div/a/img");
		
		searchPage.setXpathToDescription(".//*[contains(concat(' ',normalize-space(@class),' '),' city ')]");
		searchPage.setXpathToUrl("div/div[2]/table/tbody/tr[2]/td[1]/a");
		searchPage.setXpathToTitle(".//*[contains(concat(' ',normalize-space(@class),' '),' title ')]");
		searchPage.setXpathToPrice(".//*[contains(concat(' ',normalize-space(@class),' '),' price ')]");
		//searchPage
		//		.setOmitClassesInSearchResult("btn-item");
		List<SearchResult> processed = searchPageProcessor.processSearch(searchPage);

		LOG.info("Number of processed result from page {}: {}",
				searchPage.getUrl(), processed.size());

		for (SearchResult searchResult : processed) {
			LOG.info("Found result: {}", searchResult);
		}

	}

}
