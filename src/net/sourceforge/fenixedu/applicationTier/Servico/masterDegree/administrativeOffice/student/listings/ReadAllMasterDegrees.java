package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.student.listings;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IDegree;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class ReadAllMasterDegrees implements IService {

    public List run(DegreeType degreeType) throws FenixServiceException {

        ISuportePersistente sp = null;
        List result = new ArrayList();
        try {
            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

            // Read the master degrees
            result = sp.getICursoPersistente().readAllByDegreeType(degreeType);

            if (result == null || result.size() == 0) {
                throw new NonExistingServiceException();
            }

        } catch (ExcepcaoPersistencia ex) {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx;
        }

        List degrees = new ArrayList();
        Iterator iterator = result.iterator();
        while (iterator.hasNext())
            degrees.add(Cloner.copyIDegree2InfoDegree((IDegree) iterator.next()));
        return degrees;

    }
}