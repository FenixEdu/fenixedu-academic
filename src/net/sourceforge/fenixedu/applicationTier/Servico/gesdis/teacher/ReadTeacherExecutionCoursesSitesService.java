/*
 * Created on 20/Mar/2003
 *
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.gesdis.teacher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoSite;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IProfessorship;
import net.sourceforge.fenixedu.domain.ISite;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentProfessorship;
import net.sourceforge.fenixedu.persistenceTier.IPersistentSite;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
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

        List<InfoSite> infoSites = new ArrayList<InfoSite>();

        ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();

        IPersistentTeacher persistentTeacher = persistentSuport.getIPersistentTeacher();
        IPersistentSite persistentSite = persistentSuport.getIPersistentSite();
        IPersistentProfessorship persistentProfessorship = persistentSuport.getIPersistentProfessorship();
        
        ITeacher teacher = persistentTeacher.readByNumber(infoTeacher.getTeacherNumber());
        List professorships = persistentProfessorship.readByTeacher(teacher);
        Iterator iter = professorships.iterator();
        while (iter.hasNext()) {
            IProfessorship professorship = (IProfessorship) iter.next();
            IExecutionCourse executionCourse = professorship.getExecutionCourse();
            ISite site = persistentSite.readByExecutionCourse(executionCourse.getIdInternal());
            InfoSite infoSite = Cloner.copyISite2InfoSite(site);
            infoSites.add(infoSite);
        }

        return infoSites;
    }
}