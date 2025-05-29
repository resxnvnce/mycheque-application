package com.mycheque.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.mycheque.domain.Customer;
import com.mycheque.repository.CustomerRepository;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.mycheque.mapping.CustomerMapper;
import com.mycheque.datatransfer.accept.Credentials;

import com.mycheque.service.wrapper.UpdatesWrapper;
import com.mycheque.service.exception.TokenAlreadyInUseException;

/**
 * The default {@link CustomerService} implementation.
 *
 * @author resxnvnce
 */
@Service
@Transactional(readOnly = true)
public class TransactionalCustomerService implements CustomerService {

    /**
     * The mapper to convert from the data transfer objects to an
     * actual {@code Customer} and to apply updates on existing entities.
     */
    private final CustomerMapper customerMapper;

    /**
     * The repository.
     */
    private final CustomerRepository customerRepository;

    /**
     * Constructs a {@code TransactionalCustomerService}.
     *
     * @param customerMapper     the mapper.
     * @param customerRepository the repository.
     */
    @Autowired
    public TransactionalCustomerService(CustomerMapper customerMapper, CustomerRepository customerRepository) {
        this.customerMapper = customerMapper;
        this.customerRepository = customerRepository;
    }

    @Override
    public Optional<Customer> findByUsername(String username) {
        return customerRepository.findByUsername(username);
    }

    @Override
    public boolean existsByUsername(String username) {
        return customerRepository.existsByUsername(username);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Customer register(Credentials credentials) throws TokenAlreadyInUseException {
        final var customer = customerMapper.toCustomer(credentials);

        try {
            return customerRepository.save(customer);
        }
        /* The only reason behind this is that the given token is already taken. */
        catch (DataIntegrityViolationException dive) {
            throw new TokenAlreadyInUseException(customer.getThirdpartyToken(), "failed to register", dive);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Customer applyUpdates(UpdatesWrapper updates) throws TokenAlreadyInUseException {
        final var customer = updates.customer();

        customerMapper.applyUpdates(updates.unwrap(), customer);

        try {
            customerRepository.forceUpdate(customer);
        }
        /* The only reason behind this is that the given token is already taken. */
        catch (DataIntegrityViolationException dive) {
            throw new TokenAlreadyInUseException(customer.getThirdpartyToken(), "failed to applyUpdates", dive);
        }

        return customer;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteById(long id) {
        customerRepository.deleteById(id);
    }
}
