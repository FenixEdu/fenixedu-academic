/*
 * Created on 14/Ago/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author lmac1
 */

public class ReadAllTeachers implements IService {

    public List run() throws FenixServiceException, ExcepcaoPersistencia {

        final List<InfoTeacher> result = new ArrayList<InfoTeacher>();

        final ISuportePersistente suportePersistente = PersistenceSupportFactory
                .getDefaultPersistenceSupport();
        final Collection<ITeacher> allTeachers = suportePersistente.getIPersistentTeacher().readAll(
                Teacher.class);

        if (allTeachers != null) {
            for (final ITeacher teacher : allTeachers) {
                result.add(Cloner.copyITeacher2InfoTeacher(teacher));
            }
        }
        return result;
    }
}