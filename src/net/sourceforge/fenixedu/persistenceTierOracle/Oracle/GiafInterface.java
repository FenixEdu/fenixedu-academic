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
import net.sourceforge.fenixedu.domain.assiduousness.ExtraWorkRequest;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import oracle.jdbc.OracleTypes;

import org.joda.time.DateTimeFieldType;
import org.joda.time.LocalDate;
import org.joda.time.Partial;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class GiafInterface {

    public BigDecimal getEmployeeHourValue(Employee employee, LocalDate day) throws ExcepcaoPersistencia {
	PersistentSuportOracle persistentSuportOracle = PersistentSuportOracle.getGiafDBInstance();
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

    public BigDecimal getEmployeeSalary(Employee employee, LocalDate day) throws ExcepcaoPersistencia {
	BigDecimal salary = new BigDecimal(0.0);
	DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
	PersistentSuportOracle persistentSuportOracle = PersistentSuportOracle.getGiafDBInstance();
	try {
	    StringBuilder stringBuilder = new StringBuilder();
	    stringBuilder
		    .append("select emp_venc from(select a.emp_num, a.emp_venc, a.emp_venc_dt, min(b.emp_venc_dt) as emp_venc_dt_fim ");
	    stringBuilder
		    .append("from sldempvenc a,sldempvenc b where b.emp_venc_dt > to_date(a.emp_venc_dt, 'DD-MM-YYYY') and a.emp_num = b.emp_num and nvl(a.tipo_alt,'@') != 'A' and nvl(b.tipo_alt,'@') != 'A' ");
	    stringBuilder
		    .append("group by a.emp_num, a.emp_venc, a.emp_venc_dt union SELECT c.emp_num, c.emp_venc, c.emp_venc_dt, sysdate FROM sldemp04 c )where to_date('");
	    stringBuilder.append(fmt.print(day));
	    stringBuilder.append("', 'DD-MM-YYYY') between emp_venc_dt and emp_venc_dt_fim and emp_num=");
	    stringBuilder.append(employee.getEmployeeNumber());

	    PreparedStatement stmt = persistentSuportOracle.prepareStatement(stringBuilder.toString());
	    ResultSet rs = stmt.executeQuery();
	    if (rs.next()) {
		salary = rs.getBigDecimal("emp_venc");
	    }
	    rs.close();
	    stmt.close();
	} catch (SQLException e) {
	    e.printStackTrace();
	    throw new ExcepcaoPersistencia();
	}
	return salary;
    }

    public void updateExtraWorkRequest(ExtraWorkRequest extraWorkRequest) throws ExcepcaoPersistencia {
	PersistentSuportOracle persistentSuportOracle = PersistentSuportOracle.getGiafDBInstance();
	try {
	    PreparedStatement stmt = persistentSuportOracle.prepareStatement("SELECT ano, mes FROM sltinfdivs");
	    ResultSet rs = stmt.executeQuery();
	    Integer year = 0;
	    Integer month = 0;
	    if (rs.next()) {
		year = rs.getInt("ano");
		month = rs.getInt("mes");
	    }
	    rs.close();
	    StringBuilder query = new StringBuilder();
	    YearMonth yearMonthPayingDate = new YearMonth(extraWorkRequest.getPartialPayingDate());
	    yearMonthPayingDate.addMonth();

	    if (yearMonthPayingDate.getYear().equals(year) && yearMonthPayingDate.getNumberOfMonth() == month) {
		query.append("SELECT a.mov_cod, a.sal_val_brt FROM sldsalario a where a.emp_num=");
		query.append(extraWorkRequest.getAssiduousness().getEmployee().getEmployeeNumber());
		query.append(" and a.mov_cod in (");
		query.append(ExportClosedExtraWorkMonth.extraWorkSundayMovementCode).append(",");
		query.append(ExportClosedExtraWorkMonth.extraWorkSaturdayMovementCode).append(",");
		query.append(ExportClosedExtraWorkMonth.extraWorkHolidayMovementCode).append(",");
		query.append(ExportClosedExtraWorkMonth.extraWorkWeekDayFirstLevelMovementCode).append(",");
		query.append(ExportClosedExtraWorkMonth.extraWorkWeekDaySecondLevelMovementCode);
		query.append(") and extract(year from data_mov)=");
		query.append(extraWorkRequest.getHoursDoneInPartialDate().get(DateTimeFieldType.year()));
		query.append(" and extract(month from data_mov)=");
		query.append(extraWorkRequest.getHoursDoneInPartialDate().get(DateTimeFieldType.monthOfYear()));
	    } else {
		query.append("SELECT a.mov_cod,a.sal_val_brt FROM slhsalario a where a.ano=");
		query.append(yearMonthPayingDate.getYear());
		query.append(" and a.mes=");
		query.append(yearMonthPayingDate.getNumberOfMonth());
		query.append(" and extract(year from data_mov)=");
		query.append(extraWorkRequest.getHoursDoneInPartialDate().get(DateTimeFieldType.year()));
		query.append(" and extract(month from data_mov)=");
		query.append(extraWorkRequest.getHoursDoneInPartialDate().get(DateTimeFieldType.monthOfYear()));
		query.append(" and a.mov_cod in (");
		query.append(ExportClosedExtraWorkMonth.extraWorkSundayMovementCode).append(",");
		query.append(ExportClosedExtraWorkMonth.extraWorkSaturdayMovementCode).append(",");
		query.append(ExportClosedExtraWorkMonth.extraWorkHolidayMovementCode).append(",");
		query.append(ExportClosedExtraWorkMonth.extraWorkWeekDayFirstLevelMovementCode).append(",");
		query.append(ExportClosedExtraWorkMonth.extraWorkWeekDaySecondLevelMovementCode);
		query.append(") and a.emp_num =");
		query.append(extraWorkRequest.getAssiduousness().getEmployee().getEmployeeNumber());
	    }
	    stmt = persistentSuportOracle.prepareStatement(query.toString());
	    rs = stmt.executeQuery();
	    while (rs.next()) {
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
		}

		extraWorkRequest.updateAmount();
	    }
	    rs.close();

	    stmt.close();
	} catch (SQLException e) {
	    e.printStackTrace();
	    throw new ExcepcaoPersistencia();
	}
    }

    public double getTotalMonthAmount(Partial closedYearMonth) throws ExcepcaoPersistencia {
	PersistentSuportOracle persistentSuportOracle = PersistentSuportOracle.getGiafDBInstance();
	try {
	    PreparedStatement stmt = persistentSuportOracle.prepareStatement("SELECT ano, mes FROM sltinfdivs");
	    ResultSet rs = stmt.executeQuery();
	    Integer year = 0;
	    Integer month = 0;
	    if (rs.next()) {
		year = rs.getInt("ano");
		month = rs.getInt("mes");
	    }
	    rs.close();
	    YearMonth yearMonth = new YearMonth(closedYearMonth);
	    yearMonth.addMonth();
	    StringBuilder query = new StringBuilder();
	    if (yearMonth.getYear().equals(year) && yearMonth.getNumberOfMonth() == month) {
		query.append("SELECT sum(a.sal_val_brt) as value FROM sldsalario a where a.mov_cod in (");
		query.append(ExportClosedExtraWorkMonth.extraWorkSundayMovementCode).append(",");
		query.append(ExportClosedExtraWorkMonth.extraWorkSaturdayMovementCode).append(",");
		query.append(ExportClosedExtraWorkMonth.extraWorkHolidayMovementCode).append(",");
		query.append(ExportClosedExtraWorkMonth.extraWorkWeekDayFirstLevelMovementCode).append(",");
		query.append(ExportClosedExtraWorkMonth.extraWorkWeekDaySecondLevelMovementCode);
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
		query.append(ExportClosedExtraWorkMonth.extraWorkWeekDaySecondLevelMovementCode);
		query.append(")");
	    }
	    stmt = persistentSuportOracle.prepareStatement(query.toString());
	    rs = stmt.executeQuery();
	    if (rs.next()) {
		return rs.getDouble("value");
	    }
	    rs.close();

	    stmt.close();
	} catch (SQLException e) {
	    e.printStackTrace();
	    throw new ExcepcaoPersistencia();
	}
	return 0;
    }

    public void exportToGIAF(String file) throws SQLException, ExcepcaoPersistencia {
	PersistentSuportOracle persistentSuportOracle = PersistentSuportOracle.getGiafDBInstance();
	persistentSuportOracle.startTransaction();
	String[] lineTokens = file.split("\n");
	for (int line = 0; line < lineTokens.length; line++) {
	    String[] fieldTokens = lineTokens[line].split("\t");
	    CallableStatement cs = persistentSuportOracle
		    .prepareCall("BEGIN ist_insere_ponto(?, ?, ? ,? ,? ,? ,? ,? , ?, ?, ?,?); END;");
	    cs.setInt(1, new Integer(fieldTokens[0].trim()).intValue());
	    cs.setInt(2, new Integer(fieldTokens[1].trim()).intValue());
	    cs.setString(3, fieldTokens[2].trim());
	    cs.setString(4, fieldTokens[3].trim());

	    Integer code = new Integer(fieldTokens[4].trim());
	    DecimalFormat f = new DecimalFormat("00");
	    cs.setString(5, f.format(code));

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
		throw new SQLException();
	    }
	    cs.close();
	}
	persistentSuportOracle.commitTransaction();
    }

}
