/*
 * Created on 5/Mai/2003
 *
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionCourse;

/**
 * @author João Mota
 * 
 *  
 */
public class ExecutionCourseSiteView extends SiteView {

    private DomainReference<ExecutionCourse> executionCourseDomainReference; 

    /**
     * @param commonComponent
     * @param bodyComponent
     */
    public ExecutionCourseSiteView(ISiteComponent commonComponent, ISiteComponent bodyComponent) {
        setCommonComponent(commonComponent);
        setComponent(bodyComponent);
    }

    private ISiteComponent commonComponent;

    /**
     * @return
     */
    public ISiteComponent getCommonComponent() {

        return commonComponent;
    }

    /**
     * @param component
     */
    public void setCommonComponent(ISiteComponent component) {
        commonComponent = component;
    }

    public boolean equals(Object obj) {

        boolean resultado = false;

        if (obj instanceof ExecutionCourseSiteView) {
            ExecutionCourseSiteView siteView = (ExecutionCourseSiteView) obj;

            resultado = getCommonComponent().equals(siteView.getCommonComponent())
                    && getComponent().equals(siteView.getComponent());
        }

        return resultado;
    }

	public ExecutionCourse getExecutionCourse() {
		return executionCourseDomainReference == null ? null : executionCourseDomainReference.getObject();
	}

	public void setExecutionCourse(ExecutionCourse executionCourse) {
        executionCourseDomainReference = new DomainReference<ExecutionCourse>(executionCourse);
	}

}