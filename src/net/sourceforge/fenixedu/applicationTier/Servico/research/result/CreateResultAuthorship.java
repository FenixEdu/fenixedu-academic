package net.sourceforge.fenixedu.applicationTier.Servico.research.result;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.externalPerson.InsertExternalPerson;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.research.ProjectParticipantFullCreationBean;
import net.sourceforge.fenixedu.dataTransferObject.research.ProjectParticipantSimpleCreationBean;
import net.sourceforge.fenixedu.dataTransferObject.research.ProjectParticipantUnitCreationBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.ResultAuthorshipFullCreationBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.ResultAuthorshipSimpleCreationBean;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.ExternalPerson;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.research.project.Project;
import net.sourceforge.fenixedu.domain.research.project.ProjectParticipation;
import net.sourceforge.fenixedu.domain.research.result.Authorship;
import net.sourceforge.fenixedu.domain.research.result.Result;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class CreateResultAuthorship extends Service {
    /**
     * Service responsible for creating a result authorship (with internal person)
     * @param bean - Bean responsible for carrying the information from the presentation to the services layer
     * @param resultId - the identifier of the Result for whom the authorship is being created 
     * @return void
     * @throws ExcepcaoPersistencia
     * @throws FenixServiceException - In case the result doesn't exist.
     */
    public void run(ResultAuthorshipSimpleCreationBean bean, Integer resultId) throws ExcepcaoPersistencia, FenixServiceException {
        final Result result = rootDomainObject.readResultByOID(resultId);
        
        if(result == null){
            throw new FenixServiceException();
        }
        
        boolean newAuthorship = true;
        for(Authorship author : result.getResultAuthorships()){
            if (author.getAuthor().equals(bean.getPerson())){
                newAuthorship = false;
            }
        }
        
        if(newAuthorship){
            Authorship authorship = new Authorship();
            authorship.setAuthor(bean.getPerson());
            authorship.setAuthorOrder(result.getResultAuthorships().size());
            authorship.setResult(result);
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
    public void run(ResultAuthorshipFullCreationBean bean, Integer resultId) throws ExcepcaoPersistencia, FenixServiceException {
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
        
        boolean newAuthorship = true;
        for(Authorship author : result.getResultAuthorships()){
            if (author.getAuthor().equals(externalPerson.getPerson())){
                newAuthorship = false;
            }
        }
        
        if(newAuthorship){
            Authorship authorship = new Authorship();
            authorship.setAuthor(externalPerson.getPerson());
            authorship.setAuthorOrder(result.getResultAuthorships().size());
            authorship.setResult(result);
        }
    }    
}
