/*
 * Created on 3/Set/2003
 */
package ServidorAplicacao.Servicos.manager;

import DataBeans.InfoDegreeCurricularPlan;
import Util.DegreeCurricularPlanState;

/**
 * @author lmac1
 */
public class EditDegreeCurricularPlanTest extends TestCaseManagerInsertAndEditServices {

    public EditDegreeCurricularPlanTest(String testName) {
        super(testName);
    }

    protected String getNameOfServiceToBeTested() {
        return "EditDegreeCurricularPlan";
    }

    protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {

        InfoDegreeCurricularPlan infoDegreeCurricularPlan = new InfoDegreeCurricularPlan();

        DegreeCurricularPlanState state = new DegreeCurricularPlanState();
        state.setDegreeState(new Integer(2));

        infoDegreeCurricularPlan.setIdInternal(new Integer(3));
        infoDegreeCurricularPlan.setName("Plano novo");
        infoDegreeCurricularPlan.setState(state);
        infoDegreeCurricularPlan.setDegreeDuration(new Integer(1));
        infoDegreeCurricularPlan.setMinimalYearForOptionalCourses(new Integer(3));

        Object[] args = { infoDegreeCurricularPlan };
        return args;
    }

    protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {

        InfoDegreeCurricularPlan infoDegreeCurricularPlan = new InfoDegreeCurricularPlan();

        DegreeCurricularPlanState state = new DegreeCurricularPlanState();
        state.setDegreeState(new Integer(2));

        infoDegreeCurricularPlan.setIdInternal(new Integer(4));
        infoDegreeCurricularPlan.setName("planoParaApagar");
        infoDegreeCurricularPlan.setState(state);
        infoDegreeCurricularPlan.setDegreeDuration(new Integer(1));
        infoDegreeCurricularPlan.setMinimalYearForOptionalCourses(new Integer(3));

        Object[] args = { infoDegreeCurricularPlan };
        return args;
    }
}