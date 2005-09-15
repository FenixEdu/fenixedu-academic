/*
 * Created on 20/Mar/2003
 *
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.gesdis.teacher;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoSite;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteWithInfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.domain.IProfessorship;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentProfessorship;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author João Mota
 * 
 *  
 */
public class ReadTeacherExecutionCoursesSitesService implements IService {

    public List run(InfoTeacher infoTeacher) throws FenixServiceException, ExcepcaoPersistencia {

        final List<InfoSite> infoSites = new ArrayList<InfoSite>();

        final ISuportePersistente persistentSuport = PersistenceSupportFactory
                .getDefaultPersistenceSupport();
        //final IPersistentSite persistentSite =
        // persistentSuport.getIPersistentSite();
        final IPersistentProfessorship persistentProfessorship = persistentSuport
                .getIPersistentProfessorship();

        final List<IProfessorship> professorships = persistentProfessorship
                .readByTeacherNumber(infoTeacher.getTeacherNumber());
        for (final IProfessorship professorship : professorships) {
            InfoSite infoSite = InfoSiteWithInfoExecutionCourse.newInfoFromDomain(professorship.getExecutionCourse().getSite());
            infoSites.add(infoSite);
        }

        return infoSites;
    }
}