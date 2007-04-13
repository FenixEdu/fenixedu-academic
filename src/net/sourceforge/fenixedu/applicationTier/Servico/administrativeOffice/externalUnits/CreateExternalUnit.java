package net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.externalUnits;

import org.joda.time.YearMonthDay;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.organizationalStructureManagement.CreateUnit;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.externalUnits.CreateExternalUnitBean;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class CreateExternalUnit extends Service {
    
    public Unit run(final CreateExternalUnitBean externalUnitBean) throws DomainException, FenixFilterException, ExcepcaoPersistencia, FenixServiceException {
	return new CreateUnit().run(externalUnitBean.getParentUnit(),
		externalUnitBean.getUnitName(), null, externalUnitBean.getUnitCode(), 
		new YearMonthDay(), null, externalUnitBean.getUnitType(), null, null, null, null, null, null);
    }
}
