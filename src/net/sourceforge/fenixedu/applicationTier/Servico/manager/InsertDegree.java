/*
 * Created on 30/Mai/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.sql.Timestamp;
import java.util.Calendar;

import net.sourceforge.fenixedu.dataTransferObject.InfoDegree;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DegreeInfo;
import net.sourceforge.fenixedu.domain.IDegree;
import net.sourceforge.fenixedu.domain.IDegreeInfo;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ICursoPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.TipoCurso;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author lmac1
 */

public class InsertDegree implements IService {

    public void run(InfoDegree infoDegree) throws ExcepcaoPersistencia {

        ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        ICursoPersistente persistentDegree = persistentSuport.getICursoPersistente();

        String code = infoDegree.getSigla();
        String name = infoDegree.getNome();
        TipoCurso type = infoDegree.getTipoCurso();

        IDegree degree = new Degree();
        persistentDegree.simpleLockWrite(degree);
        degree.setSigla(code);
        degree.setNome(name);
        degree.setTipoCurso(type);
        degree.setConcreteClassForDegreeCurricularPlans(DegreeCurricularPlan.class.getName());

        IDegreeInfo degreeInfo = new DegreeInfo();
        degreeInfo.setDegree(degree);
        degreeInfo.setLastModificationDate(new Timestamp(Calendar.getInstance().getTimeInMillis()));
        persistentSuport.getIPersistentDegreeInfo().simpleLockWrite(degreeInfo);

    }
}