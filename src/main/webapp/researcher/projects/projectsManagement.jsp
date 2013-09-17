<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<h2><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.project.projectsManagement.title"/></h2>

<ul class="list5 mvert2">
	<li>
		<html:link page="/projects/createProject.do?method=prepareCreateProject"><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.project.projectsManagement.createProject" /></html:link>
	</li>
</ul>

<fr:view name="projects" layout="tabular-list" >
	<fr:layout>
		<fr:property name="subLayout" value="values"/>
		<fr:property name="subSchema" value="project.summary"/>
	
		<fr:property name="link(view)" value="/projects/viewProject.do?method=prepare"/>
		<fr:property name="param(view)" value="externalId/projectId"/>
		<fr:property name="key(view)" value="researcher.project.projectsManagement.view"/>
		<fr:property name="bundle(view)" value="RESEARCHER_RESOURCES"/>
		<fr:property name="order(view)" value="1"/>

		<fr:property name="link(delete)" value="/projects/projectsManagement.do?method=delete"/>
		<fr:property name="param(delete)" value="externalId/projectId"/>
		<fr:property name="key(delete)" value="researcher.project.projectsManagement.delete"/>
		<fr:property name="bundle(delete)" value="RESEARCHER_RESOURCES"/>
		<fr:property name="order(delete)" value="2"/>

		<fr:property name="classes" value="tstyle2"/>
		<fr:property name="columnClasses" value=",,"/>
	</fr:layout>
</fr:view>
