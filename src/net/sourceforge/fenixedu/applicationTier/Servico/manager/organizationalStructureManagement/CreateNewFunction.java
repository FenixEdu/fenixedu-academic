/*
 * Created on Nov 22, 2005
 *	by mrsp
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager.organizationalStructureManagement;

import java.util.Date;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Function;
import net.sourceforge.fenixedu.domain.organizationalStructure.FunctionType;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class CreateNewFunction extends Service {

    public void run(Integer functionID, Integer unitID, String functionName, Date beginDate,
            Date endDate, FunctionType type, Integer parentInherentFunctionID)
            throws ExcepcaoPersistencia, FenixServiceException, DomainException {
               
        Function parentInherentFunction = null;
        if (parentInherentFunctionID != null) {
            parentInherentFunction = (Function) rootDomainObject.readAccountabilityTypeByOID(parentInherentFunctionID);            
        }
        
        if (functionID == null) {
            Unit unit = null;
            if (unitID != null) {
                unit = (Unit) rootDomainObject.readPartyByOID(unitID);            
            }
            new Function(functionName, beginDate, endDate, type, unit, parentInherentFunction);
        } else {
            Function function = (Function) rootDomainObject.readAccountabilityTypeByOID(functionID);
            if (function == null) {
                throw new FenixServiceException("error.noFunction");
            }
            function.edit(functionName, beginDate, endDate, type, parentInherentFunction);
        }                      
    }
}
