package com.pavelsklenar.service;

import java.util.List;

import javax.mail.MessagingException;

import com.pavelsklenar.domain.SearchResult;

public interface EmailService {

	void sendSearchResults(List<SearchResult> listToSend) throws MessagingException;

	void sendExcetionByEmail(Exception exception) throws MessagingException;

}