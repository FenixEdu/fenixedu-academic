package net.sourceforge.fenixedu.applicationTier.Servico.research.result;

import net.sourceforge.fenixedu.accessControl.AccessControl;
import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.dataTransferObject.research.result.ResultUnitAssociationCreationBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.research.result.Result;
import net.sourceforge.fenixedu.domain.research.result.ResultUnitAssociation;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class CreateResultUnitAssociation extends Service {
    public ResultUnitAssociation run(ResultUnitAssociationCreationBean bean) throws ExcepcaoPersistencia, FenixServiceException {
        final Result result = bean.getResult();
        if(result == null){ throw new InvalidArgumentsServiceException(); } 
        final Person person = AccessControl.getUserView().getPerson();
        
        final Unit unit;
        if (bean.getUnit() == null) {
            unit = Unit.createNewExternalInstitution(bean.getUnitName());            
        }
        else { unit = bean.getUnit(); }
        
        ResultUnitAssociation association = null;
        try {
            association = new ResultUnitAssociation(result, unit, bean.getRole(), person.getName());
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
