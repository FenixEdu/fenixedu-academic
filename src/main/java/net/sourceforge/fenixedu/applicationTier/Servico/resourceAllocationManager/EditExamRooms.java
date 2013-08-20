package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExam;
import net.sourceforge.fenixedu.domain.Exam;
import net.sourceforge.fenixedu.domain.space.AllocatableSpace;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

/**
 * @author Luis Cruz & Sara Ribeiro
 */
public class EditExamRooms {

    @Checked("RolePredicates.RESOURCE_ALLOCATION_MANAGER_PREDICATE")
    @Service
    public static InfoExam run(InfoExam infoExam, final List<String> roomsForExam) throws FenixServiceException {

        final List<AllocatableSpace> finalRoomList = new ArrayList<AllocatableSpace>();
        for (final String id : roomsForExam) {
            finalRoomList.add((AllocatableSpace) AbstractDomainObject.fromExternalId(id));
        }

        final Exam exam = (Exam) AbstractDomainObject.fromExternalId(infoExam.getExternalId());
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