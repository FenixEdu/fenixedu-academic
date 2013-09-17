<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="date"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<h2><bean:message key="label.manage.content" bundle="CONTENT_RESOURCES"/></h2>

<fr:view name="content">
	<fr:layout name="manage-content-bread-crumbs">
		<fr:property name="linkFor(Section)" value="/contentManagement.do?method=viewContainer&contentId=${externalId}"/>
		<fr:property name="linkFor(Portal)" value="/contentManagement.do?method=viewContainer&contentId=${externalId}"/>
		<fr:property name="linkFor(Functionality)" value="/contentManagement.do?method=viewElement&contentId=${externalId}"/>
		<fr:property name="linkFor(FunctionalityCall)" value="/contentManagement.do?method=viewElement&contentId=${externalId}"/>
	</fr:layout>
</fr:view>

<bean:define id="contentClass" name="content" property="class.simpleName"/>

<fr:view name="content" schema="<%= "view.contents.details."  + contentClass %>">
	<fr:layout name="tabular-nonNullValues">
		<fr:property name="classes" value="tstyle2"/>
	</fr:layout>
</fr:view>
<bean:define id="cid" name="content" property="externalId"/>

<bean:message key="label.availableOperations" bundle="CONTENT_RESOURCES"/>:	

<html:link page="<%= "/contentManagement.do?method=editContent&amp;contentId=" + cid %>">
	<bean:message key="label.edit"/>
</html:link>,
<html:link page="<%= "/contentManagement.do?method=prepareEditAvailabilityPolicy&amp;contentId=" + cid %>" >
	<bean:message key="edit.availability.policy" bundle="CONTENT_RESOURCES"/>
</html:link>

<logic:notEmpty name="parentContainer">
 <bean:define id="parentId" name="parentContainer" property="externalId"/>
	,
	<html:link page="<%= "/contentManagement.do?method=deleteContent&amp;contentId=" + cid + "&amp;contentParentId=" + parentId %>" >
		<bean:message key="delete.content" bundle="CONTENT_RESOURCES"/>
	</html:link>
</logic:notEmpty>

