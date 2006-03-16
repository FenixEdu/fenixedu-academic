/*
 * Created on 06/Feb/2004
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.grant;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoPerson;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Barbosa
 * @author Pica
 *  
 */
public class ReadAllTeachersNumberAndName extends Service {

    public List run() throws FenixServiceException, ExcepcaoPersistencia {

        final List<InfoTeacher> result = new ArrayList<InfoTeacher>();

        final Collection<Teacher> teachers = persistentObject.readAll(Teacher.class);

        if (teachers != null) {
            for (final Teacher teacher : teachers) {
                InfoTeacher infoTeacher = new InfoTeacher();
                InfoPerson infoPerson = new InfoPerson();

                infoTeacher.setTeacherNumber(teacher.getTeacherNumber());
                infoTeacher.setInfoPerson(infoPerson);
                if (teacher.getPerson() != null){
    				infoPerson.setNome(teacher.getPerson().getNome());
                }

                result.add(infoTeacher);
            }
        }
        return result;
    }

}