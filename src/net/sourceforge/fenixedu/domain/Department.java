/*
 * Department.java
 * 
 * Created on 6 de Novembro de 2002, 15:57
 */

/**
 * @author Nuno Nunes & Joana Mota
 */

package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.List;


public class Department extends Department_Base {

    public String toString() {
        String result = "[DEPARTAMENT";
        result += ", codInt=" + getIdInternal();
        result += ", sigla=" + getCode();
        result += ", nome=" + getName();
        result += "]";
        return result;
    }
    
    public List getWorkingEmployees(){
        
        IUnit unit = this.getUnit();
        List employees = new ArrayList();
        
        if(unit != null){               
                        
            for(IEmployee employee : unit.getWorkingEmployees()){
                if(employee.getActive().booleanValue()){
                    employees.add(employee);
                }
            }                                    
            for(IUnit subUnit : unit.getAssociatedUnits()){
                for(IEmployee employee : subUnit.getWorkingEmployees()){
                    if(employee.getActive().booleanValue()){
                        employees.add(employee);
                    }
                }
            }                        
        }           
        return employees;
    }
    
    public List getTeachers(){
 
        List teachers = new ArrayList();
        List<IEmployee> employees = this.getWorkingEmployees();
        
        for(IEmployee employee : employees){
            ITeacher teacher = employee.getPerson().getTeacher(); 
            if(teacher != null)
                teachers.add(teacher);            
        }        
        return teachers;
    }
}
