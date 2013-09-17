package net.sourceforge.fenixedu.applicationTier.Servico.department;


import net.sourceforge.fenixedu.applicationTier.Filtro.DepartmentMemberAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.TeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.department.TeacherPersonalExpectationBean;
import net.sourceforge.fenixedu.domain.teacher.TeacherPersonalExpectation;
import pt.ist.fenixframework.Atomic;

public class InsertTeacherPersonalExpectation {

    protected TeacherPersonalExpectation run(TeacherPersonalExpectationBean bean) {
        if (bean != null) {
            return new TeacherPersonalExpectation(bean);
        }
        return null;
    }

    // Service Invokers migrated from Berserk

    private static final InsertTeacherPersonalExpectation serviceInstance = new InsertTeacherPersonalExpectation();

    @Atomic
    public static TeacherPersonalExpectation runInsertTeacherPersonalExpectation(TeacherPersonalExpectationBean bean)
            throws NotAuthorizedException {
        try {
            DepartmentMemberAuthorizationFilter.instance.execute();
            return serviceInstance.run(bean);
        } catch (NotAuthorizedException ex1) {
            try {
                TeacherAuthorizationFilter.instance.execute();
                return serviceInstance.run(bean);
            } catch (NotAuthorizedException ex2) {
                throw ex2;
            }
        }
    }

}