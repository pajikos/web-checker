package com.pavelsklenar.service.impl;

import java.io.InputStream;
import java.util.List;

import javax.mail.MessagingException;
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
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.pavelsklenar.DemoApplication;
import com.pavelsklenar.domain.SearchPage;
import com.pavelsklenar.domain.SearchResult;
import com.pavelsklenar.service.EmailService;
import com.pavelsklenar.service.SearchPageRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = DemoApplication.class)
@TestPropertySource(properties = { "run.job.webChecker:true" })
public class WebCheckerJobImplTest {

	@Autowired
	private WebCheckerJobImpl webCheckerJob;

	@Autowired
	private SearchPageRepository searchPageRepository;

	@Autowired
	private EmailServiceImpl emailService;

	// In miliseconds
	private static final int maxTimeToWaitForJob = 30000;

	private MyJavaMailSender myJavaMailSender;

	@Before
	public void setup() {
		searchPageRepository.save(createSearchPage());
		myJavaMailSender = new MyJavaMailSender();
		emailService.setMailSender(myJavaMailSender);

	}

	@Test
	public void test() throws InterruptedException {
		int currentWaitingPeriod = 0;
		int stepInMiliseconds = 200;
		while (!myJavaMailSender.isEmailSent()) {
			Thread.sleep(stepInMiliseconds);
			if (currentWaitingPeriod > maxTimeToWaitForJob) {
				Assert.fail("Max time to wait was elapsed.");
			} else {
				currentWaitingPeriod = currentWaitingPeriod + stepInMiliseconds;
			}
		}
	}

	private SearchPage createSearchPage() {
		SearchPage searchPage = new SearchPage("sreality",
				"http://www.sreality.cz/hledani/prodej/domy/ceske-budejovice");
		searchPage
				.setXpathToListOfResults("/html/body/div[2]/div[1]/div[2]/div/div[4]/div/div/div/div/div[3]/div/div");
		searchPage.setXpathToImage("a/span[1]/img");
		searchPage.setXpathToDescription("div/div/span/span[1]");
		searchPage.setXpathToUrl("div/div/span/h2/a");
		searchPage.setXpathToTitle("div/div/span/h2/a/span");
		searchPage.setXpathToPrice("div/div/span/span[2]/span");
		searchPage.setOmitClassesInSearchResult("paging");
		return searchPage;
	}
	
	private class MyJavaMailSender implements JavaMailSender {
		private boolean emailSent;

	    private JavaMailSender mailSender = new JavaMailSenderImpl();
		private final Logger LOG = LoggerFactory.getLogger(MyJavaMailSender.class);

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
