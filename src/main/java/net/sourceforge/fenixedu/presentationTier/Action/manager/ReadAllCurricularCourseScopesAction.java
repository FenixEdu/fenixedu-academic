/*
 * Created on 16/Ago/2003
 */
package net.sourceforge.fenixedu.presentationTier.Action.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pt.ist.bennu.core.domain.User;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.ReadCurricularCourseScopes;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseScope;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.bennu.core.security.Authenticate;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

/**
 * @author Fernanda Quitério 28/10/2003
 * 
 */
@Mapping(module = "manager", path = "/readAllCurricularCourseScopes", input = "/readcurricularCourse.do", scope = "session")
@Forwards(value = { @Forward(name = "viewCurricularCourseScopes", path = "/manager/readCurricularCourseScopes_bd.jsp",
        tileProperties = @Tile(navLocal = "/manager/curricularCourseNavLocalManager.jsp")) })
@Exceptions(value = { @ExceptionHandling(
        type = net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException.class,
        key = "resources.Action.exceptions.NonExistingActionException",
        handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class, scope = "request") })
public class ReadAllCurricularCourseScopesAction extends FenixAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixActionException {

        User userView = Authenticate.getUser();
        String curricularCourseId = request.getParameter("curricularCourseId");
        String degreeId = request.getParameter("degreeId");
        String degreeCurricularPlanId = request.getParameter("degreeCurricularPlanId");
        request.setAttribute("curricularCourseId", curricularCourseId);
        request.setAttribute("degreeId", degreeId);
        request.setAttribute("degreeCurricularPlanId", degreeCurricularPlanId);

        List curricularCourseScopes = new ArrayList();
        try {
            curricularCourseScopes = ReadCurricularCourseScopes.run(curricularCourseId);

        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        if (curricularCourseScopes != null && !curricularCourseScopes.isEmpty()) {
            ComparatorChain comparatorChain = new ComparatorChain();
            comparatorChain.addComparator(new BeanComparator("beginDate.time"));
            Collections.sort(curricularCourseScopes, comparatorChain);

            InfoCurricularCourse infoCurricularCourse =
                    ((InfoCurricularCourseScope) curricularCourseScopes.iterator().next()).getInfoCurricularCourse();
            request.setAttribute("infoCurricularCourse", infoCurricularCourse);
        }

        request.setAttribute("curricularCourseScopesList", curricularCourseScopes);
        return mapping.findForward("viewCurricularCourseScopes");
    }
}