/*
 * Created on Nov 21, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.presentationTier.Action.department;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;

import org.apache.struts.action.DynaActionForm;

/**
 * @author jpvl
 */
public class ReadTeacherProfessorshipsByExecutionPeriodAction extends AbstractReadProfessorshipsAction {

    /*
     * (non-Javadoc)
     * 
     * @see ServidorApresentacao.Action.department.AbstractReadProfessorshipsAction#getDetailedProfessorships(ServidorAplicacao.IUserView,
     *      java.lang.Integer, org.apache.struts.action.DynaActionForm,
     *      javax.servlet.http.HttpServletRequest)
     */
    List getDetailedProfessorships(IUserView userView, Integer teacherId, DynaActionForm actionForm,
            HttpServletRequest request) throws FenixServiceException, FenixFilterException {
        Integer executionPeriodId = (Integer) actionForm.get("executionPeriodId");
        executionPeriodId = ((executionPeriodId == null) || (executionPeriodId.intValue() == 0)) ? null
                : executionPeriodId;
        List detailedInfoProfessorshipList = (List) ServiceUtils.executeService(userView,
                "ReadDetailedTeacherProfessorshipsByExecutionPeriod", new Object[] { teacherId,
                        executionPeriodId });
        return detailedInfoProfessorshipList;
    }

}