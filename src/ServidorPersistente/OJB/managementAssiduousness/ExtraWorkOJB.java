/*
 * Created on 15/Dez/2004
 */
package ServidorPersistente.OJB.managementAssiduousness;

import java.util.Date;

import org.apache.ojb.broker.query.Criteria;

import Dominio.managementAssiduousness.ExtraWork;
import Dominio.managementAssiduousness.IExtraWork;
import ServidorPersistente.OJB.PersistentObjectOJB;
import ServidorPersistente.managementAssiduousness.IPersistentExtraWork;

/**
 * @author Tânia Pousão
 *
 */
public class ExtraWorkOJB extends PersistentObjectOJB implements IPersistentExtraWork{

    public ExtraWorkOJB() {
        super();
    }

    public IExtraWork readExtraWorkByDay(Date day) throws Exception {
        Criteria criteria = new Criteria(); 
        criteria.addEqualTo("day", day);
        
        return (IExtraWork) queryObject(ExtraWork.class, criteria);
    }    
}