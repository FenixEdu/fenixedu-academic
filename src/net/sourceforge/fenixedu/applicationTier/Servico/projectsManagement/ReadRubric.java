/*
 * Created on Jan 12, 2005
 */

package net.sourceforge.fenixedu.applicationTier.Servico.projectsManagement;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.projectsManagement.InfoRubric;
import net.sourceforge.fenixedu.domain.projectsManagement.IRubric;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTierOracle.Oracle.PersistentSuportOracle;
import net.sourceforge.fenixedu.util.projectsManagement.RubricType;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Susana Fernandes
 */
public class ReadRubric implements IService {

    public ReadRubric() {
    }

    public List run(RubricType rubricType) throws ExcepcaoPersistencia {
        PersistentSuportOracle p = PersistentSuportOracle.getInstance();
        List rubricList = p.getIPersistentRubric().getRubricList(rubricType.getRubricTableName());
        List infoRubricList = new ArrayList();
        for (int i = 0; i < rubricList.size(); i++)
            infoRubricList.add(InfoRubric.newInfoFromDomain((IRubric) rubricList.get(i)));

        return infoRubricList;
    }

}