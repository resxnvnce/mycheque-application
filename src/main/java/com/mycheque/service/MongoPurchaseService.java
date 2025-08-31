package com.mycheque.service;

import java.util.List;
import java.util.Objects;
import java.util.ArrayList;
import java.util.Collection;

import org.springframework.stereotype.Service;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Criteria;

import com.mycheque.util.Assert;
import com.mycheque.util.Lambdas;

import com.mycheque.lang.Nullable;

import com.mycheque.domain.Purchase;
import com.mycheque.domain.id.FiscalDataRecord;

import com.mycheque.datatransfer.query.ItemQuery;
import com.mycheque.datatransfer.query.PurchaseQuery;

import com.mycheque.service.observer.support.ItemQueryObserver;

/**
 * The default {@link PurchaseService} implementation.
 *
 * @author resxnvnce
 */
@Service
public class MongoPurchaseService implements PurchaseService {

    private final MongoTemplate template;

    /**
     * Constructs a {@code MongoPurchaseService}.
     *
     * @param template the data source accessor.
     */
    public MongoPurchaseService(MongoTemplate template) {
        this.template = template;
    }

    /**
     * A shortcut method.
     *
     * @param query the {@code PurchaseQuery} to derive a criteria from.
     * @return a derived criteria or {@code null} if the query is blank.
     * @see #elemMatch(ItemQuery, ItemQuery.Type)
     */
    @Nullable
    private Criteria inclusiveElemMatch(ItemQuery query) {
        return elemMatch(query, ItemQuery.Type.INCLUSIVE);
    }

    /**
     * A shortcut method.
     *
     * @param query the {@code PurchaseQuery} to derive a criteria from.
     * @return a derived criteria or {@code null} if the query is blank.
     * @see #elemMatch(ItemQuery, ItemQuery.Type)
     */
    @Nullable
    private Criteria exclusiveElemMatch(ItemQuery query) {
        return elemMatch(query, ItemQuery.Type.EXCLUSIVE);
    }

    /**
     * Derives an {@link com.mycheque.domain.Item Item}
     * criteria using the <b>$elemMatch</b> operator.
     * <p>
     * The given {@code ItemQuery.Type} is used to decide
     * whether to negate the output criteria or not.
     *
     * @param query the {@code PurchaseQuery} to derive a criteria from.
     * @param type  the type of query.
     * @return a derived criteria or {@code null} if the query is blank.
     * @see Criteria#elemMatch(Criteria)
     */
    @Nullable
    private Criteria elemMatch(ItemQuery query, ItemQuery.Type type) {
        final var base = Criteria.where("items");

        if (type == ItemQuery.Type.EXCLUSIVE) {
            base.not();
        }

        return Lambdas.applyOrNull(this.deriveCriteria(query), base::elemMatch);
    }

    @Override
    public List<Purchase> findAll(Collection<FiscalDataRecord> pool, @Nullable PurchaseQuery query) {
        Assert.notBlank(pool, () -> "pool must not be blank");

        var criteria = Criteria.where("_id").in(pool);

        if (query != null) {
            Lambdas.acceptIfPresent(this.deriveCriteria(query), criteria::andOperator);
        }

        return this.template.find(new Query(criteria), Purchase.class, "purchase");
    }

    /**
     * Derives a {@code Criteria} from a query or
     * returns {@code null} if there's not a single property.
     *
     * @param query the {@code PurchaseQuery} to derive a criteria from.
     * @return a derived criteria or {@code null} if the query is blank.
     */
    @Nullable
    private Criteria deriveCriteria(PurchaseQuery query) {
        List<Criteria> composition = new ArrayList<>();

        if (query.include() != null) {

            query.include().stream().map(this::inclusiveElemMatch)
                    .filter(Objects::nonNull)
                    .forEach(composition::add);
        }
        if (query.exclude() != null) {

            query.exclude().stream().map(this::exclusiveElemMatch)
                    .filter(Objects::nonNull)
                    .forEach(composition::add);
        }

        return composition.isEmpty() ? null : new Criteria().andOperator(composition);
    }

    /**
     * Derives a {@code Criteria} from a query or
     * returns {@code null} if there's not a single property.
     *
     * @param query the {@code ItemQuery} to derive a criteria from.
     * @return a derived criteria or {@code null} if the query is blank.
     */
    @Nullable
    private Criteria deriveCriteria(ItemQuery query) {
        List<Criteria> composition = new ArrayList<>();

        for (var observer : ItemQueryObserver.VALUES) {
            Lambdas.acceptIfPresent(observer.deriveIfNecessary(query), composition::add);
        }

        return composition.isEmpty() ? null : new Criteria().andOperator(composition);
    }
}
