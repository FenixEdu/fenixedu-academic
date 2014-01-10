package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager.ClosePunctualRoomsOccupationRequest;
import net.sourceforge.fenixedu.dataTransferObject.teacher.RoomsReserveBean;
import net.sourceforge.fenixedu.domain.PunctualRoomsOccupationRequest;

import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;

public class CreateNewRoomsReserveComment {

    protected void run(RoomsReserveBean bean, boolean reOpenRequest, boolean resolveRequest) {

        if (bean != null && bean.getReserveRequest() != null) {

            DateTime now = new DateTime();
            PunctualRoomsOccupationRequest reserveRequest = bean.getReserveRequest();

            if (reOpenRequest) {
                reserveRequest.createNewTeacherCommentAndOpenRequest(bean.getDescription(), bean.getRequestor(), now);

            } else if (resolveRequest) {
                reserveRequest.createNewEmployeeCommentAndCloseRequest(bean.getDescription(), bean.getRequestor(), now);
                ClosePunctualRoomsOccupationRequest.run(reserveRequest, bean.getRequestor());

            } else {
                reserveRequest.createNewTeacherOrEmployeeComment(bean.getDescription(), bean.getRequestor(), now);
            }
        }
    }

    // Service Invokers migrated from Berserk

    private static final CreateNewRoomsReserveComment serviceInstance = new CreateNewRoomsReserveComment();

    @Atomic
    public static void runCreateNewRoomsReserveComment(RoomsReserveBean bean, boolean reOpenRequest, boolean resolveRequest) {
        serviceInstance.run(bean, reOpenRequest, resolveRequest);
    }

}