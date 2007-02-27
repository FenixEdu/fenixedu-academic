/*
 * Created on 12/Nov/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.gesdis;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Factory.TeacherAdministrationSiteComponentBuilder;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteCommon;
import net.sourceforge.fenixedu.dataTransferObject.TeacherAdministrationSiteView;
import net.sourceforge.fenixedu.dataTransferObject.gesdis.InfoSiteCourseInformation;
import net.sourceforge.fenixedu.domain.ExecutionCourse;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class ReadCourseInformation extends Service {

    public TeacherAdministrationSiteView run(final Integer executionCourseOID) throws FenixServiceException {
        final ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(executionCourseOID);
	final InfoSiteCourseInformation resultComponent = new InfoSiteCourseInformation(executionCourse);
        
        final TeacherAdministrationSiteView result = new TeacherAdministrationSiteView();
        result.setCommonComponent(TeacherAdministrationSiteComponentBuilder.getInstance().getComponent(new InfoSiteCommon(), executionCourse.getSite(), null, null, null));
        result.setComponent(resultComponent);

        return result;
    }

}
