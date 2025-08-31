package com.mycheque.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.mycheque.domain.Customer;

import com.mycheque.mapping.CustomerMapper;

import com.mycheque.repository.jpa.CustomerRepository;

import com.mycheque.datatransfer.profile.Credentials;
import com.mycheque.datatransfer.profile.CredentialsUpdate;

import com.mycheque.service.wrapper.AuthorizedWrapper;
import com.mycheque.service.exception.TokenAlreadyInUseException;

/**
 * The default {@link CustomerService} implementation.
 *
 * @author resxnvnce
 */
@Service
@Transactional(readOnly = true)
public class TransactionalCustomerService implements CustomerService {

    private final CustomerMapper customerMapper;

    private final CustomerRepository customerRepository;

    /**
     * Constructs a {@code TransactionalCustomerService}.
     *
     * @param customerMapper     the mapper.
     * @param customerRepository the repository.
     */
    public TransactionalCustomerService(CustomerMapper customerMapper, CustomerRepository customerRepository) {
        this.customerMapper = customerMapper;
        this.customerRepository = customerRepository;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteById(long id) {
        this.customerRepository.deleteById(id);
    }

    @Override
    public boolean existsByUsername(String username) {
        return this.customerRepository.existsByUsername(username);
    }

    @Override
    public Optional<Customer> findByUsername(String username) {
        return this.customerRepository.findByUsername(username);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Customer register(Credentials credentials) throws CustomerServiceException {
        final var customer = this.customerMapper.toCustomer(credentials);

        try {
            return this.customerRepository.save(customer);
        }
        /* The only reason behind this is that the given token is already taken. */
        catch (DataIntegrityViolationException dive) {
            throw new TokenAlreadyInUseException(customer.getThirdpartyToken(), "failed to register", dive);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Customer applyUpdates(AuthorizedWrapper<CredentialsUpdate> wrapper) throws CustomerServiceException {
        final var customer = wrapper.customer();

        try {
            this.customerMapper.applyUpdates(wrapper.object(), customer);
            this.customerRepository.forceUpdate(customer);
        }
        /* The only reason behind this is that the given token is already taken. */
        catch (DataIntegrityViolationException dive) {
            throw new TokenAlreadyInUseException(customer.getThirdpartyToken(), "failed to applyUpdates", dive);
        }

        return customer;
    }
}
