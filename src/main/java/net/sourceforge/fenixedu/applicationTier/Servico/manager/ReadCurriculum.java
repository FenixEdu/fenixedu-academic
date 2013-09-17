/*
 * Created on 16/Set/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurriculum;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurriculumWithInfoCurricularCourseAndInfoDegree;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Curriculum;
import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author lmac1
 */

public class ReadCurriculum {

    @Atomic
    public static InfoCurriculum run(String curricularCourseId) throws FenixServiceException {
        check(RolePredicates.MANAGER_OR_OPERATOR_PREDICATE);

        CurricularCourse curricularCourse;
        Curriculum curriculum;

        curricularCourse = (CurricularCourse) FenixFramework.getDomainObject(curricularCourseId);
        if (curricularCourse == null) {
            throw new NonExistingServiceException();
        }
        curriculum = curricularCourse.findLatestCurriculum();

        if (curriculum == null) {
            return null;
        }
        InfoCurriculum infoCurriculum = InfoCurriculumWithInfoCurricularCourseAndInfoDegree.newInfoFromDomain(curriculum);
        return infoCurriculum;
    }
}