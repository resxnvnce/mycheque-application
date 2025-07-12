package com.mycheque.repository;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import org.springframework.test.annotation.Commit;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.jdbc.Sql;

import org.springframework.beans.factory.annotation.Autowired;

import com.mycheque.domain.Receipt;
import com.mycheque.domain.Customer;
import com.mycheque.domain.id.FiscalDataRecord;

import com.mycheque.test.TestInstance;
import com.mycheque.test.support.H2CompatibilityTestExecutionListener;

/**
 * The {@link org.springframework.data.jpa.repository.JpaRepository JpaRepository}
 * interface extensions tests.
 * <p>
 * <b>ALERT:</b> Make sure to disable the {@code NamedEnum} annotation above
 * the {@link Customer#getRole()} column descriptor before executing these tests.
 * <p>
 * <i>I don't think it's possible to somehow make it get
 * the job done both for PostgreSQL & h2database. I've tried.</i>
 *
 * @author resxnvnce
 */
@Sql(scripts = "/scripts/jpa-repository-tests.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@DataJpaTest
@ActiveProfiles("test")
@TestExecutionListeners(listeners = H2CompatibilityTestExecutionListener.class,
        mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance.PerClass
class JpaRepositoryTests {

    @Autowired
    private ReceiptRepository receiptRepository;

    @Autowired
    private CustomerRepository customerRepository;

    /**
     * Get an existing {@link Customer} having the given username.
     *
     * @param username the username.
     * @return the customer with the given username.
     * @throws AssertionError if such a {@code Customer} is not present by any means.
     */
    private Customer getByUsername(String username) {
        return this.customerRepository.findByUsername(username).orElseThrow(AssertionError::new);
    }

    /* CustomerRepository tests */

    @Test
    @DisplayName("#findByUsername('JaneDoe' && 'JohnDoe') must return an entity with the role Customer.Role.USER")
    void test00() {
        Assertions.assertEquals(
                Customer.Role.USER,
                getByUsername("JaneDoe").getRole()
        );

        Assertions.assertEquals(
                Customer.Role.USER,
                getByUsername("JohnDoe").getRole()
        );
    }

    /* ReceiptRepository tests */

    @Test
    @Order(0)
    @Commit
    void initializeReceipts() {
        Customer jane = getByUsername("JaneDoe");
        Customer john = getByUsername("JohnDoe");

        final var receiptA = new Receipt(
                new FiscalDataRecord("0101", "010101", "0101010101"),
                14999, "FNDN-01", LocalDateTime.now()
        );

        receiptA.setNew(true);
        receiptA.setCustomer(jane);
        this.receiptRepository.save(receiptA);

        final var receiptB = new Receipt(
                new FiscalDataRecord("0202", "020202", "0202020202"),
                78499, "FNDN-01", LocalDateTime.now()
        );

        receiptB.setNew(true);
        receiptB.setCustomer(jane);
        this.receiptRepository.save(receiptB);

        final var receiptC = new Receipt(
                new FiscalDataRecord("0303", "030303", "0303030303"),
                14499, "FNDN-02", LocalDateTime.now()
        );

        receiptC.setNew(true);
        receiptC.setCustomer(john);
        this.receiptRepository.save(receiptC);
    }

    @Test
    @Order(1)
    void afterInitializeReceipts01() {
        final var fiscal = new FiscalDataRecord("0101", "010101", "0101010101");

        Assertions.assertEquals(
                getByUsername("JaneDoe").getId(),
                this.receiptRepository.toOwnerReference(fiscal).orElse(null)
        );
    }

    @Test
    @Order(2)
    void afterInitializeReceipts02() {
        final var fiscal = new FiscalDataRecord("0303", "030303", "0303030303");

        Assertions.assertEquals(
                getByUsername("JohnDoe").getId(),
                this.receiptRepository.toOwnerReference(fiscal).orElse(null)
        );
    }

    @Test
    @Order(3)
    void afterInitializeReceipts03() {
        final var fiscal = new FiscalDataRecord("404404", "404404", "404404404");

        Assertions.assertTrue(
                this.receiptRepository.toOwnerReference(fiscal).isEmpty()
        );
    }
}
