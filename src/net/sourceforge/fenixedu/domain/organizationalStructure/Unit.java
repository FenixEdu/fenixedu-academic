/*
 * Created on Sep 16, 2005
 *	by mrsp
 */
package net.sourceforge.fenixedu.domain.organizationalStructure;

public class Unit extends Unit_Base {

    public IUnit getTopUnit(){
        IUnit unit = this;
        if (unit.getParentUnit() != null) {           
            while (unit.getParentUnit() != null) {                                                                      
                unit = unit.getParentUnit();                
            }            
        }
        return unit; 
    }
}
