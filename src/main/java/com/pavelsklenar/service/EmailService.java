package com.pavelsklenar.service;

import java.util.List;

import javax.mail.MessagingException;

import com.pavelsklenar.domain.SearchResult;

public interface EmailService {

	public abstract void sendMails(List<SearchResult> listToSend) throws MessagingException;

}