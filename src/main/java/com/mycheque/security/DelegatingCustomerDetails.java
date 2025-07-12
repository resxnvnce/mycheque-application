package com.mycheque.security;

import java.util.Collection;
import java.util.Collections;

import com.mycheque.domain.Customer;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * The default {@code UserDetails} implementation.
 *
 * @author resxnvnce
 */
public class DelegatingCustomerDetails implements UserDetails {

    private final Customer delegate;

    /**
     * Constructs a {@code DelegatingCustomerDetails} record.
     *
     * @param delegate the entity delegate.
     */
    public DelegatingCustomerDetails(Customer delegate) {
        this.delegate = delegate;
    }

    /**
     * Returns the entity delegate itself.
     *
     * @return the underlying {@link Customer} entity.
     */
    public Customer getDelegate() {
        return this.delegate;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        final GrantedAuthority role = this.delegate.getRole();

        /* For now, each Customer does only have one role, defining
         * the set of authorities (i.e. the API endpoints they have access to).
         *
         * However, this behavior may be changed in a future release.
         * Or maybe not. @resxnvnce */
        return Collections.singletonList(role);
    }

    @Override
    public String getPassword() {
        return this.delegate.getPassword();
    }

    @Override
    public String getUsername() {
        return this.delegate.getUsername();
    }
}
