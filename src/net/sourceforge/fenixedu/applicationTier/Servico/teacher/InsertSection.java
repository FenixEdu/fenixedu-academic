package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class InsertSection extends Service {

    public Boolean run(Integer infoExecutionCourseCode, Integer sectionCode, String sectionName,
            Integer sectionOrder) throws FenixServiceException, ExcepcaoPersistencia {

    	final ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(infoExecutionCourseCode);
        final Site site = executionCourse.getSite();

        Section parentSection = null;
        if (sectionCode != null) {
            parentSection = rootDomainObject.readSectionByOID(sectionCode);
        }
        site.createSection(sectionName, parentSection, sectionOrder);
         
        return Boolean.TRUE;
    }

}
