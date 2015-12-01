/*
 * Copyright 2012-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pavelsklenar.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;

@Entity
public class SearchPage implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Long id;

	@Column(nullable = false)
	private String name;

	@Lob
	@Column(nullable = false)
	private String url;

	@Column(nullable=false, columnDefinition="boolean default true")
	private boolean enabled = true;

	@Column
	private String xpathToListOfResults;

	@Column
	private String xpathToImage;

	@Column
	private String xpathToDescription;

	@Column
	private String xpathToTitle;

	@Column
	private String xpathToPrice;

	@Column
	private String xpathToUrl;

	@Column(nullable=false, columnDefinition="boolean default false")
	private boolean javascriptEnabled;

	@ManyToMany(fetch=FetchType.EAGER)
	private Collection<EmailAddress> emailAddresses;

	/**
	 * Comma separated list of classes which should be omitted from search result
	 */
	@Column
	private String omitClassesInSearchResult;


	protected SearchPage() {
	}

	public SearchPage(String name, String url) {
		super();
		this.name = name;
		this.url = url;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getName() {
		return this.name;
	}

	public String getState() {
		return this.url;
	}

	@Override
	public String toString() {
		return getName() + "," + getState();
	}

	public String getXpathToListOfResults() {
		return xpathToListOfResults;
	}

	public void setXpathToListOfResults(String xpathToListOfResults) {
		this.xpathToListOfResults = xpathToListOfResults;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getXpathToImage() {
		return xpathToImage;
	}

	public void setXpathToImage(String xpathToImage) {
		this.xpathToImage = xpathToImage;
	}

	public String getXpathToDescription() {
		return xpathToDescription;
	}

	public void setXpathToDescription(String xpathToDescription) {
		this.xpathToDescription = xpathToDescription;
	}

	public String getXpathToTitle() {
		return xpathToTitle;
	}

	public void setXpathToTitle(String xpathToTitle) {
		this.xpathToTitle = xpathToTitle;
	}

	public String getXpathToPrice() {
		return xpathToPrice;
	}

	public void setXpathToPrice(String xpathToPrice) {
		this.xpathToPrice = xpathToPrice;
	}

	public String getXpathToUrl() {
		return xpathToUrl;
	}

	public void setXpathToUrl(String xpathToUrl) {
		this.xpathToUrl = xpathToUrl;
	}

	public boolean isJavascriptEnabled() {
		return javascriptEnabled;
	}

	public void setJavascriptEnabled(boolean javascriptEnabled) {
		this.javascriptEnabled = javascriptEnabled;
	}

	public String getOmitClassesInSearchResult() {
		return omitClassesInSearchResult;
	}

	/**
	 * Get parsed list of classes to be omit
	 * @return
	 */
	public List<String> getOmitClassesInSearchResultAsList() {
		if (omitClassesInSearchResult == null || omitClassesInSearchResult.isEmpty()) {
			return java.util.Collections.emptyList();
		}
		ArrayList<String> result = new ArrayList<String>();
		String[] classes = omitClassesInSearchResult.split(",");
		for (String string : classes) {
			result.add(string.trim());
		}
		return result;
	}

	public void setOmitClassesInSearchResult(String omitClassesInSearchResult) {
		this.omitClassesInSearchResult = omitClassesInSearchResult;
	}

	public Collection<EmailAddress> getEmailAddresses() {
		return emailAddresses;
	}

	public void setEmailAddresses(Collection<EmailAddress> emailAddresses) {
		this.emailAddresses = emailAddresses;
	}

	public void setName(String name) {
		this.name = name;
	}
}
