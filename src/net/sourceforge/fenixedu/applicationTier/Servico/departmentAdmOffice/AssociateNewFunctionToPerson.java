/*
 * Created on Oct 27, 2005
 *	by mrsp
 */
package net.sourceforge.fenixedu.applicationTier.Servico.departmentAdmOffice;

import java.util.Date;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Function;
import net.sourceforge.fenixedu.domain.organizationalStructure.Function;
import net.sourceforge.fenixedu.domain.organizationalStructure.PersonFunction;
import net.sourceforge.fenixedu.domain.organizationalStructure.RulesRepository;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;


public class AssociateNewFunctionToPerson implements IService {

    public void run(Integer functionID, Integer personID, Double credits,
            Date beginDate, Date endDate) throws ExcepcaoPersistencia, FenixServiceException, DomainException {

        ISuportePersistente suportePersistente = PersistenceSupportFactory.getDefaultPersistenceSupport();
        
        Person person = (Person) suportePersistente.getIPessoaPersistente().readByOID(Person.class, personID);
        
        if(person == null){
            throw new FenixServiceException("error.noPerson");
        }
                       
        Function function = (Function) suportePersistente.getIPersistentObject().readByOID(Function.class, functionID);
        
        if(function == null){
            throw new FenixServiceException("error.noFunction");
        }
        
        if(RulesRepository.isElegible(person, function.getUnit(), function.getName())){                      
            PersonFunction personFunction = DomainFactory.makePersonFunction();
            personFunction.setPerson(person);
            personFunction.edit(function, beginDate, endDate, credits);                      
        }
        else{
            throw new FenixServiceException("error.associateFunction");
        }
    }
}
