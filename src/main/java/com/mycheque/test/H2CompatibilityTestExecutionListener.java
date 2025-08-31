package com.mycheque.test;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mycheque.domain.Customer;

import com.mycheque.util.hibernate6.NamedEnum;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListener;

/**
 * {@code TestExecutionListener} making sure that <i>Spring Data JPA</i>
 * tests are compatible with the {@code com.h2database} data source.
 * <p>
 * Consult the {@link #prepareTestInstance(TestContext)} method-level javadoc for details.
 *
 * @author resxnvnce
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
public class H2CompatibilityTestExecutionListener implements TestExecutionListener {

    private static final Log LOG = LogFactory.getLog(H2CompatibilityTestExecutionListener.class);

    private static final String NAMED_ENUM_NOT_SUPPORTED = "com.h2database data source does not support "
            + "the named enums feature of PostgreSQL; to execute JPA tests, disable "
            + "the [%s] annotation above the [%s] 'role' attribute.";

    /**
     * Ensures that the named enums feature is disabled, since
     * the data source of {@code com.h2database} does not support it.
     * <p>
     * Precisely, the method is looking for the {@link NamedEnum} annotation
     * above the role attribute of the {@code Customer} entity;
     * and if it's present, an exception is thrown.
     *
     * @param testContext the test context for the test; never {@code null}.
     */
    @Override
    public void prepareTestInstance(TestContext testContext) {
        try {
            var annotation = Customer.class.getDeclaredField("role").getAnnotation(NamedEnum.class);

            if (annotation != null) {
                var message = NAMED_ENUM_NOT_SUPPORTED.formatted(NamedEnum.class, Customer.class);

                LOG.error(message);
                throw new IllegalStateException(message);
            }
        }
        catch (NoSuchFieldException nsfe) {
            LOG.warn("no 'role' attribute detected at [%s] entity".formatted(Customer.class), nsfe);
        }
    }
}
