/*
 * Created on 18/Nov/2004
 *
 */
package ServidorPersistente.Conversores;

import org.apache.commons.beanutils.ConversionException;
import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;

import Util.PublicationArea;

/**
 * @author Ricardo Rodrigues
 *
 */
public class PublicationArea2SqlPublicationAreaFieldConversion implements FieldConversion{

	public Object javaToSql(Object obj) throws ConversionException {
        if (obj instanceof PublicationArea) {
            PublicationArea publicationArea = (PublicationArea) obj;
            return publicationArea.getName();
        }
        return obj;
    }

    /*
     * @see FieldConversion#sqlToJava(Object)
     */

    public Object sqlToJava(Object obj) throws ConversionException {
    	 if (obj instanceof String) {
            String src = (String) obj;
            return PublicationArea.getEnum(src);
        }
        return obj;
    }


}
