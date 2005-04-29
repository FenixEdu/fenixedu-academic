/*
 * Created on 1/Mar/2004
 *  
 */
package net.sourceforge.fenixedu.domain;

/**
 * @author Tânia Pousão
 * 
 */
public class CostCenter extends CostCenter_Base {

    public CostCenter() {
    }

    public CostCenter(String code, String departament, String section1, String section2) {
        setCode(code);
        setDepartament(departament);
        setSection1(section1);
        setSection2(section2);
    }

}