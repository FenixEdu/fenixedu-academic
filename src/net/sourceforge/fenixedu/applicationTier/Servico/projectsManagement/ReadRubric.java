/*
 * Created on Jan 12, 2005
 */

package net.sourceforge.fenixedu.applicationTier.Servico.projectsManagement;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.projectsManagement.InfoRubric;
import net.sourceforge.fenixedu.domain.projectsManagement.IRubric;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTierOracle.BackendInstance;
import net.sourceforge.fenixedu.persistenceTierOracle.Oracle.PersistentRubric;
import net.sourceforge.fenixedu.util.projectsManagement.RubricType;

/**
 * @author Susana Fernandes
 */
public class ReadRubric extends FenixService {

    public ReadRubric() {
    }

    public List run(String username, String costCenter, RubricType rubricType, BackendInstance instance, String userNumber)
	    throws ExcepcaoPersistencia {
	List<IRubric> rubricList = new PersistentRubric().getRubricList(rubricType.getRubricTableName(), instance);
	List<InfoRubric> infoRubricList = new ArrayList<InfoRubric>();
	for (IRubric rubric : rubricList)
	    infoRubricList.add(InfoRubric.newInfoFromDomain(rubric));

	return infoRubricList;
    }

}