package Util; 

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enum.ValuedEnum;

/**
 * 
 * @author:
 *   - Shezad Anavarali (sana@mega.ist.utl.pt)
 *   - Nadir Tarmahomed (naat@mega.ist.utl.pt)
 *
 */
public final class MasterDegreeClassification extends ValuedEnum {

	public static final int APPROVED_TYPE = 1;
	public static final int NOT_APPROVED_TYPE = 2;
	public static final int UNDEFINED_TYPE = 3;
		
	public static final MasterDegreeClassification APPROVED  = new MasterDegreeClassification("approved", MasterDegreeClassification.APPROVED_TYPE);
	public static final MasterDegreeClassification NOT_APPROVED = new MasterDegreeClassification("not.approved", MasterDegreeClassification.NOT_APPROVED_TYPE);
	public static final MasterDegreeClassification UNDEFINED = new MasterDegreeClassification("undefined", MasterDegreeClassification.UNDEFINED_TYPE);
	
	
	/**
	 * @param name
	 * @param value
	 */
	private MasterDegreeClassification(String name, int value) {
		super(name, value);
	}

	public static MasterDegreeClassification getEnum(String name) {
	  return (MasterDegreeClassification) getEnum(MasterDegreeClassification.class, name);
	}
 
	public static MasterDegreeClassification getEnum(int value) {
	  return (MasterDegreeClassification) getEnum(MasterDegreeClassification.class, value);
	}
 
	public static Map getEnumMap() {
	  return getEnumMap(MasterDegreeClassification.class);
	}
 
	public static List getEnumList() {
	  return getEnumList(MasterDegreeClassification.class);
	}
 
	public static Iterator iterator() {
	  return iterator(MasterDegreeClassification.class);
	}	
	
	public String toString(){
		return this.getName();		
	}

}
