/*
 * ExcepcaoPersistencia.java
 *
 * Created on 21 de Agosto de 2002, 16:29
 */

package ServidorPersistente;

/**
 *
 * @author  ars
 */
public class ExcepcaoPersistencia extends java.lang.Exception {
    public static final int OPEN_DATABASE = 1;
    public static final int BEGIN_TRANSACTION = 2;
    public static final int COMMIT_TRANSACTION = 3;
    public static final int ABORT_TRANSACTION = 4;
    public static final int UPGRADE_LOCK = 5;
    public static final int READ_LOCK = 6;
    public static final int QUERY = 7;
    public static final int CLOSE_DATABASE = 8;

    private int _erro;
    /**
     * Creates a new instance of <code>ExcepcaoPersistencia</code> without detail message.
     */
    public ExcepcaoPersistencia() {
    }

    public ExcepcaoPersistencia(int erro, Exception ex) {
      super(ex);
      _erro = erro;
    }

    /**
     * Constructs an instance of <code>ExcepcaoPersistencia</code> with the specified detail message.
     * @param msg the detail message.
     */
    public ExcepcaoPersistencia(String msg) {
        super(msg);
    }

    public int getErro() {
        return _erro;
    }
}
