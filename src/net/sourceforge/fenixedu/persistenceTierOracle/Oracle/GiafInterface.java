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

    public BigDecimal getEmployeeHourValue(Employee employee, LocalDate day) throws ExcepcaoPersistencia {
	PersistentSuportGiaf persistentSuportOracle = PersistentSuportGiaf.getInstance();
	try {
	    CallableStatement callableStatement = persistentSuportOracle.prepareCall("BEGIN ?:=ist_valor_hora(?, ?, ? ,?); END;");
	    callableStatement.registerOutParameter(1, Types.DOUBLE);
	    DecimalFormat f = new DecimalFormat("000000");
	    callableStatement.setString(2, f.format(employee.getEmployeeNumber()));
	    callableStatement.setDate(3, new Date(day.toDateTimeAtStartOfDay().getMillis()));
	    callableStatement.registerOutParameter(4, Types.DOUBLE);
	    callableStatement.registerOutParameter(5, Types.VARCHAR);
	    callableStatement.executeQuery();
	    if (callableStatement.getString(5) == null) {
		return new BigDecimal(callableStatement.getDouble(4));
	    }
	    callableStatement.close();
	} catch (SQLException e) {
	    e.printStackTrace();
	    throw new ExcepcaoPersistencia();
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
		    .append("select emp_venc from(select a.emp_num, a.emp_venc, a.emp_venc_dt, min(b.emp_venc_dt) as emp_venc_dt_fim ");
	    stringBuilder
		    .append("from sldempvenc a,sldempvenc b where b.emp_venc_dt > a.emp_venc_dt and a.emp_num = b.emp_num and nvl(a.tipo_alt,'@') != 'A' ");
	    // and nvl(b.tipo_alt,'@') != 'A'
	    stringBuilder
		    .append("group by a.emp_num, a.emp_venc, a.emp_venc_dt union SELECT c.emp_num, c.emp_venc, c.emp_venc_dt, sysdate FROM sldemp04 c )where to_date('");
	    stringBuilder.append(fmt.print(day));
	    stringBuilder.append("', 'DD-MM-YYYY') between emp_venc_dt and emp_venc_dt_fim and emp_num=");
	    stringBuilder.append(employee.getEmployeeNumber());
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
	return salary;
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
		query
			.append("SELECT a.mov_cod, a.sal_val_brt, a.emp_ccusto FROM sldsalario a where extract(year from data_mov)=");
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

	    boolean hasMoreThanOneCostCenter = hasMoreThanOneCostCenter(extraWorkRequest.getAssiduousness(), extraWorkRequest
		    .getHoursDoneInPartialDate());
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

    public void exportToGIAF(String file) throws SQLException, ExcepcaoPersistencia {
	PersistentSuportGiaf persistentSuportOracle = PersistentSuportGiaf.getInstance();
	persistentSuportOracle.startTransaction();
	String[] lineTokens = file.split("\n");
	CallableStatement cs = null;
	for (int line = 0; line < lineTokens.length; line++) {
	    try {
		String[] fieldTokens = lineTokens[line].split("\t");
		cs = persistentSuportOracle.prepareCall("BEGIN ist_insere_ponto(?, ?, ? ,? ,? ,? ,? ,? , ?, ?, ?,?); END;");
		cs.setInt(1, new Integer(fieldTokens[0].trim()).intValue());
		cs.setInt(2, new Integer(fieldTokens[1].trim()).intValue());
		cs.setString(3, fieldTokens[2].trim());
		cs.setString(4, fieldTokens[3].trim());

		String code = new String(fieldTokens[4].trim());
		cs.setString(5, code);

		String beginDateString = new Integer(fieldTokens[5].trim()).toString();
		Calendar beginDate = Calendar.getInstance();
		beginDate.set(Calendar.DAY_OF_MONTH, new Integer(beginDateString.substring(6, 8)).intValue());
		beginDate.set(Calendar.MONTH, new Integer(beginDateString.substring(4, 6)).intValue() - 1);
		beginDate.set(Calendar.YEAR, new Integer(beginDateString.substring(0, 4)).intValue());
		cs.setDate(6, new Date(beginDate.getTimeInMillis()));

		String endDateString = new Integer(fieldTokens[6].trim()).toString();
		Calendar endDate = Calendar.getInstance();
		endDate.set(Calendar.DAY_OF_MONTH, new Integer(endDateString.substring(6, 8)).intValue());
		endDate.set(Calendar.MONTH, new Integer(endDateString.substring(4, 6)).intValue() - 1);
		endDate.set(Calendar.YEAR, new Integer(endDateString.substring(0, 4)).intValue());

		cs.setDate(7, new Date(endDate.getTimeInMillis()));

		Integer value = new Integer(fieldTokens[7].trim());
		DecimalFormat df = new DecimalFormat("0,00");

		cs.setDouble(8, new Double(df.format(value)).doubleValue());
		value = new Integer(fieldTokens[8].trim());
		cs.setDouble(9, new Double(df.format(value)).doubleValue());
		if (fieldTokens.length >= 10) {
		    cs.setString(10, fieldTokens[9].trim());
		} else {
		    cs.setString(10, null);
		}
		if (fieldTokens.length >= 11) {
		    String dateString = new Integer(fieldTokens[10].trim()).toString();
		    Calendar c = Calendar.getInstance();
		    c.set(Calendar.DAY_OF_MONTH, new Integer(dateString.substring(6, 8)).intValue());
		    c.set(Calendar.MONTH, new Integer(dateString.substring(4, 6)).intValue() - 1);
		    c.set(Calendar.YEAR, new Integer(dateString.substring(0, 4)).intValue());
		    cs.setDate(11, new Date(c.getTimeInMillis()));
		} else {
		    cs.setDate(11, null);
		}
		cs.registerOutParameter(12, OracleTypes.VARCHAR);
		cs.execute();
		if (cs.getString(12) != null) {
		    System.out.println("ERRO exportToGIAF na linha - " + (line + 1) + " : " + cs.getString(12) + " DADOS: "
			    + lineTokens[line].trim());
		    cs.close();
		    persistentSuportOracle.cancelTransaction();
		    throw new InvalidGiafCodeException("errors.exportToGiafException", new Integer(line + 1).toString(), cs
			    .getString(12), lineTokens[line].trim());
		}
	    } finally {
		if (cs != null) {
		    cs.close();
		}
	    }
	}
	persistentSuportOracle.commitTransaction();
    }

}
