/*
 * CriarTurno.java Created on 27 de Outubro de 2002, 18:48
 */

package net.sourceforge.fenixedu.applicationTier.Servico.sop;

/**
 * Servi�o CriarTurno
 * 
 * @author tfc130
 * @author Pedro Santos e Rita Carvalho
 */
import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.InfoShiftEditor;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class CriarTurno extends Service {

    public InfoShift run(InfoShiftEditor infoTurno) throws FenixServiceException, ExcepcaoPersistencia {
    	final ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(infoTurno.getInfoDisciplinaExecucao().getIdInternal());

        Integer availabilityFinal = new Integer(new Double(Math.ceil(1.10 * infoTurno.getLotacao()
                .doubleValue())).intValue());

    	final Shift newShift = new Shift(executionCourse, infoTurno.getTipo(), infoTurno.getLotacao(), availabilityFinal);

        return InfoShift.newInfoFromDomain(newShift);
    }

}
