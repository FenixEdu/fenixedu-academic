<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<br/>

<bean:define id="url" value="/degreePaymentsManagement.do?method=uploadSibsPaymentsFile" />

<fr:form action="<%= url %>" encoding="multipart/form-data">

	<fr:edit id="paymentsFileBean-create" name="paymentsFileBean" visible="false" />
	
	<p>
		<strong><bean:message key="label.payments.uploadPaymentsFile.filename" bundle="MANAGER_RESOURCES" />:</strong>
		<fr:edit name="paymentsFileBean" slot="file">
			<fr:layout>
            	<fr:property name="size" value="40"/>
	        </fr:layout>
		</fr:edit>
	</p>

	<p>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message key="button.save"/></html:submit>
	</p>
</fr:form>
