package net.sourceforge.fenixedu.applicationTier.Servico.research.result;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.dataTransferObject.research.result.ResultUnitAssociationCreateBean;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.research.result.Result;
import net.sourceforge.fenixedu.domain.research.result.ResultUnitAssociation;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class CreateResultUnitAssociation extends Service {
    public ResultUnitAssociation run(ResultUnitAssociationCreateBean bean, Integer resultId, String personName) throws ExcepcaoPersistencia, FenixServiceException {
        final Result result = rootDomainObject.readResultByOID(resultId);
        if(result == null){ throw new InvalidArgumentsServiceException(); } 
        
        final Unit unit;
        if (bean.getUnit() == null) {
            unit = Unit.createNewExternalInstitution(bean.getUnitName());            
        }
        else { unit = bean.getUnit(); }
        
        ResultUnitAssociation association = null;
        try {
            association = new ResultUnitAssociation(result, unit, bean.getRole(), personName);
        }
        catch (DomainException e) {
            //If a new unit was created for this purpose and the association was not created, unit will be deleted. 
            if (unit!=null && bean.getUnit()==null) {
                unit.delete();
            }
            throw new FenixServiceException(e.getKey());
        }
        return association;
    }    
}
