package net.sourceforge.fenixedu.persistenceTier.OJB;

import java.util.List;

import net.sourceforge.fenixedu.domain.CurricularSemester;
import net.sourceforge.fenixedu.domain.ICurricularSemester;
import net.sourceforge.fenixedu.domain.ICurricularYear;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularSemester;

import org.apache.ojb.broker.query.Criteria;

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