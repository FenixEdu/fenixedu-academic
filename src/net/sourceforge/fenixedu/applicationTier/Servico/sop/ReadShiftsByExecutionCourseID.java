package net.sourceforge.fenixedu.applicationTier.Servico.sop;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IServico;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourseOccupancy;
import net.sourceforge.fenixedu.dataTransferObject.InfoShiftWithInfoLessons;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.NumberUtils;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 */

public class ReadShiftsByExecutionCourseID implements IServico {

    private static ReadShiftsByExecutionCourseID _servico = new ReadShiftsByExecutionCourseID();

    /**
     * The singleton access method of this class.
     */
    public static ReadShiftsByExecutionCourseID getService() {
        return _servico;
    }

    /**
     * The actor of this class.
     */
    private ReadShiftsByExecutionCourseID() {
    }

    /**
     * Devolve o nome do servico
     */
    public final String getNome() {
        return "ReadShiftsByExecutionCourseID";
    }

    public InfoExecutionCourseOccupancy run(Integer executionCourseID) throws FenixServiceException {

        InfoExecutionCourseOccupancy infoExecutionCourseOccupancy = new InfoExecutionCourseOccupancy();
        infoExecutionCourseOccupancy.setInfoShifts(new ArrayList());

        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

            IExecutionCourse executionCourse = (IExecutionCourse) sp.getIPersistentExecutionCourse()
                    .readByOID(ExecutionCourse.class, executionCourseID);

            List shifts = sp.getITurnoPersistente().readByExecutionCourse(executionCourse);

            infoExecutionCourseOccupancy.setInfoExecutionCourse(InfoExecutionCourse
                    .newInfoFromDomain(executionCourse));

            Iterator iterator = shifts.iterator();
            while (iterator.hasNext()) {
                IShift shift = (IShift) iterator.next();

                List studentsInShift = sp.getITurnoAlunoPersistente().readByShift(shift.getIdInternal());

                shift.setOcupation(new Integer(studentsInShift.size()));
                Integer capacity = new Integer(1);
                if (shift.getLotacao() != null && shift.getLotacao().intValue() != 0) {
                    capacity = shift.getLotacao();
                }
                shift.setPercentage(NumberUtils.formatNumber(new Double(shift.getOcupation()
                        .floatValue()
                        * 100 / capacity.floatValue()), 1));
                infoExecutionCourseOccupancy.getInfoShifts().add(
                        InfoShiftWithInfoLessons.newInfoFromDomain(shift));
            }

        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }

        return infoExecutionCourseOccupancy;
    }

}