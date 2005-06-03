package net.sourceforge.fenixedu.persistenceTier;

import java.util.List;

import net.sourceforge.fenixedu.domain.ICurricularSemester;

/**
 * @author dcs-rjao
 * 
 * 25/Mar/2003
 */

public interface IPersistentCurricularSemester extends IPersistentObject {

    public ICurricularSemester readCurricularSemesterBySemesterAndCurricularYear(Integer semester,
            Integer year) throws ExcepcaoPersistencia;

    public List readAll() throws ExcepcaoPersistencia;
}

