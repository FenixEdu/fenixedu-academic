package net.sourceforge.fenixedu.applicationTier.Servico.research.result;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.externalPerson.InsertExternalPerson;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.research.ProjectParticipantFullCreationBean;
import net.sourceforge.fenixedu.dataTransferObject.research.ProjectParticipantSimpleCreationBean;
import net.sourceforge.fenixedu.dataTransferObject.research.ProjectParticipantUnitCreationBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.ResultParticipationFullCreationBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.ResultParticipationSimpleCreationBean;

import net.sourceforge.fenixedu.domain.ExternalPerson;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.research.project.Project;
import net.sourceforge.fenixedu.domain.research.project.ProjectParticipation;
import net.sourceforge.fenixedu.domain.research.result.Result;
import net.sourceforge.fenixedu.domain.research.result.ResultParticipation;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class CreateResultParticipation extends Service {
    /**
     * Service responsible for creating a result authorship (with internal person)
     * @param bean - Bean responsible for carrying the information from the presentation to the services layer
     * @param resultId - the identifier of the Result for whom the authorship is being created 
     * @return void
     * @throws ExcepcaoPersistencia
     * @throws FenixServiceException - In case the result doesn't exist.
     */
    public void run(ResultParticipationSimpleCreationBean bean, Integer resultId) throws ExcepcaoPersistencia, FenixServiceException {
        final Result result = rootDomainObject.readResultByOID(resultId);
        
        if(result == null){
            throw new FenixServiceException();
        }
        
        boolean newResultParticipation = true;
        for(ResultParticipation resultParticipation : result.getResultParticipations()){
            if (resultParticipation.getPerson().equals(bean.getPerson())){
                newResultParticipation = false;
            }
        }
        
        if(newResultParticipation){
            ResultParticipation resultParticipation = new ResultParticipation();
            resultParticipation.setPerson(bean.getPerson());
            resultParticipation.setPersonOrder(result.getResultParticipations().size());
            resultParticipation.setResult(result);
        }
    }
    
    /**
     * Service responsible for creating a result authorship (with external person)
     * @param bean - Bean responsible for carrying the information from the presentation to the services layer
     * @param resultId - the identifier of the Result for whom the authorship is being created 
     * @return void
     * @throws ExcepcaoPersistencia
     * @throws FenixServiceException - In case the result doesn't exist.
     */
    public void run(ResultParticipationFullCreationBean bean, Integer resultId) throws ExcepcaoPersistencia, FenixServiceException {
        final ExternalPerson externalPerson;
        final Result result = rootDomainObject.readResultByOID(resultId);
        
        if(result == null){
            throw new FenixServiceException();
        }
        
        final InsertExternalPerson insertExternalPerson = new InsertExternalPerson();
        
        if (bean.getOrganization() == null) {
            externalPerson = insertExternalPerson.run(bean.getPersonName(), bean.getOrganizationName());
        }
        else {
            externalPerson = insertExternalPerson.run(bean.getPersonName(), bean.getOrganization());
        }
        
        boolean newResultParticipation = true;
        for(ResultParticipation resultParticipation : result.getResultParticipations()){
            if (resultParticipation.getPerson().equals(externalPerson.getPerson())){
                newResultParticipation = false;
            }
        }
        
        if(newResultParticipation){
            ResultParticipation resultParticipation = new ResultParticipation();
            resultParticipation.setPerson(externalPerson.getPerson());
            resultParticipation.setPersonOrder(result.getResultParticipations().size());
            resultParticipation.setResult(result);
        }
    }    
}
