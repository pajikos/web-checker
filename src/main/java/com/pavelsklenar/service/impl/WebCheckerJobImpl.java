package com.pavelsklenar.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.pavelsklenar.domain.SearchPage;
import com.pavelsklenar.domain.SearchResult;
import com.pavelsklenar.service.EmailService;
import com.pavelsklenar.service.SearchPageProcessor;
import com.pavelsklenar.service.SearchRepoService;
import com.pavelsklenar.service.SearchResultProcessor;

@ConditionalOnProperty(prefix = "job.webChecker", name = { "run", "cron" })
@Component
public class WebCheckerJobImpl {

	@Autowired
	private SearchRepoService searchRepoService;

	@Autowired
	private SearchResultProcessor searchResultProcessor;

	@Autowired
	private SearchPageProcessor searchPageProcessor;

	@Autowired
	private EmailService emailService;

	private static final Logger LOG = LoggerFactory
			.getLogger(WebCheckerJobImpl.class);

	@Scheduled(cron = "${job.webChecker.cron}")
	public void run() {

		List<SearchPage> allSearchPages = searchRepoService.getAllSearchPages();
		for (SearchPage searchPage : allSearchPages) {
			try {
				List<SearchResult> allSearchResults = searchPageProcessor
						.processSearch(searchPage);
				List<SearchResult> onlyNewSearchResults = searchResultProcessor
						.processCompare(allSearchResults);
				emailService.sendMails(onlyNewSearchResults);
				searchRepoService.saveAllSearchResults(onlyNewSearchResults);
			} catch (Exception e) {
				LOG.error("Cannot process page " + searchPage.getUrl()
						+ " due to the error " + e.getLocalizedMessage(), e);
			}
		}

	}

	public void setEmailService(EmailService emailService) {
		this.emailService = emailService;
	}

}
