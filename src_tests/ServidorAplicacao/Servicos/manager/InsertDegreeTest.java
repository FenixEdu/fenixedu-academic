/*
 * Created on 28/Jul/2003
 */
package ServidorAplicacao.Servicos.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import DataBeans.InfoDegree;
import Util.TipoCurso;

/**
 * @author lmac1
 */

public class InsertDegreeTest extends TestCaseManagerInsertAndEditServices {

    public InsertDegreeTest(String testName) {
        super(testName);
    }

    protected String getNameOfServiceToBeTested() {
        return "InsertDegree";
    }

    //	insert degree with code already existing in DB
    //	insert degree with name and degreeType already existing in DB

    protected HashMap getArgumentListOfServiceToBeTestedUnsuccessfuly() {

        HashMap result = new HashMap();
        List infoDegree1 = new ArrayList();
        infoDegree1.add(new InfoDegree("MEEC", "Nome novo", new TipoCurso(1)));
        List infoDegree2 = new ArrayList();
        infoDegree2.add(new InfoDegree("M", "Engenharia Mecanica", new TipoCurso(2)));
        result.put("infoDegree1", infoDegree1);
        result.put("infoDegree2", infoDegree2);
        return result;
    }

    protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {

        InfoDegree infoDegree = new InfoDegree("ICS", "Inserir Com Sucesso", new TipoCurso(1));
        Object[] args = { infoDegree };
        return args;
    }

}