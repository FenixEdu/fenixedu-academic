package ServidorPersistente.OJB;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.CurricularSemester;
import Dominio.ICurricularSemester;
import Dominio.ICurricularYear;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCurricularSemester;

/**
 * @author dcs-rjao
 * 
 * 20/Mar/2003
 */

public class CurricularSemesterOJB extends PersistentObjectOJB implements IPersistentCurricularSemester {

    public CurricularSemesterOJB() {
    }

    public void delete(ICurricularSemester curricularSemester) throws ExcepcaoPersistencia {
        try {
            super.delete(curricularSemester);
        } catch (ExcepcaoPersistencia ex) {
            throw ex;
        }
    }

    public ICurricularSemester readCurricularSemesterBySemesterAndCurricularYear(Integer semester,
            ICurricularYear curricularYear) throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("semester", semester);
        crit.addEqualTo("curricularYear.year", curricularYear.getYear());
        return (ICurricularSemester) queryObject(CurricularSemester.class, crit);

    }

    public List readAll() throws ExcepcaoPersistencia {
        return queryList(CurricularSemester.class, new Criteria());

    }

}