/*
 * Created on 28/Jul/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys;

import net.sourceforge.fenixedu.domain.IGroupProperties;
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

    public IGroupEnrolmentStrategy getGroupEnrolmentStrategyInstance(IGroupProperties groupProperties) {

        IGroupEnrolmentStrategy strategyInstance = null;
        EnrolmentGroupPolicyType policy = groupProperties.getEnrolmentPolicy();

        if (policy == null)
            throw new IllegalArgumentException("Must initialize Group Properties!");

        if (policy.equals(new EnrolmentGroupPolicyType(1))) {
            strategyInstance = new AtomicGroupEnrolmentStrategy();
        } else if (policy.equals(new EnrolmentGroupPolicyType(2))) {
            strategyInstance = new IndividualGroupEnrolmentStrategy();
        }
        return strategyInstance;
    }

}