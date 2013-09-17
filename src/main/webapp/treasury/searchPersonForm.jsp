<%@page import="net.sourceforge.fenixedu.util.Money"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Collections"%>
<%@page import="java.util.List"%>
<%@page import="net.sourceforge.fenixedu.domain.accounting.Event"%>
<%@page import="java.util.Set"%>
<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>


<html:xhtml />

		<h3><bean:message bundle="ACCOUNTING_RESOURCES" key="label.micropayments.search.person.header"/></h3>
		<p>
			<fr:form action="/paymentManagement.do?method=searchPeople">
				<table><tr>
					<td>
						
						<fr:edit id="searchBean" name="searchBean">
							<fr:schema bundle="ACCOUNTING_RESOURCES" type="net.sourceforge.fenixedu.presentationTier.Action.treasury.payments.PaymentManagementDA$SearchBean">
								<fr:slot name="searchString" key="label.micropayments.search.person.field">
									<fr:property name="size" value="20"/>
								</fr:slot>
							</fr:schema>
							<fr:layout name="tabular">
								<fr:property name="classes" value="thlight" />
								<fr:property name="columnClasses" value=",,tdclear tderror1" />
							</fr:layout>
						</fr:edit>

					</td>
					<td>
						<html:submit>
							<bean:message bundle="ACCOUNTING_RESOURCES" key="label.view.profile" />
						</html:submit>
					</td>
				</tr></table>
			</fr:form>
		</p>
		<logic:present name="people">
			<logic:empty name="people">
				<p>
					<em><bean:message bundle="ACCOUNTING_RESOURCES" key="label.client.not.found" />.</em>
				</p>
			</logic:empty>
		</logic:present>

