package net.sourceforge.fenixedu.applicationTier.Servico.alumni;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.BusinessArea;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import pt.ist.fenixWebFramework.services.Service;

public class CreateBusinessArea extends FenixService {

	@Service
	public static BusinessArea run(final Integer level, final String code, final String description) throws FenixServiceException {

		try {
			return new BusinessArea(level, code, description);

		} catch (DomainException e) {
			throw new FenixServiceException(e.getMessage());
		}
	}

	@Service
	public static BusinessArea run(final Integer level, final String code, final String description, final BusinessArea parentArea)
			throws FenixServiceException {

		try {
			return new BusinessArea(level, code, description, parentArea);

		} catch (DomainException e) {
			throw new FenixServiceException(e.getMessage());
		}
	}

}