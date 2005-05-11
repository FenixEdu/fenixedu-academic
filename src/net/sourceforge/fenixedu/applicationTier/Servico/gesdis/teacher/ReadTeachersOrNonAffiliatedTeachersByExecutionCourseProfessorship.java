/*
 * Created on 8/Mai/2005 - 12:02:58
 * 
 */

package net.sourceforge.fenixedu.applicationTier.Servico.gesdis.teacher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoNonAffiliatedTeacher;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.INonAffiliatedTeacher;
import net.sourceforge.fenixedu.domain.IPerson;
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
public class ReadTeachersOrNonAffiliatedTeachersByExecutionCourseProfessorship implements IService {

    public List run(final Integer executionCourseId) throws FenixServiceException, ExcepcaoPersistencia {
        final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        final IPersistentProfessorship persistentProfessorship = sp.getIPersistentProfessorship();

        final List result = persistentProfessorship.readByExecutionCourseId(executionCourseId);
        final List infoResult = new ArrayList(result.size());
        for (final Iterator iterator = result.iterator(); iterator.hasNext();) {
            final IProfessorship professorship = (IProfessorship) iterator.next();
            final ITeacher teacher = professorship.getTeacher();
            final InfoTeacher infoTeacher = new InfoTeacher();
            infoResult.add(infoTeacher);

            infoTeacher.setIdInternal(teacher.getIdInternal());

            final IPerson person = teacher.getPerson();
            final InfoPerson infoPerson = new InfoPerson();
            infoTeacher.setInfoPerson(infoPerson);

            infoPerson.setIdInternal(person.getIdInternal());
            infoPerson.setNome(person.getNome());
        }
        return infoResult;
    }

}
