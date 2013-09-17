<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<em>Projectos</em> <!-- tobundle -->
	
<h2><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.project.createProject.useCasetitle"/></h2>

<p><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.project.createProjectUseCase.step.insertDataExplanation"/></p>


<p class="mbottom0"><strong><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.project.createProject.data"/></strong></p>

<fr:create type="net.sourceforge.fenixedu.domain.research.project.ProjectParticipation" schema="projectParticipation.create-defaults" action="/projects/projectsManagement.do?method=listProjects">
	<fr:hidden slot="party" name="party"/>
 	    <fr:layout name="tabular">
    	    <fr:property name="classes" value="tstyle1 thlight thright mtop05"/>
        	<fr:property name="columnClasses" value=",,tdclear tderror1"/>
	    </fr:layout>
	<fr:destination name="cancel" path="/projects/projectsManagement.do?method=listProjects"/>
</fr:create>
		
<br/>