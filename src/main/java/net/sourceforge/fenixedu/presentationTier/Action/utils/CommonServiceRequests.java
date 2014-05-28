/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on Apr 3, 2004
 *
 */
package net.sourceforge.fenixedu.presentationTier.Action.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.commons.ReadExecutionDegreeByOID;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.ReadExecutionYearsService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.ReadBranchesByDegreeCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoBranch;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.domain.branch.BranchType;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;

import org.fenixedu.bennu.core.domain.User;

/**
 * @author Luis Cruz
 * 
 */
public class CommonServiceRequests {

    public static List getBranchesByDegreeCurricularPlan(User userView, String degreeCurricularPlanOID)
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
    public static InfoExecutionDegree getInfoExecutionDegree(User userView, String degreeOID) {
        InfoExecutionDegree infoExecutionDegree = null;

        infoExecutionDegree = ReadExecutionDegreeByOID.run(degreeOID);
        return infoExecutionDegree;
    }

    public static List<InfoExecutionYear> getInfoExecutionYears() throws FenixServiceException {
        return ReadExecutionYearsService.run();
    }

}