package ServidorPersistente;

/**
 * @author Fernanda Quitério
 * 23/09/2003
 * 
 */
import java.util.List;

import Dominio.IWebSite;

public interface IPersistentWebSite extends IPersistentObject{
    
    List readAll() throws ExcepcaoPersistencia;
    void lockWrite(IWebSite site) throws ExcepcaoPersistencia;
    void delete(IWebSite site) throws ExcepcaoPersistencia;
    
}
