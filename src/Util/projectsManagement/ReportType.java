/*
 * Created on Jan 12, 2005
 *
 */
package Util.projectsManagement;

import Util.FenixUtil;

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

    public static final String REVENUE_STRING = "revenueReport";

    public static final String EXPENSES_STRING = "expensesReport";

    public static final String SUMMARY_STRING = "summaryReport";

    public static final String SUMMARY_PTE_STRING = "summary_PTE";

    public static final String SUMMARY_EUR_STRING = "summary_EUR";

    public static final String SUMMARY_ADIANTAMENTOS_STRING = "summaryAdiantamentosReport";

    public static final String SUMMARY_CABIMENTOS_STRING = "summaryCabimentosReport";

    public static final String CABIMENTOS_STRING = "cabimentosReport";

    public static final String ADIANTAMENTOS_STRING = "adiantamentosReport";

    public static final String REVENUE_LABEL = "Listagem de Receita em Euros";

    public static final String EXPENSES_LABEL = "Listagem de Despesa em Euros";

    public static final String SUMMARY_LABEL = "Resumo por Coordenador";

    public static final String CABIMENTOS_LABEL = "Listagem de Cabimentos";

    public static final String ADIANTAMENTOS_LABEL = "Listagem de Adiantamentos";

    public static final String REVENUE_NOTE = "Nota: Nos movimentos com tipo DE (Devoluções) e ES (Estorno), embora o valor indicado no movimento seja positivo, eles diminuem o total das despesas. Se quiser usar estes dados para calcular totais efectivos de despesas fazendo somas na coluna total, deve considerar os campos DE e ES como valores negativos.";

    public static final String EXPENSES_NOTE = REVENUE_NOTE, CABIMENTOS_NOTE = REVENUE_NOTE, ADIANTAMENTOS_NOTE = REVENUE_NOTE;

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
