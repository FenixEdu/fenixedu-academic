package ServidorPersistente;

import java.util.List;

import Dominio.ICurricularSemester;
import Dominio.ICurricularYear;

/**
 * @author dcs-rjao
 *
 * 25/Mar/2003
 */

public interface IPersistentCurricularSemester extends IPersistentObject {

    public ICurricularSemester readCurricularSemesterBySemesterAndCurricularYear(Integer semester, ICurricularYear curricularYear) throws ExcepcaoPersistencia;
    public List readAll() throws ExcepcaoPersistencia;
    
    public void delete(ICurricularSemester semester) throws ExcepcaoPersistencia;
   


}

