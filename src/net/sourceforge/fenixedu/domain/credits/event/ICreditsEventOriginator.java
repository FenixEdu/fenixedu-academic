/*
 * Created on Sep 16, 2004
 */
package net.sourceforge.fenixedu.domain.credits.event;

import net.sourceforge.fenixedu.domain.ExecutionPeriod;

public interface ICreditsEventOriginator {
    public boolean belongsToExecutionPeriod(ExecutionPeriod executionPeriod);
}