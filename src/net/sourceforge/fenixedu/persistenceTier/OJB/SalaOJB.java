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
import net.sourceforge.fenixedu.domain.IExam;
import net.sourceforge.fenixedu.domain.IRoom;
import net.sourceforge.fenixedu.domain.Room;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISalaPersistente;
import net.sourceforge.fenixedu.util.TipoSala;

import org.apache.commons.collections.CollectionUtils;
import org.apache.ojb.broker.query.Criteria;
import org.odmg.QueryException;

public class SalaOJB extends ObjectFenixOJB implements ISalaPersistente {

    public IRoom readByName(String nome) throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("nome", nome);
        return (IRoom) queryObject(Room.class, crit);
    }

    public List readAll() throws ExcepcaoPersistencia {
        return queryList(Room.class, null);
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

        if (nome == null && edificio == null && piso == null && tipo == null && capacidadeExame == null
                && capacidadeNormal == null) {
            return readAll();
        }

        try {
            StringBuffer oqlQuery = new StringBuffer("select sala from ");
            boolean hasPrevious = false;

            oqlQuery.append(Room.class.getName()).append(" where ");
            if (nome != null) {
                hasPrevious = true;
                oqlQuery.append("nome = \"").append(nome).append("\"");
            }

            if (edificio != null) {
                if (hasPrevious)
                    oqlQuery.append(" and ");
                else
                    hasPrevious = true;

                oqlQuery.append(" building.name = \"").append(edificio).append("\"");
            }

            if (piso != null) {
                if (hasPrevious)
                    oqlQuery.append(" and ");
                else
                    hasPrevious = true;

                oqlQuery.append(" piso = \"").append(piso).append("\"");
            }

            if (tipo != null) {
                if (hasPrevious)
                    oqlQuery.append(" and ");
                else
                    hasPrevious = true;

                oqlQuery.append(" tipo = \"").append(tipo).append("\"");
            }

            if (capacidadeNormal != null) {
                if (hasPrevious)
                    oqlQuery.append(" and ");
                else
                    hasPrevious = true;

                oqlQuery.append(" capacidadeNormal > \"").append(capacidadeNormal.intValue() - 1)
                        .append("\"");
            }

            if (capacidadeExame != null) {
                if (hasPrevious)
                    oqlQuery.append(" and ");
                else
                    hasPrevious = true;

                oqlQuery.append(" capacidadeExame > \"").append(capacidadeExame.intValue() - 1).append(
                        "\"");
            }

            query.create(oqlQuery.toString());
            List result = (List) query.execute();
            lockRead(result);
            return result;
        } catch (QueryException ex) {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
        }
    }

    public List readAvailableRooms(Integer examOID) throws ExcepcaoPersistencia {
        List availableRooms = null;

        IExam examFromDB = (IExam) readByOID(Exam.class, examOID);
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
                IExam someOtherExam = (IExam) otherExams.get(i);
                occupiedRooms.addAll(someOtherExam.getAssociatedRooms());
            }
            Criteria crit2 = new Criteria();
            crit2.addNotEqualTo("tipo", new TipoSala(TipoSala.LABORATORIO));
            List allExamRooms = queryList(Room.class, crit2);
            availableRooms = (List) CollectionUtils.subtract(allExamRooms, occupiedRooms);

        }
        return availableRooms;
    }

    public List readForRoomReservation() throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addNotEqualTo("tipo", new TipoSala(TipoSala.LABORATORIO));
        // criteria.addNotLike("building.name", "Tagus%");
        // criteria.addNotLike("building.name", "Local%");
        return queryList(Room.class, criteria);
    }

    public List readByPavillions(List pavillionsName) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addIn("building.name", pavillionsName);
        return queryList(Room.class, criteria);
    }

    public List readByNormalCapacity(Integer capacity) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addGreaterOrEqualThan("capacidadeNormal", capacity);
        return queryList(Room.class, criteria);
    }

}