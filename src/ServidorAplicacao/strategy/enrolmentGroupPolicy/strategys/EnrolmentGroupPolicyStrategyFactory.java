/*
 * Created on 28/Jul/2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ServidorAplicacao.strategy.enrolmentGroupPolicy.strategys;

import Dominio.IGroupProperties;
import Util.EnrolmentGroupPolicyType;

/**
 * @author asnr and scpo
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */

public class EnrolmentGroupPolicyStrategyFactory implements IEnrolmentGroupPolicyStrategyFactory{
	//public class EnrolmentStrategyFactory implements IEnrolmentStrategyFactory {

		private static EnrolmentGroupPolicyStrategyFactory instance = null;

		private EnrolmentGroupPolicyStrategyFactory() {
		}

		public static synchronized EnrolmentGroupPolicyStrategyFactory getInstance() {
			if (instance == null) {
				instance = new EnrolmentGroupPolicyStrategyFactory();
			}
			return instance;
		}

		public static synchronized void resetInstance() {
			if (instance != null) {
				instance = null;
			}
		}



		public IEnrolmentGroupPolicyStrategy getEnrolmentGroupPolicyStrategyInstance(IGroupProperties groupProperties) {
		
			IEnrolmentGroupPolicyStrategy strategyInstance = null;
			EnrolmentGroupPolicyType policy = groupProperties.getEnrolmentPolicy();
			
			if (policy == null)
				throw new IllegalArgumentException("Must initialize Group Properties!");


			if (policy.equals(new EnrolmentGroupPolicyType(1))){
				strategyInstance = new AtomicGroupStrategy();
			} 
			else 
				if (policy.equals(new EnrolmentGroupPolicyType(2))){
					strategyInstance = new IndividualGroupStrategy(); 
				}  
				return strategyInstance;
			}

}
