/*
 * Created on 19/Fev/2005
 */
package net.sourceforge.fenixedu.persistenceTier.OJB.managementAssiduousness;

import net.sourceforge.fenixedu.domain.managementAssiduousness.ExtraWorkHistoric;
import net.sourceforge.fenixedu.domain.managementAssiduousness.IExtraWorkHistoric;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistentObjectOJB;
import net.sourceforge.fenixedu.persistenceTier.managementAssiduousness.IPersistentExtraWorkHistoric;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author Tânia Pousão
 *
 */
public class ExtraWorkHistoricOJB extends PersistentObjectOJB implements
        IPersistentExtraWorkHistoric {
    public IExtraWorkHistoric readEXtraWorkHistoricByYear(Integer year) throws Exception {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("year", year);
        
        return (IExtraWorkHistoric) queryObject(ExtraWorkHistoric.class, criteria);
    }

}
