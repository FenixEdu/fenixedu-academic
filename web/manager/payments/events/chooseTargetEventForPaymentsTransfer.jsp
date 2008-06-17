<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>


<logic:present role="MANAGER">

	<h2><bean:message key="label.payments.transferPayments"
		bundle="ACADEMIC_OFFICE_RESOURCES" /></h2>


	<logic:messagesPresent message="true">
		<ul class="nobullet list6">
			<html:messages id="messages" message="true"
				bundle="APPLICATION_RESOURCES">
				<li><span class="error0"><bean:write name="messages" /></span></li>
			</html:messages>
		</ul>
	</logic:messagesPresent>


	<p class="mtop15 mbottom05"><strong><bean:message
		bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.person" /></strong></p>
	<fr:view name="transferPaymentsBean" property="sourceEvent.person"
		schema="person.view-with-name-and-idDocumentType-and-documentIdNumber">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight thright mtop05" />
			<fr:property name="rowClasses" value="tdhl1,," />
		</fr:layout>
	</fr:view>

	<bean:define id="personId" name="transferPaymentsBean"
		property="sourceEvent.person.idInternal" />

	<fr:form action="<%="/payments.do?personId=" + personId.toString() %>">
		<input type="hidden" name="method" value="" />

		<logic:empty name="transferPaymentsBean" property="sourceEvent.positiveEntries">
			<bean:message key="label.payments.not.found"
				bundle="ACADEMIC_OFFICE_RESOURCES" />
			<br/><br/>
			<html:cancel bundle="HTMLALT_RESOURCES" altKey="cancel.cancel"
				onclick="this.form.method.value='backToShowEvents';">
				<bean:message bundle="APPLICATION_RESOURCES" key="label.back" />
			</html:cancel>
		</logic:empty>

		<logic:notEmpty name="transferPaymentsBean" property="sourceEvent.positiveEntries">
		
			<fr:view name="transferPaymentsBean"
			property="sourceEvent.positiveEntries" schema="entry.view-with-payment-mode">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle4 mtop05" />
					<fr:property name="columnClasses" value=",,,aright,aright,aright,acenter" />
					<fr:property name="sortBy" value="whenRegistered=desc"/>
				</fr:layout>
			</fr:view>
		
			<fr:edit id="transferPaymentsBean" name="transferPaymentsBean" schema="TransferPaymentsToOtherEventAndCancelBean.edit">
				<fr:layout name="tabular">
					<fr:property name="classes"
						value="tstyle2 thmiddle thright thlight mtop05" />
					<fr:property name="columnClasses" value=",,tdclear tderror1" />
				</fr:layout>
			</fr:edit>
						

			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit"
				onclick="this.form.method.value='transferPaymentsToOtherEventAndCancel';">
				<bean:message bundle="APPLICATION_RESOURCES" key="label.submit" />
			</html:submit>
			<html:cancel bundle="HTMLALT_RESOURCES" altKey="cancel.cancel"
				onclick="this.form.method.value='backToShowEvents';">
				<bean:message bundle="APPLICATION_RESOURCES" key="label.back" />
			</html:cancel>
		</logic:notEmpty>

	</fr:form>



</logic:present>