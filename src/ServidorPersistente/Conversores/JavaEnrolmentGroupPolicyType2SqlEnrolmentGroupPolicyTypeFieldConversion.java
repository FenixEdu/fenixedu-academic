/*
 * Created on 23/Jul/2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ServidorPersistente.Conversores;

import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

import Util.EnrolmentGroupPolicyType;

/**
 * @author asnr and scpo
 * 
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class JavaEnrolmentGroupPolicyType2SqlEnrolmentGroupPolicyTypeFieldConversion implements
        FieldConversion {

    /*
     * @see FieldConversion#javaToSql(Object)
     */
    public Object javaToSql(Object source) {
        if (source instanceof EnrolmentGroupPolicyType) {
            EnrolmentGroupPolicyType s = (EnrolmentGroupPolicyType) source;
            return s.getType();
        }

        return source;

    }

    /*
     * @see FieldConversion#sqlToJava(Object)
     */
    public Object sqlToJava(Object source) {
        if (source instanceof Integer) {
            Integer src = (Integer) source;
            return new EnrolmentGroupPolicyType(src);
        }

        return source;

    }

}