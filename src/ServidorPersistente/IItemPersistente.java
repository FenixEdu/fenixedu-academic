/*
 * IItemPersistente.java
 *
 * Created on 19 de Agosto de 2002, 12:05
 */

package ServidorPersistente;

/**
 *
 * @author  ars
 */
import Dominio.IItem;
import Dominio.ISeccao;

public interface IItemPersistente extends IPersistentObject {
    public IItem readBySeccaoAndNome(ISeccao seccao, String nome) throws ExcepcaoPersistencia;
    public void lockWrite(IItem item) throws ExcepcaoPersistencia;
    public void delete(IItem item) throws ExcepcaoPersistencia;
    public void deleteAll() throws ExcepcaoPersistencia;
}
