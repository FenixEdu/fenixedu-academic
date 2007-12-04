package net.sourceforge.fenixedu.applicationTier.Servico.assiduousness;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.sql.SQLException;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.assiduousness.ClosedMonth;
import net.sourceforge.fenixedu.domain.assiduousness.ClosedMonthDocument;
import net.sourceforge.fenixedu.domain.assiduousness.util.ClosedMonthDocumentType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTierOracle.Oracle.GiafInterface;

import org.apache.struts.action.ActionMessage;

public class ExportToGIAFAndSaveFile extends Service {

    public ActionMessage run(ClosedMonth closedMonth, String fileName,
	    ClosedMonthDocumentType closedMonthDocumentType, String file) throws FileNotFoundException {

	ByteArrayInputStream inputStream = new ByteArrayInputStream(file.getBytes());
	ClosedMonthDocument closedMonthDocument = closedMonth.addFile(inputStream, fileName,
		closedMonthDocumentType);
		GiafInterface giafInterface = new GiafInterface();
	try {
	    giafInterface.exportToGIAF(file);
	} catch (SQLException e) {
	    closedMonthDocument.delete();
	    e.printStackTrace();
	    return new ActionMessage("error.connectionError");
	} catch (ExcepcaoPersistencia e) {
	    closedMonthDocument.delete();
	    e.printStackTrace();
	    return new ActionMessage("error.connectionError");
	}
	return null;
    }
}