/*
 * Created on 25/Mar/2003
 *
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.gesdis;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IProfessorship;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentProfessorship;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author João Mota
 * 
 *  
 */
public class ReadTeachersByExecutionCourseProfessorship implements IService {

    public List run(InfoExecutionCourse infoExecutionCourse) throws FenixServiceException {
        try {
            List result = null;
            ISuportePersistente sp;
            sp = SuportePersistenteOJB.getInstance();
            IExecutionCourse executionCourse = Cloner
                    .copyInfoExecutionCourse2ExecutionCourse(infoExecutionCourse);
            IPersistentProfessorship persistentProfessorship = sp.getIPersistentProfessorship();
            result = persistentProfessorship.readByExecutionCourse(executionCourse);

            List infoResult = new ArrayList();
            if (result != null) {

                Iterator iter = result.iterator();
                while (iter.hasNext()) {
                    IProfessorship professorship = (IProfessorship) iter.next();
                    ITeacher teacher = professorship.getTeacher();
                    InfoTeacher infoTeacher = Cloner.copyITeacher2InfoTeacher(teacher);
                    infoResult.add(infoTeacher);
                }
                return infoResult;
            }

            return result;

        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }

    }
}