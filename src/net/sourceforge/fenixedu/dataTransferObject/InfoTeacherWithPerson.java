/*
 * Created on Jun 7, 2004
 *  
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

/**
 * @author João Mota
 * 
 */
public class InfoTeacherWithPerson extends InfoTeacher {

    public void copyFromDomain(ITeacher teacher) {
        super.copyFromDomain(teacher);
        if (teacher != null) {
            setInfoPerson(InfoPerson.newInfoFromDomain(teacher.getPerson()));
        }
    }

    public static InfoTeacher newInfoFromDomain(ITeacher teacher) {
        InfoTeacherWithPerson infoTeacher = null;
        if (teacher != null) {
            infoTeacher = new InfoTeacherWithPerson();
            infoTeacher.copyFromDomain(teacher);
        }
        return infoTeacher;
    }

    public void copyToDomain(InfoTeacher infoTeacher, ITeacher teacher) throws ExcepcaoPersistencia {
        super.copyToDomain(infoTeacher, teacher);
        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPerson person = (IPerson) sp.getIPessoaPersistente().readByOID(Person.class,
                infoTeacher.getInfoPerson().getIdInternal());

        teacher.setPerson(person);
    }

}
