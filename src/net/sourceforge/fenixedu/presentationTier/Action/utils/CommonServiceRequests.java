/*
 * Created on Apr 3, 2004
 *
 */
package net.sourceforge.fenixedu.presentationTier.Action.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoBranch;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.domain.branch.BranchType;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;

/**
 * @author Luis Cruz
 *  
 */
public class CommonServiceRequests {

    /**
     * @param userView
     * @param degreeCurricularPlanOID
     * @return
     */
    public static List getBranchesByDegreeCurricularPlan(IUserView userView, Integer degreeCurricularPlanOID) throws FenixActionException, FenixFilterException {        
	
	Object[] argsBranches = { degreeCurricularPlanOID };
        List branches = null;
        try {
            branches = (List) ServiceUtils.executeService(userView, "ReadBranchesByDegreeCurricularPlanId", argsBranches);
        } catch (FenixServiceException fse) {
            throw new FenixActionException(fse);
        }

        List newBranches = new ArrayList();
        if(branches != null) {
            Iterator iterator = branches.iterator();
            while (iterator.hasNext()) {
                InfoBranch infoBranch = (InfoBranch) iterator.next();
                if (infoBranch != null && infoBranch.getBranchType() != null
                        && !infoBranch.getBranchType().equals(BranchType.COMNBR))
                    newBranches.add(infoBranch);
            }
        }
        return newBranches;
    }

    /**
     * @param degreeOID
     * @return
     */
    public static InfoExecutionDegree getInfoExecutionDegree(IUserView userView, Integer degreeOID)
            throws FenixActionException, FenixFilterException {
        InfoExecutionDegree infoExecutionDegree = null;
        Object[] args = { degreeOID };
        try {
            infoExecutionDegree = (InfoExecutionDegree) ServiceUtils.executeService(userView,
                    "ReadExecutionDegreeByOID", args);
        } catch (FenixServiceException fse) {
            throw new FenixActionException(fse);
        }
        return infoExecutionDegree;
    }

	public static List<InfoExecutionYear> getInfoExecutionYears() throws FenixFilterException, FenixServiceException {
		return (List) ServiceUtils.executeService(null, "ReadExecutionYearsService", null);
	}

}