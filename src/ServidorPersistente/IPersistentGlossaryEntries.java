package ServidorPersistente;

import java.util.List;

/**
 * Created on 2004/08/30
 * 
 * @author Luis Cruz
 *  
 */
public interface IPersistentGlossaryEntries extends IPersistentObject {

    public List readAll() throws ExcepcaoPersistencia;

}