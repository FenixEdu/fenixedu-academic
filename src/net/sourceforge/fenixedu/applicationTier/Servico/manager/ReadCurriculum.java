/*
 * Created on 16/Set/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurriculum;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurriculumWithInfoCurricularCourseAndInfoDegree;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Curriculum;

/**
 * @author lmac1
 */

public class ReadCurriculum extends Service {

    public InfoCurriculum run(Integer curricularCourseId) throws FenixServiceException {

	CurricularCourse curricularCourse;
	Curriculum curriculum;

	curricularCourse = (CurricularCourse) rootDomainObject.readDegreeModuleByOID(curricularCourseId);
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