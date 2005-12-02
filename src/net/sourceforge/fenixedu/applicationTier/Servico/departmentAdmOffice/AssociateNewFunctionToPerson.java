/*
 * Created on Oct 27, 2005
 *	by mrsp
 */
package net.sourceforge.fenixedu.applicationTier.Servico.departmentAdmOffice;

import java.util.Date;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.organizationalStructure.Function;
import net.sourceforge.fenixedu.domain.organizationalStructure.IFunction;
import net.sourceforge.fenixedu.domain.organizationalStructure.IPersonFunction;
import net.sourceforge.fenixedu.domain.organizationalStructure.RulesRepository;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;


public class AssociateNewFunctionToPerson implements IService {

    public void run(Integer functionID, Integer personID, Integer credits,
            Date beginDate, Date endDate) throws ExcepcaoPersistencia, FenixServiceException {

        ISuportePersistente suportePersistente = PersistenceSupportFactory.getDefaultPersistenceSupport();
        
        IPerson person = (IPerson) suportePersistente.getIPessoaPersistente().readByOID(Person.class, personID);
        
        if(person == null){
            throw new FenixServiceException("error.noPerson");
        }
                       
        IFunction function = (IFunction) suportePersistente.getIPersistentObject().readByOID(Function.class, functionID);
        
        if(function == null){
            throw new FenixServiceException("error.noFunction");
        }
        
        if(RulesRepository.isElegible(person, function.getUnit(), function.getName())){                      
            IPersonFunction personFunction = DomainFactory.makePersonFunction();
            personFunction.setPerson(person);
            personFunction.setFunction(function);
            personFunction.setCredits(credits);
            personFunction.setBeginDate(beginDate);
            personFunction.setEndDate(endDate);
        }
        else{
            throw new FenixServiceException("error.associateFunction");
        }
    }
}
