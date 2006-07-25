package net.sourceforge.fenixedu.applicationTier.Servico.research.result;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.externalPerson.InsertExternalPerson;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.dataTransferObject.research.result.ResultParticipationFullCreationBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.ResultParticipationSimpleCreationBean;
import net.sourceforge.fenixedu.domain.ExternalPerson;
import net.sourceforge.fenixedu.domain.research.result.Result;
import net.sourceforge.fenixedu.domain.research.result.ResultParticipation;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class CreateResultParticipation extends Service {

    public ResultParticipation run(ResultParticipationSimpleCreationBean bean, Integer resultId, String personName) throws ExcepcaoPersistencia, FenixServiceException {
        final Result result = rootDomainObject.readResultByOID(resultId);
        
        if(result == null){
            throw new InvalidArgumentsServiceException();
        }
        
        return new ResultParticipation(result,bean.getPerson(),result.getResultParticipationsCount(),bean.getResultParticipationRole(), personName);
    }
    
    public ResultParticipation run(ResultParticipationFullCreationBean bean, Integer resultId, String personName) throws ExcepcaoPersistencia, FenixServiceException {
        final ExternalPerson externalPerson;
        final Result result = rootDomainObject.readResultByOID(resultId);
        
        if(result == null){
            throw new InvalidArgumentsServiceException();
        }
        
        final InsertExternalPerson insertExternalPerson = new InsertExternalPerson();
        if (bean.getOrganization() == null) {
            externalPerson = insertExternalPerson.run(bean.getPersonName(), bean.getOrganizationName());
        }
        else {
            externalPerson = insertExternalPerson.run(bean.getPersonName(), bean.getOrganization());
        }
        
        return new ResultParticipation(result,externalPerson.getPerson(),result.getResultParticipationsCount(),bean.getResultParticipationRole(),personName);
    }    
}
