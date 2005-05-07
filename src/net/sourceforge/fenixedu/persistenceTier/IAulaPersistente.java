/*
 * IAulaPersistente.java
 *
 * Created on 17 de Outubro de 2002, 20:55
 */

package net.sourceforge.fenixedu.persistenceTier;

/**
 * 
 * @author tfc130
 */
import java.util.Calendar;
import java.util.List;

import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.ILesson;
import net.sourceforge.fenixedu.domain.IRoom;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.util.DiaSemana;
import net.sourceforge.fenixedu.util.TipoAula;

public interface IAulaPersistente extends IPersistentObject {

    public List readByRoomAndExecutionPeriod(Integer roomOID, Integer executionPeriodOID)
            throws ExcepcaoPersistencia;

    public List readLessonsByStudent(String username) throws ExcepcaoPersistencia;

}