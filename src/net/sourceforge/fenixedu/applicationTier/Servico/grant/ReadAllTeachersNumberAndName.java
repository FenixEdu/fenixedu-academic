/*
 * Created on 06/Feb/2004
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.grant;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Barbosa
 * @author Pica
 *  
 */
public class ReadAllTeachersNumberAndName implements IService {

    /**
     * The constructor of this class.
     */
    public ReadAllTeachersNumberAndName() {
    }

    public List run() throws FenixServiceException {
        List teachers = null;
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentTeacher persistentTeacher = sp.getIPersistentTeacher();
            teachers = persistentTeacher.readAll();
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e.getMessage());
        }

        if (teachers == null) {
            return new ArrayList();
        }

        Iterator teachersIterator = teachers.iterator();

        /*
         * Set the InfoObject only with the needed fields to be presented.
         */
        List teachersList = new ArrayList();
        while (teachersIterator.hasNext()) {
            InfoTeacher infoTeacher = new InfoTeacher();
            InfoPerson infoPerson = new InfoPerson();
            ITeacher teacher = (ITeacher) teachersIterator.next();
            infoTeacher.setTeacherNumber(teacher.getTeacherNumber());
            infoPerson.setNome(teacher.getPerson().getNome());
            infoTeacher.setInfoPerson(infoPerson);
            teachersList.add(infoTeacher);
        }

        return teachersList;
    }

}