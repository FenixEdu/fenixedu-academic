/*
 * Department.java
 * 
 * Created on 6 de Novembro de 2002, 15:57
 */

/**
 * @author Nuno Nunes & Joana Mota
 */

package net.sourceforge.fenixedu.domain;


public class Department extends Department_Base {

    public String toString() {
        String result = "[DEPARTAMENT";
        result += ", codInt=" + getIdInternal();
        result += ", sigla=" + getCode();
        result += ", nome=" + getName();
        result += "]";
        return result;
    }

}
