package net.sourceforge.fenixedu.applicationTier.Servico.research.activity;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.research.activity.ResearchActivityParticipantEditionBean;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.research.activity.Participation;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class EditResearchActivityParticipants extends FenixService {

	@Checked("ResultPredicates.author")
	@Service
	public static List<ResearchActivityParticipantEditionBean> run(List<ResearchActivityParticipantEditionBean> beans)
			throws FenixServiceException {

		List<ResearchActivityParticipantEditionBean> notEditedParticipants =
				new ArrayList<ResearchActivityParticipantEditionBean>();

		for (ResearchActivityParticipantEditionBean bean : beans) {
			Participation participation = bean.getParticipation();
			participation.setRoleMessage(bean.getRoleMessage());

			if (!bean.getRole().equals(participation.getRole())) {
				try {
					participation.setRole(bean.getRole());
				} catch (DomainException e) {
					notEditedParticipants.add(bean);
				}
			}
		}

		return notEditedParticipants;

	}

}