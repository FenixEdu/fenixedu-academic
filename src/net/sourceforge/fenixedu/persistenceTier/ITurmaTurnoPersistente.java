/*
 * ITurmaTurnoPersistente.java
 *
 * Created on 19 de Outubro de 2002, 15:21
 */

package net.sourceforge.fenixedu.persistenceTier;

/**
 * 
 * @author tfc130
 */
import java.util.List;

import net.sourceforge.fenixedu.domain.ISchoolClassShift;

public interface ITurmaTurnoPersistente extends IPersistentObject {
    public ISchoolClassShift readByTurmaAndTurno(Integer turmaOID, Integer turnoOID)
            throws ExcepcaoPersistencia;

    public List readByClass(Integer schoolClassOID) throws ExcepcaoPersistencia;

    public List readClassesWithShift(Integer turnoOID) throws ExcepcaoPersistencia;
}