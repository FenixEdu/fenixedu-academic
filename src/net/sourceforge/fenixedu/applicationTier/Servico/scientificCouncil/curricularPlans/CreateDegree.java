package net.sourceforge.fenixedu.applicationTier.Servico.scientificCouncil.curricularPlans;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.degree.BolonhaDegreeType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.util.MarkType;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class CreateDegree implements IService {

    public void run(String namePt, String nameEn, String code, BolonhaDegreeType bolonhaDegreeType, MarkType markType) throws ExcepcaoPersistencia, FenixServiceException {
        DomainFactory.makeDegree(namePt, nameEn, code, bolonhaDegreeType, markType);
    }

}
