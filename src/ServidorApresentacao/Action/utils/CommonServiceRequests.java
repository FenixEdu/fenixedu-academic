/*
 * Created on Apr 3, 2004
 *
 */
package ServidorApresentacao.Action.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.InfoBranch;
import DataBeans.InfoExecutionDegree;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import Util.BranchType;

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
	public static List getBranchesByDegreeCurricularPlan(
		IUserView userView,
		Integer degreeCurricularPlanOID)
		throws FenixActionException {
		Object[] argsBranches = { degreeCurricularPlanOID };
		List branches = null;
		try {
			branches =
				(List) ServiceUtils.executeService(
					userView,
					"ReadBranchesByDegreeCurricularPlanId",
					argsBranches);
		} catch (FenixServiceException fse) {
			throw new FenixActionException(fse);
		}

		ArrayList newBranches = new ArrayList();
		Iterator iterator = branches.iterator();
		while (iterator.hasNext()) {
			InfoBranch infoBranch = (InfoBranch) iterator.next();
			if (infoBranch != null
				&& infoBranch.getBranchType() != null
				&& !infoBranch.getBranchType().equals(BranchType.COMMON_BRANCH))
				newBranches.add(infoBranch);
		}
		return newBranches;
	}

	/**
	 * @param degreeOID
	 * @return
	 */
	public static InfoExecutionDegree getInfoExecutionDegree(
		IUserView userView,
		Integer degreeOID)
		throws FenixActionException {
		InfoExecutionDegree infoExecutionDegree = null;
		Object[] args = { degreeOID };
		try {
			infoExecutionDegree =
				(InfoExecutionDegree) ServiceUtils.executeService(
					userView,
					"ReadExecutionDegreeByOID",
					args);
		} catch (FenixServiceException fse) {
			throw new FenixActionException(fse);
		}
		return infoExecutionDegree;
	}

}
