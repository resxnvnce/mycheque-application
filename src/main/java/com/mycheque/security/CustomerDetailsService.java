package com.mycheque.security;

import com.mycheque.service.CustomerService;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * The default {@code UserDetailsService} implementation,
 * relying on the {@link CustomerService}.
 * 
 * @author resxnvnce
 */
@Service
public class CustomerDetailsService implements UserDetailsService {

    /**
     * The service delegate.
     */
    private final CustomerService service;

    /**
     * Constructs a new instance of {@code CustomerDetailsService}
     * for Spring Security to use.
     *
     * @param service the service to delegate to.
     */
    public CustomerDetailsService(@Autowired CustomerService service) {
        this.service = service;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final var customer = service.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("cannot locate a customer by username: '" + username + "'")
        );
        return new DelegatingCustomerDetails(customer);
    }
}
