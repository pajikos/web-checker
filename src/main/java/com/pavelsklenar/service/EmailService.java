package com.pavelsklenar.service;

import java.util.List;

import javax.mail.MessagingException;

import com.pavelsklenar.domain.SearchPage;
import com.pavelsklenar.domain.SearchResult;

/**
 * Interface for sending emails
 * @author pavel.sklenar
 *
 */
public interface EmailService {

	void sendExcetionByEmail(Exception exception) throws MessagingException;

	void sendSearchResults(SearchPage searchPage, List<SearchResult> listToSend)
			throws MessagingException;

}