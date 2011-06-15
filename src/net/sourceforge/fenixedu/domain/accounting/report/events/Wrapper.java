package net.sourceforge.fenixedu.domain.accounting.report.events;

import net.sourceforge.fenixedu.domain.ExecutionYear;

public interface Wrapper {
    public static final String PHD_PROGRAM_STUDIES = "Programa Doutoral";
    public static final String REGISTRATION_STUDIES = "Curso";

    public String getStudentNumber();

    public String getStudentName();

    public String getRegistrationStartDate();

    public String getExecutionYear();

    public String getDegreeName();

    public String getDegreeType();

    public String getPhdProgramName();

    public String getEnrolledECTS();

    public String getRegime();

    public String getEnrolmentModel();

    public String getResidenceYear();

    public String getResidenceMonth();

    public String getStudiesType();
    
    public String getTotalDiscount();

    public boolean isAfterOrEqualExecutionYear(final ExecutionYear executionYear);
}
