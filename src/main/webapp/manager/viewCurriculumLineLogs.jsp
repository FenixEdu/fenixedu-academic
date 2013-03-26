<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message bundle="MANAGER_RESOURCES" key="label.view.curriculumLineLogs"/></h2>

<logic:messagesPresent message="true">
	<ul>
		<html:messages bundle="MANAGER_RESOURCES" id="messages" message="true">
			<li><span class="error0"><bean:write name="messages" /></span></li>
		</html:messages>
	</ul>
</logic:messagesPresent>

<fr:edit id="search" name="bean" schema="curriculumLineLog.search" action="/curriculumLineLogs.do?method=viewCurriculumLineLogs">
	<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thright mtop025"/>
	        <fr:property name="columnClasses" value=",,tdclear tderror1"/>
	</fr:layout>
</fr:edit>

<logic:present name="curriculumLineLogs">
	<p>
		<logic:empty name="curriculumLineLogs">
			<em><bean:message bundle="MANAGER_RESOURCES" key="label.noCurriculumLineLogsFound"/></em>	
		</logic:empty>
		<logic:notEmpty name="curriculumLineLogs">
			<fr:view name="curriculumLineLogs" schema="curriculumLineLogs.list">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle4" />
					<fr:property name="sortBy" value="dateDateTime=asc,description=asc"/>
				</fr:layout>
			</fr:view>	
		</logic:notEmpty>
	</p>
</logic:present>

<logic:present name="bean" property="executionPeriod">
	<html:link action="curriculumLineLogs.do?method=viewCurriculumLineLogStatistics" paramId="executionSemesterId" paramName="bean" paramProperty="executionPeriod.externalId">
		<bean:message bundle="MANAGER_RESOURCES" key="label.view.curriculumLineLogs.statistics"/>
	</html:link>
</logic:present>
