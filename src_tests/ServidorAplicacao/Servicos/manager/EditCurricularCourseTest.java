/*
 * Created on 3/Set/2003
 */
package ServidorAplicacao.Servicos.manager;

import DataBeans.InfoCurricularCourse;

/**
 * @author lmac1
 */
public class EditCurricularCourseTest extends TestCaseManagerInsertAndEditServices {

    public EditCurricularCourseTest(String testName) {
        super(testName);
    }

    protected String getNameOfServiceToBeTested() {
        return "EditCurricularCourse";
    }

    //	edit curricular course existing in BD with another name
    protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {

        InfoCurricularCourse infoCurricularCourse = new InfoCurricularCourse();
        infoCurricularCourse.setName("Editar com sucesso");
        infoCurricularCourse.setCode("AMI");
        infoCurricularCourse.setIdInternal(new Integer(1));

        Object[] args = { infoCurricularCourse };
        return args;
    }

    //	edit curricular course with name,code already existing in DB and another
    // key_degree_curricular_plan

    protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly() {

        InfoCurricularCourse infoCurricularCourse = new InfoCurricularCourse();
        infoCurricularCourse.setName("Analise Matematica I");
        infoCurricularCourse.setCode("AMI");
        infoCurricularCourse.setIdInternal(new Integer(3));

        Object[] args = { infoCurricularCourse };
        return args;

    }

}