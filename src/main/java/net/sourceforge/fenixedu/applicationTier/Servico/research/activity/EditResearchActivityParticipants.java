package net.sourceforge.fenixedu.applicationTier.Servico.research.activity;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.research.activity.ResearchActivityParticipantEditionBean;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.research.activity.Participation;
import net.sourceforge.fenixedu.predicates.ResultPredicates;
import pt.ist.fenixframework.Atomic;

public class EditResearchActivityParticipants {

    @Atomic
    public static List<ResearchActivityParticipantEditionBean> run(List<ResearchActivityParticipantEditionBean> beans)
            throws FenixServiceException {
        check(ResultPredicates.author);

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