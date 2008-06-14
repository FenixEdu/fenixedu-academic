<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="date"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<bean:define id="showVigilants" value="<%= request.getParameter("showVigilants") != null ? request.getParameter("showVigilants") : "false" %>"/>
<bean:define id="groupId" name="vigilantGroup" property="idInternal"/>

<em><bean:message bundle="VIGILANCY_RESOURCES" key="label.navheader.person.examCoordinator"/></em>
<h2><bean:message bundle="VIGILANCY_RESOURCES" key="label.GroupReport"/></h2>

<strong><bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.vigilantGroup"/></strong>: <fr:view name="vigilantGroup" property="name"/>

<logic:equal name="showVigilants" value="false">
	<ul>
		<li><span class="highlight1"><bean:message bundle="VIGILANCY_RESOURCES" key="label.showNoNames"/></span> | <html:link page="<%= "/vigilancy/vigilantGroupManagement.do?method=generateReportForGroup&oid=" + groupId + "&amp;showVigilants=true"%>"><bean:message bundle="VIGILANCY_RESOURCES" key="label.showNames"/></html:link> | <html:link page="<%= "/vigilancy/exportReport.do?method=exportGroupReportAsXLS&oid=" +  groupId + "&amp;showVigilants=" + showVigilants %>"><bean:message bundle="VIGILANCY_RESOURCES" key="label.exportGroupReport"/></html:link></li>
	</ul>
	<fr:view name="stats" schema="showGroupStats">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1"/>
			<fr:property name="columnClasses" value="nowrap,acenter width6em,acenter width6em,acenter width6em,acenter width6em,acenter width6em,acenter width6em"/>
			<fr:property name="rowClasses" value=",bgcolor4"/>
		</fr:layout>
	</fr:view>
	<ul>
		<li><span class="highlight1"><bean:message bundle="VIGILANCY_RESOURCES" key="label.showNoNames"/></span> | <html:link page="<%= "/vigilancy/vigilantGroupManagement.do?method=generateReportForGroup&oid=" + groupId + "&amp;showVigilants=true"%>"><bean:message bundle="VIGILANCY_RESOURCES" key="label.showNames"/></html:link> | <html:link page="<%= "/vigilancy/exportReport.do?method=exportGroupReportAsXLS&oid=" +  groupId + "&amp;showVigilants=" + showVigilants %>"><bean:message bundle="VIGILANCY_RESOURCES" key="label.exportGroupReport"/></html:link></li>
	</ul>
</logic:equal>

<logic:equal name="showVigilants" value="true">
	<ul>
<li><html:link page="<%= "/vigilancy/vigilantGroupManagement.do?method=generateReportForGroup&oid=" + groupId + "&showVigilants=false" %>"><bean:message bundle="VIGILANCY_RESOURCES" key="label.showNoNames"/></html:link> | <span class="highlight1"><bean:message bundle="VIGILANCY_RESOURCES" key="label.showNames"/></span> | <html:link page="<%= "/vigilancy/exportReport.do?method=exportGroupReportAsXLS&oid=" +  groupId + "&amp;showVigilants=" + showVigilants %>"><bean:message bundle="VIGILANCY_RESOURCES" key="label.exportGroupReport"/></html:link></li>
	</ul>
	<fr:view name="stats" schema="showGroupStatsWithNames">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1"/>
			<fr:property name="columnClasses" value="nowrap,acenter width6em,acenter width6em,nowrap,acenter width6em,nowrap,acenter width6em,nowrap,acenter width6em,nowrap,acenter width6em,nowrap"/>
			<fr:property name="rowClasses" value=",bgcolor4"/>
		</fr:layout>
	</fr:view>
	<ul>
		<li><html:link page="<%= "/vigilancy/vigilantGroupManagement.do?method=generateReportForGroup&oid=" + groupId + "&showVigilants=false" %>"><bean:message bundle="VIGILANCY_RESOURCES" key="label.showNoNames"/></html:link> | <span class="highlight1"><bean:message bundle="VIGILANCY_RESOURCES" key="label.showNames"/></span> | <html:link page="<%= "/vigilancy/exportReport.do?method=exportGroupReportAsXLS&oid=" +  groupId + "&amp;showVigilants=" + showVigilants %>"><bean:message bundle="VIGILANCY_RESOURCES" key="label.exportGroupReport"/></html:link></li>
	</ul>
</logic:equal>
