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

    public abstract IPersistentProject getIPersistentProject() throws ExcepcaoPersistencia;

    public abstract IPersistentProjectUser getIPersistentProjectUser() throws ExcepcaoPersistencia;

    public abstract IPersistentRubric getIPersistentRubric() throws ExcepcaoPersistencia;

    public abstract IPersistentExpensesReport getIPersistentExpensesReport() throws ExcepcaoPersistencia;

    public abstract IPersistentReport getIPersistentRevenueReport() throws ExcepcaoPersistencia;

    public abstract IPersistentSummaryReport getIPersistentSummaryReport() throws ExcepcaoPersistencia;

    public abstract IPersistentExpensesResume getIPersistentExpensesResume() throws ExcepcaoPersistencia;

    public abstract IPersistentReport getIPersistentMovementReport() throws ExcepcaoPersistencia;

    public abstract IPersistentExpensesReport getIPersistentCompleteExpensesReport() throws ExcepcaoPersistencia;

    public abstract IPersistentOpeningProjectFileReport getIPersistentOpeningProjectFileReport() throws ExcepcaoPersistencia;

    public abstract IPersistentReport getIPersistentProjectMemberBudget() throws ExcepcaoPersistencia;

    public abstract IPersistentReport getIPersistentProjectBudgetaryBalanceReport() throws ExcepcaoPersistencia;

    public abstract IPersistentReport getIPersistentGeneratedOverheadsReport() throws ExcepcaoPersistencia;

    public abstract IPersistentReport getIPersistentTransferedOverheadsReport() throws ExcepcaoPersistencia;

    public abstract IPersistentReport getIPersistentOverheadsSummaryReport() throws ExcepcaoPersistencia;

}