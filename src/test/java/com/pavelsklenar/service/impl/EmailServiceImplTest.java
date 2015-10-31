package com.pavelsklenar.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.mail.MessagingException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.pavelsklenar.DemoApplication;
import com.pavelsklenar.domain.SearchPage;
import com.pavelsklenar.domain.SearchResult;
import com.pavelsklenar.service.EmailService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = DemoApplication.class)
public class EmailServiceImplTest {

	@Autowired
	private EmailService emailService;

	@Before
	public void setup() {
	}

	@Test
	public void test() throws MessagingException {
		List<SearchResult> listToSend = new ArrayList<SearchResult>();
		listToSend.add(createTestSearchResult());
		listToSend.add(createTestSearchResult());
		emailService.sendMails(listToSend);
	}

	private SearchResult createTestSearchResult() {
		SearchPage searchPage = new SearchPage("sreality.cz", "http://www.sreality.cz/hledani/prodej/domy/ceske-budejovice");
		SearchResult searchResult = new SearchResult(searchPage);
		searchResult.setDescription("Včelná, okres České Budějovice");
		searchResult.setPrice("5 390 000 Kč");
		searchResult.setImageUrl("http://img.sreality.cz/middle2/dyn/201510/0206/3b/560e1f4b3bd8761813960000");
		searchResult.setTitle("Prodej rodinného domu 160 m², pozemek 754 m²");
		searchResult.setUrl("http://www.sreality.cz/detail/prodej/dum/rodinny/vcelna-vcelna-/510021724");
		return searchResult;
	}

}
