package net.sourceforge.fenixedu.dataTransferObject;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.presentationTier.Action.manager.FileItemCreationBean.EducationalResourceType;
import pt.utl.ist.fenix.tools.file.FileSearchCriteria;
import pt.utl.ist.fenix.tools.file.FileSearchCriteria.SearchField;
import pt.utl.ist.fenix.tools.file.FilesetMetadataQuery.ConjunctionType;

public class SearchDSpaceCoursesBean extends SearchDSpaceBean{
	
	
	DomainReference<ExecutionYear> executionYear;
	DomainReference<ExecutionPeriod> executionPeriod;
	List<EducationalResourceType> educationalResourceTypes;
	
	public SearchDSpaceCoursesBean() {
		super();
		this.setExecutionYear(null);
		this.setExecutionPeriod(null);
		this.educationalResourceTypes = new ArrayList<EducationalResourceType>();
	} 
		
	public List<EducationalResourceType> getEducationalResourceTypes() {
		return educationalResourceTypes;
	}

	public void setEducationalResourceTypes(List<EducationalResourceType> educationalResourceTypes) {
		this.educationalResourceTypes = educationalResourceTypes;
	}
	
	public ExecutionYear getExecutionYear() {
		return executionYear.getObject();
	}

	public void setExecutionYear(ExecutionYear executionYear) {
		this.executionYear = new DomainReference<ExecutionYear>(executionYear);
	}
	
	public ExecutionPeriod getExecutionPeriod() {
		return executionPeriod.getObject();
	}

	public void setExecutionPeriod(ExecutionPeriod executionPeriod) {
		this.executionPeriod = new DomainReference<ExecutionPeriod>(executionPeriod);
	}
	
	
	@Override
	public String getSearchElementsAsParameters() {
		String parameters = super.getSearchElementsAsParameters();
		for(EducationalResourceType type : getEducationalResourceTypes()) {
			parameters += "&amp;type=" + type.toString();
		}
		ExecutionYear executionYear = getExecutionYear();
		ExecutionPeriod period = getExecutionPeriod();
		if(executionYear!=null) {
			parameters += "&amp;executionYearId=" + getExecutionYear().getIdInternal();
		}
		if(period!=null) {
			parameters += "&amp;executionPeriodId=" + getExecutionPeriod().getIdInternal();
		}
		return parameters;
	}
	
	
	@Override
	public FileSearchCriteria getSearchCriteria(int start) {
	
		FileSearchCriteria criteria = new FileSearchCriteria(start,pageSize);
		List<SearchElement> elements = getSearchElements();
		
		if(hasSearchElements()) {
			for(EducationalResourceType type : getEducationalResourceTypes()) {
				criteria.addOrCriteria(SearchField.TYPE,type.getType());
			}
			for(SearchElement element : elements) {
				if(element.getConjunction().equals(ConjunctionType.AND)) {
					criteria.addAndCriteria(element.getSearchField(), element.getQueryValue());
				}
				if(element.getConjunction().equals(ConjunctionType.OR)) {
					criteria.addOrCriteria(element.getSearchField(), element.getQueryValue());
				}
			}
		}
		return criteria;
	}
}
