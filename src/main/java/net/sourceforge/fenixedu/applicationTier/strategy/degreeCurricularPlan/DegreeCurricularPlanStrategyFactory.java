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
package net.sourceforge.fenixedu.applicationTier.strategy.degreeCurricularPlan;

import net.sourceforge.fenixedu.applicationTier.strategy.degreeCurricularPlan.strategys.DegreeCurricularPlanStrategy;
import net.sourceforge.fenixedu.applicationTier.strategy.degreeCurricularPlan.strategys.IDegreeCurricularPlanStrategy;
import net.sourceforge.fenixedu.applicationTier.strategy.degreeCurricularPlan.strategys.MasterDegreeCurricularPlanStrategy;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * @author Joana Mota (jccm@rnl.ist.utl.pt)
 */

public class DegreeCurricularPlanStrategyFactory implements IDegreeCurricularPlanStrategyFactory {

    private static DegreeCurricularPlanStrategyFactory instance = null;

    private DegreeCurricularPlanStrategyFactory() {
    }

    public static synchronized DegreeCurricularPlanStrategyFactory getInstance() {
        if (instance == null) {
            instance = new DegreeCurricularPlanStrategyFactory();
        }
        return instance;
    }

    public static synchronized void resetInstance() {
        if (instance != null) {
            instance = null;
        }
    }

    @Override
    public IDegreeCurricularPlanStrategy getDegreeCurricularPlanStrategy(DegreeCurricularPlan degreeCurricularPlan) {

        IDegreeCurricularPlanStrategy strategyInstance = null;

        if (degreeCurricularPlan == null) {
            throw new IllegalArgumentException("Must initialize Degree Curricular Plan!");
        }

        if (degreeCurricularPlan.getDegree().getDegreeType().equals(DegreeType.DEGREE)) {
            strategyInstance = new DegreeCurricularPlanStrategy(degreeCurricularPlan);
        } else if (degreeCurricularPlan.getDegree().getDegreeType().equals(DegreeType.MASTER_DEGREE)) {
            strategyInstance = new MasterDegreeCurricularPlanStrategy(degreeCurricularPlan);
        }
        return strategyInstance;
    }

}