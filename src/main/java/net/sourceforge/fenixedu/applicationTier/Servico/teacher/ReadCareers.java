package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.dataTransferObject.SiteView;
import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoCareer;
import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoSiteCareers;
import net.sourceforge.fenixedu.domain.CareerType;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.teacher.Career;
import net.sourceforge.fenixedu.domain.teacher.ProfessionalCareer;
import net.sourceforge.fenixedu.domain.teacher.TeachingCareer;
import net.sourceforge.fenixedu.predicates.RolePredicates;

import org.apache.commons.beanutils.BeanComparator;

import pt.ist.fenixframework.Atomic;

public class ReadCareers {

    @Atomic
    public static SiteView run(CareerType careerType, String user) {
        check(RolePredicates.TEACHER_PREDICATE);
        final Teacher teacher = Teacher.readTeacherByUsername(user);

        final InfoSiteCareers bodyComponent = new InfoSiteCareers();
        bodyComponent.setInfoCareers(getInfoCareers(teacher, careerType));
        bodyComponent.setCareerType(careerType);

        final InfoTeacher infoTeacher = InfoTeacher.newInfoFromDomain(teacher);
        bodyComponent.setInfoTeacher(infoTeacher);

        SiteView siteView = new SiteView(bodyComponent);
        return siteView;
    }

    private static List getInfoCareers(Teacher teacher, CareerType careerType) {

        final List<InfoCareer> oldestCareers = new ArrayList();
        final List<InfoCareer> newestCareers = new ArrayList();

        final Collection<Career> careers = teacher.getAssociatedCareers();
        for (final Career career : careers) {
            boolean addCareer = false;
            if (careerType == null
                    || (careerType.equals(CareerType.PROFESSIONAL) && career.getClass().getName()
                            .equals(ProfessionalCareer.class.getName()))) {
                addCareer = true;
            } else if (careerType.equals(CareerType.TEACHING)
                    && career.getClass().getName().equals(TeachingCareer.class.getName())) {
                addCareer = true;
            }
            if (addCareer && career.getBeginYear() == null) {
                oldestCareers.add(InfoCareer.newInfoFromDomain(career));
            } else if (addCareer) {
                newestCareers.add(InfoCareer.newInfoFromDomain(career));
            }
        }
        Collections.sort(newestCareers, new BeanComparator("beginYear"));
        oldestCareers.addAll(newestCareers);

        return oldestCareers;
    }
}