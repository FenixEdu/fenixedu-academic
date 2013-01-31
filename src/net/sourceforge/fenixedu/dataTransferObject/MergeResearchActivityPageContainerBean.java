package net.sourceforge.fenixedu.dataTransferObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.DomainObject;

public abstract class MergeResearchActivityPageContainerBean extends PageContainerBean implements Serializable {

	private List<DomainObject> selectedObjects = new ArrayList<DomainObject>();

	private PageContainerBean pageContainerBean;

	public MergeResearchActivityPageContainerBean() {
		pageContainerBean = new PageContainerBean();
	}

	public List<DomainObject> getSelectedObjects() {
		return this.selectedObjects;
	}

	@Override
	protected void setPageObjects(List<DomainObject> pageObjects) {
		this.selectedObjects = pageObjects;
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
		if (getPageContainerBean().getSelected() != null) {
			this.selectedObjects.add(getPageContainerBean().getSelected());
			getPageContainerBean().setSelected(null);
		}
	}

	public void removeSelected() {
		if (getSelected() != null) {
			this.selectedObjects.remove(getSelected());
			setSelected(null);
		}
	}

	public void reset() {
		setSelected(null);
		selectedObjects = new ArrayList<DomainObject>();
	}
}
