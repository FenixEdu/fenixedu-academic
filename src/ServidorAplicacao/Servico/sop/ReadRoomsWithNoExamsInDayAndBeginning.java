/*
 * ReadExamsByDayAndBeginning.java
 *
 * Created on 2003/03/19
 */

package ServidorAplicacao.Servico.sop;

/**
 * @author Luis Cruz & Sara Ribeiro
 *  
 */
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import DataBeans.InfoRoom;
import DataBeans.util.Cloner;
import Dominio.IExam;
import Dominio.ISala;
import ServidorAplicacao.IServico;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class ReadRoomsWithNoExamsInDayAndBeginning implements IServico {

    private static ReadRoomsWithNoExamsInDayAndBeginning _servico = new ReadRoomsWithNoExamsInDayAndBeginning();

    /**
     * The singleton access method of this class.
     */
    public static ReadRoomsWithNoExamsInDayAndBeginning getService() {
        return _servico;
    }

    /**
     * The actor of this class.
     */
    private ReadRoomsWithNoExamsInDayAndBeginning() {
    }

    /**
     * Devolve o nome do servico
     */
    public final String getNome() {
        return "ReadRoomsWithNoExamsInDayAndBeginning";
    }

    public List run(Calendar day, Calendar beginning) {
        List availableRooms = new ArrayList();
        List availableInfoRooms = new ArrayList();

        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            List exams = sp.getIPersistentExam().readBy(day, beginning);
            List allRooms = sp.getISalaPersistente().readAll();

            List occupiedRooms = new ArrayList();
            for (int i = 0; i < exams.size(); i++) {
                List examRooms = ((IExam) exams.get(i)).getAssociatedRooms();
                if (examRooms != null && examRooms.size() > 0) {
                    for (int r = 0; r < examRooms.size(); r++) {
                        occupiedRooms.add(examRooms.get(r));
                    }
                }
            }
            //System.out.println("## All Rooms = " + allRooms.size());
            //System.out.println("## Occupied Rooms = " +
            // occupiedRooms.size());

            availableRooms = (ArrayList) CollectionUtils.subtract(allRooms, occupiedRooms);
            //System.out.println("## Available Rooms = " +
            // availableRooms.size());
            for (int i = 0; i < availableRooms.size(); i++) {
                ISala room = (ISala) availableRooms.get(i);
                InfoRoom infoRoom = Cloner.copyRoom2InfoRoom(room);
                availableInfoRooms.add(infoRoom);
            }
        } catch (ExcepcaoPersistencia ex) {
            ex.printStackTrace();
        }
        return availableInfoRooms;
    }
}