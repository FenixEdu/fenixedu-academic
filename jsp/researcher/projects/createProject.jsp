<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="RESEARCHER">		

	<em><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.project.projectsManagement.superUseCaseTitle"/></em>
		
	<h2/> <bean:message bundle="RESEARCHER_RESOURCES" key="researcher.project.createProject.useCasetitle"/> </h2>
  	
	<strong>
		<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.project.createProjectUseCase.step.title"/> 1 :
	</strong>
	<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.project.createProjectUseCase.step.searchProject"/> >
 	<strong>
 		<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.project.createProjectUseCase.step.title"/> 2 :
 	</strong>
 	<u><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.project.createProjectUseCase.step.insertData"/></u> >
  	<strong>
	  	<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.project.createProjectUseCase.step.title"/> 3 :
	</strong>
		<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.project.createProjectUseCase.step.createTranslations"/>
		
	<br/>
	<br/>
	
	<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.project.createProjectUseCase.step.insertDataExplanation"/>
  	
  	<br/>
  	<br/>
		
	
	<fr:create type="net.sourceforge.fenixedu.domain.research.project.ProjectParticipation" schema="projectParticipation.create-defaults">
		<fr:hidden slot="party" name="party"/>
	    <fr:layout name="tabular">
    	    <fr:property name="classes" value="style1"/>
        	<fr:property name="columnClasses" value="listClasses,,"/>
	    </fr:layout>
	</fr:create>
	
</logic:present>
		
<br/>