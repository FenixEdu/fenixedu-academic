/*
 * Created on 18/Dez/2003
 *
 */
package net.sourceforge.fenixedu.persistenceTier.OJB;

import java.util.List;

import net.sourceforge.fenixedu.domain.IBranch;
import net.sourceforge.fenixedu.domain.IScientificArea;
import net.sourceforge.fenixedu.domain.ScientificArea;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentScientificArea;

import org.apache.ojb.broker.query.Criteria;

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