/*
 * Created on 18/Dez/2003
 *
 */
package middleware.studentMigration.ileecDataMigration;

import java.util.Iterator;
import java.util.List;

import middleware.middlewareDomain.MWAreaCientificaIleec;
import middleware.middlewareDomain.MWDisciplinaIleec;
import middleware.persistentMiddlewareSupport.IPersistentMWAreaCientificaIleec;
import middleware.persistentMiddlewareSupport.IPersistentMWDisciplinaIleec;
import middleware.persistentMiddlewareSupport.IPersistentMiddlewareSupport;
import middleware.persistentMiddlewareSupport.OJBDatabaseSupport.PersistentMiddlewareSupportOJB;
import Dominio.DegreeCurricularPlan;
import Dominio.ICurricularCourse;
import Dominio.IDegreeCurricularPlan;
import Dominio.IScientificArea;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.IPersistentDegreeCurricularPlan;
import ServidorPersistente.IPersistentScientificArea;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 */
public class associateILeecCoursesToScientificAreas
{

    public static void main(String[] args)
    {
        System.out.println("INICIO");
        ISuportePersistente sp;
        try
        {
            sp = SuportePersistenteOJB.getInstance();

            IPersistentDegreeCurricularPlan pdcp = sp.getIPersistentDegreeCurricularPlan();
            IPersistentCurricularCourse pcc = sp.getIPersistentCurricularCourse();

            sp.iniciarTransaccao();
            IDegreeCurricularPlan dcp =
                (IDegreeCurricularPlan) pdcp.readByOID(DegreeCurricularPlan.class, new Integer(48));
            //48->dcp LEEC2003/2004
            
            List curricularCourses = pcc.readCurricularCoursesByDegreeCurricularPlan(dcp);

            Iterator iterator = curricularCourses.iterator();
            while (iterator.hasNext())
            {                
                ICurricularCourse cc = (ICurricularCourse) iterator.next();
                pcc.simpleLockWrite(cc);

                if (!cc.getName().equalsIgnoreCase("Opção Livre") && !cc.getCode().equalsIgnoreCase("##13"))
                {
                    IScientificArea scientificArea = getScientificArea(cc, sp);
                    cc.setScientificArea(scientificArea);
                }

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

    public static IScientificArea getScientificArea(
        ICurricularCourse curricularCourse,
        ISuportePersistente sp)
    {
        IPersistentMiddlewareSupport pms = PersistentMiddlewareSupportOJB.getInstance();
        IPersistentMWDisciplinaIleec pmwdi = pms.getIPersistentMWDisciplinaIleec();
        IPersistentMWAreaCientificaIleec mwaci = pms.getIPersistentMWAreaCientificaIleec();

        IPersistentScientificArea psa = sp.getIPersistentScientificArea();

        IScientificArea scientificArea = null;
        try
        {
            MWDisciplinaIleec mwDiscIleec = pmwdi.readByCodigoDisciplina(curricularCourse.getCode());
            Integer idScientificArea = mwDiscIleec.getIdAreaCientifica();

            MWAreaCientificaIleec mwAreaCientifIleec = mwaci.readById(idScientificArea);
            String scientificAreaName = mwAreaCientifIleec.getNome();

            scientificArea = psa.readByName(scientificAreaName);
        }
        catch (ExcepcaoPersistencia e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return scientificArea;
    }

}
