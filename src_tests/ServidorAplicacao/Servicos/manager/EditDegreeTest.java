/*
 * Created on 3/Set/2003
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
public class EditDegreeTest extends TestCaseManagerInsertAndEditServices {

    public EditDegreeTest(String testName) {
        super(testName);
    }

    protected String getNameOfServiceToBeTested() {
        return "EditDegree";
    }

    protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly() {
        InfoDegree infoDegree = new InfoDegree("LEIC", "Novo nome", new TipoCurso(1));
        infoDegree.setIdInternal(new Integer(8));
        Object[] args = { infoDegree };
        return args;
    }

    protected HashMap getArgumentListOfServiceToBeTestedUnsuccessfuly() {
        HashMap result = new HashMap();
        List infoDegree1 = new ArrayList();
        InfoDegree degree1 = new InfoDegree("MEEC", "Novo nome", new TipoCurso(1));
        degree1.setIdInternal(new Integer(11));
        infoDegree1.add(degree1);
        List infoDegree2 = new ArrayList();
        InfoDegree degree2 = new InfoDegree("M", "Engenharia Mecanica", new TipoCurso(2));
        degree2.setIdInternal(new Integer(8));
        infoDegree2.add(degree2);
        result.put("infoDegree1", infoDegree1);
        result.put("infoDegree2", infoDegree2);
        return result;
    }
}