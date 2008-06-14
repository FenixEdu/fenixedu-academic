<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e" %>
<html:xhtml/>

<h2><bean:message bundle="MANAGER_RESOURCES" key="title.send.mail"/></h2>
<br/>

<p><bean:message bundle="MANAGER_RESOURCES" key="message.email.sent"/></p>

<fr:view name="sendEmailBean" schema="net.sourceforge.fenixedu.dataTransferObject.SendEmailBean.common">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4 thright thlight mtop05"/>
	</fr:layout>
</fr:view>

<fr:view name="sendEmailBean" schema="net.sourceforge.fenixedu.dataTransferObject.SendEmailBean.recipients">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4 thleft thlight mtop05"/>
	</fr:layout>
</fr:view>
