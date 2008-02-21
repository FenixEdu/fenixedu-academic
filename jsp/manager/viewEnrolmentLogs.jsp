<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message bundle="MANAGER_RESOURCES" key="label.view.enrolmentLogs"/></h2>

<logic:messagesPresent message="true">
	<ul>
		<html:messages bundle="MANAGER_RESOURCES" id="messages" message="true">
			<li><span class="error0"><bean:write name="messages" /></span></li>
		</html:messages>
	</ul>
</logic:messagesPresent>

<fr:edit id="search"
		 name="bean"
		 type="net.sourceforge.fenixedu.dataTransferObject.enrolmentLog.SearchEnrolmentLog"
		 schema="enrolmentLog.search"
		 action="/enrolmentLogs.do?method=viewEnrolmentLogs">
	<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thright mtop025"/>
	        <fr:property name="columnClasses" value=",,tdclear tderror1"/>
	</fr:layout>
</fr:edit>

<logic:present name="enrolmentLogs">
	<p>
		<logic:empty name="enrolmentLogs">
			<em><bean:message bundle="MANAGER_RESOURCES" key="label.noEnrolmentLogsFound"/></em>	
		</logic:empty>
		<logic:notEmpty name="enrolmentLogs">
			<fr:view name="enrolmentLogs" schema="enrolmentLogs.list">
				<fr:layout name="tabular">
					<fr:property name="sortBy" value="dateDateTime=asc"/>
					<fr:property name="classes" value="tstyle4" />
				</fr:layout>
			</fr:view>	
		</logic:notEmpty>
	</p>
</logic:present>
