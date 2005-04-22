/*
 * Department.java
 * 
 * Created on 6 de Novembro de 2002, 15:57
 */

/**
 * @author Nuno Nunes & Joana Mota
 */

package net.sourceforge.fenixedu.domain;

import java.util.Set;

public class Department extends Department_Base {

//    private Set disciplinasAssociadas;

    public Department() {
    }

    public Department(String name, String code) {
        setName(name);
        setCode(code);
    }

    public boolean equals(Object obj) {
        boolean resultado = false;
        if (obj instanceof IDepartment) {
            IDepartment d = (IDepartment) obj;
            resultado = getCode().equals(d.getCode());
        }
        return resultado;
    }

    public String toString() {
        String result = "[DEPARTAMENT";
        result += ", codInt=" + getIdInternal();
        result += ", sigla=" + getCode();
        result += ", nome=" + getName();
        result += "]";
        return result;
    }

  
/*	
    public Set getDisciplinasAssociadas() {
        return disciplinasAssociadas;
    }


    public void setDisciplinasAssociadas(Set disciplinasAssociadas) {
        this.disciplinasAssociadas = disciplinasAssociadas;
    }
*/
}