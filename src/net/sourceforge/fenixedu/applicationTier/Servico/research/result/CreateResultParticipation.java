package net.sourceforge.fenixedu.applicationTier.Servico.research.result;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.externalPerson.InsertExternalPerson;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.research.result.ResultParticipationCreationBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.research.result.ResearchResult;
import net.sourceforge.fenixedu.domain.research.result.ResultParticipation.ResultParticipationRole;
import net.sourceforge.fenixedu.util.researcher.ResearchResultMetaDataManager;

public class CreateResultParticipation extends Service {

	public void run(ResultParticipationCreationBean bean) throws FenixServiceException {
		final ResearchResult result = bean.getResult();
		final ResultParticipationRole role = bean.getRole();
		final Unit organization = bean.getOrganization();
		Person participator = bean.getParticipator().getPerson();

		if (participator == null) {
			if (bean.isBeanExternal()) {
				final InsertExternalPerson newPerson = new InsertExternalPerson();
				final String participatorName = bean.getParticipatorName();

				if (organization != null) {
					participator = (newPerson.run(participatorName, organization)).getPerson();
				} else {
					if(bean.isUnitExternal()) {
						final String orgName = bean.getOrganizationName();
						participator = (newPerson.run(participatorName, orgName)).getPerson();
					}
					else {
						throw new DomainException("error.label.invalidNameForInternalUnit");
					}
				}
			} else {
				throw new DomainException("error.label.invalidNameForInternalPerson");
			}
		}
		result.addParticipation(participator, role);
		ResearchResultMetaDataManager.updateMetaDataInStorageFor(result);
	}
}
