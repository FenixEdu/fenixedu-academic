/*
 * 
 * Created on 2003/08/15
 */

package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceMultipleException;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class DeleteLessons {

    @Checked("RolePredicates.RESOURCE_ALLOCATION_MANAGER_PREDICATE")
    @Atomic
    public static void run(final List<String> lessonOIDs) throws FenixServiceException {
        final List<DomainException> exceptionList = new ArrayList<DomainException>();

        for (final String lessonOID : lessonOIDs) {
            try {
                Lesson lesson = FenixFramework.getDomainObject(lessonOID);
                if (lesson != null) {
                    lesson.delete();
                }
            } catch (DomainException e) {
                exceptionList.add(e);
            }
        }

        if (!exceptionList.isEmpty()) {
            throw new FenixServiceMultipleException(exceptionList);
        }
    }

}