<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e"%>

<html:form action="/payments.do">

	<html:hidden property="method" />

	<h2><bean:message key="label.masterDegree.administrativeOffice.payments" /></h2>
	<hr>

	<logic:messagesPresent message="true">
		<ul>
			<html:messages id="messages" message="true">
				<li><span class="error0"><bean:write name="messages" /></span></li>
			</html:messages>
		</ul>
		<br />
	</logic:messagesPresent>

	<h3><bean:message
		key="label.masterDegree.administrativeOffice.payments.searchByCandidacyNumber" /></h3>
	<bean:message
		key="label.masterDegree.administrativeOffice.payments.number" />:
	<html:text property="candidacyNumber" />
	<br />
	<br />
	<html:submit styleClass="inputbutton"
		onclick="this.form.method.value='searchPersonByCandidacyNumber'">
		<bean:message
			key="label.masterDegree.administrativeOffice.payments.search" />
	</html:submit>

	<br />
	<br />
	<br />

	<h3><bean:message
		key="label.masterDegree.administrativeOffice.payments.searchByUsername" /></h3>
	<bean:message
		key="label.masterDegree.administrativeOffice.payments.username" />:
	<html:text property="username" />
	<br />
	<br />
	<html:submit styleClass="inputbutton"
		onclick="this.form.method.value='searchPersonByUsername'">
		<bean:message
			key="label.masterDegree.administrativeOffice.payments.search" />
	</html:submit>

	<br />
	<br />
	<br />

	<h3><bean:message
		key="label.masterDegree.administrativeOffice.payments.searchByDocumentIDandType" /></h3>
	<bean:message
		key="label.masterDegree.administrativeOffice.payments.documentType" />:
	<e:labelValues id="documentTypes"
		enumeration="net.sourceforge.fenixedu.domain.person.IDDocumentType"
		bundle="ENUMERATION_RESOURCES" />
	<html:select property="documentType">
		<html:options collection="documentTypes" property="value"
			labelProperty="label" />
	</html:select>
	<br />
	<bean:message
		key="label.masterDegree.administrativeOffice.payments.documentNumber" />:
	<html:text property="documentNumber" />
	<br />
	<br />
	<html:submit styleClass="inputbutton"
		onclick="this.form.method.value='searchPersonByDocumentIDandDocumentType'">
		<bean:message
			key="label.masterDegree.administrativeOffice.payments.search" />
	</html:submit>

</html:form>
