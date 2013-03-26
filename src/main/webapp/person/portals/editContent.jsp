<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="date"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<fr:view name="content">
	<fr:layout name="manage-content-bread-crumbs">
		<fr:property name="linkFor(Section)" value="/contentManagement.do?method=viewContainer&contentId=${idInternal}"/>
		<fr:property name="linkFor(Portal)" value="/contentManagement.do?method=viewContainer&contentId=${idInternal}"/>
		<fr:property name="linkFor(MetaDomainObjectPortal)" value="/contentManagement.do?method=viewContainer&contentId=${idInternal}"/>
		<fr:property name="linkFor(Functionality)" value="/contentManagement.do?method=viewElement&contentId=${idInternal}"/>
	</fr:layout>
</fr:view>

<bean:define id="method" value="viewContainer"/>

<logic:equal name="content" property="element" value="true">
	<bean:define id="method" value="viewElement"/>
</logic:equal>

<bean:define id="contentId" name="content" property="idInternal"/>

<bean:define id="type" name="content" property="class.simpleName" />

<fr:edit name="content" schema="<%= "edit.contents." + type%>" action="<%= "/contentManagement.do?method=" + method + "&contentId=" + contentId%>">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2"/>
	</fr:layout>
</fr:edit>