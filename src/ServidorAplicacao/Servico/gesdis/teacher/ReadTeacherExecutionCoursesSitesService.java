/*
 * Created on 20/Mar/2003
 *
 * 
 */
package ServidorAplicacao.Servico.gesdis.teacher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoSite;
import DataBeans.InfoTeacher;
import DataBeans.util.Cloner;
import Dominio.IExecutionCourse;
import Dominio.IProfessorship;
import Dominio.ISite;
import Dominio.ITeacher;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentProfessorship;
import ServidorPersistente.IPersistentSite;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author João Mota
 * 
 *  
 */
public class ReadTeacherExecutionCoursesSitesService implements IService {

    public List run(InfoTeacher infoTeacher) throws FenixServiceException {

        List infoSites = new ArrayList();

        try {
            ISuportePersistente persistentSuport;
            persistentSuport = SuportePersistenteOJB.getInstance();
            IPersistentTeacher persistentTeacher = persistentSuport.getIPersistentTeacher();
            IPersistentSite persistentSite = persistentSuport.getIPersistentSite();
            IPersistentProfessorship persistentProfessorship = persistentSuport
                    .getIPersistentProfessorship();
            ITeacher teacher = persistentTeacher.readByNumber(infoTeacher.getTeacherNumber());
            List professorships = persistentProfessorship.readByTeacher(teacher);
            Iterator iter = professorships.iterator();
            while (iter.hasNext()) {
                IProfessorship professorship = (IProfessorship) iter.next();
                IExecutionCourse executionCourse = professorship.getExecutionCourse();
                ISite site = persistentSite.readByExecutionCourse(executionCourse);
                InfoSite infoSite = Cloner.copyISite2InfoSite(site);
                infoSites.add(infoSite);
            }

            return infoSites;
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
    }
}