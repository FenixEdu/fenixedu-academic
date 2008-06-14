<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="RESEARCHER">		

	<bean:define id="projectId" name="selectedProject" property="idInternal" />
	<em>Projectos</em> <!-- tobundle -->
	<h2><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.project.editProject.data.useCaseTitle"/></h2>
  	
	<p class="mtop2 mbottom05"><strong><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.project.editProject.data"/></strong></p>
	
	<fr:edit name="selectedProject" schema="project.edit-defaults" action="<%="/projects/viewProject.do?method=prepare&projectId="+projectId%>">
	    <fr:layout name="tabular">
    	    <fr:property name="classes" value="tstyle1 thlight thright throp mtop05"/>
        	<fr:property name="columnClasses" value=",,tdclear tderror1"/>
	    </fr:layout>
	    <fr:destination name="cancel" path="<%="/projects/viewProject.do?method=prepare&projectId="+projectId%>"/>
	</fr:edit>
	
</logic:present>
