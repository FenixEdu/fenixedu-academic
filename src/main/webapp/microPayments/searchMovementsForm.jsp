<%@page import="net.sourceforge.fenixedu.presentationTier.Action.treasury.payments.SearchBeanType"%>
<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<html:xhtml />

<h3>
	<bean:message bundle="ACCOUNTING_RESOURCES" key="label.micropayments.search.movements.header"/>
</h3>
<p>
	<bean:define id="searchBeanTransactions" name="searchBeanTransactions" type="net.sourceforge.fenixedu.presentationTier.Action.treasury.payments.PaymentManagementDA.SearchTransactions"/>
	<fr:form action="/operator.do">
		<html:hidden property="method" value="startPage"/>
		<table>
			<tr>
				<td colspan="2">
					<fr:edit id="searchBeanTransactions" name="searchBeanTransactions">
						<fr:schema bundle="ACCOUNTING_RESOURCES" type="net.sourceforge.fenixedu.presentationTier.Action.treasury.payments.PaymentManagementDA$SearchTransactions">
							<fr:slot name="searchBeanType" key="label.micropayments.search.type" layout="menu-postback"/>
						</fr:schema>
						<fr:layout name="tabular">
							<fr:property name="classes" value="dinline" />
							<fr:property name="columnClasses" value=",,tdclear tderror1" />
						</fr:layout>
					</fr:edit>
				</td>
			</tr>
			<%
				if (searchBeanTransactions.getSearchBeanType() == SearchBeanType.PERSON_SEARCH_BEAN) {
			%>
					<tr>
						<td>
							<fr:edit id="searchBeanTransactionsClient" name="searchBeanTransactions">
								<fr:schema bundle="ACCOUNTING_RESOURCES" type="net.sourceforge.fenixedu.presentationTier.Action.treasury.payments.PaymentManagementDA$SearchTransactions">
									<fr:slot name="searchBean.searchString" key="label.micropayments.search.person.field">
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
							<html:submit onclick="this.form.method.value='showTransactions';">
								<bean:message bundle="ACCOUNTING_RESOURCES" key="label.view.profile" />
							</html:submit>
						</td>
					</tr>
			<%
				}
			%>
			<%
				if (searchBeanTransactions.getSearchBeanType() == SearchBeanType.OPERATOR_SEARCH_BEAN) {
			%>
					<tr>
						<td colspan="2">
							<fr:edit id="searchBeanTransactionsOperator" name="searchBeanTransactions" property="operatorSearchBean">
								<fr:schema bundle="ACCOUNTING_RESOURCES" type="net.sourceforge.fenixedu.presentationTier.Action.treasury.payments.PaymentManagementDA$SearchTransactions">
									<fr:slot name="operator" key="label.micropayments.search.unit" layout="menu-select-postback">
										<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.Action.treasury.payments.PaymentManagementDA$MicroPaymentOperatorProvider" />
										<fr:property name="format" value="${nickname}"/>
										<fr:property name="destination" value="postback"/>
										<fr:property name="saveOptions" value="true" />
									</fr:slot>
								</fr:schema>
								<fr:layout name="tabular">
									<fr:property name="classes" value="dinline" />
									<fr:property name="columnClasses" value=",,tdclear tderror1" />
								</fr:layout>
								<fr:destination name="postback" path="/operator.do?method=showTransactions"/>
							</fr:edit>
						</td>
					</tr>
			<%
				}
			%>
			<%
				if (searchBeanTransactions.getSearchBeanType() == SearchBeanType.UNIT_SEARCH_BEAN) {
			%>
					<tr>
						<td colspan="2">
							<fr:edit id="searchBeanTransactionsUnit" name="searchBeanTransactions" property="unitSearchBean">
								<fr:schema bundle="ACCOUNTING_RESOURCES" type="net.sourceforge.fenixedu.presentationTier.Action.treasury.payments.PaymentManagementDA$UnitSearchBean">
									<fr:slot name="unit" key="label.micropayments.search.unit" layout="menu-select-postback">
										<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.Action.treasury.payments.PaymentManagementDA$MicroPaymentUnitsProvider" />
										<fr:property name="format" value="${presentationName}"/>
										<fr:property name="destination" value="postback"/>
										<fr:property name="saveOptions" value="true" />
									</fr:slot>
								</fr:schema>
								<fr:layout name="tabular">
									<fr:property name="classes" value="dinline" />
									<fr:property name="columnClasses" value=",,tdclear tderror1" />
								</fr:layout>
								<fr:destination name="postback" path="/operator.do?method=showTransactions"/>
							</fr:edit>
						</td>
					</tr>
			<%
				}
			%>
		</table>
	</fr:form>
</p>
<!-- 
	Movimentos de:
	<select> 
		<option selected="selected">Operador de reprografia</option> 
		<option>Cliente</option> 
		<option>Unidade</option> 
	</select>
<p>Cliente (<%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%> ID): <input type="text"/> <input type="button" value="Pesquisar"  onClick="parent.location='reprografia_movimentos_01.html'"/></p> 
<p>Operador: <select><option>Joaquim da Silva Arimateia (ist167200)</option><option>Joaquim da Silva Arimateia (ist167200)</option></select></p>
<p>Unidade: <select><option>Reprografia do Central .012</option><option>Reprografia do Central .012</option></select></p>
</p>
 -->

		<logic:notEmpty name="searchBeanTransactions" property="searchBean.searchString">
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
										<html:link action="/operator.do?method=showTransactionsForPerson" paramId="personOid" paramName="person" paramProperty="externalId">
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
