/*
 * Created on Jan 12, 2005
 */

package ServidorAplicacao.Servico.projectsManagement;

import java.util.ArrayList;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.projectsManagement.InfoRubric;
import Dominio.projectsManagement.IRubric;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistenteOracle.Oracle.PersistentSuportOracle;
import Util.projectsManagement.RubricType;

/**
 * @author Susana Fernandes
 */
public class ReadRubric implements IService {

    public ReadRubric() {
    }

    public List run(RubricType rubricType) throws FenixServiceException, ExcepcaoPersistencia {
        PersistentSuportOracle p = PersistentSuportOracle.getInstance();
        List rubricList = p.getIPersistentRubric().getRubricList(rubricType.getRubricTableName());
        List infoRubricList = new ArrayList();
        for (int i = 0; i < rubricList.size(); i++)
            infoRubricList.add(InfoRubric.newInfoFromDomain((IRubric) rubricList.get(i)));

        return infoRubricList;
    }

}