package ServidorAplicacao.Servico.coordinator;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoDegreeInfo;
import DataBeans.InfoDegreeInfoWithDegree;
import Dominio.DegreeCurricularPlan;
import Dominio.DegreeInfo;
import Dominio.ICurso;
import Dominio.ICursoExecucao;
import Dominio.IDegreeCurricularPlan;
import Dominio.IDegreeInfo;
import Dominio.IExecutionYear;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentDegreeInfo;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * 
 * @author  <a href="mailto:amam@mega.ist.utl.pt">Amin Amirali</a>
 * @author  <a href="mailto:frnp@mega.ist.utl.pt">Francisco Paulo</a>
 * 
 */
public class EditDegreeInfoByDegreeCurricularPlanID implements IService {
    public InfoDegreeInfo run(Integer degreeCurricularPlanID, Integer infoDegreeInfoId,
            InfoDegreeInfo infoDegreeInfo) throws FenixServiceException {
        IDegreeInfo degreeInfo = null;
        List executionDegrees = null;
        try {
            ISuportePersistente suportePersistente = SuportePersistenteOJB.getInstance();

            if (degreeCurricularPlanID == null || infoDegreeInfoId == null || infoDegreeInfo == null) {
                throw new FenixServiceException("error.impossibleEditDegreeInfo");
            }

            // degree curricular plan
            IDegreeCurricularPlan degreeCurricularPlan = (IDegreeCurricularPlan) suportePersistente
                    .getIPersistentDegreeCurricularPlan().readByOID(DegreeCurricularPlan.class,
                            degreeCurricularPlanID);

            if (degreeCurricularPlan == null) {
                throw new FenixServiceException("error.impossibleEditDegreeInfo");
            }

            // and correspondent execution degrees
            executionDegrees = suportePersistente.getIPersistentExecutionDegree()
                    .readByDegreeCurricularPlan(degreeCurricularPlan);

            // sort them by begin date

            Collections.sort(executionDegrees, new Comparator() {
                public int compare(Object o1, Object o2) {
                    return ((ICursoExecucao) o1).getExecutionYear().getBeginDate().compareTo(
                            ((ICursoExecucao) o1).getExecutionYear().getBeginDate());
                }
            });

            if (executionDegrees == null) {
                throw new FenixServiceException("error.impossibleEditDegreeInfo");
            }

            //Degree
            ICurso degree = degreeCurricularPlan.getDegree();
            if (degree == null) {
                throw new FenixServiceException("error.impossibleEditDegreeInfo");
            }

            //DegreeInfo
            IPersistentDegreeInfo persistentDegreeInfo = suportePersistente.getIPersistentDegreeInfo();
            degreeInfo = (IDegreeInfo) persistentDegreeInfo.readByOID(DegreeInfo.class,
                    infoDegreeInfoId, true);

            //decide which is the execution year which we want to edit
            ICursoExecucao executionDegree = (ICursoExecucao) getActiveExecutionYear(executionDegrees);
            if (executionDegree == null) {
                throw new FenixServiceException("error.impossibleEditDegreeInfo");
            }
            
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

    /**
     * This method basicly returns the most active execution degree, counting
     * from the present day
     * This means that a previous execution degree cannot be chosen (because it has
     * already finished), and that only the first execution degree can be chosen,
     * fom a curricular plan that has not yet started
     * @param infoExecutionDegreeList a list of infoexecution degrees sorted by beginDate
     */

    private ICursoExecucao getActiveExecutionYear(List executionDegreeList) {
        Date todayDate = new Date();
        int i;
        for (i = 0; i < executionDegreeList.size(); i++) {
            //if the first is in the future, the rest is also
            if ((ExecutionDegreeDuringDate(todayDate, (ICursoExecucao) executionDegreeList
                    .get(i)) == 1)
                    && (i == 0))
                return (ICursoExecucao) executionDegreeList.get(i);            
            // if the last is in the past, there is no editable executionDegree
            if ((ExecutionDegreeDuringDate(todayDate, (ICursoExecucao) executionDegreeList
                    .get(i)) == 1)
                    && (i == executionDegreeList.size()-1))
                return null;
            
            if (ExecutionDegreeDuringDate(todayDate, (ICursoExecucao) executionDegreeList
                    .get(i)) == 0)
                return (ICursoExecucao) executionDegreeList.get(i);
        }
        return null;
    }

    /**
     * @return
     * 			-1 if the InfoExecutionDegree has already finished
     * 			0 if it is active
     * 			1 if it is in the future
     */

    private int ExecutionDegreeDuringDate(Date date, ICursoExecucao info) {
        if (date.after(info.getExecutionYear().getEndDate()))
            return -1;
        if ((date.after(info.getExecutionYear().getBeginDate()))
                && date.before(info.getExecutionYear().getEndDate()))
            return 0;
        return 1;
    }

}