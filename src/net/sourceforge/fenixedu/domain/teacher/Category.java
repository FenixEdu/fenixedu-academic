/*
 * Created on 7/Nov/2003
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package net.sourceforge.fenixedu.domain.teacher;


/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 * 
 */
public class Category extends Category_Base {

    public boolean equals(Object obj) {
        boolean resultado = false;
        if (obj instanceof ICategory) {
            resultado = getCode().equals(((ICategory) obj).getCode());
        }
        return resultado;
    }

    public String toString() {
        String result = "[Dominio.teacher.Category ";
        result += ", code=" + getCode();
        result += ", shortName=" + getShortName();
        result += ", longName=" + getLongName();
        result += "]";
        return result;
    }

}