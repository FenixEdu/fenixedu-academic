package net.sourceforge.fenixedu.persistenceTierOracle.Oracle;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.DecimalFormat;
import java.util.Calendar;

import net.sourceforge.fenixedu.applicationTier.Servico.assiduousness.ExportClosedExtraWorkMonth;
import net.sourceforge.fenixedu.dataTransferObject.assiduousness.YearMonth;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.assiduousness.Assiduousness;
import net.sourceforge.fenixedu.domain.assiduousness.ExtraWorkRequest;
import net.sourceforge.fenixedu.domain.exceptions.InvalidGiafCodeException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import oracle.jdbc.OracleTypes;

import org.joda.time.DateTimeFieldType;
import org.joda.time.LocalDate;
import org.joda.time.Partial;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class GiafInterface {

    private static DecimalFormat employeeNumberFormat = new DecimalFormat("000000");

    public BigDecimal getEmployeeHourValue(Employee employee, LocalDate day) throws ExcepcaoPersistencia {
	PersistentSuportGiaf persistentSuportOracle = PersistentSuportGiaf.getInstance();
	CallableStatement callableStatement = null;
	try {
	    callableStatement = persistentSuportOracle.prepareCall("BEGIN ?:=ist_valor_hora(?, ?, ? ,?); END;");
	    callableStatement.registerOutParameter(1, Types.DOUBLE);
	    callableStatement.setString(2, employeeNumberFormat.format(employee.getEmployeeNumber()));
	    callableStatement.setDate(3, new Date(day.toDateTimeAtStartOfDay().getMillis()));
	    callableStatement.registerOutParameter(4, Types.DOUBLE);
	    callableStatement.registerOutParameter(5, Types.VARCHAR);
	    callableStatement.executeQuery();
	    if (callableStatement.getString(5) == null) {
		BigDecimal result = callableStatement.getBigDecimal(4);
		LocalDate today = new LocalDate();
		return result.equals(BigDecimal.ZERO) && !day.equals(today) ? getEmployeeHourValue(employee, today) : result;
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	    throw new ExcepcaoPersistencia();
	} finally {
	    if (callableStatement != null) {
		try {
		    callableStatement.close();
		} catch (SQLException e) {
		    e.printStackTrace();
		}
	    }
	    persistentSuportOracle.closeConnection();
	}
	return null;
    }

    // public BigDecimal getEmployeeHourValue(Employee employee, LocalDate day)
    // throws ExcepcaoPersistencia {
    // BigDecimal salary = getEmployeeSalary(employee, day);
    // if (salary != null) {
    // BigDecimal salaryInYear = salary.multiply(new BigDecimal(12));
    // BigDecimal hoursInYear = new BigDecimal(52 * 35);
    // return salaryInYear.divide(hoursInYear, 2, BigDecimal.ROUND_HALF_UP);
    // }
    // return null;
    // }

    public BigDecimal getEmployeeSalary(Employee employee, LocalDate day) throws ExcepcaoPersistencia {
	BigDecimal salary = new BigDecimal(0.0);
	DateTimeFormatter fmt = DateTimeFormat.forPattern("dd-MM-yyyy");
	PersistentSuportGiaf persistentSuportOracle = PersistentSuportGiaf.getInstance();
	PreparedStatement stmt = null;
	ResultSet rs = null;
	try {
	    StringBuilder stringBuilder = new StringBuilder();
	    stringBuilder
		    .append("select emp_num, emp_venc, emp_venc_dt from (select a.emp_num, a.emp_venc, a.emp_venc_dt from sldempvenc a where  nvl(a.tipo_alt,'@') != 'A'");
	    stringBuilder
		    .append(" union SELECT c.emp_num, c.emp_venc, c.emp_venc_dt FROM sldemp04 c) where emp_venc_dt < to_date('");
	    stringBuilder.append(fmt.print(day));
	    stringBuilder.append("', 'DD-MM-YYYY') and emp_num='");
	    stringBuilder.append(employeeNumberFormat.format(employee.getEmployeeNumber()));
	    stringBuilder.append("' order by emp_venc_dt desc");

	    stmt = persistentSuportOracle.prepareStatement(stringBuilder.toString());
	    rs = stmt.executeQuery();
	    if (rs.next()) {
		salary = rs.getBigDecimal("emp_venc");
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	    throw new ExcepcaoPersistencia();
	} finally {
	    try {
		if (rs != null) {
		    rs.close();
		}
	    } catch (SQLException e) {
		e.printStackTrace();
	    }
	    try {
		if (stmt != null) {
		    stmt.close();
		}
	    } catch (SQLException e) {
		e.printStackTrace();
	    }
	    persistentSuportOracle.closeConnection();
	}
	LocalDate today = new LocalDate();
	return salary.equals(BigDecimal.ZERO) && !day.equals(today) ? getEmployeeSalary(employee, today) : salary;
    }

    public void updateExtraWorkRequest(ExtraWorkRequest extraWorkRequest) throws ExcepcaoPersistencia {
	PersistentSuportGiaf persistentSuportOracle = PersistentSuportGiaf.getInstance();
	PreparedStatement stmt = null;
	ResultSet rs = null;
	try {
	    stmt = persistentSuportOracle.prepareStatement("SELECT ano, mes FROM sltinfdivs");
	    rs = stmt.executeQuery();
	    Integer year = 0;
	    Integer month = 0;
	    if (rs.next()) {
		year = rs.getInt("ano");
		month = rs.getInt("mes");
	    }
	    rs.close();
	    stmt.close();
	    StringBuilder query = new StringBuilder();
	    YearMonth yearMonthPayingDate = new YearMonth(extraWorkRequest.getPartialPayingDate());
	    yearMonthPayingDate.addMonth();

	    int paymentYear = extraWorkRequest.getHoursDoneInPartialDate().get(DateTimeFieldType.year());
	    int paymentMonth = extraWorkRequest.getHoursDoneInPartialDate().get(DateTimeFieldType.monthOfYear());
	    if (yearMonthPayingDate.getYear().equals(year) && yearMonthPayingDate.getNumberOfMonth() == month) {
		query.append("SELECT a.mov_cod, a.sal_val_brt, a.emp_ccusto FROM sldsalario a where extract(year from data_mov)=");
		query.append(paymentYear);
		query.append(" and extract(month from data_mov)=");
		query.append(paymentMonth);
	    } else {
		query.append("SELECT a.mov_cod,a.sal_val_brt, a.emp_ccusto FROM slhsalario a where a.ano=");
		query.append(yearMonthPayingDate.getYear());
		query.append(" and a.mes=");
		query.append(yearMonthPayingDate.getNumberOfMonth());
		query.append(" and extract(year from data_mov)=");
		query.append(paymentYear);
		query.append(" and extract(month from data_mov)=");
		query.append(paymentMonth);
	    }
	    query.append(" and a.mov_cod in (");
	    query.append(ExportClosedExtraWorkMonth.extraWorkSundayMovementCode).append(",");
	    query.append(ExportClosedExtraWorkMonth.extraWorkSaturdayMovementCode).append(",");
	    query.append(ExportClosedExtraWorkMonth.extraWorkHolidayMovementCode).append(",");
	    query.append(ExportClosedExtraWorkMonth.extraWorkWeekDayFirstLevelMovementCode).append(",");
	    query.append(ExportClosedExtraWorkMonth.extraWorkWeekDaySecondLevelMovementCode).append(",");
	    query.append(ExportClosedExtraWorkMonth.extraNightWorkFirstLevelMovementCode).append(",");
	    query.append(ExportClosedExtraWorkMonth.extraNightWorkSecondLevelMovementCode).append(",");
	    query.append(ExportClosedExtraWorkMonth.extraNightWorkMealMovementCode);
	    query.append(") and a.emp_num =");
	    query.append(extraWorkRequest.getAssiduousness().getEmployee().getEmployeeNumber());

	    boolean hasMoreThanOneCostCenter = hasMoreThanOneCostCenter(extraWorkRequest.getAssiduousness(),
		    extraWorkRequest.getPartialPayingDate());
	    if (hasMoreThanOneCostCenter) {
		query.append(" and emp_ccusto =");
		query.append(extraWorkRequest.getUnit().getCostCenterCode());
	    }
	    System.out.println(query.toString());
	    stmt = persistentSuportOracle.prepareStatement(query.toString());
	    rs = stmt.executeQuery();
	    while (rs.next()) {
		if (hasMoreThanOneCostCenter) {
		    if (extraWorkRequest.getUnit().getCostCenterCode().intValue() != rs.getInt("emp_ccusto")) {
			continue;
		    }
		}
		if (rs.getInt("mov_cod") == new Integer(ExportClosedExtraWorkMonth.extraWorkSundayMovementCode)) {
		    extraWorkRequest.setSundayAmount(rs.getDouble("sal_val_brt"));
		} else if (rs.getInt("mov_cod") == new Integer(ExportClosedExtraWorkMonth.extraWorkSaturdayMovementCode)) {
		    extraWorkRequest.setSaturdayAmount(rs.getDouble("sal_val_brt"));
		} else if (rs.getInt("mov_cod") == new Integer(ExportClosedExtraWorkMonth.extraWorkHolidayMovementCode)) {
		    extraWorkRequest.setHolidayAmount(rs.getDouble("sal_val_brt"));
		} else if (rs.getInt("mov_cod") == new Integer(ExportClosedExtraWorkMonth.extraWorkWeekDayFirstLevelMovementCode)) {
		    extraWorkRequest.setWorkdayFirstLevelAmount(rs.getDouble("sal_val_brt"));
		} else if (rs.getInt("mov_cod") == new Integer(ExportClosedExtraWorkMonth.extraWorkWeekDaySecondLevelMovementCode)) {
		    extraWorkRequest.setWorkdaySecondLevelAmount(rs.getDouble("sal_val_brt"));
		} else if (rs.getInt("mov_cod") == new Integer(ExportClosedExtraWorkMonth.extraNightWorkFirstLevelMovementCode)) {
		    extraWorkRequest.setExtraNightFirstLevelAmount(rs.getDouble("sal_val_brt"));
		} else if (rs.getInt("mov_cod") == new Integer(ExportClosedExtraWorkMonth.extraNightWorkSecondLevelMovementCode)) {
		    extraWorkRequest.setExtraNightSecondLevelAmount(rs.getDouble("sal_val_brt"));
		} else if (rs.getInt("mov_cod") == new Integer(ExportClosedExtraWorkMonth.extraNightWorkMealMovementCode)) {
		    extraWorkRequest.setExtraNightMealAmount(rs.getDouble("sal_val_brt"));
		}
		extraWorkRequest.updateAmount();
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	    throw new ExcepcaoPersistencia();
	} finally {
	    if (rs != null) {
		try {
		    rs.close();
		} catch (SQLException e) {
		    e.printStackTrace();
		}
	    }
	    if (stmt != null) {
		try {
		    stmt.close();
		} catch (SQLException e) {
		    e.printStackTrace();
		}
	    }
	    persistentSuportOracle.closeConnection();
	}
    }

    private boolean hasMoreThanOneCostCenter(Assiduousness assiduousness, Partial paymentPartialDate) {
	Unit unit = null;
	for (ExtraWorkRequest extraWorkRequest : assiduousness.getExtraWorkRequests(paymentPartialDate)) {
	    if (unit == null) {
		unit = extraWorkRequest.getUnit();
	    } else if (!unit.equals(extraWorkRequest.getUnit())) {
		return true;
	    }
	}
	return false;
    }

    public double getTotalMonthAmount(Partial closedYearMonth) throws ExcepcaoPersistencia {
	PersistentSuportGiaf persistentSuportOracle = PersistentSuportGiaf.getInstance();
	PreparedStatement stmt = null;
	ResultSet rs = null;
	try {
	    stmt = persistentSuportOracle.prepareStatement("SELECT ano, mes FROM sltinfdivs");
	    rs = stmt.executeQuery();
	    Integer year = 0;
	    Integer month = 0;
	    if (rs.next()) {
		year = rs.getInt("ano");
		month = rs.getInt("mes");
	    }
	    rs.close();
	    stmt.close();
	    YearMonth yearMonth = new YearMonth(closedYearMonth);
	    yearMonth.addMonth();
	    StringBuilder query = new StringBuilder();
	    if (yearMonth.getYear().equals(year) && yearMonth.getNumberOfMonth() == month) {
		query.append("SELECT sum(a.sal_val_brt) as value FROM sldsalario a where a.mov_cod in (");
		query.append(ExportClosedExtraWorkMonth.extraWorkSundayMovementCode).append(",");
		query.append(ExportClosedExtraWorkMonth.extraWorkSaturdayMovementCode).append(",");
		query.append(ExportClosedExtraWorkMonth.extraWorkHolidayMovementCode).append(",");
		query.append(ExportClosedExtraWorkMonth.extraWorkWeekDayFirstLevelMovementCode).append(",");
		query.append(ExportClosedExtraWorkMonth.extraWorkWeekDaySecondLevelMovementCode).append(",");
		query.append(ExportClosedExtraWorkMonth.extraNightWorkFirstLevelMovementCode).append(",");
		query.append(ExportClosedExtraWorkMonth.extraNightWorkSecondLevelMovementCode).append(",");
		query.append(ExportClosedExtraWorkMonth.extraNightWorkMealMovementCode);
		query.append(") and a.ano_pag=");
		query.append(yearMonth.getYear());
		query.append(" and a.mes_pag=");
		query.append(yearMonth.getNumberOfMonth());
	    } else {
		query.append("SELECT sum(a.sal_val_brt) as value FROM slhsalario a where a.ano=");
		query.append(yearMonth.getYear());
		query.append(" and a.mes=");
		query.append(yearMonth.getNumberOfMonth());
		query.append(" and a.mov_cod in (");
		query.append(ExportClosedExtraWorkMonth.extraWorkSundayMovementCode).append(",");
		query.append(ExportClosedExtraWorkMonth.extraWorkSaturdayMovementCode).append(",");
		query.append(ExportClosedExtraWorkMonth.extraWorkHolidayMovementCode).append(",");
		query.append(ExportClosedExtraWorkMonth.extraWorkWeekDayFirstLevelMovementCode).append(",");
		query.append(ExportClosedExtraWorkMonth.extraWorkWeekDaySecondLevelMovementCode).append(",");
		query.append(ExportClosedExtraWorkMonth.extraNightWorkFirstLevelMovementCode).append(",");
		query.append(ExportClosedExtraWorkMonth.extraNightWorkSecondLevelMovementCode).append(",");
		query.append(ExportClosedExtraWorkMonth.extraNightWorkMealMovementCode);
		query.append(")");
	    }
	    stmt = persistentSuportOracle.prepareStatement(query.toString());
	    rs = stmt.executeQuery();
	    if (rs.next()) {
		return rs.getDouble("value");
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	    throw new ExcepcaoPersistencia();
	} finally {
	    try {
		if (rs != null) {
		    rs.close();
		}
	    } catch (SQLException e) {
		e.printStackTrace();
	    }
	    try {
		if (stmt != null) {
		    stmt.close();
		}
	    } catch (SQLException e) {
		e.printStackTrace();
	    }
	    persistentSuportOracle.closeConnection();
	}
	return 0;
    }

    /*
     * PROCEDURE IST_INSERE_PONTO( ANO In NUMBER, MES In NUMBER, NUMERO In
     * VARCHAR2, TIPO In VARCHAR2, CODIGO In VARCHAR2, DATA_INI In DATE,
     * DATA_FIM In DATE, QT_DC In NUMBER, QT_DU In NUMBER, DT_FACTO In DATE,
     * UTILIZADOR_CRIACAO In VARCHAR2, DATA_CRIACAO In DATE, ERRO Out VARCHAR )
     */
    public void exportToGIAF(String file) throws SQLException, ExcepcaoPersistencia {
	PersistentSuportGiaf persistentSuportOracle = PersistentSuportGiaf.getInstance();
	persistentSuportOracle.startTransaction();
	String[] lineTokens = file.split("\n");
	CallableStatement cs = null;
	for (int line = 0; line < lineTokens.length; line++) {
	    try {
		String[] fieldTokens = lineTokens[line].split("\t");
		cs = persistentSuportOracle.prepareCall("BEGIN ist_insere_ponto(?, ?, ? ,? ,? ,? ,? ,? , ?, ?, ?,?,?); END;");
		cs.setInt(1, new Integer(fieldTokens[0].trim()).intValue());// ANO
		cs.setInt(2, new Integer(fieldTokens[1].trim()).intValue());// MES
		cs.setString(3, fieldTokens[2].trim());// NUMERO
		cs.setString(4, fieldTokens[3].trim());// TIPO

		String code = new String(fieldTokens[4].trim());// CODIGO
		cs.setString(5, code);

		String beginDateString = new Integer(fieldTokens[5].trim()).toString();// DATA_INI
		cs.setDate(6, getDate(beginDateString));

		String endDateString = new Integer(fieldTokens[6].trim()).toString();// DATA_FIM
		cs.setDate(7, getDate(endDateString));

		DecimalFormat df = new DecimalFormat("0,00");

		Integer value = new Integer(fieldTokens[7].trim());// QT_DC
		cs.setDouble(8, new Double(df.format(value)).doubleValue());

		value = new Integer(fieldTokens[8].trim());// QT_DU
		cs.setDouble(9, new Double(df.format(value)).doubleValue());

		if (fieldTokens.length >= 10) {// DT_FACTO
		    String factoDateString = new Integer(fieldTokens[9].trim()).toString();
		    cs.setDate(10, getDate(factoDateString));
		} else {
		    cs.setDate(10, null);
		}

		if (fieldTokens.length >= 11) {// UTILIZADOR_CRIACAO
		    cs.setString(11, fieldTokens[10].trim());
		} else {
		    cs.setString(11, null);
		}

		if (fieldTokens.length >= 12) {// DATA_CRIACAO
		    String creationDateString = new Integer(fieldTokens[11].trim()).toString();
		    cs.setDate(12, getDate(creationDateString));
		} else {
		    cs.setDate(12, null);
		}
		cs.registerOutParameter(13, OracleTypes.VARCHAR);
		cs.execute();
		if (cs.getString(13) != null) {
		    System.out.println("ERRO exportToGIAF na linha - " + (line + 1) + " : " + cs.getString(13) + " DADOS: "
			    + lineTokens[line].trim());
		    cs.close();
		    persistentSuportOracle.cancelTransaction();
		    throw new InvalidGiafCodeException("errors.exportToGiafException", new Integer(line + 1).toString(),
			    cs.getString(13), lineTokens[line].trim());
		}
	    } finally {
		if (cs != null) {
		    cs.close();
		}
	    }
	}
	persistentSuportOracle.commitTransaction();
    }

    public void exportVacationsToGIAF(String file) throws SQLException, ExcepcaoPersistencia {
	PersistentSuportGiaf persistentSuportOracle = PersistentSuportGiaf.getInstance();
	persistentSuportOracle.startTransaction();
	String[] lineTokens = file.split("\n");
	CallableStatement cs = null;
	for (int line = 0; line < lineTokens.length; line++) {
	    try {
		String[] fieldTokens = lineTokens[line].split("\t");
		cs = persistentSuportOracle.prepareCall("BEGIN ist_insere_artigo_17(?, ?, ? ,? ,? ,? ); END;");

		cs.setInt(1, new Integer(fieldTokens[0].trim()).intValue());// ANO
		cs.setString(2, fieldTokens[1].trim());// NUMERO
		cs.setInt(3, new Integer(fieldTokens[2].trim()).intValue());// QUANTIDADE
		// DIAS
		if (fieldTokens.length >= 4) {// UTILIZADOR_CRIACAO
		    cs.setString(4, fieldTokens[3].trim());
		} else {
		    cs.setString(4, null);
		}
		if (fieldTokens.length >= 5) {// DATA_CRIACAO
		    String endDateString = new Integer(fieldTokens[4].trim()).toString();// DATA_FIM
		    cs.setDate(5, getDate(endDateString));
		} else {
		    cs.setDate(5, null);

		}
		cs.registerOutParameter(6, OracleTypes.VARCHAR);
		cs.execute();

		if (cs.getString(6) != null) {
		    System.out.println("ERRO exportToGIAF na linha - " + (line + 1) + " : " + cs.getString(6) + " DADOS: "
			    + lineTokens[line].trim());
		    cs.close();
		    persistentSuportOracle.cancelTransaction();
		    throw new InvalidGiafCodeException("errors.exportToGiafException", new Integer(line + 1).toString(),
			    cs.getString(6), lineTokens[line].trim());
		}
	    } finally {
		if (cs != null) {
		    cs.close();
		}
	    }
	}
	persistentSuportOracle.commitTransaction();
    }

    private Date getDate(String dateString) {
	Calendar date = Calendar.getInstance();
	date.set(Calendar.DAY_OF_MONTH, new Integer(dateString.substring(6, 8)).intValue());
	date.set(Calendar.MONTH, new Integer(dateString.substring(4, 6)).intValue() - 1);
	date.set(Calendar.YEAR, new Integer(dateString.substring(0, 4)).intValue());
	return new Date(date.getTimeInMillis());
    }

}
