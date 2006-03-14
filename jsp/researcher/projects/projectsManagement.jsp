<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="RESEARCHER">		
  	<h2 id='pageTitle'/> <bean:message bundle="RESEARCHER_RESOURCES" key="researcher.project.projectsManagement.title"/> </h2>
		
		
		<fr:view name="projects" layout="tabular-list" >
			<fr:layout>
				<fr:property name="subLayout" value="values"/>
				<fr:property name="subSchema" value="project.summary"/>
			
				<fr:property name="link(edit)" value="/project/projectsManagement.do?method=edit"/>
				<fr:property name="param(edit)" value="idInternal/oid"/>
				<fr:property name="key(edit)" value="researcher.project.projectsManagement.edit"/>
				<fr:property name="bundle(edit)" value="RESEARCHER_RESOURCES"/>
				<fr:property name="order(edit)" value="1"/>

				<fr:property name="link(delete)" value="/project/projectsManagement.do?method=delete"/>
				<fr:property name="param(delete)" value="idInternal/oid"/>
				<fr:property name="key(delete)" value="researcher.project.projectsManagement.delete"/>
				<fr:property name="bundle(delete)" value="RESEARCHER_RESOURCES"/>
				<fr:property name="order(delete)" value="2"/>
			</fr:layout>
		</fr:view>
		
	<html:link page="/projects/createProject.do"><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.project.projectsManagement.createProject" /></html:link>
</logic:present>
		
<br/>