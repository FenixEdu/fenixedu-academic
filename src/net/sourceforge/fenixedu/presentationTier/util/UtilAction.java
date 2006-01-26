/*
 * Created on Mar 7, 2005
 *
 */
package net.sourceforge.fenixedu.presentationTier.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

/**
 * @author velouria
 * 
 */
public class UtilAction {
    
    
    public static ArrayList stringArrayToArrayList(String[] stringArray) {
        ArrayList arrayList = new ArrayList();
        int sizeArray = stringArray.length;
        for (int i = 0; i < sizeArray; i++) {
            arrayList.add(stringArray[i]);
        }
        return arrayList;
    }    

	// convert the error from the DTO to the Struts ActionErrors
	public static ActionErrors convertFromDTOtoActionErrors(DTO dtoErrors) {
	   Map errors = (HashMap)dtoErrors.get(DTOConstants.DTO_PRESENTATION_ERRORS);
	   ActionErrors actionErrors = new ActionErrors();
	   
	   Set keys = errors.keySet();
	   Iterator itErrorKeys = keys.iterator();
	   while (itErrorKeys.hasNext()) {
	       String key = (String) itErrorKeys.next();
	       actionErrors.add(key, new ActionError((String) errors.get(key)));
	   }
	    return actionErrors; 
	}
    
	public static long convertDurationToMillis(String stringHours, String stringMinutes) {
	    long hours = new Long(stringHours).longValue();
	    long minutes = new Long(stringMinutes).longValue();
	    return ((hours * 3600 + minutes * 60) * 1000);
	}

}
