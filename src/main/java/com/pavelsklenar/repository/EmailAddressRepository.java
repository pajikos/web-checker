package com.pavelsklenar.repository;

import org.springframework.data.repository.CrudRepository;

import com.pavelsklenar.domain.EmailAddress;

/**
 * Interface for manipulation with {@link EmailAddress}
 * @author pavel.sklenar
 *
 */
public interface EmailAddressRepository extends
		CrudRepository<EmailAddress, Long> {

}
