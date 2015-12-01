package com.pavelsklenar.service.impl;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.mail.MessagingException;

import org.easymock.Capture;
import org.easymock.EasyMock;
import org.easymock.IAnswer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.pavelsklenar.DemoApplication;
import com.pavelsklenar.domain.EmailAddress;
import com.pavelsklenar.domain.SearchPage;
import com.pavelsklenar.domain.SearchResult;
import com.pavelsklenar.service.EmailAddressRepository;
import com.pavelsklenar.service.EmailService;
import com.pavelsklenar.service.SearchPageRepository;

/**
 * The main integration test for {@link WebCheckerJobImpl}
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = DemoApplication.class)
@TestPropertySource(properties = { "job.webChecker.run:true",
		"job.webChecker.cron=* */1 * * * *" })
public class WebCheckerJobImplTest {

	@Autowired
	private WebCheckerJobImpl webCheckerJob;

	@Autowired
	private SearchPageRepository searchPageRepository;

	@Autowired
	private EmailAddressRepository emailAddressRepository;

	private static final String RESULT_MAIL_TO = "email@xyz.com";

	@Rule
	public WireMockRule wireMockRule = new WireMockRule(8089);

	// In miliseconds
	private static final int maxTimeToWaitForJob = 30000;

	private EmailService emailService;
	
	private static final Logger LOG = LoggerFactory.getLogger(WebCheckerJobImplTest.class);

	@Before
	public void setup() throws Exception {
		SearchPage createdSearchPage = createSearchPage();
		createHttpStub(createdSearchPage);
		emailService = EasyMock.createMock(EmailService.class);
		webCheckerJob.setEmailService(emailService);
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
//	@Test
//	public void testFullJobRun() throws InterruptedException,
//			MessagingException {
//		int currentWaitingPeriod = 0;
//		int stepInMiliseconds = 200;
//		while (!emailService.isEmailSent()) {
//			Thread.sleep(stepInMiliseconds);
//			if (currentWaitingPeriod > maxTimeToWaitForJob) {
//				Assert.fail("Max time to wait was elapsed.");
//			} else {
//				currentWaitingPeriod = currentWaitingPeriod + stepInMiliseconds;
//			}
//		}
//
//	}

	@Test
	public void testFullJobRun2() throws MessagingException, InterruptedException {
		final ReentrantLock lock = new ReentrantLock();
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				lock.lock();
				LOG.info("Locked.");
			}
		}).start();
		System.err.println("BBBBBBBBBBBBb" + lock.isLocked());
		
		Capture<SearchPage> capturedSearchPage = Capture.newInstance();
		Capture<List<SearchResult>> capturedList = Capture.newInstance();
		emailService.sendSearchResults(EasyMock.capture(capturedSearchPage),
				EasyMock.capture(capturedList));

		EasyMock.expectLastCall().andAnswer(new IAnswer<Void>() {
			@Override
			public Void answer() {
				LOG.error("XXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
				lock.unlock();
				return null;
			}
		});
		System.err.println("GGGGGGGGGGGGGGGG" + lock.isLocked());
		EasyMock.replay(emailService);
		if (lock.tryLock(30, TimeUnit.SECONDS))  {
			LOG.error("YYYYYYYYYYYYYYYYYYYYYYYYYYYYYYY");
			Assert.assertEquals(20, capturedList.getValue().size());
		} else {
			Assert.fail("No email was sent.");
		}

		EasyMock.verify(emailService);

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

	private class MyEmailService implements EmailService {

		boolean isEmailSent;
		boolean isErrorEmailSent;

		@Override
		public void sendExcetionByEmail(Exception exception)
				throws MessagingException {
			this.isErrorEmailSent = true;
		}

		@Override
		public void sendSearchResults(SearchPage searchPage,
				List<SearchResult> listToSend) throws MessagingException {
			this.isEmailSent = true;
		}

		public boolean isEmailSent() {
			return isEmailSent;
		}

		public boolean isErrorEmailSent() {
			return isErrorEmailSent;
		}

	}

}
