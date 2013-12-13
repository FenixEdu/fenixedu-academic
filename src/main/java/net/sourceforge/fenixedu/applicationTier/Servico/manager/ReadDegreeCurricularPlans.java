/*
 * Created on 4/Set/2003, 13:55:41
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Filtro.SeminaryCoordinatorOrStudentFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;
import pt.ist.fenixframework.Atomic;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 *         Created at 4/Set/2003, 13:55:41
 * 
 */
public class ReadDegreeCurricularPlans {

    /**
     * Executes the service. Returns the current InfoDegreeCurricularPlan.
     * 
     * @throws ExcepcaoPersistencia
     */
    @Atomic
    public static List run() {
        final List curricularPlans = DegreeCurricularPlan.readByCurricularStage(CurricularStage.OLD);
        final List infoCurricularPlans = new ArrayList(curricularPlans.size());

        for (final Iterator iter = curricularPlans.iterator(); iter.hasNext();) {
            final DegreeCurricularPlan curricularPlan = (DegreeCurricularPlan) iter.next();
            infoCurricularPlans.add(InfoDegreeCurricularPlan.newInfoFromDomain(curricularPlan));
        }
        return infoCurricularPlans;
    }

    // Service Invokers migrated from Berserk

    private static final ReadDegreeCurricularPlans serviceInstance = new ReadDegreeCurricularPlans();

    @Atomic
    public static List runReadDegreeCurricularPlans() throws NotAuthorizedException {
        SeminaryCoordinatorOrStudentFilter.instance.execute();
        return serviceInstance.run();
    }

}