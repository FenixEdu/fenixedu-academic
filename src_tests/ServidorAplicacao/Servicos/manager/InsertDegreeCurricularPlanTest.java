/*
 * Created on 2/Set/2003
 */
package ServidorAplicacao.Servicos.manager;

import DataBeans.InfoDegree;
import DataBeans.InfoDegreeCurricularPlan;
import Util.DegreeCurricularPlanState;
import Util.MarkType;

/**
 * @author lmac1
 */
public class InsertDegreeCurricularPlanTest extends TestCaseManagerInsertAndEditServices {

    public InsertDegreeCurricularPlanTest(String testName) {
        super(testName);
    }

    protected String getNameOfServiceToBeTested() {
        return "InsertDegreeCurricularPlan";
    }

    //	insert curricular course with name existing in DB but another
    // key_degree_curricular_plan
    protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {

        InfoDegree infoDegree = new InfoDegree();
        infoDegree.setIdInternal(new Integer(8));

        DegreeCurricularPlanState state = new DegreeCurricularPlanState();
        state.setDegreeState(new Integer(1));

        MarkType markType = new MarkType();
        markType.setType(new Integer(2));

        InfoDegreeCurricularPlan infoDegreeCurricularPlan = new InfoDegreeCurricularPlan();
        infoDegreeCurricularPlan.setInfoDegree(infoDegree);
        infoDegreeCurricularPlan.setName("plano3");
        infoDegreeCurricularPlan.setState(state);
        infoDegreeCurricularPlan.setDegreeDuration(new Integer(1));
        infoDegreeCurricularPlan.setMinimalYearForOptionalCourses(new Integer(5));
        infoDegreeCurricularPlan.setMarkType(markType);

        Object[] args = { infoDegreeCurricularPlan };
        return args;
    }

    //	insert curricular course with name and key_degree already existing in DB
    protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {

        InfoDegree infoDegree = new InfoDegree();
        infoDegree.setIdInternal(new Integer(10));

        DegreeCurricularPlanState state = new DegreeCurricularPlanState();
        state.setDegreeState(new Integer(1));

        MarkType markType = new MarkType();
        markType.setType(new Integer(2));

        InfoDegreeCurricularPlan infoDegreeCurricularPlan = new InfoDegreeCurricularPlan();
        infoDegreeCurricularPlan.setInfoDegree(infoDegree);
        infoDegreeCurricularPlan.setName("plano3");
        infoDegreeCurricularPlan.setState(state);
        infoDegreeCurricularPlan.setDegreeDuration(new Integer(1));
        infoDegreeCurricularPlan.setMinimalYearForOptionalCourses(new Integer(5));
        infoDegreeCurricularPlan.setMarkType(markType);

        Object[] args = { infoDegreeCurricularPlan };
        return args;
    }

}

