/*
 * Created on Jan 12, 2005
 *
 */
package net.sourceforge.fenixedu.util.projectsManagement;

import net.sourceforge.fenixedu.util.FenixUtil;

/**
 * @author Susana Fernandes
 * 
 */
public class ReportType extends FenixUtil {
    public static final int REVENUE_TYPE = 2;

    // public static final int SALDO = 4;
    public static final int EXPENSES_TYPE = 5;

    public static final int SUMMARY_PTE_TYPE = 6;

    public static final int SUMMARY_EUR_TYPE = 7;

    public static final int SUMMARY_ADIANTAMENTOS_TYPE = 8;

    public static final int SUMMARY_CABIMENTOS_TYPE = 9;

    public static final int SUMMARY_TYPE = 10;

    // public static final int OVERHEADS_GERADOS = 11;
    // public static final int OVERHEADS_TRANSFERIDOS = 12;
    // public static final int OVERHEADS_RESUMO = 13;
    // public static final int OVERHEADS_RESUMO_CD = 14;

    public static final int CABIMENTOS_TYPE = 15;

    public static final int ADIANTAMENTOS_TYPE = 16;

    public static final int COMPLETE_EXPENSES_TYPE = 17;

    public static final int OPENING_PROJECT_FILE_TYPE = 18;

    public static final int PROJECT_FINANCIAL_ENTITIES_TYPE = 19;

    public static final int PROJECT_RUBRIC_BUDGET_TYPE = 20;

    public static final int PROJECT_INVESTIGATION_TEAM_TYPE = 21;

    public static final int PROJECT_MEMBERS_TYPE = 22;

    public static final int RUBRIC_BUDGET_MEMBER_TYPE = 23;

    public static final int PROJECT_BUDGETARY_BALANCE_TYPE = 24;

    // public static final int COORDINATOR_BUDGETARY_BALANCE_TYPE = 25;

    public static final String REVENUE_STRING = "revenueReport";

    public static final String EXPENSES_STRING = "expensesReport";

    public static final String SUMMARY_STRING = "summaryReport";

    public static final String SUMMARY_PTE_STRING = "summary_PTE";

    public static final String SUMMARY_EUR_STRING = "summary_EUR";

    public static final String SUMMARY_ADIANTAMENTOS_STRING = "summaryAdiantamentosReport";

    public static final String SUMMARY_CABIMENTOS_STRING = "summaryCabimentosReport";

    public static final String CABIMENTOS_STRING = "cabimentosReport";

    public static final String ADIANTAMENTOS_STRING = "adiantamentosReport";

    public static final String COMPLETE_EXPENSES_STRING = "completeExpensesReport";

    public static final String OPENING_PROJECT_FILE_STRING = "openingProjectFileReport";

    public static final String PROJECT_BUDGETARY_BALANCE_STRING = "projectBudgetaryBalanceReport";

    // public static final String COORDINATOR_BUDGETARY_BALANCE_STRING =
    // "coordinatorBudgetaryBalanceReport";

    public static final String REVENUE_LABEL = "Listagem de Receita em Euros";

    public static final String EXPENSES_LABEL = "Listagem de Despesa em Euros";

    public static final String SUMMARY_LABEL = "Resumo por Coordenador";

    public static final String CABIMENTOS_LABEL = "Listagem de Cabimentos";

    public static final String ADIANTAMENTOS_LABEL = "Listagem de Adiantamentos";

    public static final String COMPLETE_EXPENSES_LABEL = "Listagem de Despesas Detalhada";

    public static final String OPENING_PROJECT_FILE_LABEL = "Ficha de Abertura de Projecto";

    public static final String PROJECT_BUDGETARY_BALANCE_LABEL = "Saldo Orçamental por Rubrica";

    // public static final String COORDINATOR_BUDGETARY_BALANCE_LABEL = "Saldo
    // Orçamental por Coordenador";

    public static final String REVENUE_NOTE = "Nota: Nos movimentos com tipo DE (Devoluções) e ES (Estorno), embora o valor indicado no movimento seja positivo, eles diminuem o total das despesas. Se quiser usar estes dados para calcular totais efectivos de despesas fazendo somas na coluna total, deve considerar os campos DE e ES como valores negativos.";

    public static final String EXPENSES_NOTE = REVENUE_NOTE, CABIMENTOS_NOTE = REVENUE_NOTE, ADIANTAMENTOS_NOTE = REVENUE_NOTE,
            COMPLETE_EXPENSES_NOTE = REVENUE_NOTE;

    public static final String SUMMARY_NOTE = "(*) O Saldo Orçamental é calculado com base no valor da coluna Máximo Financiável";

    public static final ReportType REVENUE = new ReportType(REVENUE_TYPE);

    public static final ReportType EXPENSES = new ReportType(EXPENSES_TYPE);

    public static final ReportType SUMMARY = new ReportType(SUMMARY_TYPE);

    public static final ReportType SUMMARY_PTE = new ReportType(SUMMARY_PTE_TYPE);

    public static final ReportType SUMMARY_EUR = new ReportType(SUMMARY_EUR_TYPE);

    public static final ReportType SUMMARY_ADIANTAMENTOS = new ReportType(SUMMARY_ADIANTAMENTOS_TYPE);

    public static final ReportType SUMMARY_CABIMENTOS = new ReportType(SUMMARY_CABIMENTOS_TYPE);

    public static final ReportType CABIMENTOS = new ReportType(CABIMENTOS_TYPE);

    public static final ReportType ADIANTAMENTOS = new ReportType(ADIANTAMENTOS_TYPE);

    public static final ReportType COMPLETE_EXPENSES = new ReportType(COMPLETE_EXPENSES_TYPE);

    public static final ReportType OPENING_PROJECT_FILE = new ReportType(OPENING_PROJECT_FILE_TYPE);

    public static final ReportType PROJECT_FINANCIAL_ENTITIES = new ReportType(PROJECT_FINANCIAL_ENTITIES_TYPE);

    public static final ReportType PROJECT_RUBRIC_BUDGET = new ReportType(PROJECT_RUBRIC_BUDGET_TYPE);

    public static final ReportType PROJECT_INVESTIGATION_TEAM = new ReportType(PROJECT_INVESTIGATION_TEAM_TYPE);

    public static final ReportType PROJECT_MEMBERS = new ReportType(PROJECT_MEMBERS_TYPE);

    public static final ReportType RUBRIC_BUDGET_MEMBER = new ReportType(RUBRIC_BUDGET_MEMBER_TYPE);

    public static final ReportType PROJECT_BUDGETARY_BALANCE = new ReportType(PROJECT_BUDGETARY_BALANCE_TYPE);

    // public static final ReportType COORDINATOR_BUDGETARY_BALANCE = new
    // ReportType(COORDINATOR_BUDGETARY_BALANCE_TYPE);

    private Integer reportType;

    public ReportType(int reportType) {
        this.reportType = new Integer(reportType);
    }

    public ReportType(Integer reportType) {
        this.reportType = reportType;
    }

    public ReportType(String reportType) {
        if (reportType.equals(REVENUE_STRING))
            this.reportType = new Integer(REVENUE_TYPE);
        else if (reportType.equals(EXPENSES_STRING))
            this.reportType = new Integer(EXPENSES_TYPE);
        else if (reportType.equals(SUMMARY_PTE_STRING))
            this.reportType = new Integer(SUMMARY_PTE_TYPE);
        else if (reportType.equals(SUMMARY_EUR_STRING))
            this.reportType = new Integer(SUMMARY_EUR_TYPE);
        else if (reportType.equals(SUMMARY_ADIANTAMENTOS_STRING))
            this.reportType = new Integer(SUMMARY_ADIANTAMENTOS_TYPE);
        else if (reportType.equals(SUMMARY_CABIMENTOS_STRING))
            this.reportType = new Integer(SUMMARY_CABIMENTOS_TYPE);
        else if (reportType.equals(SUMMARY_STRING))
            this.reportType = new Integer(SUMMARY_TYPE);
        else if (reportType.equals(CABIMENTOS_STRING))
            this.reportType = new Integer(CABIMENTOS_TYPE);
        else if (reportType.equals(ADIANTAMENTOS_STRING))
            this.reportType = new Integer(ADIANTAMENTOS_TYPE);
        else if (reportType.equals(COMPLETE_EXPENSES_STRING))
            this.reportType = new Integer(COMPLETE_EXPENSES_TYPE);
        else if (reportType.equals(OPENING_PROJECT_FILE_STRING))
            this.reportType = new Integer(OPENING_PROJECT_FILE_TYPE);
        else if (reportType.equals(PROJECT_BUDGETARY_BALANCE_STRING))
            this.reportType = new Integer(PROJECT_BUDGETARY_BALANCE_TYPE);
        // else if (reportType.equals(COORDINATOR_BUDGETARY_BALANCE_STRING))
        // this.reportType = new Integer(COORDINATOR_BUDGETARY_BALANCE_TYPE);
        else
            this.reportType = null;
    }

    public static ReportType getReportType(Integer reportType) {
        return getReportType(reportType.intValue());
    }

    public static ReportType getReportType(int reportType) {
        if (reportType == REVENUE_TYPE)
            return REVENUE;
        if (reportType == EXPENSES_TYPE)
            return EXPENSES;
        if (reportType == SUMMARY_PTE_TYPE)
            return SUMMARY_PTE;
        if (reportType == SUMMARY_EUR_TYPE)
            return SUMMARY_EUR;
        if (reportType == SUMMARY_ADIANTAMENTOS_TYPE)
            return SUMMARY_ADIANTAMENTOS;
        if (reportType == SUMMARY_CABIMENTOS_TYPE)
            return SUMMARY_CABIMENTOS;
        if (reportType == SUMMARY_TYPE)
            return SUMMARY;
        if (reportType == CABIMENTOS_TYPE)
            return CABIMENTOS;
        if (reportType == ADIANTAMENTOS_TYPE)
            return ADIANTAMENTOS;
        if (reportType == COMPLETE_EXPENSES_TYPE)
            return COMPLETE_EXPENSES;
        if (reportType == OPENING_PROJECT_FILE_TYPE)
            return OPENING_PROJECT_FILE;
        if (reportType == PROJECT_BUDGETARY_BALANCE_TYPE)
            return PROJECT_BUDGETARY_BALANCE;
        // if (reportType == COORDINATOR_BUDGETARY_BALANCE_TYPE)
        // return COORDINATOR_BUDGETARY_BALANCE;
        return null;
    }

    public String getReportTypeString() {
        if (reportType.intValue() == REVENUE_TYPE)
            return REVENUE_STRING;
        if (reportType.intValue() == EXPENSES_TYPE)
            return EXPENSES_STRING;
        if (reportType.intValue() == SUMMARY_PTE_TYPE)
            return SUMMARY_PTE_STRING;
        if (reportType.intValue() == SUMMARY_EUR_TYPE)
            return SUMMARY_EUR_STRING;
        if (reportType.intValue() == SUMMARY_ADIANTAMENTOS_TYPE)
            return SUMMARY_ADIANTAMENTOS_STRING;
        if (reportType.intValue() == SUMMARY_CABIMENTOS_TYPE)
            return SUMMARY_CABIMENTOS_STRING;
        if (reportType.intValue() == SUMMARY_TYPE)
            return SUMMARY_STRING;
        if (reportType.intValue() == CABIMENTOS_TYPE)
            return CABIMENTOS_STRING;
        if (reportType.intValue() == ADIANTAMENTOS_TYPE)
            return ADIANTAMENTOS_STRING;
        if (reportType.intValue() == COMPLETE_EXPENSES_TYPE)
            return COMPLETE_EXPENSES_STRING;
        if (reportType.intValue() == OPENING_PROJECT_FILE_TYPE)
            return OPENING_PROJECT_FILE_STRING;
        if (reportType.intValue() == PROJECT_BUDGETARY_BALANCE_TYPE)
            return PROJECT_BUDGETARY_BALANCE_STRING;
        // if (reportType.intValue() == COORDINATOR_BUDGETARY_BALANCE_TYPE)
        // return COORDINATOR_BUDGETARY_BALANCE_STRING;
        return null;
    }

    public String getReportLabel() {
        if (reportType.intValue() == REVENUE_TYPE)
            return REVENUE_LABEL;
        else if (reportType.intValue() == EXPENSES_TYPE)
            return EXPENSES_LABEL;
        else if (reportType.intValue() == SUMMARY_TYPE)
            return SUMMARY_LABEL;
        else if (reportType.intValue() == CABIMENTOS_TYPE)
            return CABIMENTOS_LABEL;
        else if (reportType.intValue() == ADIANTAMENTOS_TYPE)
            return ADIANTAMENTOS_LABEL;
        else if (reportType.intValue() == COMPLETE_EXPENSES_TYPE)
            return COMPLETE_EXPENSES_LABEL;
        else if (reportType.intValue() == OPENING_PROJECT_FILE_TYPE)
            return OPENING_PROJECT_FILE_LABEL;
        else if (reportType.intValue() == PROJECT_BUDGETARY_BALANCE_TYPE)
            return PROJECT_BUDGETARY_BALANCE_LABEL;
        // else if (reportType.intValue() == COORDINATOR_BUDGETARY_BALANCE_TYPE)
        // return COORDINATOR_BUDGETARY_BALANCE_LABEL;
        return null;
    }

    public String getReportNote() {
        if (reportType.intValue() == REVENUE_TYPE)
            return REVENUE_NOTE;
        else if (reportType.intValue() == EXPENSES_TYPE)
            return EXPENSES_NOTE;
        else if (reportType.intValue() == SUMMARY_TYPE)
            return SUMMARY_NOTE;
        else if (reportType.intValue() == CABIMENTOS_TYPE)
            return CABIMENTOS_NOTE;
        else if (reportType.intValue() == ADIANTAMENTOS_TYPE)
            return ADIANTAMENTOS_NOTE;
        else if (reportType.intValue() == COMPLETE_EXPENSES_TYPE)
            return COMPLETE_EXPENSES_NOTE;
        return null;
    }

    public Integer getReportType() {
        return reportType;
    }

    public void setReportType(Integer reportType) {
        this.reportType = reportType;
    }

    public boolean equals(Object obj) {
        if (obj instanceof ReportType)
            return reportType.equals(((ReportType) obj).getReportType());
        return false;
    }
}
