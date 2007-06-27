package net.sourceforge.fenixedu.persistenceTierOracle.Oracle;

import java.sql.CallableStatement;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.DecimalFormat;

import net.sourceforge.fenixedu.applicationTier.Servico.assiduousness.ExportClosedExtraWorkMonth;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.assiduousness.ExtraWorkRequest;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.joda.time.DateTimeFieldType;
import org.joda.time.YearMonthDay;

public class GiafInterface {

    public Double getEmployeeHourValue(Employee employee, YearMonthDay day) throws ExcepcaoPersistencia {
        PersistentSuportOracle persistentSuportOracle = PersistentSuportOracle.getGiafDBInstance();
        try {
            CallableStatement callableStatement = persistentSuportOracle
                    .prepareCall("BEGIN ?:=ist_valor_hora(?, ?, ? ,?); END;");
            callableStatement.registerOutParameter(1, Types.DOUBLE);
            DecimalFormat f = new DecimalFormat("000000");
            callableStatement.setString(2, f.format(employee.getEmployeeNumber()));
            callableStatement.setDate(3, new Date(day.toDateTimeAtMidnight().getMillis()));
            callableStatement.registerOutParameter(4, Types.DOUBLE);
            callableStatement.registerOutParameter(5, Types.VARCHAR);
            callableStatement.executeQuery();
            if (callableStatement.getString(5) == null) {
                return new Double(callableStatement.getDouble(4));
            }
            callableStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ExcepcaoPersistencia();
        }
        return null;
    }

    public Double getEmployeeSalary(Employee employee, YearMonthDay day) throws ExcepcaoPersistencia {
        Double salary = 0.0;
        PersistentSuportOracle persistentSuportOracle = PersistentSuportOracle.getGiafDBInstance();
        try {
            PreparedStatement stmt = persistentSuportOracle
                    .prepareStatement("SELECT ano, mes FROM sltinfdivs");
            ResultSet rs = stmt.executeQuery();
            Integer year = 0;
            Integer month = 0;
            if (rs.next()) {
                year = rs.getInt("ano");
                month = rs.getInt("mes");
            }
            rs.close();
            StringBuilder query = new StringBuilder();
            query.append("select emp_venc ");
            if (day.getYear() == year && day.getMonthOfYear() == month) {
                query.append("from sldemp04 where sldemp04.emp_num = ");
                query.append(employee.getEmployeeNumber());
            } else {
                query.append("from slhemp04 where slhemp04.emp_num = ");
                query.append(employee.getEmployeeNumber());
                query.append(" and slhemp04.ano = ");
                query.append(day.getYear());
                query.append(" and slhemp04.mes = ");
                query.append(day.getMonthOfYear());
            }
            stmt = persistentSuportOracle.prepareStatement(query.toString());
            rs = stmt.executeQuery();
            if (rs.next()) {
                salary = rs.getDouble("emp_venc");
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
            PreparedStatement stmt = persistentSuportOracle
                    .prepareStatement("SELECT ano, mes FROM sltinfdivs");
            ResultSet rs = stmt.executeQuery();
            Integer year = 0;
            Integer month = 0;
            if (rs.next()) {
                year = rs.getInt("ano");
                month = rs.getInt("mes");
            }
            rs.close();
            StringBuilder query = new StringBuilder();
            if (extraWorkRequest.getPartialPayingDate().get(DateTimeFieldType.year()) == year
                    && extraWorkRequest.getPartialPayingDate().get(DateTimeFieldType.monthOfYear()) + 1 == month) {
                query.append("SELECT a.mov_cod, a.sal_val_brt FROM sldsalario a where a.emp_num=");
                query.append(extraWorkRequest.getAssiduousness().getEmployee().getEmployeeNumber());
                query.append(" and a.mov_cod in (");
                query.append(ExportClosedExtraWorkMonth.extraWorkSundayMovementCode).append(",");
                query.append(ExportClosedExtraWorkMonth.extraWorkSaturdayMovementCode).append(",");
                query.append(ExportClosedExtraWorkMonth.extraWorkHolidayMovementCode);
                query.append(") and a.ano_pag=");
                query.append(extraWorkRequest.getHoursDoneInPartialDate().get(DateTimeFieldType.year()));
                query.append(" and a.mes_pag=");
                query.append(extraWorkRequest.getHoursDoneInPartialDate().get(
                        DateTimeFieldType.monthOfYear()) + 1);
            } else {
                query.append("SELECT a.mov_cod,a.sal_val_brt FROM slhsalario a where a.ano=");
                query.append(extraWorkRequest.getPartialPayingDate().get(DateTimeFieldType.year()));
                query.append(" and a.mes=");
                query.append(extraWorkRequest.getPartialPayingDate()
                        .get(DateTimeFieldType.monthOfYear()) + 1);
                query.append(" and a.ano_pag=");
                query.append(extraWorkRequest.getHoursDoneInPartialDate().get(DateTimeFieldType.year()));
                query.append(" and a.mes_pag=");
                if (extraWorkRequest.getPartialPayingDate() != extraWorkRequest
                        .getHoursDoneInPartialDate()) {
                    query.append(extraWorkRequest.getHoursDoneInPartialDate().get(
                            DateTimeFieldType.monthOfYear()));
                } else {
                    query.append(extraWorkRequest.getHoursDoneInPartialDate().get(
                            DateTimeFieldType.monthOfYear()) + 1);
                }
                query.append(" and a.mov_cod in (");
                query.append(ExportClosedExtraWorkMonth.extraWorkSundayMovementCode).append(",");
                query.append(ExportClosedExtraWorkMonth.extraWorkSaturdayMovementCode).append(",");
                query.append(ExportClosedExtraWorkMonth.extraWorkHolidayMovementCode);
                query.append(") and a.emp_num =");
                query.append(extraWorkRequest.getAssiduousness().getEmployee().getEmployeeNumber());
            }
            stmt = persistentSuportOracle.prepareStatement(query.toString());
            rs = stmt.executeQuery();
            while (rs.next()) {
                if (rs.getInt("mov_cod") == new Integer(
                        ExportClosedExtraWorkMonth.extraWorkSundayMovementCode)) {
                    extraWorkRequest.setSundayAmount(rs.getDouble("sal_val_brt"));
                } else if (rs.getInt("mov_cod") == new Integer(
                        ExportClosedExtraWorkMonth.extraWorkSaturdayMovementCode)) {
                    extraWorkRequest.setSaturdayAmount(rs.getDouble("sal_val_brt"));
                } else if (rs.getInt("mov_cod") == new Integer(
                        ExportClosedExtraWorkMonth.extraWorkHolidayMovementCode)) {
                    extraWorkRequest.setHolidayAmount(rs.getDouble("sal_val_brt"));
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
}
