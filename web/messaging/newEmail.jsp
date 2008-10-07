<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<html:xhtml/>

<h2><bean:message bundle="MANAGER_RESOURCES" key="title.send.mail"/></h2>
<br/>

<fr:form action="/emails.do?method=sendEmail">

	<fr:edit id="emailBean" name="emailBean" schema="net.sourceforge.fenixedu.domain.util.email.EmailBean.Create">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 thright thlight mtop05"/>
		</fr:layout>

		<fr:destination name="selectSender" path="/emails.do?method=newEmail"/>
	</fr:edit>

	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" onclick="this.form.method.value='moveEnrolments';">
		<bean:message bundle="MANAGER_RESOURCES" key="button.send" />
	</html:submit>

</fr:form>
