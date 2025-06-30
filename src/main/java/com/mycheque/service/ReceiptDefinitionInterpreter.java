package com.mycheque.service;

import java.util.List;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.lang.Nullable;

import com.mycheque.util.Assert;
import com.mycheque.util.local.RegExp;

import com.mycheque.domain.id.FiscalDataRecord;

import com.mycheque.datatransfer.query.ReceiptDefinition;

/**
 * Certain utility methods working with {@link ReceiptDefinition}s.
 *
 * @author resxnvnce
 */
public final class ReceiptDefinitionInterpreter {

    /**
     * The {@code DateTimeFormatter} for parsing timestamps from raw QR codes.
     */
    private static final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmm");

    /**
     * <i>Interpret</i> the given {@code ReceiptDefinition}
     * if it's possible or return the same instance otherwise.
     *
     * @param definition a receipt definition to be attempted to <i>interpret</i>.
     * @return an <i>interpreted</i> receipt definition or the same instance.
     */
    public static ReceiptDefinition interpretIfPossible(ReceiptDefinition definition) {
        ReceiptDefinition interpeted = null;
        if (definition instanceof ReceiptDefinition.ByQrRaw byQrRaw) {
            interpeted = interpret(byQrRaw);
        }

        return interpeted != null ? interpeted : definition;
    }

    /**
     * <i>Interpret</i> each of the {@code ReceiptDefinition}s in the supplied list, if it's possible.
     * <p>
     * A list returned has the same encounter order,
     * whether a definition has been <i>interpreted</i> or not.
     *
     * @param definitions a list of receipt definitions to be attempted to <i>interpret</i>.
     *                    Must not be {@code null}.
     * @return a {@link List} of receipt definitions attempted to be <i>interpreted</i>.
     * @see #interpretIfPossible(ReceiptDefinition) 
     */
    public static List<ReceiptDefinition> interpretAllIfPossible(List<ReceiptDefinition> definitions) {
        Assert.notNull(definitions, () -> "definitions must not be null");
        return definitions.stream().map(ReceiptDefinitionInterpreter::interpretIfPossible).toList();
    }

    /**
     * Try to <i>interpret</i> the QR code in the specified receipt definition,
     * i.e. to extract the whole dataset the QR code contains.
     * <p>
     * Returns {@code null} if it's unable to interpret the QR code.
     * However, this doesn't mean the QR code is completely uninterpretable.
     *
     * @param definition a receipt definition, based on a raw QR code.
     * @return the {@code ReceiptDefinition.ByDetails} interpreted or {@code null}.
     */
    @Nullable
    public static ReceiptDefinition.ByDetails interpret(ReceiptDefinition.ByQrRaw definition) {
        String qrraw = definition.qrraw();
        return qrraw.matches(RegExp.QR_RAW) ? interpretByQrRaw(qrraw) : null;
    }

    /**
     * Perform the actual intepretation.
     *
     * @param qrraw a QR code in the raw form.
     * @return the {@code ReceiptDefinition.ByDetails} interpreted or {@code null}.
     */
    @Nullable
    private static ReceiptDefinition.ByDetails interpretByQrRaw(String qrraw) {
        final String[] tokens = qrraw.substring(2).split("&\\w+=");

        try {
            var id = new FiscalDataRecord(tokens[2], tokens[3], tokens[4]);
            var timestamp = LocalDateTime.parse(tokens[0], TIMESTAMP_FORMATTER);

            return new ReceiptDefinition.ByDetails(id, Float.parseFloat(tokens[1]), timestamp);
        }
        catch (Exception ignored) {
            return null;
        }
    }

    /**
     * Nah-uh.
     */
    private ReceiptDefinitionInterpreter() {
        throw new AssertionError("Utility class instantiation is not allowed!");
    }
}
