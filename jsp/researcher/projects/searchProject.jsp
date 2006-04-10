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
	<u><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.project.createProjectUseCase.step.searchProject"/></u> >
 	<strong>
 		<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.project.createProjectUseCase.step.title"/> 2 :
 	</strong>
 	<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.project.createProjectUseCase.step.insertData"/>
		
	<br/>
	<br/>
	
	<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.project.createProjectUseCase.step.searchProjectExplanation"/>
  	
  	<br/>
  	<br/>
		
	<html:form action="/projects/createProject.do?method=searchProjectTitle">
		<p>
			<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.project.searchProject.projectTitle"/>: 
			<html:text property="searchedProjectTitle" />
			<html:submit styleClass="inputbutton">
				<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.project.searchProject.label.button.search"/>
			</html:submit>
		</p>
	</html:form>
	
	<logic:present name="orderedProjectsList">
		<fr:view name="orderedProjectsList" layout="tabular-list" >
			<fr:layout>
				<fr:property name="subLayout" value="values"/>
				<fr:property name="subSchema" value="project.summary"/>
			
				<fr:property name="link(select)" value="/projects/createProject.do?method=selectProject"/>
				<fr:property name="param(select)" value="idInternal/oid"/>
				<fr:property name="key(select)" value="researcher.project.createProject.select"/>
				<fr:property name="bundle(select)" value="RESEARCHER_RESOURCES"/>
				<fr:property name="order(select)" value="1"/>
			</fr:layout>
		</fr:view>
		
		<html:link page="/projects/createProject.do?method=prepareCreateProject"><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.project.projectsManagement.createProject" /></html:link>
	</logic:present>
				
	
</logic:present>
		
<br/>