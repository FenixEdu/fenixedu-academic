/*
 * Created on 28/Jul/2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ServidorAplicacao.strategy.enrolmentGroupPolicy.strategys;

import Dominio.IGroupProperties;

/**
 * @author scpo and asnr
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public interface IEnrolmentGroupPolicyStrategyFactory {
	public IEnrolmentGroupPolicyStrategy getEnrolmentGroupPolicyStrategyInstance(IGroupProperties groupProperties);
		
}
