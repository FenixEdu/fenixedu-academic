package ServidorPersistente;

/**
 * @author Luis Cruz
 * 
 */
import java.util.List;

import Dominio.IBuilding;

public interface IPersistentBuilding extends IPersistentObject {

    public List readAll() throws ExcepcaoPersistencia;
    public void delete(final IBuilding building) throws ExcepcaoPersistencia;

}