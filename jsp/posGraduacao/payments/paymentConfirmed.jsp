<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<bean:define id="personId" name="person" property="idInternal"/>
<html:form action="<%="/payments.do?personId=" + personId %>">
	<html:hidden property="method" />
	<h2><bean:message key="label.masterDegree.administrativeOffice.payments.paymentConfirmed" /></h2>
	<hr/>
	<br/>
	<fr:edit id="entriesToSelect" name="entriesToSelect" visible="false" nested="true"/>
	
	<span class="success0"><bean:message key="label.masterDegree.administrativeOffice.payments.paymentConfirmed"/></span>
	<br/><br/><br/><br/>
			
	<html:submit styleClass="inputbutton" onclick="this.form.method.value='showPaymentsWithoutReceipt';"><bean:message key="button.masterDegree.administrativeOffice.payments.createReceipt"/></html:submit>
	<html:submit styleClass="inputbutton" onclick="this.form.method.value='backToShowOperations';"><bean:message key="button.masterDegree.administrativeOffice.payments.back"/></html:submit>
</html:form>
