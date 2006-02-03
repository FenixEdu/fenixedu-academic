package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Fernanda Quitério
 */
public class InsertSection extends Service {


    public Boolean run(Integer infoExecutionCourseCode, Integer sectionCode, String sectionName,
            Integer sectionOrder) throws FenixServiceException, ExcepcaoPersistencia {

    	final ExecutionCourse executionCourse = (ExecutionCourse) persistentObject.readByOID(ExecutionCourse.class, infoExecutionCourseCode);
        final Site site = executionCourse.getSite();

        Section parentSection = null;
        if (sectionCode != null) {
            parentSection = (Section) persistentObject.readByOID(Section.class, sectionCode);
        }
        site.createSection(sectionName, parentSection, sectionOrder);
         
        return new Boolean(true);
    }
}