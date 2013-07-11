package net.sourceforge.fenixedu.applicationTier.Servico.teacher;


import net.sourceforge.fenixedu.dataTransferObject.teacher.RoomsReserveBean;
import net.sourceforge.fenixedu.domain.PunctualRoomsOccupationRequest;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import pt.ist.fenixframework.Atomic;

public class CreateNewRoomsReserve {

    @Atomic
    public static PunctualRoomsOccupationRequest run(RoomsReserveBean bean) {
        if (AccessControl.getPerson().hasRole(RoleType.TEACHER) || AccessControl.getPerson().hasAnyProfessorships()) {

            if (bean != null) {
                return new PunctualRoomsOccupationRequest(bean.getRequestor(), bean.getSubject(), bean.getDescription());
            }
            return null;
        } else {
            throw new DomainException("person.is.not.authorized");
        }
    }
}