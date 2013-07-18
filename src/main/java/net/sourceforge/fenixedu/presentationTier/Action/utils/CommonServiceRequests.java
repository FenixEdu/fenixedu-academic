/*
 * Created on Apr 3, 2004
 *
 */
package net.sourceforge.fenixedu.presentationTier.Action.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.ReadExecutionDegreeByOID;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.ReadExecutionYearsService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.ReadBranchesByDegreeCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoBranch;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.domain.branch.BranchType;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;

/**
 * @author Luis Cruz
 * 
 */
public class CommonServiceRequests {

    public static List getBranchesByDegreeCurricularPlan(IUserView userView, Integer degreeCurricularPlanOID)
            throws FenixActionException {

        List branches = null;
        try {
            branches = ReadBranchesByDegreeCurricularPlan.run(degreeCurricularPlanOID);
        } catch (FenixServiceException fse) {
            throw new FenixActionException(fse);
        }

        List newBranches = new ArrayList();
        if (branches != null) {
            Iterator iterator = branches.iterator();
            while (iterator.hasNext()) {
                InfoBranch infoBranch = (InfoBranch) iterator.next();
                if (infoBranch != null && infoBranch.getBranchType() != null
                        && !infoBranch.getBranchType().equals(BranchType.COMNBR)) {
                    newBranches.add(infoBranch);
                }
            }
        }
        return newBranches;
    }

    /**
     * @param degreeOID
     * @return
     */
    public static InfoExecutionDegree getInfoExecutionDegree(IUserView userView, Integer degreeOID) {
        InfoExecutionDegree infoExecutionDegree = null;

        infoExecutionDegree = ReadExecutionDegreeByOID.run(degreeOID);
        return infoExecutionDegree;
    }

    public static List<InfoExecutionYear> getInfoExecutionYears() throws FenixServiceException {
        return ReadExecutionYearsService.run();
    }

}