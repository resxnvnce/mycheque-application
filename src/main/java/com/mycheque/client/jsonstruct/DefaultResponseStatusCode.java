package com.mycheque.client.jsonstruct;

/**
 * Default implementation of {@link ResponseStatusCode}.
 *
 * @author resxnvnce
 */
record DefaultResponseStatusCode(int value) implements ResponseStatusCode {

    @Override
    public boolean isError() {
        return this.value != 1;
    }
}
