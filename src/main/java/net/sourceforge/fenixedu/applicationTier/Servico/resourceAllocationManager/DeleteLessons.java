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
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

public class DeleteLessons {

    @Checked("RolePredicates.RESOURCE_ALLOCATION_MANAGER_PREDICATE")
    @Service
    public static void run(final List<Integer> lessonOIDs) throws FenixServiceException {
        final List<DomainException> exceptionList = new ArrayList<DomainException>();

        for (final Integer lessonOID : lessonOIDs) {
            try {
                Lesson lesson = AbstractDomainObject.fromExternalId(lessonOID);
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