/*
 * Created on 25/Abr/2004
 *  
 */
package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.guide.reimbursementGuide;

import java.text.DateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.dataTransferObject.guide.reimbursementGuide.InfoReimbursementGuide;
import net.sourceforge.fenixedu.dataTransferObject.guide.reimbursementGuide.InfoReimbursementGuideSituation;
import net.sourceforge.fenixedu.domain.gratuity.ReimbursementGuideState;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;
import net.sourceforge.fenixedu.util.TipoCurso;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 *  
 */
public class PrintReimbursementGuideDispatchAction extends FenixDispatchAction {

    public ActionForward print(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException {

        IUserView userView = SessionUtils.getUserView(request);

        Integer reimbursementGuideId = new Integer(this.getFromRequest("id", request));

        InfoReimbursementGuide infoReimbursementGuide = null;
        List infoStudents = null;
        InfoStudent infoStudent = null;

        Object args[] = { reimbursementGuideId };
        try {
            infoReimbursementGuide = (InfoReimbursementGuide) ServiceUtils.executeService(userView,
                    "ViewReimbursementGuide", args);

            Object args2[] = { infoReimbursementGuide.getInfoGuide().getInfoPerson() };

            infoStudents = (List) ServiceUtils.executeService(userView, "ReadStudentsByPerson", args2);

            Iterator it = infoStudents.iterator();
            while (it.hasNext()) {
                infoStudent = (InfoStudent) it.next();
                if (infoStudent.getDegreeType().equals(TipoCurso.MESTRADO_OBJ))
                    break;
            }

        } catch (FenixServiceException e) {
            throw new FenixActionException(e.getMessage(), mapping.findForward("error"));
        }

        Locale locale = this.getLocale(request);

        Date date = null;
        InfoReimbursementGuideSituation infoReimbursementGuideSituation = null;

        List infoReimbursementGuideSituations = infoReimbursementGuide
                .getInfoReimbursementGuideSituations();

        Iterator it = infoReimbursementGuideSituations.iterator();
        while (it.hasNext()) {
            infoReimbursementGuideSituation = (InfoReimbursementGuideSituation) it.next();
            if (infoReimbursementGuideSituation.getReimbursementGuideState().equals(
                    ReimbursementGuideState.ISSUED))
                date = infoReimbursementGuideSituation.getOfficialDate().getTime();
        }

        String formatedDate = DateFormat.getDateInstance(DateFormat.LONG, locale).format(date);

        request.setAttribute(SessionConstants.DATE, formatedDate);
        request.setAttribute(SessionConstants.REIMBURSEMENT_GUIDE, infoReimbursementGuide);
        request.setAttribute(SessionConstants.STUDENT, infoStudent);

        return mapping.findForward("start");

    }

    private String getFromRequest(String parameter, HttpServletRequest request) {
        String parameterString = request.getParameter(parameter);
        if (parameterString == null) {
            parameterString = (String) request.getAttribute(parameter);
        }
        return parameterString;
    }

}