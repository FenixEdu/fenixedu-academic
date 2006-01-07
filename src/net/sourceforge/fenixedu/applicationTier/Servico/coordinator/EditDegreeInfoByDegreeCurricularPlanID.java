package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;

import java.util.Calendar;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeInfo;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DomainFactory;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DegreeInfo;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 *
 */
public class EditDegreeInfoByDegreeCurricularPlanID implements IService {
    public void run(Integer degreeCurricularPlanID, InfoDegreeInfo infoDegreeInfo)
            throws FenixServiceException, ExcepcaoPersistencia {
        
        ISuportePersistente suportePersistente = PersistenceSupportFactory
                .getDefaultPersistenceSupport();

        if (degreeCurricularPlanID == null) {
            throw new FenixServiceException("error.impossibleEditDegreeInfo");
        }

        DegreeCurricularPlan degreeCurricularPlan = (DegreeCurricularPlan) suportePersistente
                .getIPersistentDegreeCurricularPlan().readByOID(DegreeCurricularPlan.class,
                        degreeCurricularPlanID);

        DegreeInfo degreeInfo = DomainFactory.makeDegreeInfo();

        // update information that it will be displayed in degree site.
        degreeInfo.setDescription(infoDegreeInfo.getDescription());
        degreeInfo.setObjectives(infoDegreeInfo.getObjectives());
        degreeInfo.setHistory(infoDegreeInfo.getHistory());
        degreeInfo.setProfessionalExits(infoDegreeInfo.getProfessionalExits());
        degreeInfo.setAdditionalInfo(infoDegreeInfo.getAdditionalInfo());
        degreeInfo.setLinks(infoDegreeInfo.getLinks());
        degreeInfo.setTestIngression(infoDegreeInfo.getTestIngression());
        degreeInfo.setDriftsInitial(infoDegreeInfo.getDriftsInitial());
        degreeInfo.setDriftsFirst(infoDegreeInfo.getDriftsFirst());
        degreeInfo.setDriftsSecond(infoDegreeInfo.getDriftsSecond());
        degreeInfo.setClassifications(infoDegreeInfo.getClassifications());
        degreeInfo.setMarkMin(infoDegreeInfo.getMarkMin());
        degreeInfo.setMarkMax(infoDegreeInfo.getMarkMax());
        degreeInfo.setMarkAverage(infoDegreeInfo.getMarkAverage());
        degreeInfo.setQualificationLevel(infoDegreeInfo.getQualificationLevel());
        degreeInfo.setRecognitions(infoDegreeInfo.getRecognitions());

        // update information in english that it will be displayed in degree
        // site.
        degreeInfo.setDescriptionEn(infoDegreeInfo.getDescriptionEn());
        degreeInfo.setObjectivesEn(infoDegreeInfo.getObjectivesEn());
        degreeInfo.setHistoryEn(infoDegreeInfo.getHistoryEn());
        degreeInfo.setProfessionalExitsEn(infoDegreeInfo.getProfessionalExitsEn());
        degreeInfo.setAdditionalInfoEn(infoDegreeInfo.getAdditionalInfoEn());
        degreeInfo.setLinksEn(infoDegreeInfo.getLinksEn());
        degreeInfo.setTestIngressionEn(infoDegreeInfo.getTestIngressionEn());
        degreeInfo.setClassificationsEn(infoDegreeInfo.getClassificationsEn());
        degreeInfo.setQualificationLevelEn(infoDegreeInfo.getQualificationLevelEn());
        degreeInfo.setRecognitionsEn(infoDegreeInfo.getRecognitionsEn());

        // update last modification date
        degreeInfo.setLastModificationDate(Calendar.getInstance().getTime());

        degreeCurricularPlan.getDegree().addDegreeInfos(degreeInfo);

    }
}