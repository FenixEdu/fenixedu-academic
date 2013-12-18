/*
 * Created on Dec 17, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.presentationTier.Action.department;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.ReadTeacherByOID;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;

/**
 * @author jpvl
 */
public abstract class AbstractReadProfessorshipsAction extends Action {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        User userView = Authenticate.getUser();
        DynaActionForm dynaForm = (DynaActionForm) form;

        InfoTeacher infoTeacher = getInfoTeacher(request, dynaForm);

        List detailedInfoProfessorshipList = getDetailedProfessorships(userView, infoTeacher.getExternalId(), dynaForm, request);

        ComparatorChain chain = new ComparatorChain();

        Comparator executionPeriodComparator =
                new BeanComparator("infoProfessorship.infoExecutionCourse.infoExecutionPeriod.semester");
        Comparator nameComparator = new BeanComparator("infoProfessorship.infoExecutionCourse.nome");

        chain.addComparator(executionPeriodComparator);
        chain.addComparator(nameComparator);
        Collections.sort(detailedInfoProfessorshipList, chain);

        request.setAttribute("detailedProfessorshipList", detailedInfoProfessorshipList);

        extraPreparation(userView, infoTeacher, request, dynaForm);
        return mapping.findForward("list-professorships");
    }

    protected void extraPreparation(User userView, InfoTeacher infoTeacher, HttpServletRequest request,
            DynaActionForm dynaForm) throws Exception {
    }

    protected InfoTeacher getInfoTeacher(HttpServletRequest request, DynaActionForm dynaForm) throws Exception {
        InfoTeacher infoTeacher = (InfoTeacher) request.getAttribute("infoTeacher");
        if (infoTeacher == null) {
            final User userView = Authenticate.getUser();
            infoTeacher = (InfoTeacher) ReadTeacherByOID.runReadTeacherByOID(dynaForm.get("externalId").toString());
            request.setAttribute("infoTeacher", infoTeacher);

        }
        return infoTeacher;
    }

    abstract List getDetailedProfessorships(User userView, String teacherId, DynaActionForm actionForm,
            HttpServletRequest request) throws FenixServiceException;
}