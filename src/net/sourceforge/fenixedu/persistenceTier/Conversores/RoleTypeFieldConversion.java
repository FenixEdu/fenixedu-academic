/*
 * Created on 11/Mar/2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.util.RoleType;

import org.apache.commons.beanutils.ConversionException;
import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

/**
 * @author jpvl
 */
public class RoleTypeFieldConversion implements FieldConversion {

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.ojb.broker.accesslayer.conversions.FieldConversion#javaToSql(java.lang.Object)
     */
    public Object javaToSql(Object obj) throws ConversionException {
        if (obj instanceof RoleType) {
            RoleType roleType = (RoleType) obj;
            return new Integer(roleType.getValue());
        }
        return obj;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.ojb.broker.accesslayer.conversions.FieldConversion#sqlToJava(java.lang.Object)
     */
    public Object sqlToJava(Object obj) throws ConversionException {
        RoleType roleType = null;
        if (obj instanceof Integer) {
            Integer roleTypeId = (Integer) obj;

            roleType = RoleType.getEnum(roleTypeId.intValue());

            if (roleType == null) {
                throw new IllegalArgumentException(this.getClass().getName() + ": Illegal role type!("
                        + obj + ")");
            }
        } else {
            throw new IllegalArgumentException("Illegal role type!(" + obj + ")");
        }
        return roleType;

    }

}