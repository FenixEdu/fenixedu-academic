/*
 * Created on 28/Jul/2003
 *
 */
package ServidorAplicacao.strategy.groupEnrolment.strategys;

import Dominio.IGroupProperties;

/**
 * @author scpo and asnr
 *  
 */
public interface IGroupEnrolmentStrategyFactory {
    public IGroupEnrolmentStrategy getGroupEnrolmentStrategyInstance(IGroupProperties groupProperties);

}