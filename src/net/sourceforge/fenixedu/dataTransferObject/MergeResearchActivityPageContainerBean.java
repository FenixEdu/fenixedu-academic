package net.sourceforge.fenixedu.dataTransferObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.DomainReference;

public abstract class MergeResearchActivityPageContainerBean extends PageContainerBean implements Serializable {
    
    private List<DomainReference<DomainObject>> selectedObjects = new ArrayList<DomainReference<DomainObject>>();
    
    private PageContainerBean pageContainerBean;
    
    public MergeResearchActivityPageContainerBean() {
	pageContainerBean = new PageContainerBean();
    }
    
    public List<DomainObject> getSelectedObjects() {
	List<DomainObject> result = new ArrayList<DomainObject>();
	for (DomainReference<DomainObject> domainReference : this.selectedObjects) {
	    result.add(domainReference.getObject());
	}
	return result;
    }

    public void setPageObjects(List<DomainObject> pageObjects) {
	this.selectedObjects = new ArrayList<DomainReference<DomainObject>>();
	for (DomainObject object : pageObjects) {
	    this.selectedObjects.add(new DomainReference<DomainObject>(object));
	}
    }

    public PageContainerBean getPageContainerBean() {
        return pageContainerBean;
    }

    public void setPageContainerBean(PageContainerBean pageContainerBean) {
        this.pageContainerBean = pageContainerBean;
    }
    
    @Override
    public List<DomainObject> getObjects() {
        return getSelectedObjects();
    }
    
    @Override
    public void setObjects(List<DomainObject> objects) {
	List<DomainObject> result = new ArrayList<DomainObject>(objects);
	result.removeAll(getSelectedObjects());
        getPageContainerBean().setObjects(result);
    }

    public void addSelected() {
	if(getPageContainerBean().getSelected() != null) {
	    this.selectedObjects.add(new DomainReference<DomainObject>(getPageContainerBean().getSelected()));
	    getPageContainerBean().setSelected(null);
	}
    }

    public void removeSelected() {
	if(getSelected() != null) {
	    this.selectedObjects.remove(new DomainReference<DomainObject>(getSelected()));
	    setSelected(null);
	}
    }

}
