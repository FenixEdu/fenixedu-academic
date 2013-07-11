package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;


import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.PunctualRoomsOccupationRequest;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import pt.ist.fenixframework.Atomic;

public class MarkPunctualRoomsOccupationCommentsAsRead {
    @Atomic
    public static void run(PunctualRoomsOccupationRequest request, boolean forTeacher) {
        Person person = AccessControl.getPerson();

        if (person.hasRole(RoleType.TEACHER) || person.hasRole(RoleType.RESOURCE_ALLOCATION_MANAGER)
                || person.hasAnyProfessorships()) {
            if (request != null) {
                if (forTeacher) {
                    request.setTeacherReadComments(request.getCommentsCount());
                } else {
                    request.setEmployeeReadComments(request.getCommentsCount());
                }
            }
        }
    }
}
