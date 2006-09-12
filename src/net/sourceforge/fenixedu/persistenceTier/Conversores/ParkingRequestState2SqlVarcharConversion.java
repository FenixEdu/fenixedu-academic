package net.sourceforge.fenixedu.persistenceTier.Conversores;

import net.sourceforge.fenixedu.domain.parking.ParkingRequestState;

import org.apache.ojb.broker.accesslayer.conversions.ConversionException;
import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

public class ParkingRequestState2SqlVarcharConversion implements FieldConversion {

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.ojb.broker.accesslayer.conversions.FieldConversion#javaToSql(java.lang.Object)
     */
    public Object javaToSql(Object obj) throws ConversionException {
        if (obj instanceof ParkingRequestState) {
            final ParkingRequestState parkingRequestState = (ParkingRequestState) obj;
            return parkingRequestState.toString();
        }
        return obj;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.ojb.broker.accesslayer.conversions.FieldConversion#sqlToJava(java.lang.Object)
     */
    public Object sqlToJava(Object obj) throws ConversionException {
        ParkingRequestState parkingRequestState = null;
        if (obj instanceof String) {
            final String parkingRequestStateString = (String) obj;
            return ParkingRequestState.valueOf(parkingRequestStateString);
        }
        return parkingRequestState;
    }
}
