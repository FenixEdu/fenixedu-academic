<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml />

<h2><bean:message bundle="TREASURY_RESOURCES" key="portal.treasury" /></h2>

<logic:present name="searchBean">
	<fr:form action="/paymentManagement.do?method=searchPeople">
		<table>
			<tr>
				<td style="vertical-align: middle;">
					<fr:edit id="searchBean" name="searchBean">
						<fr:schema bundle="TREASURY_RESOURCES" type="net.sourceforge.fenixedu.presentationTier.Action.treasury.payments.PaymentManagementDA$SearchBean">
							<fr:slot name="searchString" key="label.searchString">
								<fr:property name="size" value="50"/>
							</fr:slot>
						</fr:schema>
						<fr:layout name="tabular">
							<fr:property name="classes" value="tstyle2 thlight thleft mbottom0" />
							<fr:property name="columnClasses" value=",,tdclear tderror1" />
						</fr:layout>
					</fr:edit>
				</td>
				<td style="vertical-align: middle;">
					<br/>
					<html:submit>
						<bean:message key="button.search" bundle="TREASURY_RESOURCES" />
					</html:submit>
				</td>
			</tr>
		</table>
	</fr:form>

	<bean:define id="people" name="searchBean" property="searchResult" toScope="request"/>

	<logic:empty name="people">
		<logic:notEmpty name="searchBean" property="searchString">
			<bean:message bundle="TREASURY_RESOURCES" key="label.string.no.results.found"/>
		</logic:notEmpty>
	</logic:empty>

	<logic:notEmpty name="people">
		<bean:size id="personCount" name="people"/>
		<logic:equal name="personCount" value="1">
			<jsp:include page="paymentManagementForPerson.jsp"/>
		</logic:equal>
		<logic:notEqual name="personCount" value="1">
			<jsp:include page="selectPersonForPaymentManagement.jsp"/>
		</logic:notEqual>
		
	</logic:notEmpty>

</logic:present>
