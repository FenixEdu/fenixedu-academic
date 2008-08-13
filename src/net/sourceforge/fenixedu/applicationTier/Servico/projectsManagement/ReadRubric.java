/*
 * Created on Jan 12, 2005
 */

package net.sourceforge.fenixedu.applicationTier.Servico.projectsManagement;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.projectsManagement.InfoRubric;
import net.sourceforge.fenixedu.domain.projectsManagement.IRubric;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTierOracle.Oracle.PersistentSuportOracle;
import net.sourceforge.fenixedu.util.projectsManagement.RubricType;

/**
 * @author Susana Fernandes
 */
public class ReadRubric extends Service {

    public ReadRubric() {
    }

    public List run(String username, String costCenter, RubricType rubricType, String userNumber) throws ExcepcaoPersistencia {
	PersistentSuportOracle p = PersistentSuportOracle.getProjectDBInstance();
	List<IRubric> rubricList = p.getIPersistentRubric().getRubricList(rubricType.getRubricTableName());
	List<InfoRubric> infoRubricList = new ArrayList<InfoRubric>();
	for (IRubric rubric : rubricList)
	    infoRubricList.add(InfoRubric.newInfoFromDomain(rubric));

	return infoRubricList;
    }

}