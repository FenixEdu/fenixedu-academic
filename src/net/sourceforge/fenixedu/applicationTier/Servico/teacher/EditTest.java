/*
 * Created on 1/Ago/2003
 *
 */

package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.Calendar;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import net.sourceforge.fenixedu.domain.ITest;
import net.sourceforge.fenixedu.domain.Test;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTest;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;

/**
 * @author Susana Fernandes
 */
public class EditTest implements IService {

    public EditTest() {
    }

    public boolean run(Integer executionCourseId, Integer testId, String title, String information)
            throws FenixServiceException {
        try {
            ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
            IPersistentTest persistentTest = persistentSuport.getIPersistentTest();
            ITest test = (ITest) persistentTest.readByOID(Test.class, testId, true);
            test.setTitle(title);
            test.setInformation(information);
            test.setLastModifiedDate(Calendar.getInstance().getTime());
            return true;
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
    }
}