<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<em><bean:message key="label.manage.content" bundle="CONTENT_RESOURCES"/></em>
<h2><bean:message key="create.section" bundle="CONTENT_RESOURCES"/></h2>

<html:xhtml/>

<bean:define id="cid" name="container" property="externalId"/>

<fr:edit id="createSection" name="bean" schema="create.section.for.container" action="<%= "/contentManagement.do?method=createSection&contentId=" + cid%>"> 
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle5"/>
	</fr:layout>
	
	<fr:destination name="cancel" path="<%= "/contentManagement.do?method=viewContainer&contentId=" + cid%>"/> 
</fr:edit>

