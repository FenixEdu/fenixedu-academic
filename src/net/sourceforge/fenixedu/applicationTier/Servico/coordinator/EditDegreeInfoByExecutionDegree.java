package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;

import java.sql.Timestamp;
import java.util.Calendar;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeInfo;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeInfoWithDegree;
import net.sourceforge.fenixedu.domain.DegreeInfo;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.IDegree;
import net.sourceforge.fenixedu.domain.IDegreeInfo;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.IExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentDegreeInfo;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Tânia Pousão Created on 5/Nov/2003
 */
public class EditDegreeInfoByExecutionDegree implements IService {

    public InfoDegreeInfo run(Integer infoExecutionDegreeId, Integer infoDegreeInfoId,
            InfoDegreeInfo infoDegreeInfo) throws FenixServiceException {
        IDegreeInfo degreeInfo = null;
        try {
            ISuportePersistente suportePersistente = PersistenceSupportFactory.getDefaultPersistenceSupport();

            if (infoExecutionDegreeId == null || infoDegreeInfoId == null || infoDegreeInfo == null) {
                throw new FenixServiceException("error.impossibleEditDegreeInfo");
            }

            //Execution Degree
            IPersistentExecutionDegree cursoExecucaoPersistente = suportePersistente
                    .getIPersistentExecutionDegree();
            IExecutionDegree executionDegree = (IExecutionDegree) cursoExecucaoPersistente.readByOID(
                    ExecutionDegree.class, infoExecutionDegreeId);

            if (executionDegree == null) {
                throw new FenixServiceException("error.impossibleEditDegreeInfo");
            }
            if (executionDegree.getDegreeCurricularPlan() == null) {
                throw new FenixServiceException("error.impossibleEditDegreeInfo");
            }

            //Degree
            IDegree degree = executionDegree.getDegreeCurricularPlan().getDegree();
            if (degree == null) {
                throw new FenixServiceException("error.impossibleEditDegreeInfo");
            }

            //DegreeInfo
            IPersistentDegreeInfo persistentDegreeInfo = suportePersistente.getIPersistentDegreeInfo();
            degreeInfo = (IDegreeInfo) persistentDegreeInfo.readByOID(DegreeInfo.class,
                    infoDegreeInfoId, true);

            //verify if the record found is in this execution period
            //or if is new in database
            //if it isn't, is necessary to create a new record
            if (degreeInfo == null
                    || (!verifyExecutionYear(degreeInfo.getLastModificationDate(), executionDegree
                            .getExecutionYear()))) {
                degreeInfo = new DegreeInfo();

                //associate the degree
                degreeInfo.setDegree(degree);

                persistentDegreeInfo.simpleLockWrite(degreeInfo);
            }

            //update information that it will be displayed in degree site.
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

            //update information in english that it will be displayed in degree
            // site.
            degreeInfo.setDescriptionEn(infoDegreeInfo.getDescriptionEn());
            degreeInfo.setObjectivesEn(infoDegreeInfo.getObjectivesEn());
            degreeInfo.setHistoryEn(infoDegreeInfo.getHistoryEn());
            degreeInfo.setProfessionalExitsEn(infoDegreeInfo.getProfessionalExitsEn());
            degreeInfo.setAdditionalInfoEn(infoDegreeInfo.getAdditionalInfoEn());
            degreeInfo.setLinksEn(infoDegreeInfo.getLinksEn());
            degreeInfo.setTestIngressionEn(infoDegreeInfo.getTestIngressionEn());
            degreeInfo.setClassificationsEn(infoDegreeInfo.getClassificationsEn());

            //update last modification date
            degreeInfo.setLastModificationDate(new Timestamp(Calendar.getInstance().getTimeInMillis()));

        } catch (ExcepcaoPersistencia e) {
            e.printStackTrace();
            throw new FenixServiceException(e);
        }
        //CLONER
        //return Cloner.copyIDegreeInfo2InfoDegree(degreeInfo);
        return InfoDegreeInfoWithDegree.newInfoFromDomain(degreeInfo);
    }

    private boolean verifyExecutionYear(Timestamp lastModificationDate, IExecutionYear year) {
        boolean result = false;

        if ((!lastModificationDate.before(year.getBeginDate()))
                && (!lastModificationDate.after(year.getEndDate()))) {
            result = true;
        }
        return result;
    }
}