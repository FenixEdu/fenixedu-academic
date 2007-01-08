package net.sourceforge.fenixedu.dataTransferObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.presentationTier.Action.teacher.FileItemCreationBean.EducationalResourceType;
import pt.utl.ist.fenix.tools.file.FileSearchCriteria;
import pt.utl.ist.fenix.tools.file.FileSearchCriteria.SearchField;
import pt.utl.ist.fenix.tools.file.FilesetMetadataQuery.ConjunctionType;

public class SearchDSpaceBean implements Serializable {
	
	protected static final int pageSize=15;
	
	List<SearchElement> searchElements;
	List<DomainReference> results;
	
	private int page;
	private int totalItems;
	private int numberOfPages;
	
	public SearchDSpaceBean() {
		this.searchElements = new ArrayList<SearchElement>();
		this.results = null;
		this.page=1;
		this.totalItems=0;
		this.numberOfPages=1;
	} 
	
	public int getPageSize() {
		return pageSize;
	}
		
	public int getSearchElementsSize() {
		return this.searchElements.size();
	}
	
	public List<SearchElement> getSearchElements() {
		return searchElements;
	}

	public void setSearchElements(List<SearchElement> searchElements) {
		this.searchElements = searchElements;
	}

	public void addSearchElement() {
		this.searchElements.add(SearchElement.emptySearchElement());
	}
	
	public void addSearchElement(Integer index) {
		this.searchElements.add(index,SearchElement.emptySearchElement());
	}
	public void addSearchElement(SearchElement element) {
		this.searchElements.add(element);
	}
	
	public void removeSearchElement(int i) {
		this.searchElements.remove(i);
	}
	
	public String getSearchElementsAsParameters() {
		String parameters ="";

		for(SearchElement element : getSearchElements()) {
			parameters += "&amp;criteria=" + element.getConjunction() + ":" + element.getSearchField() + ":" + element.getQueryValue();
		}
		return parameters;
	}
	
	public FileSearchCriteria getSearchCriteria(int start) {
		FileSearchCriteria criteria = new FileSearchCriteria(start,pageSize);
		List<SearchElement> elements = getSearchElements();
		
		if(hasSearchElements()) {
		
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
	
	protected boolean hasSearchElements() {
		for(SearchElement element : getSearchElements()) {
			if(element.queryValue.length()>0) return true;
		}
		return false;
	}
	
	public void setResults(List<DomainObject> results) {
		this.results = new ArrayList<DomainReference>();
		for(DomainObject result : results) {
			this.results.add(new DomainReference(result));
		}
	}
	
	public List<DomainObject> getResults() {
		if(this.results==null) return null;
		List<DomainObject> objects = new ArrayList<DomainObject> ();
		for(DomainReference reference : this.results) {
			objects.add(reference.getObject());
		}
		return objects;
	}
	
	public static class SearchElement implements Serializable {
		private SearchField field;
		private String queryValue;
		private ConjunctionType conjunction;
		
		private SearchElement() {
			super();
		}
	
		public SearchElement(SearchField field, String queryValue, ConjunctionType type) {
			this();
			this.field = field;
			this.queryValue = queryValue;
			this.conjunction = type;
		}

		public SearchField getSearchField() {
			return this.field;
		}
		
		public void setSearchField(SearchField field) {
			this.field =field; 
		}
		
		public String getQueryValue() {
			return this.queryValue;
		}
		
		public void setQueryValue(String queryValue) {
			this.queryValue = queryValue;
		}
		
		public static SearchElement emptySearchElement() {
			return new SearchElement(SearchField.ANY," ",ConjunctionType.AND);
		}
		
		public ConjunctionType getConjunction() {
			return conjunction;
		}
		
		public void setConjunction(ConjunctionType type) {
			this.conjunction = type;
		}
	}

	public int getNumberOfPages() {
		return numberOfPages;
	}

	public void setNumberOfPages(int numberOfPages) {
		this.numberOfPages = numberOfPages;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getTotalItems() {
		return totalItems;
	}

	public void setTotalItems(int totalItems) {
		this.totalItems = totalItems;
	}

}
