/*
 * Created on Sep 16, 2004
 */
package Dominio.credits.event;

import Dominio.IExecutionPeriod;



public interface ICreditsEventOriginator {
    public boolean belongsToExecutionPeriod(IExecutionPeriod executionPeriod);
}
