package com.mycheque.service.context;

import java.util.HashSet;

import com.mycheque.datatransfer.query.Patchnotes;
import com.mycheque.datatransfer.query.ReceiptDefinition;
import com.mycheque.datatransfer.intermediate.Remarkable;
import com.mycheque.datatransfer.intermediate.PatchRemark;

import com.mycheque.service.wrapper.AuthorizedWrapper;
import com.mycheque.service.commons.PatchStateTracker;

import static com.mycheque.service.ReceiptDefinitionInterpreter.interpretAllIfPossible;

/**
 * Utility class helping with initial setup of a {@link PatchnotesContext}.
 *
 * @author resxnvnce
 */
public final class PatchnotesContextSetup {

    /**
     * Create and set up a {@code PatchnotesContext} within a request scope.
     *
     * @param wrapper an authorized wrapper with the patchnotes to be applied.
     * @return a properly configured patchnotes context.
     */
    public static PatchnotesContext with(AuthorizedWrapper<Patchnotes> wrapper) {
        final var context = PatchnotesContext.unwrapping(wrapper);

        var patches = context.getPatchnotes().patches();
        var patchesDeduplicated = new HashSet<ReceiptDefinition>();

        for (var iterator = interpretAllIfPossible(patches).listIterator(); iterator.hasNext(); ) {
            int nextIndex = iterator.nextIndex();

            var source = patches.get(nextIndex);
            var intermediate = iterator.next();

            boolean isDuplicatePatch = !patchesDeduplicated.add(intermediate);
            if (isDuplicatePatch) {
                var duplicated = Remarkable.of(source, PatchRemark.DUPLICATED);

                context.remark(duplicated);
            }
            else {
                var pst = PatchStateTracker.ofSource(source);
                pst.setIntermediateState(intermediate);

                context.getStateTrackers().add(pst);
            }
        }

        return context;
    }

    /**
     * Nah-uh.
     */
    private PatchnotesContextSetup() {
        throw new AssertionError("Utility class instantiation is not allowed!");
    }
}
