/*
 * Created on 19/Ago/2003
 *
 */
package Util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.struts.util.LabelValueBean;

/**
 * @author Susana Fernandes
 */
public class TestType implements Serializable {
	public static final int EVALUATION = 1;
	public static final int STUDY = 2;
	public static final String EVALUATION_STRING =
		"Ficha de Avaliação (guardar primeira resposta)";
	public static final String STUDY_STRING =
		"Ficha de Estudo (guardar última resposta)";

	private Integer type;
	private String typeString;
	
	public TestType() {
	}

	public TestType(int type) {
		this.type = new Integer(type);
	}

	public TestType(Integer type) {
		this.type = type;
	}
	public TestType(String typeString) {
		this.type = getTypeCode(typeString);
	}

	public Integer getType() {
		return type;
	}

	public void setType(int type) {
		this.type = new Integer(type);
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public List getAllTypes() {
		List result = new ArrayList();
		result.add(
			new LabelValueBean(
				EVALUATION_STRING,
				new Integer(EVALUATION).toString()));
		result.add(
			new LabelValueBean(STUDY_STRING, new Integer(STUDY).toString()));
		return result;
	}

	public Integer getTypeCode(String typeName) {
		if (typeName.equals(EVALUATION_STRING))
			return new Integer(EVALUATION);
		else if (typeName.equals(STUDY_STRING))
			return new Integer(STUDY);
		return null;
	}
	
	public String getTypeString() {
		if (type.intValue() == EVALUATION)
			return new String(EVALUATION_STRING);
		else if (type.intValue() == STUDY)
			return new String(STUDY_STRING);
		return null;
	}

	public boolean equals(Object obj) {
		boolean resultado = false;
		if (obj instanceof TestType) {
			TestType tt = (TestType) obj;
			resultado = this.getType().equals(tt.getType());
		}
		return resultado;
	}
}