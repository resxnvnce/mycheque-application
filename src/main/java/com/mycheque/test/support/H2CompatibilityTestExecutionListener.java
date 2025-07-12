package com.mycheque.test.support;

import com.mycheque.domain.Customer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.core.Ordered;

import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

import com.mycheque.util.hibernate6.NamedEnum;

/**
 * {@code TestExecutionListener} making sure that <i>Spring Data JPA</i>
 * tests are compatible with the {@code com.h2database} data source.
 * <p>
 * Consult the {@link #beforeTestClass(TestContext)} method-level javadoc for details.
 *
 * @author resxnvnce
 */
public class H2CompatibilityTestExecutionListener extends AbstractTestExecutionListener {

    private static final Log LOG = LogFactory.getLog(H2CompatibilityTestExecutionListener.class);

    private static final String NAMED_ENUM_NOT_SUPPORTED = "h2database does not support "
            + "the named enums feature of PostgreSQL; to execute JPA tests, disable "
            + "the [%s] annotation above the Customer#role attribute.";

    /**
     * Returns the highest precedence value, since it relies only on the metamodel.
     *
     * @return the highest precedence value.
     */
    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    /**
     * Ensures that the named enums feature is disabled, since
     * {@code com.h2database} data source does not support it.
     * <p>
     * Precisely, the method is looking for the {@link NamedEnum} annotation
     * above the role attribute of the {@code Customer} entity;
     * and if it's present, an exception is thrown.
     *
     * @param testContext the test context for the test; never {@code null}.
     */
    @Override
    public void beforeTestClass(TestContext testContext) {
        try {
            var annotation = Customer.class.getDeclaredField("role").getAnnotation(NamedEnum.class);

            if (annotation != null) {
                var message = String.format(NAMED_ENUM_NOT_SUPPORTED, NamedEnum.class);

                LOG.error(message);
                throw new IllegalStateException(message);
            }
        }
        catch (NoSuchFieldException nsfe) {
            LOG.warn("No Customer#role entity attribute detected.", nsfe);
        }
    }
}
