package net.sourceforge.fenixedu.persistenceTierOracle.Oracle;

import java.sql.CallableStatement;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Types;
import java.text.DecimalFormat;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

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
	    throw new ExcepcaoPersistencia();
	}
	return null;
    }

}
