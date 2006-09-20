package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.domain.parking.ParkingDocumentState;
import net.sourceforge.fenixedu.domain.parking.ParkingDocumentType;

import org.apache.ojb.broker.accesslayer.conversions.ConversionException;
import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

public class ParkingDocumentState2SqlVarcharConversion implements FieldConversion {

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.ojb.broker.accesslayer.conversions.FieldConversion#javaToSql(java.lang.Object)
     */
    public Object javaToSql(Object obj) throws ConversionException {
        if (obj instanceof ParkingDocumentType) {
            final ParkingDocumentState parkingDocumentState = (ParkingDocumentState) obj;
            return parkingDocumentState.toString();
        }
        return obj;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.ojb.broker.accesslayer.conversions.FieldConversion#sqlToJava(java.lang.Object)
     */
    public Object sqlToJava(Object obj) throws ConversionException {
        ParkingDocumentState parkingDocumentState = null;
        if (obj instanceof String) {
            final String parkingDocumentStateString = (String) obj;
            return ParkingDocumentState.valueOf(parkingDocumentStateString);
        }
        return parkingDocumentState;
    }
}
