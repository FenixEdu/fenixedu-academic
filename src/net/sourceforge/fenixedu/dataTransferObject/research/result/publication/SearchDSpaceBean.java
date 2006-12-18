package net.sourceforge.fenixedu.dataTransferObject.research.result.publication;

import java.io.Serializable;

import net.sourceforge.fenixedu.util.StringNormalizer;
import pt.utl.ist.fenix.tools.file.FileSearchCriteria;
import pt.utl.ist.fenix.tools.file.FileSearchCriteria.SearchField;
import pt.utl.ist.fenix.tools.file.FilesetMetadataQuery.ConjunctionType;

public class SearchDSpaceBean implements Serializable {
	
	private SearchField field1;
	private SearchField field2;
	private SearchField field3;
	
	private String value1;
	private	String value2;
	private String value3;
	
	private ConjunctionType firstConjunction;
	private ConjunctionType secondConjunction;
	
	private static final int pageSize=15;
	
	public SearchDSpaceBean() {
		setField1(SearchField.ANY);
		setField2(SearchField.ANY);
		setField3(SearchField.ANY);
		setFirstConjunction(ConjunctionType.AND);
		setSecondConjunction(ConjunctionType.AND);
		setValue1("");
		setValue2("");
		setValue3("");
	} 
	
	public String getValue() {
		return getValue1();
	}
	
	public void setValue(String value) {
		setValue1(value);
	}
	
	
	public SearchField getSearchField() {
		return getField1();
	}
	public void setSearchField(SearchField searchField) {
		setField1(searchField);
	}
		
	public SearchField getField1() {
		return field1;
	}

	public void setField1(SearchField field1) {
		this.field1 = (field1==null) ? SearchField.ANY : field1;
	}

	public SearchField getField2() {
		return field2;
	}

	public void setField2(SearchField field2) {
		this.field2 = (field2==null) ? SearchField.ANY : field2;
	}

	public SearchField getField3() {
		return field3;
	}

	public void setField3(SearchField field3) {
		this.field3 = (field3==null) ? SearchField.ANY : field3;
	}

	public ConjunctionType getFirstConjunction() {
		return firstConjunction;
	}

	public void setFirstConjunction(ConjunctionType firstConjunction) {
		this.firstConjunction = (firstConjunction==null) ? ConjunctionType.AND : firstConjunction;
	}

	public ConjunctionType getSecondConjunction() {
		return secondConjunction;
	}

	public void setSecondConjunction(ConjunctionType secondConjunction) {
		this.secondConjunction = (secondConjunction==null) ? ConjunctionType.AND : secondConjunction;
	}

	public String getValue1() {
		return value1;
	}

	public void setValue1(String value1) {
		this.value1 = (value1==null) ? "" : StringNormalizer.normalize(value1);
	}

	public String getValue2() {
		return value2;
	}

	public void setValue2(String value2) {
		this.value2 = (value2==null) ? "" : StringNormalizer.normalize(value2);
	}

	public String getValue3() {
		return value3;
	}

	public void setValue3(String value3) {
		this.value3 = (value3==null) ? "" : StringNormalizer.normalize(value3);
	}

	public int getPageSize() {
		return pageSize;
	}
	
	public FileSearchCriteria getSearchCriteria(int start) {
		FileSearchCriteria criteria = new FileSearchCriteria(start,pageSize);
		
		criteria.addAndCriteria(getSearchField(), getValue());
		if(value2.length()>0) {
			if(firstConjunction.equals(ConjunctionType.AND)) {
				criteria.addAndCriteria(getField2(), getValue2());
			}
			else {
				criteria.addOrCriteria(getField2(), getValue2());
			}
		}
		if(value3.length()>0) {
			if(secondConjunction.equals(ConjunctionType.AND)) {
				criteria.addAndCriteria(getField3(), getValue3());
			}
			else {
				criteria.addOrCriteria(getField3(), getValue3());
			}
		}
		return criteria;
	}
}
