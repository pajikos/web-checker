package com.pavelsklenar.service;

import java.util.List;

import javax.mail.MessagingException;

import com.pavelsklenar.domain.SearchPage;
import com.pavelsklenar.domain.SearchResult;

public interface EmailService {

	void sendExcetionByEmail(Exception exception) throws MessagingException;

	void sendSearchResults(SearchPage searchPage, List<SearchResult> listToSend)
			throws MessagingException;

}