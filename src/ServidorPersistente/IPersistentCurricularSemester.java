package ServidorPersistente;

import java.util.ArrayList;

import Dominio.ICurricularSemester;
import Dominio.ICurricularYear;
import ServidorPersistente.exceptions.ExistingPersistentException;

/**
 * @author dcs-rjao
 *
 * 25/Mar/2003
 */

public interface IPersistentCurricularSemester extends IPersistentObject {

    public ICurricularSemester readCurricularSemesterBySemesterAndCurricularYear(Integer semester, ICurricularYear curricularYear) throws ExcepcaoPersistencia;
    public ArrayList readAll() throws ExcepcaoPersistencia;
    public void lockWrite(ICurricularSemester semester) throws ExcepcaoPersistencia, ExistingPersistentException;
    public void delete(ICurricularSemester semester) throws ExcepcaoPersistencia;
    public void deleteAll() throws ExcepcaoPersistencia;


}

