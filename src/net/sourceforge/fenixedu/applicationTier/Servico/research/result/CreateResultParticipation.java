package net.sourceforge.fenixedu.applicationTier.Servico.research.result;

import net.sourceforge.fenixedu.accessControl.AccessControl;
import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.externalPerson.InsertExternalPerson;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.dataTransferObject.research.result.ResultParticipationCreationBean;

import net.sourceforge.fenixedu.domain.ExternalPerson;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.research.result.Result;
import net.sourceforge.fenixedu.domain.research.result.ResultParticipation;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class CreateResultParticipation extends Service {

    public ResultParticipation run(ResultParticipationCreationBean bean) throws ExcepcaoPersistencia, FenixServiceException {
        final Result result = bean.getResult();
        if(result == null){ throw new InvalidArgumentsServiceException(); }
        
        final Person person = AccessControl.getUserView().getPerson();
        ResultParticipation resultParticipation = null;
        
        if(bean.getPerson()!=null) {
            resultParticipation = new ResultParticipation(result,bean.getPerson(),bean.getResultParticipationRole(), person);
        }
        else {
            if (!(bean.getOrganization()==null && (bean.getOrganizationName()==null||bean.getOrganizationName().equals("")))) {
                final InsertExternalPerson insertExternalPerson = new InsertExternalPerson();
                final ExternalPerson externalPerson;
                if (bean.getOrganization() == null) {
                    externalPerson = insertExternalPerson.run(bean.getPersonName(), bean.getOrganizationName());
                }
                else {
                    externalPerson = insertExternalPerson.run(bean.getPersonName(), bean.getOrganization());
                }
                resultParticipation = new ResultParticipation(result,externalPerson.getPerson(),bean.getResultParticipationRole(), person);
            }
        }
        
        
        return resultParticipation;
    }
}
