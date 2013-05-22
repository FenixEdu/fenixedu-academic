package net.sourceforge.fenixedu.applicationTier.Servico.teacherServiceDistribution;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
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

import pt.ist.fenixWebFramework.services.Service;

public class CreateTSDTeacher extends FenixService {
    protected Boolean run(String teacherName, Integer categoryId, Double requiredHours, Integer tsdId) {

        ProfessionalCategory category = rootDomainObject.readProfessionalCategoryByOID(categoryId);
        TeacherServiceDistribution tsd = rootDomainObject.readTeacherServiceDistributionByOID(tsdId);

        if (existsVirtualTeacherWithSameName(tsd.getTSDTeachers(), teacherName)) {
            return false;
        }

        TSDTeacher tsdTeacher = new TSDVirtualTeacher(category, teacherName, requiredHours);
        tsd.addTSDTeachers(tsdTeacher);

        return true;
    }

    private boolean existsVirtualTeacherWithSameName(List<TSDTeacher> tsdTeachers, final String teacherName) {
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

    @Service
    public static Boolean runCreateTSDTeacher(String teacherName, Integer categoryId, Double requiredHours, Integer tsdId)
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