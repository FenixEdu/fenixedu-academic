<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="RESEARCHER">		

	<em><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.project.projectsManagement.superUseCaseTitle"/></em>
		
	<h2/> <bean:message bundle="RESEARCHER_RESOURCES" key="researcher.project.editProject.useCasetitle"/> </h2>
  	
	<br/>
	<br/>
	
	<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.project.editProjectUseCase.editDataExplanation"/>
  	
  	<br/>
  	<br/>
		
	
	<fr:edit name="selectedProjectParticipation" schema="projectParticipation.edit-defaults" action="/projects/projectsManagement.do?method=listProjects">
		<fr:hidden slot="party" name="party"/>
	    <fr:layout name="tabular">
    	    <fr:property name="classes" value="style1"/>
        	<fr:property name="columnClasses" value="listClasses,,"/>
	    </fr:layout>
	    <fr:destination name="cancel" path="/projects/projectsManagement.do?method=listProjects"/>
	</fr:edit>
	
</logic:present>
		
<br/>