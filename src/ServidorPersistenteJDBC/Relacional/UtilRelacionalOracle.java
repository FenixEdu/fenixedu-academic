package ServidorPersistenteJDBC.Relacional;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import org.apache.commons.collections.FastHashMap;
import org.apache.ojb.broker.PBKey;
import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerFactory;
import org.apache.ojb.broker.accesslayer.LookupException;

/**
 * @author Nanda & Tânia
 */
public class UtilRelacionalOracle {
    private static Map pbMap = new FastHashMap();

    private static PBKey pbKey = new PBKey("assiduousness");

    public static synchronized void inicializarBaseDados(String filename) {
    }

    /**
     * @throws Exception
     * @deprecated
     */
    public static void limparTabelas() throws Exception {
    }

    public static int ultimoIdGerado() throws Exception {
        int ultimoIdGerado = 0;
        try {
            //verificar para oracle
            PreparedStatement comando = prepararComando("SELECT LAST_INSERT_ID()");
            ResultSet resultado = comando.executeQuery();
            if (resultado.next())
                ultimoIdGerado = resultado.getInt(1);
        } catch (Exception e) {
            System.out.println("UtilRelacionalOracle.ultimoIdGerado: " + e.toString());
            throw e;
        }
        return ultimoIdGerado;
    }

    public static void iniciarTransaccao() throws Exception {
        PersistenceBroker pb = PersistenceBrokerFactory.createPersistenceBroker(pbKey);
        pbMap.put(Thread.currentThread(), pb);
        pb.beginTransaction();
    }

    public static void confirmarTransaccao() throws Exception {
        PersistenceBroker pb = (PersistenceBroker) pbMap.remove(Thread.currentThread());
        pb.commitTransaction();
        pb.close();
    }

    public static void cancelarTransaccao() throws Exception {
        PersistenceBroker pb = (PersistenceBroker) pbMap.remove(Thread.currentThread());
        pb.abortTransaction();
        pb.close();
    }

    public static PreparedStatement prepararComando(String statement) {
        Connection conn;

        PersistenceBroker pb = (PersistenceBroker) pbMap.get(Thread.currentThread());
        if (pb == null) {
            throw new IllegalStateException("#MAYBE IT IS OLD SHARED CONNECTION CODE!");
        }
        try {
            conn = pb.serviceConnectionManager().getConnection();
        } catch (LookupException e1) {
            e1.printStackTrace(System.out);
            throw new IllegalStateException(e1.getMessage());
        }
        try {
            if (conn == null || conn.isClosed()) {
                throw new IllegalStateException("#Connection closed!");
            }
        } catch (SQLException e2) {
            e2.printStackTrace();
            throw new IllegalStateException(e2.getMessage());
        }

        PreparedStatement sql = null;
        try {
            sql = conn.prepareStatement(statement);
        } catch (java.sql.SQLException e) {
            e.printStackTrace(System.out);
            System.out.println("#UtilRelacional: " + e.toString());
        }
        return sql;

    }
}