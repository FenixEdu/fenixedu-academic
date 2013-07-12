package net.sourceforge.fenixedu.applicationTier.Servico.teacherServiceDistribution;

import java.util.Collection;

import net.sourceforge.fenixedu.applicationTier.Filtro.DepartmentMemberAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.EmployeeAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.TeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.commons.CollectionUtils;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.ProfessionalCategory;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDTeacher;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDVirtualTeacher;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TeacherServiceDistribution;

import org.apache.commons.collections.Predicate;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class CreateTSDTeacher {
    protected Boolean run(String teacherName, String categoryId, Double requiredHours, String tsdId) {

        ProfessionalCategory category = FenixFramework.getDomainObject(categoryId);
        TeacherServiceDistribution tsd = FenixFramework.getDomainObject(tsdId);

        if (existsVirtualTeacherWithSameName(tsd.getTSDTeachers(), teacherName)) {
            return false;
        }

        TSDTeacher tsdTeacher = new TSDVirtualTeacher(category, teacherName, requiredHours);
        tsd.addTSDTeachers(tsdTeacher);

        return true;
    }

    private boolean existsVirtualTeacherWithSameName(Collection<TSDTeacher> tsdTeachers, final String teacherName) {
        return CollectionUtils.exists(tsdTeachers, new Predicate() {
            @Override
            public boolean evaluate(Object arg) {
                if (arg instanceof TSDVirtualTeacher) {
                    TSDVirtualTeacher tsdTeacher = (TSDVirtualTeacher) arg;
                    return tsdTeacher.getName().equals(teacherName);
                }
                return false;
            }
        });
    }

    // Service Invokers migrated from Berserk

    private static final CreateTSDTeacher serviceInstance = new CreateTSDTeacher();

    @Atomic
    public static Boolean runCreateTSDTeacher(String teacherName, String categoryId, Double requiredHours, String tsdId)
            throws NotAuthorizedException {
        try {
            DepartmentMemberAuthorizationFilter.instance.execute();
            return serviceInstance.run(teacherName, categoryId, requiredHours, tsdId);
        } catch (NotAuthorizedException ex1) {
            try {
                TeacherAuthorizationFilter.instance.execute();
                return serviceInstance.run(teacherName, categoryId, requiredHours, tsdId);
            } catch (NotAuthorizedException ex2) {
                try {
                    EmployeeAuthorizationFilter.instance.execute();
                    return serviceInstance.run(teacherName, categoryId, requiredHours, tsdId);
                } catch (NotAuthorizedException ex3) {
                    throw ex3;
                }
            }
        }
    }

}