package com.mycheque.security;

import com.mycheque.service.CustomerService;

import org.springframework.stereotype.Service;

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

    private final CustomerService service;

    /**
     * Constructs a new instance of {@code CustomerDetailsService}
     * for Spring Security to use.
     *
     * @param service the service to delegate to.
     */
    public CustomerDetailsService(CustomerService service) {
        this.service = service;
    }

    private UsernameNotFoundException getUsernameNotFoundException() {
        return new UsernameNotFoundException("Could not find a customer by the username provided.");
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.service.findByUsername(username).map(DelegatingCustomerDetails::new)
                .orElseThrow(this::getUsernameNotFoundException);
    }
}
