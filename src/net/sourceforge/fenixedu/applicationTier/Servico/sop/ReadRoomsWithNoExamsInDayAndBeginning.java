/*
 * ReadExamsByDayAndBeginning.java
 *
 * Created on 2003/03/19
 */

package net.sourceforge.fenixedu.applicationTier.Servico.sop;

/**
 * @author Luis Cruz & Sara Ribeiro
 *  
 */
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IServico;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoom;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IExam;
import net.sourceforge.fenixedu.domain.IRoom;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.commons.collections.CollectionUtils;

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
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
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

            availableRooms = (ArrayList) CollectionUtils.subtract(allRooms, occupiedRooms);
            for (int i = 0; i < availableRooms.size(); i++) {
                IRoom room = (IRoom) availableRooms.get(i);
                InfoRoom infoRoom = Cloner.copyRoom2InfoRoom(room);
                availableInfoRooms.add(infoRoom);
            }
        } catch (ExcepcaoPersistencia ex) {
            ex.printStackTrace();
        }
        return availableInfoRooms;
    }
}