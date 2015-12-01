package com.pavelsklenar.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.pavelsklenar.domain.EmailAddress;
import com.pavelsklenar.domain.SearchPage;
import com.pavelsklenar.domain.SearchResult;
import com.pavelsklenar.service.EmailService;

@Component
public class EmailServiceImpl implements EmailService {

	private static final String UTF_8 = "UTF-8";

	private static final Logger LOG = LoggerFactory
			.getLogger(EmailServiceImpl.class);

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private TemplateEngine templateEngine;

	@Value("${email.error}")
	private String errorEmail;

	@Value("${email.from}")
	private String emailFrom;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.pavelsklenar.service.impl.EmailService#sendMails(java.util.List)
	 */
	public void sendSearchResults(SearchPage searchPage,
			List<SearchResult> listToSend) throws MessagingException {
		if (listToSend == null || listToSend.isEmpty()) {
			LOG.info("Cannot send emails, it would be empty, no results to send.");
			return;
		}
		LOG.info("Trying to send mails with {} new search results",
				listToSend.size());
		// Prepare the evaluation context
		final Context ctx = new Context(Locale.getDefault());
		ctx.setVariable("name", listToSend.get(0).getSearchPage().getName());
		ctx.setVariable("searchUrl", listToSend.get(0).getSearchPage().getUrl());
		ctx.setVariable("subscriptionDate", new Date());
		ctx.setVariable("results", listToSend);

		// Prepare message using a Spring helper
		final MimeMessage mimeMessage = this.mailSender.createMimeMessage();
		final MimeMessageHelper message = new MimeMessageHelper(mimeMessage,
				UTF_8);
		if (listToSend.size() == 1) {
			message.setSubject(listToSend.get(0).getTitle() + ", "
					+ listToSend.get(0).getPrice());
		} else {
			message.setSubject("Found " + listToSend.size()
					+ " new results on "
					+ listToSend.get(0).getSearchPage().getName());
		}

		message.setFrom(emailFrom);
		if (searchPage.getEmailAddresses().isEmpty()) {
			LOG.warn(
					"SearchPage {} has empty e-mail addresses of recipients, no e-mail will be sent.",
					searchPage.getName());
		}

		for (EmailAddress emailAddress : searchPage.getEmailAddresses()) {
			message.setTo(emailAddress.getEmailAddress());
		}

		// Create the HTML body using Thymeleaf
		final String htmlContent = this.templateEngine.process("email", ctx);
		message.setText(htmlContent, true /* isHtml */);

		// Send email
		this.mailSender.send(mimeMessage);

		LOG.info("Email successfully send.");

	}

	/**
	 * Send an email with error
	 * 
	 * @param exception
	 * @throws MessagingException
	 */
	public void sendExcetionByEmail(Exception exception)
			throws MessagingException {
		LOG.info("Trying to send an email with error: {}",
				exception.getLocalizedMessage());
		// Prepare the evaluation context
		final Context ctx = new Context(Locale.getDefault());
		ctx.setVariable("message", exception.getLocalizedMessage());
		ctx.setVariable("stackTrace", stackTraceToHtmlString(exception));
		ctx.setVariable("generatedDate", new Date());

		// Prepare message using a Spring helper
		final MimeMessage mimeMessage = this.mailSender.createMimeMessage();
		final MimeMessageHelper message = new MimeMessageHelper(mimeMessage,
				UTF_8);
		message.setSubject("Web-checker exception: "
				+ exception.getLocalizedMessage());

		message.setFrom(emailFrom);
		message.setTo(errorEmail);

		final String htmlContent = this.templateEngine.process("error", ctx);
		message.setText(htmlContent, true /* isHtml */);

		// Send email
		this.mailSender.send(mimeMessage);

		LOG.info("Email with error successfully send.");
	}

	public void setMailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	private String stackTraceToHtmlString(Throwable e) {
		StringBuilder sb = new StringBuilder();
		for (StackTraceElement element : e.getStackTrace()) {
			sb.append(element.toString());
			sb.append("<br />");
		}
		return sb.toString();
	}
}
