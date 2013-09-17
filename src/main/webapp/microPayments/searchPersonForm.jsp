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
			<fr:form action="/operator.do?method=startPage">
				<table><tr>
					<td>
						<fr:edit id="searchBean" name="searchBean">
							<fr:schema bundle="ACCOUNTING_RESOURCES" type="net.sourceforge.fenixedu.presentationTier.Action.treasury.payments.PaymentManagementDA$SearchBean">
								<fr:slot name="searchString" key="label.micropayments.search.person.field">
									<fr:property name="size" value="15"/>
								</fr:slot>
							</fr:schema>
													
							<fr:layout name="tabular">
								<fr:property name="classes" value="dinline" />
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
		<logic:notEmpty name="searchBean" property="searchString">
			<logic:present name="people">
				<logic:empty name="people">
					<span class="error0">
						<bean:message bundle="ACCOUNTING_RESOURCES" key="label.client.not.found" />
					</span>
				</logic:empty>
				<logic:notEmpty name="people">
					<table class="search-clients">
						<logic:iterate id="person" name="people">
							<tr>
								<td class="search-clients-photo">
									<bean:define id="url" type="java.lang.String">/publico/retrievePersonalPhoto.do?method=retrieveByUUID&amp;<%=net.sourceforge.fenixedu.presentationTier.servlets.filters.ContentInjectionRewriter.CONTEXT_ATTRIBUTE_NAME%>=/homepage&amp;uuid=<bean:write name="person" property="username"/></bean:define>
									<div>
										<img width="60" height="60" src="<%= request.getContextPath() + url %>"/>
									</div>
								</td>
								<td>
									<p class="mvert025">
										<html:link action="/operator.do?method=showPerson" paramId="personOid" paramName="person" paramProperty="externalId">
											<bean:write name="person" property="name"/>
										</html:link>
									</p>
									<p class="mvert025">
										<bean:write name="person" property="username"/>
									</p>
								</td>
							</tr>
						</logic:iterate>
					</table>
				</logic:notEmpty>
			</logic:present>
		</logic:notEmpty>
