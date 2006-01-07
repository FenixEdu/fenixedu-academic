/*
 * SalaOJB.java
 * 
 * Created on 21 de Agosto de 2002, 16:36
 */

package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao;

/**
 * @author ars
 */

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.Exam;
import net.sourceforge.fenixedu.domain.Exam;
import net.sourceforge.fenixedu.domain.space.Building;
import net.sourceforge.fenixedu.domain.space.Building;
import net.sourceforge.fenixedu.domain.space.Room;
import net.sourceforge.fenixedu.domain.space.RoomOccupation;
import net.sourceforge.fenixedu.domain.space.Room;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISalaPersistente;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;
import net.sourceforge.fenixedu.util.TipoSala;

public class SalaVO extends VersionedObjectsBase implements ISalaPersistente {

    public Room readByName(String nome) throws ExcepcaoPersistencia {
        List<Room> rooms = (List<Room>) readAll(Room.class);
        for (Room room : rooms) {
            if (room.getNome().equalsIgnoreCase(nome)) {
                return room;
            }
        }
        return null;

        /*
         * Criteria crit = new Criteria(); crit.addEqualTo("nome", nome); return
         * (Room) queryObject(Room.class, crit);
         */
    }

    public List readAll() throws ExcepcaoPersistencia {
        return (List) readAll(Room.class);
    }

    public List readSalas(String nome, String edificio, Integer piso, Integer tipo,
            Integer capacidadeNormal, Integer capacidadeExame) throws ExcepcaoPersistencia {

        List<Room> rooms = (List<Room>) readAll(Room.class);
        List<Room> result = new ArrayList();
        for (Room room : rooms) {
            boolean isAcceptable = true;
            if (nome != null && !room.getNome().equalsIgnoreCase(nome)) {
                isAcceptable = false;
            }
            if (edificio != null && !room.getBuilding().getName().equalsIgnoreCase(edificio)) {
                isAcceptable = false;
            }
            if (piso != null && !room.getPiso().equals(piso)) {
                isAcceptable = false;
            }
            if (tipo != null && !room.getTipo().getTipo().equals(tipo)) {
                isAcceptable = false;
            }
            if (capacidadeNormal != null
                    && room.getCapacidadeNormal().intValue() < capacidadeNormal.intValue()) {
                isAcceptable = false;
            }
            if (capacidadeExame != null
                    && room.getCapacidadeExame().intValue() < capacidadeExame.intValue()) {
                isAcceptable = false;
            }
            if (isAcceptable) {
                result.add(room);
            }
        }
        return result;
    }

    public List readAvailableRooms(Integer examOID) throws ExcepcaoPersistencia {
        Exam exam = (Exam) readByOID(Exam.class, examOID);
        List<Room> result = new ArrayList();
        List<Room> rooms = (List<Room>) readAll(Room.class);
        for (Room room : rooms) {
            if (!(room.getTipo().getTipo().intValue() == TipoSala.LABORATORIO)) {
                List<RoomOccupation> roomOccupations = room.getRoomOccupations();
                boolean isOccupied = false;
                for (RoomOccupation roomOccupation : roomOccupations) {
                    if (roomOccupation.containedIn(exam.getDay(), exam.getBeginning(), exam.getEnd())) {
                        isOccupied = true;
                        break;
                    }
                }
                if (!isOccupied) {
                    result.add(room);
                }
            }
        }
        return result;

    }

    public List readForRoomReservation() throws ExcepcaoPersistencia {
        List<Room> rooms = (List<Room>) readAll(Room.class);
        List<Room> result = new ArrayList();
        for (Room room : rooms) {
            if (!(room.getTipo().getTipo().intValue() == TipoSala.LABORATORIO)) {
                result.add(room);
            }
        }
        return result;

        /*
         * Criteria criteria = new Criteria(); criteria.addNotEqualTo("tipo",
         * new TipoSala(TipoSala.LABORATORIO)); //
         * criteria.addNotLike("building.name", "Tagus%"); //
         * criteria.addNotLike("building.name", "Local%"); return
         * queryList(Room.class, criteria);
         */
    }

    public List readByPavillions(List pavillionsName) throws ExcepcaoPersistencia {
        List<Room> result = new ArrayList();
        List<Building> buildings = (List<Building>) readAll(Building.class);
        for (Object buildingName : pavillionsName) {
            for (Building building : buildings) {
                if (((String) buildingName).equalsIgnoreCase(building.getName())) {
                    result.addAll(building.getRooms());
                }
            }
        }
        return result;

        /*
         * Criteria criteria = new Criteria(); criteria.addIn("building.name",
         * pavillionsName); return queryList(Room.class, criteria);
         */
    }

    public List readByNormalCapacity(Integer capacity) throws ExcepcaoPersistencia {
        List<Room> rooms = (List<Room>) readAll(Room.class);
        List<Room> result = new ArrayList();
        for (Room room : rooms) {
            if (room.getCapacidadeNormal().intValue() >= capacity.intValue()) {
                result.add(room);
            }
        }
        return result;

        /*
         * Criteria criteria = new Criteria();
         * criteria.addGreaterOrEqualThan("capacidadeNormal", capacity); return
         * queryList(Room.class, criteria);
         */
    }
}