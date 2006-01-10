/*
 * Created on Nov 22, 2005
 *	by mrsp
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager.organizationalStructureManagement;

import java.util.Date;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Function;
import net.sourceforge.fenixedu.domain.organizationalStructure.FunctionType;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class CreateNewFunction implements IService {

    public void run(Integer functionID, Integer unitID, String functionName, Date beginDate,
            Date endDate, FunctionType type, Integer parentInherentFunctionID)
            throws ExcepcaoPersistencia, FenixServiceException, DomainException {

        ISuportePersistente suportePersistente = PersistenceSupportFactory
                .getDefaultPersistenceSupport();

        Function function = null;
        if (functionID == null) {
            function = DomainFactory.makeFunction();
        } else {
            function = (Function) suportePersistente.getIPersistentObject().readByOID(Function.class,
                    functionID);
            if (function == null) {
                throw new FenixServiceException("error.noFunction");
            }
        }        

        Unit unit = null;
        if (unitID != null) {
            unit = (Unit) suportePersistente.getIPersistentObject().readByOID(Unit.class, unitID);            
        }

        Function parentInherentFunction = null;
        if (parentInherentFunctionID != null) {
            parentInherentFunction = (Function) suportePersistente.getIPersistentObject()
                    .readByOID(Function.class, parentInherentFunctionID);            
        }
        
        function.edit(functionName, beginDate, endDate, type, unit, parentInherentFunction);
    }
}
