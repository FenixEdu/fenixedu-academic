<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<html:xhtml/>

<h2><bean:message bundle="MESSAGING_RESOURCES" key="title.email.sent.emails"/></h2>

<logic:present name="message">

	<logic:present name="created">
		<p class="mtop15">
			<span class="success0">
				<bean:message bundle="MESSAGING_RESOURCES" key="message.email.sent"/>
			</span>
		</p>
	</logic:present>

	<logic:notPresent name="message" property="sent">
		<html:link page="/emails.do?method=deleteMessage" paramId="messagesId" paramName="message" paramProperty="idInternal">
			<bean:message bundle="APPLICATION_RESOURCES" key="label.delete"/>
		</html:link>
	</logic:notPresent>

	<fr:view name="message" schema="net.sourceforge.fenixedu.domain.util.email.Message.info">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1 thlight thleft thtop"/>
			<fr:property name="columnClasses" value="width11em,,,"/>
		</fr:layout>
	</fr:view>

</logic:present>
