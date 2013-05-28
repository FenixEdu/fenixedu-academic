package net.sourceforge.fenixedu.applicationTier.Servico.student;


import net.sourceforge.fenixedu.domain.Attends;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

public class CreateWeeklyWorkLoad {

    @Checked("RolePredicates.STUDENT_PREDICATE")
    @Service
    public static void run(final Integer attendsID, final Integer contact, final Integer autonomousStudy, final Integer other) {
        final Attends attends = AbstractDomainObject.fromExternalId(attendsID);
        attends.createWeeklyWorkLoad(contact, autonomousStudy, other);
    }

}