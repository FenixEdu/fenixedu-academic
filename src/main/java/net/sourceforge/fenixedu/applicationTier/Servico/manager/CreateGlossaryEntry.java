/*
 * Created on 2004/08/30
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;


import net.sourceforge.fenixedu.dataTransferObject.support.InfoGlossaryEntry;
import net.sourceforge.fenixedu.domain.support.GlossaryEntry;
import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;

/**
 * @author Luis Cruz
 */
public class CreateGlossaryEntry {

    @Atomic
    public static void run(InfoGlossaryEntry infoGlossaryEntry) {
        check(RolePredicates.MANAGER_PREDICATE);
        GlossaryEntry glossaryEntry = new GlossaryEntry();
        glossaryEntry.setTerm(infoGlossaryEntry.getTerm());
        glossaryEntry.setDefinition(infoGlossaryEntry.getDefinition());
    }

}