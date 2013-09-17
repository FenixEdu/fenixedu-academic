<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="date"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<fr:view name="content">
	<fr:layout name="manage-content-bread-crumbs">
		<fr:property name="linkFor(Section)" value="/contentManagement.do?method=viewContainer&contentId=${externalId}"/>
		<fr:property name="linkFor(Portal)" value="/contentManagement.do?method=viewContainer&contentId=${externalId}"/>
		<fr:property name="linkFor(MetaDomainObjectPortal)" value="/contentManagement.do?method=viewContainer&contentId=${externalId}"/>
		<fr:property name="linkFor(Functionality)" value="/contentManagement.do?method=viewElement&contentId=${externalId}"/>
	</fr:layout>
</fr:view>

<bean:define id="method" value="viewContainer"/>

<logic:equal name="content" property="element" value="true">
	<bean:define id="method" value="viewElement"/>
</logic:equal>

<bean:define id="contentId" name="content" property="externalId"/>

<bean:define id="type" name="content" property="class.simpleName" />

<fr:edit name="content" schema="<%= "edit.contents." + type%>" action="<%= "/contentManagement.do?method=" + method + "&contentId=" + contentId%>">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2"/>
	</fr:layout>
</fr:edit>