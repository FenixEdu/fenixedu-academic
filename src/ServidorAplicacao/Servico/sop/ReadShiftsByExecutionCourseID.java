package ServidorAplicacao.Servico.sop;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.InfoExecutionCourse;
import DataBeans.InfoExecutionCourseOccupancy;
import DataBeans.InfoShiftWithInfoLessons;
import Dominio.ExecutionCourse;
import Dominio.IExecutionCourse;
import Dominio.ITurno;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.NumberUtils;

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

    public InfoExecutionCourseOccupancy run(Integer executionCourseID)
            throws FenixServiceException {

        InfoExecutionCourseOccupancy infoExecutionCourseOccupancy = new InfoExecutionCourseOccupancy();
        infoExecutionCourseOccupancy.setInfoShifts(new ArrayList());

        try {
            SuportePersistenteOJB sp = SuportePersistenteOJB.getInstance();

            IExecutionCourse executionCourse = (IExecutionCourse) sp
                    .getIPersistentExecutionCourse().readByOID(
                            ExecutionCourse.class, executionCourseID);

            List shifts = sp.getITurnoPersistente().readByExecutionCourse(
                    executionCourse);

            infoExecutionCourseOccupancy
                    .setInfoExecutionCourse(InfoExecutionCourse
                            .newInfoFromDomain(executionCourse));

            Iterator iterator = shifts.iterator();
            while (iterator.hasNext()) {
                ITurno shift = (ITurno) iterator.next();

                List studentsInShift = sp.getITurnoAlunoPersistente()
                        .readByShift(shift);

                shift.setOcupation(new Integer(studentsInShift.size()));
                Integer capacity = new Integer(1);
                if (shift.getLotacao() != null
                        && shift.getLotacao().intValue() != 0) {
                    capacity = shift.getLotacao();
                }
                shift.setPercentage(NumberUtils.formatNumber(new Double(shift
                        .getOcupation().floatValue()
                        * 100 / capacity.floatValue()), 1));
                //CLONER
                //infoExecutionCourseOccupancy.getInfoShifts().add(Cloner.get(shift));
                infoExecutionCourseOccupancy.getInfoShifts().add(
                        InfoShiftWithInfoLessons.newInfoFromDomain(shift));
            }

        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }

        return infoExecutionCourseOccupancy;
    }

}