package net.sourceforge.fenixedu.persistenceTier.OJB;

import java.util.List;

import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.ICurricularYear;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCurricularYear;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author dcs-rjao 20/Mar/2003
 */

public class CurricularYearOJB extends PersistentObjectOJB implements IPersistentCurricularYear {

    public CurricularYearOJB() {
    }

    public void delete(ICurricularYear curricularYear) throws ExcepcaoPersistencia {
        try {
            super.delete(curricularYear);
        } catch (ExcepcaoPersistencia ex) {
            throw ex;
        }
    }

    public ICurricularYear readCurricularYearByYear(Integer year) throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("year", year);
        return (ICurricularYear) queryObject(CurricularYear.class, crit);

    }

    public List readAll() throws ExcepcaoPersistencia {
        return queryList(CurricularYear.class, new Criteria());

    }

}