/*
 * JavaStudentCurricularPlanState2SqlStudentCurricularPlanStateFieldConversion.java
 * 
 * Created on 21 de Dezembro de 2002, 16:21
 */

package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.domain.masterDegree.MasterDegreeClassification;

import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

public class JavaMasterDegreeClassification2SQLMasterDegreeClassificationFieldConversion implements
        FieldConversion {

    /*
     * @see FieldConversion#javaToSql(Object)
     */
    public Object javaToSql(Object source) {
        if (source instanceof MasterDegreeClassification) {
            MasterDegreeClassification masterDegreeClassification = (MasterDegreeClassification) source;
            return masterDegreeClassification.name();
        }

        return source;

    }

    /*
     * @see FieldConversion#sqlToJava(Object)
     */
    public Object sqlToJava(Object source) {
        MasterDegreeClassification masterDegreeClassification = null;
        if (source instanceof String) {
            String masterDegreeClassificationString = (String) source;
            masterDegreeClassification = MasterDegreeClassification
                    .valueOf(masterDegreeClassificationString);

            if (masterDegreeClassification == null) {
                throw new IllegalArgumentException(this.getClass().getName()
                        + ": Illegal MasterDegreeClassification!(" + source + ")");
            }
        } else {
            throw new IllegalArgumentException("Illegal MasterDegreeClassification!(" + source + ")");
        }
        return masterDegreeClassification;
    }
}
