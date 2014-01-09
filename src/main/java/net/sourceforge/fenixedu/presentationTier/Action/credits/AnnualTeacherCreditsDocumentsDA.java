package net.sourceforge.fenixedu.presentationTier.Action.credits;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.credits.AnnualTeachingCredits;
import net.sourceforge.fenixedu.domain.credits.util.AnnualTeachingCreditsBean;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Forwards(value = { @Forward(name = "showAnnualTeacherCreditsDocument", path = "/credits/showAnnualTeacherCreditsDocument.jsp",
        tileProperties = @Tile(head = "/commons/blank.jsp", navGeral = "/commons/blank.jsp", navLocal = "/commons/blank.jsp",
                bodyContext = "/commons/blank.jsp", extend = "df.layout.blank")) })
public abstract class AnnualTeacherCreditsDocumentsDA extends FenixDispatchAction {

    public abstract ActionForward getAnnualTeachingCreditsPdf(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws NumberFormatException, FenixServiceException, Exception;

    protected ActionForward getTeacherCreditsDocument(ActionMapping mapping, HttpServletRequest request, RoleType roleType) {
        Teacher teacher = (Teacher) request.getAttribute("teacher");
        ExecutionYear executionYear = (ExecutionYear) request.getAttribute("executionYear");

        AnnualTeachingCreditsBean annualTeachingCreditsBean = null;
        AnnualTeachingCredits annualTeachingCredits = AnnualTeachingCredits.readByYearAndTeacher(executionYear, teacher);
        if (annualTeachingCredits != null) {
            annualTeachingCreditsBean = new AnnualTeachingCreditsBean(annualTeachingCredits, roleType);
        } else {
            annualTeachingCreditsBean = new AnnualTeachingCreditsBean(executionYear, teacher, roleType);
        }
        request.setAttribute("annualTeachingCreditsBean", annualTeachingCreditsBean);
        return mapping.findForward("showAnnualTeacherCreditsDocument");
    }

}