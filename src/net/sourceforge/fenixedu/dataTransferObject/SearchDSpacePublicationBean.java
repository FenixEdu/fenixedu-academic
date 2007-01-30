package net.sourceforge.fenixedu.dataTransferObject;

import pt.utl.ist.fenix.tools.file.VirtualPathNode;

public class SearchDSpacePublicationBean extends SearchDSpaceBean {

	Boolean searchPublications;
	Boolean searchPatents;
	
	public SearchDSpacePublicationBean() {
		super();
		searchPublications = Boolean.TRUE;
		searchPatents = Boolean.TRUE;
	}
	
	public boolean getSearchPatents() {
		return searchPatents;
	}
	public void setSearchPatents(boolean searchPatents) {
		this.searchPatents = searchPatents;
	}
	public boolean getSearchPublications() {
		return searchPublications;
	}
	public void setSearchPublications(boolean searchPublications) {
		this.searchPublications = searchPublications;
	}
	
	@Override
	public String getSearchElementsAsParameters() {
		String parameters = super.getSearchElementsAsParameters();
		parameters += "&searchPublications=" + getSearchPublications() + "&searchPatents=" + getSearchPatents();
		return parameters;
	}
	
	public VirtualPathNode getSearchNode() {
		VirtualPathNode node = null;
		if(this.getSearchPatents() && !this.getSearchPublications()) {
			node = new VirtualPathNode("Patents", "Patents");
		}
		if(!this.getSearchPatents() && this.getSearchPublications()) {
			node =  new VirtualPathNode("Publications", "Publications");
		}
		return node;
	}
	
	
}
