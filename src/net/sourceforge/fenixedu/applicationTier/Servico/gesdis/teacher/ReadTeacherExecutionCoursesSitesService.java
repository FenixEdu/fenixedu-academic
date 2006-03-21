/*
 * Created on 20/Mar/2003
 *
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.gesdis.teacher;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoSite;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteWithInfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author João Mota
 * 
 * 
 */
public class ReadTeacherExecutionCoursesSitesService extends Service {

    public List run(InfoTeacher infoTeacher) throws FenixServiceException, ExcepcaoPersistencia {

        final List<InfoSite> infoSites = new ArrayList<InfoSite>();

        // final IPersistentSite persistentSite =
        // persistentSupport.getIPersistentSite();

        Teacher teacher = RootDomainObject.getInstance().readTeacherByOID(infoTeacher.getIdInternal());
        final List<Professorship> professorships = teacher.getProfessorships();
        for (final Professorship professorship : professorships) {
            InfoSite infoSite = InfoSiteWithInfoExecutionCourse.newInfoFromDomain(professorship
                    .getExecutionCourse().getSite());
            infoSites.add(infoSite);
        }

        return infoSites;
    }
}