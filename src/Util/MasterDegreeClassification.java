package Util; 

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enum.ValuedEnum;
import org.apache.struts.util.LabelValueBean;

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
	
	public static final String APPROVED_STRING = "Aprovado";
	public static final String NOT_APPROVED_STRING = "Recusado";
	public static final String UNDEFINED_STRING = "Não Definido";
	public static final String DEFAULT_STRING = "[Escolha uma Opção]";
	
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
 
 	public static String getClassificationString (int value){
 		if (value == 1) return "Aprovado";
		if (value == 2) return "Recusado";
		else return "Não Definido";
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
	
	public static ArrayList toArrayList() {
		ArrayList result = new ArrayList();
		//result.add(new LabelValueBean(MasterDegreeClassification.DEFAULT_STRING, MasterDegreeClassification.DEFAULT_STRING));
		result.add(new LabelValueBean(MasterDegreeClassification.APPROVED_STRING, String.valueOf(MasterDegreeClassification.APPROVED_TYPE)));
		result.add(new LabelValueBean(MasterDegreeClassification.NOT_APPROVED_STRING, String.valueOf(MasterDegreeClassification.NOT_APPROVED_TYPE)));
		result.add(new LabelValueBean(MasterDegreeClassification.UNDEFINED_STRING, String.valueOf(MasterDegreeClassification.UNDEFINED_TYPE)));
			
		return result;	
	}

}
