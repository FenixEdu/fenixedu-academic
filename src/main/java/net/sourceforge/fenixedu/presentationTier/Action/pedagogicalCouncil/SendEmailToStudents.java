package net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil;

import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.pedagogicalCouncil.elections.ElectionPeriodBean;
import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.accessControl.groups.StudentsByDegreeAndCurricularYear;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.organizationalStructure.PedagogicalCouncilUnit;
import net.sourceforge.fenixedu.domain.util.email.EmailBean;
import net.sourceforge.fenixedu.domain.util.email.Recipient;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

@Mapping(module = "pedagogicalCouncil", path = "/sendEmailToStudents", scope = "request", parameter = "method")
@Forwards(value = {
        @Forward(name = "showDegrees", path = "/pedagogicalCouncil/sendEmailToStudents.jsp", tileProperties = @Tile(
                title = "private.pedagogiccouncil.communication.sendemailtostudents")),
        @Forward(name = "sendEmail", path = "/messaging/emails.do?method=newEmail", tileProperties = @Tile(
                title = "private.pedagogiccouncil.communication.sendemailtostudents"), contextRelative = true) })
public class SendEmailToStudents extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();

        ElectionPeriodBean bean = new ElectionPeriodBean();
        bean.setDegreeType(DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE); // default
        bean.setExecutionYear(currentExecutionYear); // default

        return selectDegreeType(mapping, actionForm, request, response, bean);
    }

    public ActionForward selectDegreeType(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response, ElectionPeriodBean electionPeriodBean) throws Exception {
        final ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();
        request.setAttribute("currentExecutionYear", currentExecutionYear);
        if (electionPeriodBean == null) {
            Integer degreeOID = Integer.parseInt(request.getParameter("degreeOID"));
            final Degree degree = AbstractDomainObject.fromExternalId(degreeOID);

            electionPeriodBean = new ElectionPeriodBean();
            electionPeriodBean.setDegree(degree);
            electionPeriodBean.setDegreeType(degree.getDegreeType());
            electionPeriodBean.setExecutionYear(currentExecutionYear); // default
        } else {
            if (electionPeriodBean.getExecutionYear() == null) {
                electionPeriodBean.setExecutionYear(currentExecutionYear); // default
            }
            if (electionPeriodBean.getDegreeType() == null) {
                electionPeriodBean.setDegreeType(DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE); // default
            }
        }

        request.setAttribute("electionPeriodBean", electionPeriodBean);
        request.setAttribute("currentExecutionYear", currentExecutionYear);
        request.setAttribute("degrees", Degree.readAllByDegreeType(electionPeriodBean.getDegreeType()));
        return mapping.findForward("showDegrees");
    }

    public ActionForward selectDegreeTypePostBack(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        ElectionPeriodBean periodBean = getRenderedObject();
        RenderUtils.invalidateViewState();

        return selectDegreeType(mapping, actionForm, request, response, periodBean);

    }

    public ActionForward sendMail(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        ExecutionYear executionYear = ExecutionYear.readExecutionYearByName(request.getParameter("executionYear"));
        CurricularYear curricularYear = CurricularYear.readByYear(Integer.valueOf(request.getParameter("curricularYear")));
        Degree degree = AbstractDomainObject.fromExternalId(Integer.valueOf(request.getParameter("degreeId")));

        StudentsByDegreeAndCurricularYear studentsByDegreeAndCurricularYear =
                new StudentsByDegreeAndCurricularYear(degree, curricularYear, executionYear);

        String message =
                MessageResources.getMessageResources("resources.PedagogicalCouncilResources").getMessage(
                        "label.mail.student.year.degree", curricularYear.getYear().toString(), degree.getSigla());

        Recipient recipient = Recipient.newInstance(message, studentsByDegreeAndCurricularYear);
        EmailBean bean = new EmailBean();
        bean.setRecipients(Collections.singletonList(recipient));
        bean.setSender(PedagogicalCouncilUnit.getPedagogicalCouncilUnit().getUnitBasedSender().iterator().next());

        request.setAttribute("emailBean", bean);

        return mapping.findForward("sendEmail");
    }
}
