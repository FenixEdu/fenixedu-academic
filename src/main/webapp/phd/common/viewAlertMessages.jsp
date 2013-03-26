<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<style>
.unreadSubject { font-weight: bold; background: #fafaea !important; }
.unread { background: #fafaea !important; } 
</style>

<ul>
	<li>
		<html:link action="/phdIndividualProgramProcess.do?method=viewAlertMessages">
			<bean:message key="label.phd.recentAlertMessages" bundle="PHD_RESOURCES" />
		</html:link>
	</li>
	
	<li>
		<html:link action="/phdIndividualProgramProcess.do?method=viewUnreadAlertMessages">
			<bean:message key="label.phd.unreadAlertMessages" bundle="PHD_RESOURCES" />
			<bean:size id="unreadMessagesSize" name="alertMessagesToNotify"/>
			(<%= unreadMessagesSize.toString() %>)
		</html:link>
	</li>
	
	<li>
		<html:link action="/phdIndividualProgramProcess.do?method=viewAlertMessageArchive">
			<bean:message key="label.phd.archive" bundle="PHD_RESOURCES" />
		</html:link>
	</li>
</ul>


<bean:define id="unread" name="unread" type="String"/> 
<bean:size id="size" name="alertMessages" />
<logic:equal name="unread" value="true">
	<h3 class="mtop15 mbottom05">
		<bean:message key="label.phd.unreadAlertMessages" bundle="PHD_RESOURCES" />
		<logic:greaterEqual name="size" value="1">(<%= size.toString() %>)</logic:greaterEqual>
	</h3>
</logic:equal>

<logic:equal name="unread" value="false">
	<h3 class="mtop15 mbottom05">
		<logic:equal name="tooManyMessages" value="false">
			<bean:message key="label.phd.messages" bundle="PHD_RESOURCES" />
			<logic:greaterEqual name="size" value="1">(<%= size.toString() %>)</logic:greaterEqual>
		</logic:equal>
		<logic:equal name="tooManyMessages" value="true">
			<bean:message key="label.phd.lastAlertMessages" arg0="<%= size.toString() %>" bundle="PHD_RESOURCES" />
		</logic:equal>
	</h3>
</logic:equal>


<logic:notEmpty name="alertMessages">
	<fr:view name="alertMessages">
		<fr:schema type="net.sourceforge.fenixedu.domain.phd.alert.PhdAlertMessage" bundle="PHD_RESOURCES">	
			<fr:slot name="whenCreated" layout="no-time" />
			<fr:slot name="process" layout="link">
				<fr:property name="contextRelative" value="true"/>
				<fr:property name="moduleRelative" value="true"/>
				<fr:property name="linkFormat" value="/phdIndividualProgramProcess.do?method=viewProcess&backMethod=viewAlertMessages&processId=${externalId}" />
				<fr:property name="format" value="${processNumber}"/>
			</fr:slot>
			<fr:slot name="subject" layout="link">
				<fr:property name="contextRelative" value="true"/>
				<fr:property name="moduleRelative" value="true"/>
				<fr:property name="useParent" value="true"/>
				<fr:property name="linkFormat" value="<%= "/phdIndividualProgramProcess.do?method=readAlertMessage&global=true&alertMessageId=${externalId}&unread=" + unread %>" />
			</fr:slot>
			<fr:slot name="readed" />
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight"/>
			<fr:property name="columnClasses" value=",,,acenter"/>
			<fr:property name="conditionalColumnClass(whenCreated)" value="unread"/>
			<fr:property name="useCssIfNot(whenCreated)" value="readed"/>
			<fr:property name="column(whenCreated)" value="0"/>
			<fr:property name="conditionalColumnClass(process)" value="unread"/>
			<fr:property name="useCssIfNot(process)" value="readed"/>
			<fr:property name="column(process)" value="1"/>
			<fr:property name="conditionalColumnClass(subject)" value="unreadSubject"/>
			<fr:property name="useCssIfNot(subject)" value="readed"/>
			<fr:property name="column(subject)" value="2"/>
			<fr:property name="conditionalColumnClass(readed)" value="unread"/>
			<fr:property name="useCssIfNot(readed)" value="readed"/>
			<fr:property name="column(readed)" value="3"/>
		</fr:layout>
	</fr:view>	
</logic:notEmpty>
<logic:empty name="alertMessages">
	<p><em><bean:message  key="label.phd.noAlertMessages" bundle="PHD_RESOURCES"/>.</em></p>
</logic:empty>
