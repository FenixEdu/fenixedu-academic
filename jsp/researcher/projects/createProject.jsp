<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="RESEARCHER">		

	<em><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.project.projectsManagement.superUseCaseTitle"/></em>
		
	<h2/> <bean:message bundle="RESEARCHER_RESOURCES" key="researcher.project.createProject.useCasetitle"/> </h2>
  	
 	<strong>
 		<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.project.createProjectUseCase.step.title"/> 1 :
 	</strong>
 	<u><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.project.createProjectUseCase.step.insertData"/></u>
		
	<br/>
	<br/>
	
	<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.project.createProjectUseCase.step.insertDataExplanation"/>
  	
  	<br/>
  	<br/>
 	<br/>
		
	
	<h3/> <bean:message bundle="RESEARCHER_RESOURCES" key="researcher.project.createProject.participants"/> </h3>
	
	<h3/> <bean:message bundle="RESEARCHER_RESOURCES" key="researcher.project.createProject.data"/> </h3>
	
	<fr:create type="net.sourceforge.fenixedu.domain.research.project.ProjectParticipation" schema="projectParticipation.create-defaults" action="/projects/projectsManagement.do?method=listProjects">
		<fr:hidden slot="party" name="party"/>
	    <fr:layout name="tabular">
    	    <fr:property name="classes" value="style1"/>
        	<fr:property name="columnClasses" value="listClasses,,"/>
	    </fr:layout>
   		<fr:destination name="cancel" path="/projects/projectsManagement.do?method=listProjects"/>
	</fr:create>
	
</logic:present>
		
<br/>