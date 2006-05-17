<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="RESEARCHER">		

	<bean:define id="projectId" name="selectedProject" property="idInternal" />
	
	<em><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.project.projectsManagement.superUseCaseTitle"/></em>
		
	<h2/> <bean:message bundle="RESEARCHER_RESOURCES" key="researcher.project.viewProject.useCasetitle"/> </h2>
  	
  	<br/>
  	<br/>
		
	<%-- PARTICIPATIONS --%>	
	<h3/> <bean:message bundle="RESEARCHER_RESOURCES" key="researcher.project.editProject.participants"/> </h3>
	<fr:view name="participations" layout="tabular" schema="projectParticipation.summary"/>
	<html:link page="<%="/projects/editProject.do?method=prepareEditParticipants&projectId="+projectId%>">
		<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.project.editProject.editParticipants" />
	</html:link> <br/>	
	
	<%-- DATA --%>		
	<h3/> <bean:message bundle="RESEARCHER_RESOURCES" key="researcher.project.editProject.data"/> </h3>
	<fr:view name="selectedProject" schema="project.view-defaults">
	    <fr:layout name="tabular">
    	    <fr:property name="classes" value="style1"/>
        	<fr:property name="columnClasses" value="listClasses,,"/>
	    </fr:layout>
	</fr:view>
	<html:link page="<%="/projects/editProject.do?method=prepareEditData&projectId="+projectId%>">
		<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.project.editProject.editData" />
	</html:link> <br/>

	<%-- EVENTS --%>		
	<h3/> <bean:message bundle="RESEARCHER_RESOURCES" key="researcher.project.editProject.events"/> </h3>
	<fr:view name="selectedProject" property="associatedEvents" schema="projectEventAssociation.event-summary">
	    <fr:layout name="tabular">
    	    <fr:property name="classes" value="style1"/>
        	<fr:property name="columnClasses" value="listClasses,,"/>
	    </fr:layout>
	</fr:view>
	<html:link page="<%="/projects/editProject.do?method=prepareEditEventAssociations&projectId="+projectId%>">
		<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.project.editProject.editAssociatedEvents" />
	</html:link>  <br/>
	
	<%-- UNITS --%>		
	<h3/> <bean:message bundle="RESEARCHER_RESOURCES" key="researcher.project.editProject.units"/> </h3>
	<html:link page="/projects/editProject.do?method=manageAssociatedUnits">
		<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.project.editProject.editAssociatedUnits" />
	</html:link>  <br/>
		
	
</logic:present>
		
<br/>