package com.pavelsklenar.service.impl;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;

import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
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

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.pavelsklenar.DemoApplication;
import com.pavelsklenar.domain.EmailAddress;
import com.pavelsklenar.domain.SearchPage;
import com.pavelsklenar.service.EmailAddressRepository;
import com.pavelsklenar.service.SearchPageRepository;

/**
 * The main integration test for {@link WebCheckerJobImpl}
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = DemoApplication.class)
@TestPropertySource(properties = { "job.webChecker.run:true",
		"job.webChecker.cron=* */1 * * * *" })
public class WebCheckerJobImplTest {

	private static final String RESULT_MAIL_TO = "email@xyz.com";

	@Autowired
	private WebCheckerJobImpl webCheckerJob;

	@Autowired
	private SearchPageRepository searchPageRepository;

	@Autowired
	private EmailAddressRepository emailAddressRepository;

	@Autowired
	private EmailServiceImpl emailService;

	@Rule
	public WireMockRule wireMockRule = new WireMockRule(8089);

	// In miliseconds
	private static final int maxTimeToWaitForJob = 30000;

	private MyJavaMailSender myJavaMailSender;

	@Before
	public void setup() throws Exception {
		SearchPage createdSearchPage = createSearchPage();
		myJavaMailSender = new MyJavaMailSender();
		emailService.setMailSender(myJavaMailSender);
		createHttpStub(createdSearchPage);
	}

	/**
	 * Global test which runs a global job with trying to parse a remote html
	 * page and send an email with results<br />
	 * Remote web page is simulated with mock and email service does not send
	 * any real e-mail
	 *
	 * @throws InterruptedException
	 * @throws MessagingException
	 */
	@Test
	public void testFullJobRun() throws InterruptedException,
			MessagingException {
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

		emailAddressRepository.save(emailAddress);

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

		searchPageRepository.save(searchPage);
		return searchPage;
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

	/**
	 * Create a HTTP server page which returns a specific page to a specific
	 * request<br />
	 *
	 * @param searchPage
	 * @throws Exception
	 */
	private void createHttpStub(SearchPage searchPage) throws Exception {
		stubFor(get(urlEqualTo("/" + searchPage.getName() + ".html"))
				.willReturn(
						aResponse()
								.withStatus(200)
								.withHeader("Content-Type",
										"text/html; charset=UTF-8")
								.withBody(
										readFileFromClassPathAsString(
												"/pages/"
														+ searchPage.getName()
														+ ".html").getBytes())));
	}

	/**
	 * Read content of file on classpath in to String
	 *
	 * @param file
	 * @return
	 * @throws Exception
	 * @throws IOException
	 */
	private String readFileFromClassPathAsString(String file) throws Exception {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(this.getClass()
					.getResourceAsStream(file)));
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			return sb.toString();
		} finally {
			if (br != null) {
				br.close();
			}
		}
	}
}
