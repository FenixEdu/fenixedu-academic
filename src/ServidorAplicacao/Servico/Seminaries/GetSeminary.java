/*
 * Created on 31/Jul/2003, 19:12:41
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package ServidorAplicacao.Servico.Seminaries;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.Seminaries.InfoSeminary;
import DataBeans.Seminaries.InfoSeminaryWithEquivalenciesWithAll;
import Dominio.Seminaries.ISeminary;
import Dominio.Seminaries.Seminary;
import ServidorApresentacao.Action.Seminaries.Exceptions.BDException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.Seminaries.IPersistentSeminary;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 * Created at 31/Jul/2003, 19:12:41
 *  
 */
public class GetSeminary implements IService {

    public GetSeminary() {
    }

    public InfoSeminary run(Integer seminaryID) throws BDException {
        InfoSeminary infoSeminary = null;
        try {
            ISuportePersistente persistenceSupport = SuportePersistenteOJB.getInstance();
            IPersistentSeminary persistentSeminary = persistenceSupport.getIPersistentSeminary();
            ISeminary seminary = (ISeminary) persistentSeminary.readByOID(Seminary.class, seminaryID);
            if (seminary != null) {

                infoSeminary = InfoSeminaryWithEquivalenciesWithAll.newInfoFromDomain(seminary);
            }
        } catch (ExcepcaoPersistencia ex) {
            throw new BDException("Got an error while trying to retrieve a seminary from the database",
                    ex);
        }
        return infoSeminary;
    }
}