<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<html:form action="/guidesManagement">
	<html:hidden property="method" value="printGuide" />
	
	<h2><bean:message key="label.masterDegree.administrativeOffice.payments.candidacy.events"/></h2>
	<br/>
	
	<strong><bean:message key="label.masterDegree.administrativeOffice.payments.candidacy"/></strong>:
	<bean:write name="paymentsManagementDTO" property="candidacy.person.name" />
	
	<strong><bean:message key="label.masterDegree.administrativeOffice.payments.candidacyNumber"/></strong>:
	<bean:write name="paymentsManagementDTO" property="candidacy.number" />
	<br/>
	
	<fr:edit id="guides" name="paymentsManagementDTO" visible="false"/>
	
	<fr:edit id="guides-entries" name="paymentsManagementDTO" property="entryDTOs" schema="payments-guides-entry">
		<fr:layout name="tabular" >
			<fr:property name="classes" value="tstyle4"/>
	        <fr:property name="columnClasses" value="listClasses,,"/>
	        <fr:property name="sortBy" value="event.whenOccured"/>
		</fr:layout>
	</fr:edit>
	
	<html:submit><bean:message key="label.masterDegree.administrativeOffice.payments.print"/></html:submit>
	<html:cancel styleClass="inputbutton" onclick="this.form.method.value='firstPage';"><bean:message key="button.cancel"/></html:cancel>

</html:form>