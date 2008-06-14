<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<em><bean:message key="label.manage.content" bundle="CONTENT_RESOURCES"/></em>
<h2><bean:message key="create.section" bundle="CONTENT_RESOURCES"/></h2>

<html:xhtml/>

<bean:define id="cid" name="container" property="idInternal"/>

<fr:edit id="createSection" name="bean" schema="create.section.for.container" action="<%= "/contentManagement.do?method=createSection&contentId=" + cid%>"> 
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle5"/>
	</fr:layout>
	
	<fr:destination name="cancel" path="<%= "/contentManagement.do?method=viewContainer&contentId=" + cid%>"/> 
</fr:edit>

