/*
 * Created on 8/Mai/2005 - 12:02:58
 * 
 */

package net.sourceforge.fenixedu.applicationTier.Servico.gesdis.teacher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoNonAffiliatedTeacher;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.INonAffiliatedTeacher;
import net.sourceforge.fenixedu.domain.IProfessorship;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentProfessorship;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author João Fialho & Rita Ferreira
 *
 */
public class ReadTeachersOrNonAffiliatedTeachersByExecutionCourseProfessorship
		implements IService {

    public List run(Integer executionCourseId) throws FenixServiceException {
        try {
            List result = null;
            ISuportePersistente sp;
            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentProfessorship persistentProfessorship = sp.getIPersistentProfessorship();
            result = persistentProfessorship.readByExecutionCourseId(executionCourseId);

            List infoResult = new ArrayList();
            if (result != null) {

                Iterator iter = result.iterator();
                while (iter.hasNext()) {
                    IProfessorship professorship = (IProfessorship) iter.next();
					if(professorship.getTeacher() != null) {
	                    ITeacher teacher = professorship.getTeacher();
	                    InfoTeacher infoTeacher = Cloner.copyITeacher2InfoTeacher(teacher);
	                    infoResult.add(infoTeacher);

					} else if(professorship.getNonAffiliatedTeacher() != null) {
						INonAffiliatedTeacher nonAffiliatedTeacher = professorship.getNonAffiliatedTeacher();
						InfoNonAffiliatedTeacher infoNonAffiliatedTeacher = new InfoNonAffiliatedTeacher();
						infoNonAffiliatedTeacher.copyFromDomain(nonAffiliatedTeacher);
						infoResult.add(infoNonAffiliatedTeacher);
					}
                }
                return infoResult;
            }

            return result;

        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }

    }

}
