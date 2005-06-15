import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExam;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.OJB.ObjectFenixOJB;

/*
 * Created on 6/Jun/2005
 */

public class DaoTester extends ObjectFenixOJB {

    private static final ISuportePersistente persistentSupport;
    private static final DaoTester instance = new DaoTester();

    static {
        try {
            persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        } catch (ExcepcaoPersistencia e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        System.out.println("Hello world.");
        try {
            System.out.println("Hellooooooo");
            persistentSupport.iniciarTransaccao();
            System.out.println("Transaction started.");
            instance.doTest();
            persistentSupport.confirmarTransaccao();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.exit(0);
    }

    private void doTest() throws ExcepcaoPersistencia {
        IPersistentExam persistentExam = persistentSupport.getIPersistentExam();
        persistentExam.readByRoomAndExecutionPeriod("Ga1", "2 Semestre", "2004/2005");
    }

}
