package net.sourceforge.fenixedu.applicationTier.Servico.research.result;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.research.result.ResultUnitAssociationCreateBean;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.research.result.Result;
import net.sourceforge.fenixedu.domain.research.result.ResultUnitAssociation;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class CreateResultUnitAssociation extends Service {
    public ResultUnitAssociation run(ResultUnitAssociationCreateBean bean, Integer resultId) throws ExcepcaoPersistencia, FenixServiceException {
        
        ResultUnitAssociation association = null;
        final Unit unit;
        boolean newUnit = false;
        
        final Result result = rootDomainObject.readResultByOID(resultId);
        if(result == null){
            throw new FenixServiceException();
        }
        
        if (bean.getUnit() == null) {
            unit = Unit.createNewExternalInstitution(bean.getUnitName());            
            newUnit=true;
        }
        else{
            unit = bean.getUnit();
        }
        
        if(!result.hasAssociationWithUnitRole(unit, bean.getRole())){
            association = new ResultUnitAssociation();
            association.setResult(result);
            association.setUnit(unit);
            association.setRole(bean.getRole());        
        }
        else {
            if(newUnit) {
                unit.delete();
            }
        }
        
        return association;
    }    
}
