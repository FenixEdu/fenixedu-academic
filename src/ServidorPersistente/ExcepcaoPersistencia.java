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
    
    public static final int EXISTING = 9;
    public static final String EXISTING_KEY = "exception.error.existing";
	public static final String NON_EXISTING_KEY = "exception.error.nonExisting";

    private int _erro;
    private String errorKey;
    /**
     * Creates a new instance of <code>ExcepcaoPersistencia</code> without detail message.
     */
    public ExcepcaoPersistencia() {
    }

    public ExcepcaoPersistencia(int erro, Exception ex) {
      super(ex);
      _erro = erro;
    }

	public ExcepcaoPersistencia(String error, Exception ex) {
	  super(ex);
	  this.errorKey = error;
	}



    /**
     * Constructs an instance of <code>ExcepcaoPersistencia</code> with the specified detail message.
     * @param msg the detail message.
     */
    public ExcepcaoPersistencia(String msg) {
        super(msg);
        this.errorKey = msg;
    }

    public int getErro() {
        return _erro;
    }
    
	public String getErrorKey() {
		return this.errorKey;
	}
    
    
}

