package ServidorPersistente.OJB;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.CurricularSemester;
import Dominio.ICurricularSemester;
import Dominio.ICurricularYear;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCurricularSemester;
import ServidorPersistente.exceptions.ExistingPersistentException;

/**
 * @author dcs-rjao
 *
 * 20/Mar/2003
 */

public class CurricularSemesterOJB extends ObjectFenixOJB implements IPersistentCurricularSemester {

	public CurricularSemesterOJB() {
	}

	

	public void lockWrite(ICurricularSemester curricularSemesterToWrite) throws ExcepcaoPersistencia, ExistingPersistentException {

		ICurricularSemester curricularSemesterFromDB = null;

		// If there is nothing to write, simply return.
		if (curricularSemesterToWrite == null) {
			return;
		}

		// Read CurricularSemester from database.
		curricularSemesterFromDB = this.readCurricularSemesterBySemesterAndCurricularYear(curricularSemesterToWrite.getSemester(), curricularSemesterToWrite.getCurricularYear());

		// If CurricularSemester is not in database, then write it.
		if (curricularSemesterFromDB == null) {
			super.lockWrite(curricularSemesterToWrite);
		// else If the CurricularSemester is mapped to the database, then write any existing changes.
		} else if ((curricularSemesterToWrite instanceof CurricularSemester) && ((CurricularSemester) curricularSemesterFromDB).getIdInternal().equals(((CurricularSemester) curricularSemesterToWrite).getIdInternal())) {
			super.lockWrite(curricularSemesterToWrite);
			// else Throw an already existing exception
		} else
			throw new ExistingPersistentException();
	}

	public void delete(ICurricularSemester curricularSemester) throws ExcepcaoPersistencia {
		try {
			super.delete(curricularSemester);
		} catch (ExcepcaoPersistencia ex) {
			throw ex;
		}
	}

	public ICurricularSemester readCurricularSemesterBySemesterAndCurricularYear(Integer semester, ICurricularYear curricularYear) throws ExcepcaoPersistencia {
	    Criteria crit = new Criteria();
        crit.addEqualTo("semester",semester);
        crit.addEqualTo("curricularYear.year",curricularYear.getYear());
        return (ICurricularSemester) queryObject(CurricularSemester.class,crit);
		
	}

	public List readAll() throws ExcepcaoPersistencia {
	    return queryList(CurricularSemester.class,new Criteria());
		
	}

}