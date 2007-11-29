package net.sourceforge.fenixedu.applicationTier.Servico.assiduousness;

import java.sql.SQLException;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTierOracle.Oracle.GiafInterface;

import org.apache.struts.action.ActionMessage;

public class ExportToGIAF extends Service {

    public ActionMessage run(String file) {
	GiafInterface giafInterface = new GiafInterface();
	try {
	    giafInterface.exportToGIAF(file);
	} catch (SQLException e) {
	    e.printStackTrace();
	    return new ActionMessage("error.connectionError");
	} catch (ExcepcaoPersistencia e) {
	    e.printStackTrace();
	    return new ActionMessage("error.connectionError");
	}
	return null;
    }
}