/*
 * Created on Jan 11, 2005
 *
 */
package net.sourceforge.fenixedu.persistenceTierOracle;

import java.sql.PreparedStatement;

import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Susana Fernandes
 * 
 */
public interface IPersistentSuportOracle {
    public abstract void startTransaction() throws ExcepcaoPersistencia;

    public abstract void commitTransaction() throws ExcepcaoPersistencia;

    public abstract void cancelTransaction() throws ExcepcaoPersistencia;

    public abstract PreparedStatement prepareStatement(String statement) throws ExcepcaoPersistencia;

    public abstract IPersistentProject getIPersistentProject();

    public abstract IPersistentProjectUser getIPersistentProjectUser();

    public abstract IPersistentRubric getIPersistentRubric();

    public abstract IPersistentExpensesReport getIPersistentExpensesReport();

    public abstract IPersistentReport getIPersistentRevenueReport();

    public abstract IPersistentSummaryReport getIPersistentSummaryReport();

    public abstract IPersistentReport getIPersistentAdiantamentosReport();

    public abstract IPersistentReport getIPersistentCabimentosReport();

    public abstract IPersistentReport getIPersistentSummaryPTEReport();

    public abstract IPersistentReport getIPersistentSummaryEURReport();

    public abstract IPersistentReport getIPersistentMovementReport();

    public abstract IPersistentExpensesReport getIPersistentCompleteExpensesReport();

    public abstract IPersistentOpeningProjectFileReport getIPersistentOpeningProjectFileReport();

    public abstract IPersistentReport getIPersistentProjectMemberBudget();

    public abstract IPersistentReport getIPersistentProjectBudgetaryBalanceReport();
}