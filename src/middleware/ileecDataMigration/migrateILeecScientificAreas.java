/*
 * Created on 18/Dez/2003
 *
 */
package middleware.ileecDataMigration;

import java.util.Iterator;
import java.util.List;

import middleware.middlewareDomain.MWAreaCientificaIleec;
import middleware.persistentMiddlewareSupport.IPersistentMWAreaCientificaIleec;
import middleware.persistentMiddlewareSupport.IPersistentMiddlewareSupport;
import middleware.persistentMiddlewareSupport.OJBDatabaseSupport.PersistentMiddlewareSupportOJB;
import Dominio.IScientificArea;
import Dominio.ScientificArea;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentScientificArea;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 */
public class migrateILeecScientificAreas
{

    public static void main(String[] args)
    {
    	System.out.println("INICIO");
        try
        {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentMiddlewareSupport pms = PersistentMiddlewareSupportOJB.getInstance();

            IPersistentMWAreaCientificaIleec pmwac = pms.getIPersistentMWAreaCientificaIleec();
            IPersistentScientificArea psa = sp.getIPersistentScientificArea();

            sp.iniciarTransaccao();
            List areasCientificasIleec = pmwac.readAll();

            Iterator iter = areasCientificasIleec.iterator();
            while (iter.hasNext())
            {

                MWAreaCientificaIleec areaCientificaIleec = (MWAreaCientificaIleec) iter.next();
                String scientificAreaName = areaCientificaIleec.getNome();

                IScientificArea scientificArea = new ScientificArea();

                psa.simpleLockWrite(scientificArea);

                scientificArea.setName(scientificAreaName);

            }

            sp.confirmarTransaccao();
            System.out.println("FIM");

        }
        catch (ExcepcaoPersistencia e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
