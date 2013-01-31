/*
 * Created on 24/Nov/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.guide;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoGuide;
import net.sourceforge.fenixedu.dataTransferObject.InfoGuideWithPersonAndExecutionDegreeAndContributor;
import net.sourceforge.fenixedu.domain.Guide;

/**
 * @author <a href="mailto:joao.mota@ist.utl.pt">João Mota </a>
 */
public class ReadGuide extends FenixService {

	public InfoGuide run(Integer guideId) throws FenixServiceException {
		Guide guide;
		InfoGuide infoGuide = null;
		guide = rootDomainObject.readGuideByOID(guideId);
		if (guide == null) {
			throw new InvalidArgumentsServiceException();
		}
		infoGuide = InfoGuideWithPersonAndExecutionDegreeAndContributor.newInfoFromDomain(guide);

		return infoGuide;
	}

}