/*
 * Created on 18/Dez/2003
 *
 */
package ServidorPersistente.OJB;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.IBranch;
import Dominio.IScientificArea;
import Dominio.ScientificArea;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentScientificArea;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 */
public class ScientificAreaOJB extends PersistentObjectOJB implements IPersistentScientificArea {
    public ScientificAreaOJB() {
    }

    public IScientificArea readByName(String name) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("name", name);
        return (IScientificArea) queryObject(ScientificArea.class, criteria);
    }

    public List readAllByBranch(IBranch branch) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("areaCurricularCourseGroups.branch.idInternal", branch.getIdInternal());
        return queryList(ScientificArea.class, criteria);
    }
}