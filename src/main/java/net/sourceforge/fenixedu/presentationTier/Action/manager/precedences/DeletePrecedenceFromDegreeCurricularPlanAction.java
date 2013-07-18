package net.sourceforge.fenixedu.presentationTier.Action.manager.precedences;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.precedences.DeletePrecedenceFromDegreeCurricularPlan;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.precedences.ReadPrecedencesFromDegreeCurricularPlan;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.security.UserView;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

/**
 * @author David Santos in Jul 28, 2004
 */

@Mapping(module = "manager", path = "/deletePrecedence", input = "df.page.manage.precedences", scope = "request")
@Forwards(value = { @Forward(name = "showPrecedences", path = "/manager/precedences/managePrecedences.jsp",
        tileProperties = @Tile(navLocal = "/manager/degreeCurricularPlanNavLocalManager.jsp")) })
public class DeletePrecedenceFromDegreeCurricularPlanAction extends FenixAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixActionException {

        IUserView userView = UserView.getUser();

        Integer degreeID = new Integer(request.getParameter("degreeId"));
        Integer degreeCurricularPlanID = new Integer(request.getParameter("degreeCurricularPlanId"));
        Integer precedenceID = new Integer(request.getParameter("precedenceId"));

        try {
            DeletePrecedenceFromDegreeCurricularPlan.run(precedenceID);
            Map result = ReadPrecedencesFromDegreeCurricularPlan.run(degreeCurricularPlanID);
            request.setAttribute("precedences", result);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        request.setAttribute("degreeId", degreeID);
        request.setAttribute("degreeCurricularPlanId", degreeCurricularPlanID);

        return mapping.findForward("showPrecedences");
    }
}