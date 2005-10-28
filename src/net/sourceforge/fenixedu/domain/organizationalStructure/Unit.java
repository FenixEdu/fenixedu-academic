/*
 * Created on Sep 16, 2005
 *	by mrsp
 */
package net.sourceforge.fenixedu.domain.organizationalStructure;

public class Unit extends Unit_Base {

    public IUnit getTopUnit(){              
        IUnit unit = null;
        if (this.getParentUnit() != null) {
            unit = this;
            while (unit.getParentUnit() != null) {                                                                      
                unit = this.getParentUnit();
            }         
        }
        return unit;
    }
}
