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

import net.sourceforge.fenixedu.domain.organizationalStructure.IUnit;


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
                        
            for(IContract contract : unit.getWorkingContracts()){
                IEmployee employee = contract.getEmployee();               
                if(employee.getActive().booleanValue() && contract.equals(employee.getCurrentContract())){
                    employees.add(employee);
}
            }                                    
            for(IUnit subUnit : unit.getAssociatedUnits()){
                for(IContract contract : subUnit.getWorkingContracts()){
                    IEmployee employee = contract.getEmployee();
                    if(employee.getActive().booleanValue() && contract.equals(employee.getCurrentContract())){
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

