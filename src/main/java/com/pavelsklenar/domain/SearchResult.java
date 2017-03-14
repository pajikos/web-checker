package com.pavelsklenar.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import org.springframework.data.annotation.CreatedDate;

/**
 * Entity to save info about search result, i.e. result from searching on
 * {@link SearchPage}
 * 
 * @author pavel.sklenar
 * 
 */
@Entity
public class SearchResult implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne(optional = false)
	private SearchPage searchPage;

	@Column
	@Lob
	private String url;

	@Column
	private String price;

	@Column
	private String title;

	@Lob
	@Column
	private String imageUrl;

	@Lob
	@Column
	private String description;

	@Column
	@CreatedDate
	private Date createdDate;

	protected SearchResult() {
	}

	public SearchResult(SearchPage page) {
		this.searchPage = page;
	}

	public SearchPage getSearchPage() {
		return this.searchPage;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setSearchPage(SearchPage searchPage) {
		this.searchPage = searchPage;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "SearchResult [id=" + id + ", searchPage=" + searchPage
				+ ", url=" + url + ", price=" + price + ", title=" + title
				+ ", image=" + imageUrl + ", description=" + description + "]";
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
}
