/*
 * ISeccaoPersistente.java
 *
 * Created on 23 de Agosto de 2002, 16:42
 */

package ServidorPersistente;

/**
 *
 * @author  ars
 */

import Dominio.ISeccao;
import Dominio.ISitio;

public interface ISeccaoPersistente extends IPersistentObject {
    ISeccao readBySitioAndSeccaoAndNome(ISitio sitio, ISeccao seccao, String nome) throws ExcepcaoPersistencia;
    void lockWrite(ISeccao seccao) throws ExcepcaoPersistencia;
    void delete(ISeccao seccao) throws ExcepcaoPersistencia;
    void deleteAll() throws ExcepcaoPersistencia;
}
