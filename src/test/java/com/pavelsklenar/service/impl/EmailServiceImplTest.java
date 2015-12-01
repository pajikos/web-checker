package com.pavelsklenar.service.impl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.Message.RecipientType;
import javax.mail.internet.MimeMessage;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.pavelsklenar.DemoApplication;
import com.pavelsklenar.domain.EmailAddress;
import com.pavelsklenar.domain.SearchPage;
import com.pavelsklenar.domain.SearchResult;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = DemoApplication.class)
public class EmailServiceImplTest {

	private static final String RESULT_MAIL_TO = "email@xyz.com";

	private MyJavaMailSender myJavaMailSender;

	@Autowired
	private EmailServiceImpl emailService;

	@Before
	public void setup() {
		myJavaMailSender = new MyJavaMailSender();
		emailService.setMailSender(myJavaMailSender);
	}

	@Test
	public void testSendEmailWithResults() throws MessagingException {
		List<SearchResult> listToSend = new ArrayList<SearchResult>();
		SearchPage searchPage = createSearchPage();
		listToSend.add(createTestSearchResult(searchPage));
		listToSend.add(createTestSearchResult(searchPage));

		emailService.sendSearchResults(searchPage, listToSend);
		
		MimeMessage lastMimeMessage = myJavaMailSender.getLastMimeMessage();
		Assert.assertEquals(1,
				lastMimeMessage.getRecipients(RecipientType.TO).length);
		Assert.assertEquals(RESULT_MAIL_TO,
				lastMimeMessage.getRecipients(RecipientType.TO)[0].toString());
	}

	/**
	 * Create instance of {@link SearchPage} for testing purpose
	 * 
	 * @return
	 */
	private SearchPage createSearchPage() {
		EmailAddress emailAddress = new EmailAddress();
		emailAddress.setEmailAddress(RESULT_MAIL_TO);
		emailAddress.setName("TEST");

		SearchPage searchPage = new SearchPage("sreality",
				"http://localhost:8089/sreality.html");
		searchPage
				.setXpathToListOfResults("/html/body/div[2]/div[1]/div[2]/div/div[4]/div/div/div/div/div[3]/div/div");
		searchPage.setXpathToImage("a/span[1]/img");
		searchPage.setXpathToDescription("div/div/span/span[1]");
		searchPage.setXpathToUrl("div/div/span/h2/a");
		searchPage.setXpathToTitle("div/div/span/h2/a/span");
		searchPage.setXpathToPrice("div/div/span/span[2]/span");
		searchPage.setOmitClassesInSearchResult("paging");

		searchPage.setEmailAddresses(Collections.singleton(emailAddress));

		return searchPage;
	}

	/**
	 * Create {@link SearchResult} for testing with defined {@link SearchPage}
	 * 
	 * @param searchPage
	 * @return
	 */
	private SearchResult createTestSearchResult(SearchPage searchPage) {
		SearchResult searchResult = new SearchResult(searchPage);
		searchResult.setDescription("Včelná, okres České Budějovice");
		searchResult.setPrice("5 390 000 Kč");
		searchResult
				.setImageUrl("http://img.sreality.cz/middle2/dyn/201510/0206/3b/560e1f4b3bd8761813960000");
		searchResult.setTitle("Prodej rodinného domu 160 m², pozemek 754 m²");
		searchResult
				.setUrl("http://www.sreality.cz/detail/prodej/dum/rodinny/vcelna-vcelna-/510021724");
		return searchResult;
	}

	private class MyJavaMailSender implements JavaMailSender {
		private boolean emailSent;
		private MimeMessage lastMimeMessage;

		public MimeMessage getLastMimeMessage() {
			return lastMimeMessage;
		}

		private JavaMailSender mailSender = new JavaMailSenderImpl();
		private final Logger LOG = LoggerFactory
				.getLogger(MyJavaMailSender.class);

		public void send(SimpleMailMessage arg0) throws MailException {
			// TODO Auto-generated method stub

		}

		public void send(SimpleMailMessage... arg0) throws MailException {
			// TODO Auto-generated method stub

		}

		public MimeMessage createMimeMessage() {
			return mailSender.createMimeMessage();
		}

		public MimeMessage createMimeMessage(InputStream arg0)
				throws MailException {
			return mailSender.createMimeMessage(arg0);
		}

		public void send(MimeMessage arg0) throws MailException {
			try {
				LOG.info("Fake email sent with subject: {}", arg0.getSubject());
				lastMimeMessage = arg0;
			} catch (MessagingException e) {
				throw new RuntimeException(e);
			}
			emailSent = true;

		}

		public void send(MimeMessage... arg0) throws MailException {
			emailSent = true;

		}

		public void send(MimeMessagePreparator arg0) throws MailException {
			// TODO Auto-generated method stub

		}

		public void send(MimeMessagePreparator... arg0) throws MailException {
			// TODO Auto-generated method stub

		}

		public boolean isEmailSent() {
			return emailSent;
		}

	}

}
