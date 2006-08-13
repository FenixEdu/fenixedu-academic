/*
 * Created on 28/Jul/2003
 */
package net.sourceforge.fenixedu.presentationTier.Action.manager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegree;
import net.sourceforge.fenixedu.domain.GradeScale;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.ExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

/**
 * @author lmac1
 */

public class EditDegreeDispatchAction extends FenixDispatchAction {

    public ActionForward prepareEdit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException {

        IUserView userView = SessionUtils.getUserView(request);

        DynaActionForm readDegreeForm = (DynaActionForm) form;

        Integer degreeId = new Integer(request.getParameter("degreeId"));

        InfoDegree oldInfoDegree = null;

        Object args[] = { degreeId };

        try {
            oldInfoDegree = (InfoDegree) ServiceUtils.executeService(userView, "ReadDegree", args);

        } catch (NonExistingServiceException e) {
            throw new NonExistingActionException("message.nonExistingDegree", mapping
                    .findForward("readDegrees"));
        } catch (FenixServiceException fenixServiceException) {
            throw new FenixActionException(fenixServiceException.getMessage());
        }

        DegreeType degreeType = (DegreeType) oldInfoDegree.getTipoCurso();

        readDegreeForm.set("name", oldInfoDegree.getNome());
        readDegreeForm.set("code", oldInfoDegree.getSigla());
        readDegreeForm.set("nameEn",oldInfoDegree.getNameEn());
        readDegreeForm.set("degreeType", degreeType.toString());
        if(oldInfoDegree.getGradeScale() != null) {
        	readDegreeForm.set("gradeType", oldInfoDegree.getGradeScale().toString());
        }
        return mapping.findForward("editDegree");
    }

    public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException {

        IUserView userView = SessionUtils.getUserView(request);

        DynaActionForm editDegreeForm = (DynaActionForm) form;
        Integer oldDegreeId = new Integer(request.getParameter("degreeId"));
        String code = (String) editDegreeForm.get("code");
        String name = (String) editDegreeForm.get("name");
        String nameEn = (String) editDegreeForm.get("nameEn");
        String degreeTypeInt = (String) editDegreeForm.get("degreeType");
        String gradeTypeString = (String) editDegreeForm.get("gradeType");        

        DegreeType degreeType = DegreeType.valueOf(degreeTypeInt);
        GradeScale gradeScale = null;
        if(gradeTypeString != null && gradeTypeString.length() > 0) {
        	gradeScale = GradeScale.valueOf(gradeTypeString);
        }
        
        Object args[] = { oldDegreeId, code, name, nameEn, degreeType, gradeScale };

        try {
            ServiceUtils.executeService(userView, "EditDegreeInOldDegreeStructure", args);

        } catch (NonExistingServiceException e) {
            throw new NonExistingActionException("message.nonExistingDegree", mapping
                    .findForward("readDegrees"));
        } catch (ExistingServiceException e) {
            throw new ExistingActionException("message.manager.existing.degree");
        } catch (FenixServiceException fenixServiceException) {
            throw new FenixActionException(fenixServiceException.getMessage());
        }
        return mapping.findForward("readDegree");
    }
}