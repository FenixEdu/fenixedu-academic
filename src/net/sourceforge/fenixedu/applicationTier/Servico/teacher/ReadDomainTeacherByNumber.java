/**
* Nov 14, 2005
*/
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Ricardo Rodrigues
 *
 */

public class ReadDomainTeacherByNumber implements IService {
    
    public ITeacher run(final Integer number) throws ExcepcaoPersistencia, NotFoundTeacher{
        
        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        List<ITeacher> teachers = (List<ITeacher>) sp.getIPersistentTeacher().readAll(Teacher.class);
        ITeacher teacher =  (ITeacher) CollectionUtils.find(teachers, new Predicate(){

            public boolean evaluate(Object arg0) {
                ITeacher tempTeacher = (ITeacher) arg0;
                return tempTeacher.getTeacherNumber().equals(number);
            }});
        
        if(teacher == null){
            throw new NotFoundTeacher();
        }
        return teacher;         
    }

    public class NotFoundTeacher extends FenixServiceException {
    }
}


