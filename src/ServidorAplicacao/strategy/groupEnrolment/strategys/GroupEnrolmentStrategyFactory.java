/*
 * Created on 28/Jul/2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ServidorAplicacao.strategy.groupEnrolment.strategys;

import Dominio.IGroupProperties;
import Util.EnrolmentGroupPolicyType;

/**
 * @author asnr and scpo
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */

public class GroupEnrolmentStrategyFactory implements IGroupEnrolmentStrategyFactory{
	//public class EnrolmentStrategyFactory implements IEnrolmentStrategyFactory {

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


			if (policy.equals(new EnrolmentGroupPolicyType(1))){
				strategyInstance = new AtomicGroupEnrolmentStrategy();
			} 
			else 
				if (policy.equals(new EnrolmentGroupPolicyType(2))){
					strategyInstance = new IndividualGroupEnrolmentStrategy(); 
				}  
				return strategyInstance;
			}

}
