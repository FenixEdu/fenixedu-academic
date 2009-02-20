package net.sourceforge.fenixedu.applicationTier.Servico.assiduousness;

import java.io.FileNotFoundException;
import java.sql.SQLException;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.assiduousness.ClosedMonth;
import net.sourceforge.fenixedu.domain.assiduousness.ClosedMonthDocument;
import net.sourceforge.fenixedu.domain.assiduousness.util.ClosedMonthDocumentType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTierOracle.Oracle.GiafInterface;

import org.apache.struts.action.ActionMessage;

import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class ExportToGIAFAndSaveFile extends FenixService {

    @Checked("RolePredicates.PERSONNEL_SECTION_PREDICATE")
    @Service
    public static ActionMessage run(ClosedMonth closedMonth, String fileName, ClosedMonthDocumentType closedMonthDocumentType,
	    String file) throws FileNotFoundException {
	ClosedMonthDocument closedMonthDocument = closedMonth.addFile(file.getBytes(), fileName, closedMonthDocumentType);
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
	} catch (Exception e) {
	    closedMonthDocument.delete();
	}
	return null;
    }
}