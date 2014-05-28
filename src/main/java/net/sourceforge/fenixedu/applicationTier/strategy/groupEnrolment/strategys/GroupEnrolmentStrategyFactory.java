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
 * Created on 28/Jul/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys;

import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.util.EnrolmentGroupPolicyType;

/**
 * @author asnr and scpo
 * 
 */

public class GroupEnrolmentStrategyFactory implements IGroupEnrolmentStrategyFactory {

    private static GroupEnrolmentStrategyFactory instance = null;

    private GroupEnrolmentStrategyFactory() {
    }

    public static synchronized GroupEnrolmentStrategyFactory getInstance() {
        if (instance == null) {
            instance = new GroupEnrolmentStrategyFactory();
        }
        return instance;
    }

    public static synchronized void resetInstance() {
        if (instance != null) {
            instance = null;
        }
    }

    @Override
    public IGroupEnrolmentStrategy getGroupEnrolmentStrategyInstance(Grouping grouping) {

        IGroupEnrolmentStrategy strategyInstance = null;
        EnrolmentGroupPolicyType policy = grouping.getEnrolmentPolicy();

        if (policy == null) {
            throw new IllegalArgumentException("Must initialize Group Properties!");
        }

        if (policy.equals(new EnrolmentGroupPolicyType(1))) {
            strategyInstance = new AtomicGroupEnrolmentStrategy();
        } else if (policy.equals(new EnrolmentGroupPolicyType(2))) {
            strategyInstance = new IndividualGroupEnrolmentStrategy();
        }
        return strategyInstance;
    }

}