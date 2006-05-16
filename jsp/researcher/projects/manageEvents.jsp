<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="RESEARCHER">		
  	<h2 id='pageTitle'/>
  		<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.project.editProject.manageEvents.title"/>
  		<bean:define id="projectTitle" name="selectedProject" property="title.content"/>
  		<%= projectTitle %>
  	</h2>
		
		
	<fr:view name="selectedProject" property="associatedEvents" layout="tabular-list" >
		<fr:layout>
			<fr:property name="subLayout" value="values"/>
			<fr:property name="subSchema" value="projectEventAssociation.event-summary"/>
		
			<fr:property name="link(view)" value="/projects/editProject.do?method=viewEvent"/>
			<fr:property name="param(view)" value="idInternal/eventId"/>
			<fr:property name="key(view)" value="researcher.project.editProject.manageEvents.view"/>
			<fr:property name="bundle(view)" value="RESEARCHER_RESOURCES"/>
			<fr:property name="order(view)" value="1"/>

			<fr:property name="link(delete)" value="/projects/editProject.do?method=removeEvent"/>
			<fr:property name="param(delete)" value="idInternal/eventId"/>
			<fr:property name="key(delete)" value="researcher.project.editProject.manageEvents.remove"/>
			<fr:property name="bundle(delete)" value="RESEARCHER_RESOURCES"/>
			<fr:property name="order(delete)" value="2"/>
		</fr:layout>
	</fr:view>
		
	<bean:define id="projectId" name="selectedProject" property="idInternal"/>
	<html:link page="<%="/projects/editProject.do?method=prepareAssociateEvent?projectId="+projectId%>">
		<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.project.editProject.associateEvent" />
	</html:link>
</logic:present>
		
<br/>