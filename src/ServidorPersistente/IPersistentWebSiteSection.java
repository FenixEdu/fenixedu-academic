package ServidorPersistente;

import java.util.List;

import Dominio.IWebSite;
import Dominio.IWebSiteSection;

/**
 * @author Fernanda Quitério 23/09/2003
 *  
 */
public interface IPersistentWebSiteSection extends IPersistentObject {

    public void delete(IWebSiteSection section) throws ExcepcaoPersistencia;

    public IWebSiteSection readByWebSiteAndName(IWebSite webSite, String name)
            throws ExcepcaoPersistencia;

    public IWebSiteSection readByName(String name) throws ExcepcaoPersistencia;

    public List readByWebSite(IWebSite webSite) throws ExcepcaoPersistencia;

    public List readAll() throws ExcepcaoPersistencia;
}