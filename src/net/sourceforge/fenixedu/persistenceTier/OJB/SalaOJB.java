/*
 * SalaOJB.java
 * 
 * Created on 21 de Agosto de 2002, 16:36
 */

package net.sourceforge.fenixedu.persistenceTier.OJB;

/**
 * @author ars
 */

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.Exam;
import net.sourceforge.fenixedu.domain.space.OldRoom;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISalaPersistente;
import net.sourceforge.fenixedu.util.TipoSala;

import org.apache.commons.collections.CollectionUtils;
import org.apache.ojb.broker.query.Criteria;

public class SalaOJB extends ObjectFenixOJB implements ISalaPersistente {

    public OldRoom readByName(String nome) throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("nome", nome);
        return (OldRoom) queryObject(OldRoom.class, crit);
    }

    public List readAll() throws ExcepcaoPersistencia {
        return queryList(OldRoom.class, null);
    }

    /**
     * Reads all salas that with certains properties. The properties are
     * specified by the arguments of this method. If an argument is null, then
     * the sala can have any value concerning that argument. In what concerns
     * the capacidadeNormal and capacidadeExame, this two arguments specify the
     * minimal value that a sala can have in order to be selected.
     * 
     * @return a list with all salas that satisfy the conditions specified by
     *         the non-null arguments.
     */
    public List readSalas(String nome, String edificio, Integer piso, Integer tipo,
            Integer capacidadeNormal, Integer capacidadeExame) throws ExcepcaoPersistencia {
        
        List<OldRoom> rooms = (List<OldRoom>) readAll(OldRoom.class);
        List<OldRoom> result = new ArrayList();
        for (OldRoom room : rooms) {
            boolean isAcceptable = true;
            if (nome != null && !room.getNome().equalsIgnoreCase(nome)) {
                continue;
            }
            if (edificio != null && !room.getBuilding().getName().equalsIgnoreCase(edificio)) {
                continue;
            }
            if (piso != null && !room.getPiso().equals(piso)) {
                continue;
            }
            if (tipo != null && !room.getTipo().getTipo().equals(tipo)) {
                continue;
            }
            if (capacidadeNormal != null
                    && room.getCapacidadeNormal().intValue() < capacidadeNormal.intValue()) {
                continue;
            }
            if (capacidadeExame != null
                    && room.getCapacidadeExame().intValue() < capacidadeExame.intValue()) {
                continue;
            }
            if (isAcceptable) {
                result.add(room);
            }
        }
        return result;
    }

    public List readAvailableRooms(Integer examOID) throws ExcepcaoPersistencia {
        List availableRooms = null;

        Exam examFromDB = (Exam) readByOID(Exam.class, examOID);
        if (examFromDB != null) {
            Criteria crit = new Criteria();
            crit.addNotEqualTo("idInternal", examFromDB.getIdInternal());
            crit.addEqualTo("day", examFromDB.getDay());
            crit.addEqualTo("beginning", examFromDB.getBeginning());
            crit.addEqualTo("associatedExecutionCourses.executionPeriod.name",
                    examFromDB.getAssociatedExecutionCourses().get(0)
                            .getExecutionPeriod().getName());
            crit.addEqualTo("associatedExecutionCourses.executionPeriod.executionYear.year",
                    examFromDB.getAssociatedExecutionCourses().get(0)
                            .getExecutionPeriod().getExecutionYear().getYear());
            List otherExams = queryList(Exam.class, crit);

            List occupiedRooms = new ArrayList();
            for (int i = 0; i < otherExams.size(); i++) {
                Exam someOtherExam = (Exam) otherExams.get(i);
                occupiedRooms.addAll(someOtherExam.getAssociatedRooms());
            }
            Criteria crit2 = new Criteria();
            crit2.addNotEqualTo("tipo", new TipoSala(TipoSala.LABORATORIO));
            List allExamRooms = queryList(OldRoom.class, crit2);
            availableRooms = (List) CollectionUtils.subtract(allExamRooms, occupiedRooms);

        }
        return availableRooms;
    }

    public List readForRoomReservation() throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addNotEqualTo("tipo", new TipoSala(TipoSala.LABORATORIO));
        return queryList(OldRoom.class, criteria);
    }

    public List readByPavillions(List pavillionsName) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addIn("building.name", pavillionsName);
        return queryList(OldRoom.class, criteria);
    }

    public List readByNormalCapacity(Integer capacity) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addGreaterOrEqualThan("capacidadeNormal", capacity);
        return queryList(OldRoom.class, criteria);
    }

}