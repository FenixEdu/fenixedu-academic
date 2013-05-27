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
import net.sourceforge.fenixedu.domain.RootDomainObject;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author lmac1
 */

public class ReadCurriculum {

    @Checked("RolePredicates.MANAGER_OR_OPERATOR_PREDICATE")
    @Service
    public static InfoCurriculum run(Integer curricularCourseId) throws FenixServiceException {

        CurricularCourse curricularCourse;
        Curriculum curriculum;

        curricularCourse = (CurricularCourse) RootDomainObject.getInstance().readDegreeModuleByOID(curricularCourseId);
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