/*
 * Created on 14/Ago/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacherWithPersonAndCategory;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author lmac1
 */

public class ReadAllTeachers extends Service {

    public List run() throws FenixServiceException, ExcepcaoPersistencia {

        final List<InfoTeacher> result = new ArrayList<InfoTeacher>();

        final Collection<Teacher> allTeachers = persistentSupport.getIPersistentTeacher().readAll(
                Teacher.class);

        if (allTeachers != null) {
            for (final Teacher teacher : allTeachers) {
                result.add(InfoTeacherWithPersonAndCategory.newInfoFromDomain(teacher));
            }
        }
        return result;
    }
}