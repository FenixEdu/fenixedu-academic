/*
 * Created 2004/04/25
 */
package ServidorApresentacao.Action.student.finalDegreeWork;

import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import DataBeans.finalDegreeWork.InfoGroup;
import DataBeans.finalDegreeWork.InfoGroupProposal;
import DataBeans.finalDegreeWork.InfoGroupStudent;
import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author Luis Cruz
 */
public class FinalDegreeWorkAttributionDA extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        IUserView userView = SessionUtils.getUserView(request);

        Object[] args = { userView.getUtilizador() };
        InfoGroup infoGroup = (InfoGroup) ServiceUtils.executeService(userView,
                "ReadFinalDegreeWorkStudentGroupByUsername", args);
        if (infoGroup != null && infoGroup.getGroupProposals() != null) {
            Collections.sort(infoGroup.getGroupProposals(), new BeanComparator("orderOfPreference"));

            request.setAttribute("infoGroup", infoGroup);

            DynaActionForm finalDegreeWorkAttributionForm = (DynaActionForm) form;

            InfoGroupProposal infoGroupProposal = (InfoGroupProposal) CollectionUtils.find(infoGroup
                    .getGroupProposals(), new PREDICATE_FIND_PROPOSAL_ATTRIBUTED_TO_GROUP_BY_TEACHER(
                    infoGroup.getIdInternal()));
            if (infoGroupProposal != null) {
                finalDegreeWorkAttributionForm.set("attributedByTeacher", infoGroupProposal
                        .getFinalDegreeWorkProposal().getIdInternal().toString());
            }

            String confirmAttributions[] = new String[infoGroup.getGroupStudents().size()];
            for (int i = 0; i < infoGroup.getGroupStudents().size(); i++) {
                InfoGroupStudent infoGroupStudent = (InfoGroupStudent) infoGroup.getGroupStudents().get(
                        i);
                if (infoGroupStudent != null
                        && infoGroupStudent.getFinalDegreeWorkProposalConfirmation() != null) {
                    confirmAttributions[i] = infoGroupStudent.getFinalDegreeWorkProposalConfirmation()
                            .getIdInternal().toString();
                    confirmAttributions[i] += infoGroupStudent.getStudent().getIdInternal();
                }
            }
            finalDegreeWorkAttributionForm.set("confirmAttributions", confirmAttributions);

            request.setAttribute("finalDegreeWorkAttributionForm", finalDegreeWorkAttributionForm);
        }

        return mapping.findForward("showFinalDegreeWorkList");
    }

    public ActionForward confirmAttribution(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        DynaActionForm finalDegreeWorkAttributionForm = (DynaActionForm) form;

        String selectedGroupProposalOID = (String) finalDegreeWorkAttributionForm
                .get("selectedGroupProposal");

        IUserView userView = SessionUtils.getUserView(request);

        if (selectedGroupProposalOID != null && !selectedGroupProposalOID.equals("")) {
            Object args[] = { userView.getUtilizador(), new Integer(selectedGroupProposalOID) };
            ServiceUtils.executeService(userView, "ConfirmAttributionOfFinalDegreeWork", args);
        }

        return mapping.findForward("prepareShowFinalDegreeWorkList");
    }

    private class PREDICATE_FIND_PROPOSAL_ATTRIBUTED_TO_GROUP_BY_TEACHER implements Predicate {

        Integer groupID = null;

        public boolean evaluate(Object arg0) {
            InfoGroupProposal infoGroupProposal = (InfoGroupProposal) arg0;
            return (infoGroupProposal.getFinalDegreeWorkProposal().getGroupAttributedByTeacher() != null)
                    && (groupID.equals(infoGroupProposal.getFinalDegreeWorkProposal()
                            .getGroupAttributedByTeacher().getIdInternal()));
        }

        public PREDICATE_FIND_PROPOSAL_ATTRIBUTED_TO_GROUP_BY_TEACHER(Integer groupID) {
            super();
            this.groupID = groupID;
        }
    }

}