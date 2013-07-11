package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExam;
import net.sourceforge.fenixedu.domain.Exam;
import net.sourceforge.fenixedu.domain.space.AllocatableSpace;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Luis Cruz & Sara Ribeiro
 */
public class EditExamRooms {

    @Checked("RolePredicates.RESOURCE_ALLOCATION_MANAGER_PREDICATE")
    @Atomic
    public static InfoExam run(InfoExam infoExam, final List<String> roomsForExam) throws FenixServiceException {

        final List<AllocatableSpace> finalRoomList = new ArrayList<AllocatableSpace>();
        for (final String id : roomsForExam) {
            finalRoomList.add((AllocatableSpace) FenixFramework.getDomainObject(id));
        }

        final Exam exam = (Exam) FenixFramework.getDomainObject(infoExam.getExternalId());
        if (exam == null) {
            throw new NonExistingServiceException();
        }

        // Remove all elements
        // TODO : Do this more intelegently.
        exam.getAssociatedRooms().clear();

        // Add all elements
        exam.getAssociatedRooms().addAll(finalRoomList);

        return InfoExam.newInfoFromDomain(exam);
    }

}