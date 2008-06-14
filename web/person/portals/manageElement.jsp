<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="date"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message key="label.manage.content" bundle="CONTENT_RESOURCES"/></h2>

<fr:view name="content">
	<fr:layout name="manage-content-bread-crumbs">
		<fr:property name="linkFor(Section)" value="/contentManagement.do?method=viewContainer&contentId=${idInternal}"/>
		<fr:property name="linkFor(Portal)" value="/contentManagement.do?method=viewContainer&contentId=${idInternal}"/>
		<fr:property name="linkFor(Functionality)" value="/contentManagement.do?method=viewElement&contentId=${idInternal}"/>
		<fr:property name="linkFor(FunctionalityCall)" value="/contentManagement.do?method=viewElement&contentId=${idInternal}"/>
	</fr:layout>
</fr:view>

<bean:define id="contentClass" name="content" property="class.simpleName"/>

<fr:view name="content" schema="<%= "view.contents.details."  + contentClass %>">
	<fr:layout name="tabular-nonNullValues">
		<fr:property name="classes" value="tstyle2"/>
	</fr:layout>
</fr:view>
<bean:define id="cid" name="content" property="idInternal"/>

<bean:message key="label.availableOperations" bundle="CONTENT_RESOURCES"/>:	

<html:link page="<%= "/contentManagement.do?method=editContent&amp;contentId=" + cid %>">
	<bean:message key="label.edit"/>
</html:link>,
<html:link page="<%= "/contentManagement.do?method=prepareEditAvailabilityPolicy&amp;contentId=" + cid %>" >
	<bean:message key="edit.availability.policy" bundle="CONTENT_RESOURCES"/>
</html:link>

<logic:notEmpty name="parentContainer">
 <bean:define id="parentId" name="parentContainer" property="idInternal"/>
	,
	<html:link page="<%= "/contentManagement.do?method=deleteContent&amp;contentId=" + cid + "&amp;contentParentId=" + parentId %>" >
		<bean:message key="delete.content" bundle="CONTENT_RESOURCES"/>
	</html:link>
</logic:notEmpty>

