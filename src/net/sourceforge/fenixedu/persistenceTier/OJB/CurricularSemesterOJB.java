package net.sourceforge.fenixedu.persistenceTier.OJB;

import java.util.List;

import net.sourceforge.fenixedu.domain.CurricularSemester;
import net.sourceforge.fenixedu.domain.ICurricularSemester;
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

    public ICurricularSemester readCurricularSemesterBySemesterAndCurricularYear(Integer semester,
            Integer year) throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("semester", semester);
        crit.addEqualTo("curricularYear.year", year);
        return (ICurricularSemester) queryObject(CurricularSemester.class, crit);

    }

    public List readAll() throws ExcepcaoPersistencia {
        return queryList(CurricularSemester.class, new Criteria());
    }
}