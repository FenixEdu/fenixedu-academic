/*
 * Created on 10/Nov/2003
 */
package middleware.ScopesClean;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.PersistenceBrokerException;
import org.apache.ojb.broker.PersistenceBrokerFactory;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.Query;
import org.apache.ojb.broker.query.QueryByCriteria;

import Dominio.CurricularCourse;
import Dominio.CurricularCourseScope;
import Dominio.DegreeCurricularPlan;
import Dominio.Enrolment;
import Dominio.ExecutionPeriod;
import Dominio.ICurricularCourse;
import Dominio.ICurricularCourseScope;
import Dominio.IDegreeCurricularPlan;
import Dominio.IEnrolment;
import Dominio.IExecutionPeriod;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.PeriodState;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 * 
 */
public class ChangeRelationBetweenCCScopeAndCCOfMathAnalysisA
{

    static String[] dcpName =
        { "LEA2003/2004", "LEBM2003/2004", "LEFT2003/2004", "LCI2003/2004", "LMAC2003/2004" };
    static String[] ccACodesStrings = { "AZ3", "AZ4", "AZ5", "AZ6" };
    static String[] ccCodesStrings = { "PY", "P5", "UN", "U8" };

    static LinkedList ccACodesList;
    static LinkedList ccCodesList;

    static int changedEnrolments = 0;
    static int deletedEnrolments = 0;

    static Integer currentSemester;

    public static void main(String[] args)
    {

        System.out.println("Running ChangeRelationBetweenCCScopeAndCCOfMathAnalysisA script");

        initialize();

        PersistenceBroker broker = PersistenceBrokerFactory.defaultPersistenceBroker();

        LinkedList dcpList = new LinkedList();

        broker.clearCache();
        broker.beginTransaction();

        for (int dcpNameIterator = 0; dcpNameIterator < dcpName.length; dcpNameIterator++)
        {
            Criteria criteria = new Criteria();
            criteria.addEqualTo("name", dcpName[dcpNameIterator]);
            Query query = new QueryByCriteria(DegreeCurricularPlan.class, criteria);
            IDegreeCurricularPlan degreeCurricularPlan =
                (DegreeCurricularPlan) broker.getObjectByQuery(query);
            dcpList.add(degreeCurricularPlan);
        }

        List ccsList = (List) getCurricularCourseScopes(dcpList);

        changeAssociationBetweenCCScopeOfMathAnalysisA(ccsList);

        System.out.println(
            changedEnrolments
                + " enrolments were changed and "
                + deletedEnrolments
                + " enrolments were deleted from "
                + ccCodesList.size()
                + " curricular courses");
        broker.commitTransaction();
        broker.close();

    }

    private static LinkedList getCurricularCourseScopes(LinkedList dcpList)
    {

        Criteria criteria = new Criteria();
        Criteria orCriteria;
        Criteria andCriteria;
        PersistenceBroker broker = PersistenceBrokerFactory.defaultPersistenceBroker();
        List ccList;
        List ccsList;
        LinkedList allCCSList = new LinkedList();
        Iterator dcpIterator = dcpList.iterator();

        broker.clearCache();
        broker.beginTransaction();

        try
        {

            while (dcpIterator.hasNext())
            {

                andCriteria = new Criteria();
                IDegreeCurricularPlan degreeCurricularPlan = (DegreeCurricularPlan) dcpIterator.next();
                andCriteria.addEqualTo(
                    "degreeCurricularPlan.idInternal",
                    degreeCurricularPlan.getIdInternal());

                Iterator ccACodeIterator = ccACodesList.iterator();
                orCriteria = new Criteria();
                orCriteria.addEqualTo("code", ccACodeIterator.next());

                //It gives all curricular courses specified on ccACodeStrings of the current degree curricular plan 
                while (ccACodeIterator.hasNext())
                {
                    criteria = new Criteria();
                    criteria.addEqualTo("code", ccACodeIterator.next());
                    orCriteria.addOrCriteria(criteria);
                }

                andCriteria.addAndCriteria(orCriteria);
                Query query = new QueryByCriteria(CurricularCourse.class, andCriteria);

                ccList = (List) broker.getCollectionByQuery(query);

                Iterator ccIterator = ccList.iterator();

                //It gives all curricular course scopes of the curricular courses selected before
                while (ccIterator.hasNext())
                {

                    ICurricularCourse curricularCourse = (CurricularCourse) ccIterator.next();

                    criteria = new Criteria();
                    criteria.addEqualTo("curricularCourse.idInternal", curricularCourse.getIdInternal());
                    query = new QueryByCriteria(CurricularCourseScope.class, criteria);
                    ccsList = (List) broker.getCollectionByQuery(query);
                    allCCSList.addAll(ccsList);

                }

            }
            broker.commitTransaction();
            broker.close();
        }
        catch (PersistenceBrokerException pbe)
        {
            System.out.println("getCurricularCourseScopes: A PersistenceBrokerException has occured");
            pbe.printStackTrace();
        }

        return allCCSList;
    }

    private static void changeAssociationBetweenCCScopeOfMathAnalysisA(List ccsList)
    {

        PersistenceBroker broker = PersistenceBrokerFactory.defaultPersistenceBroker();
        Criteria criteria = new Criteria();
        Query query;

        Iterator ccsIterator = ccsList.iterator();

        broker.clearCache();
        broker.beginTransaction();

        try
        {

            while (ccsIterator.hasNext())
            {
                criteria = new Criteria();
                ICurricularCourseScope ccScope = (CurricularCourseScope) ccsIterator.next();

                if (ccScope.getCurricularSemester().getSemester().equals(currentSemester))
                    continue;

                criteria.addEqualTo("curricularCourseScope.idInternal", ccScope.getIdInternal());
                query = new QueryByCriteria(Enrolment.class, criteria);
                List enrolmentACurricularCourseList = (List) broker.getCollectionByQuery(query);

                if ((enrolmentACurricularCourseList != null)
                    && (!enrolmentACurricularCourseList.isEmpty()))
                {

                    int ccCodeIndex = ccACodesList.indexOf(ccScope.getCurricularCourse().getCode());

                    criteria = new Criteria();
                    //get the curricular course of the corresponding non advanced curricular course
                    IDegreeCurricularPlan degreeCurricularPlan =
                        ccScope.getCurricularCourse().getDegreeCurricularPlan();

                    criteria.addEqualTo(
                        "degreeCurricularPlan.idInternal",
                        degreeCurricularPlan.getIdInternal());
                    criteria.addEqualTo("code", ccCodesList.get(ccCodeIndex));

                    query = new QueryByCriteria(CurricularCourse.class, criteria);
                    ICurricularCourse curricularCourse =
                        (CurricularCourse) broker.getObjectByQuery(query);

                    if (curricularCourse != null)
                    {

                        criteria = new Criteria();
                        criteria.addEqualTo(
                            "curricularCourse.idInternal",
                            curricularCourse.getIdInternal());
                        query = new QueryByCriteria(CurricularCourseScope.class, criteria);
                        ICurricularCourseScope curricularCourseScope =
                            (CurricularCourseScope) broker.getObjectByQuery(query);

                        deleteDuplicateEnrolments(enrolmentACurricularCourseList, curricularCourseScope);
                    }
                }
            }
            broker.commitTransaction();
            broker.close();
        }
        catch (PersistenceBrokerException pbe)
        {
            System.out.println(
                "changeAssociationBetweenCCScopeOfMathAnalysisA: A PersistenceBrokerException has occured");
            pbe.printStackTrace();
        }

    }

    private static void deleteDuplicateEnrolments(
        List enrolmentACurricularCourseList,
        ICurricularCourseScope curricularCourseScope)
    {

        PersistenceBroker broker = PersistenceBrokerFactory.defaultPersistenceBroker();

        try
        {
            broker.clearCache();
            broker.beginTransaction();

            Criteria criteria = new Criteria();
            criteria.addEqualTo(
                "curricularCourseScope.idInternal",
                curricularCourseScope.getIdInternal());
            //criteria.addEqualTo("keyExecutionPeriod", currentExecutionPeriod.getIdInternal());
            Query query = new QueryByCriteria(Enrolment.class, criteria);
            List enrolmentCurricularCourseList = (List) broker.getCollectionByQuery(query);

            if ((enrolmentCurricularCourseList == null) || enrolmentCurricularCourseList.isEmpty())
            {

                Iterator enrolmentsIterator = enrolmentACurricularCourseList.iterator();
                while (enrolmentsIterator.hasNext())
                {
                    IEnrolment enrolment = (Enrolment) enrolmentsIterator.next();
                    enrolment.setCurricularCourseScope(curricularCourseScope);
                    broker.store(enrolment);
                    changedEnrolments++;
                }

            }
            else
            {

                Iterator enrolmentsAIterator = enrolmentACurricularCourseList.iterator();
                while (enrolmentsAIterator.hasNext())
                {

                    IEnrolment enrolmentA = (Enrolment) enrolmentsAIterator.next();

                    criteria = new Criteria();
                    criteria.addEqualTo(
                        "curricularCourseScope.idInternal",
                        curricularCourseScope.getIdInternal());
                    criteria.addEqualTo(
                        "keyExecutionPeriod",
                        enrolmentA.getExecutionPeriod().getIdInternal());
                    criteria.addEqualTo(
                        "studentCurricularPlan.idInternal",
                        enrolmentA.getStudentCurricularPlan().getIdInternal());
                    query = new QueryByCriteria(Enrolment.class, criteria);
                    IEnrolment enrolment = (Enrolment) broker.getObjectByQuery(query);

                    if (enrolment == null)
                    {
                        changedEnrolments++;
                        enrolmentA.setCurricularCourseScope(curricularCourseScope);
                        broker.store(enrolmentA);
                    }
                    else
                    {
                        broker.delete(enrolmentA);
                        deletedEnrolments++;
                    }
                }

            }

            broker.commitTransaction();
            broker.close();
        }

        catch (PersistenceBrokerException pbe)
        {
            System.out.println("deleteDuplicateEnrolments: A PersistenceBrokerException has occured");
            pbe.printStackTrace();
        }
    }

    private static void initialize()
    {

        ccACodesList = new LinkedList();
        ccCodesList = new LinkedList();
        List listExecutionPeriods = null;
        IExecutionPeriod currentExecutionPeriod;

        try
        {
            ISuportePersistente suportePersistente = SuportePersistenteOJB.getInstance();
            suportePersistente.iniciarTransaccao();
            IPersistentExecutionPeriod persistentExecutionPeriod =
                suportePersistente.getIPersistentExecutionPeriod();
            listExecutionPeriods = persistentExecutionPeriod.readAllExecutionPeriod();
            suportePersistente.confirmarTransaccao();
        }
        catch (ExcepcaoPersistencia ep)
        {
            System.out.println("An exception occured retreiving the list of execution peridos");
            ep.printStackTrace();
        }

        for (int iterator = 0; iterator < ccACodesStrings.length; iterator++)
        {
            ccACodesList.add(ccACodesStrings[iterator]);
            ccCodesList.add(ccCodesStrings[iterator]);
        }

        Iterator exPeriodsIterator = listExecutionPeriods.iterator();

        while (exPeriodsIterator.hasNext())
        {
            currentExecutionPeriod = (ExecutionPeriod) exPeriodsIterator.next();

            if (currentExecutionPeriod.getState().equals(PeriodState.CURRENT))
            {
                currentSemester = currentExecutionPeriod.getSemester();
                break;
            }
        }

    }

}
