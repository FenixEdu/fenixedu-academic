/*
 * Created on Nov 21, 2003 by jpvl
 *  
 */
package ServidorApresentacao.Action.department;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.DynaActionForm;

import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;

/**
 * @author jpvl
 */
public class ReadTeacherProfessorshipsByExecutionPeriodAction extends AbstractReadProfessorshipsAction
{

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorApresentacao.Action.department.AbstractReadProfessorshipsAction#getDetailedProfessorships(ServidorAplicacao.IUserView,
	 *          java.lang.Integer, org.apache.struts.action.DynaActionForm,
	 *          javax.servlet.http.HttpServletRequest)
	 */
    List getDetailedProfessorships(IUserView userView, Integer teacherId, DynaActionForm actionForm,
            HttpServletRequest request) throws FenixServiceException
    {
        List detailedInfoProfessorshipList = (List) ServiceUtils.executeService(userView,
                "ReadDetailedTeacherProfessorshipsByExecutionPeriod", new Object[]{teacherId, null});
        return detailedInfoProfessorshipList;
    }

}