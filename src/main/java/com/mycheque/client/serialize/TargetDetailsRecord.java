package com.mycheque.client.serialize;

import java.time.LocalDateTime;

import org.springframework.lang.Nullable;

import com.mycheque.client.jsonstruct.FiscalIdentifier;

/**
 * The default {@link TargetDetails} implementation.
 *
 * @param token     the authorization token.
 * @param id        the fiscal data identifier of a target entity.
 * @param total     the total cash sum.
 * @param timestamp the date-time.
 * @author resxnvnce
 */
record TargetDetailsRecord(String token, FiscalIdentifier id, @Nullable Float total, @Nullable LocalDateTime timestamp)
        implements TargetDetails {
}
