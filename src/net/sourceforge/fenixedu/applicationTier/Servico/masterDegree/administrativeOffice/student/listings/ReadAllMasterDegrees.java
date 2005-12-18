package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.student.listings;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeWithInfoDegreeCurricularPlansAndInfoDegreeInfos;
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

	public List run(DegreeType degreeType) throws FenixServiceException, ExcepcaoPersistencia {

		ISuportePersistente sp = null;
		List result = new ArrayList();

		sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

		// Read the master degrees
		result = sp.getICursoPersistente().readAllByDegreeType(degreeType);

		if (result == null || result.size() == 0) {
			throw new NonExistingServiceException();
		}

		List degrees = new ArrayList();
		Iterator iterator = result.iterator();
		while (iterator.hasNext())
			degrees.add(InfoDegreeWithInfoDegreeCurricularPlansAndInfoDegreeInfos
					.newInfoFromDomain((IDegree) iterator.next()));
		return degrees;

	}
}