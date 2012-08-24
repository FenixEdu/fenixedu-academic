<%@page import="net.sourceforge.fenixedu.util.Money"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Collections"%>
<%@page import="java.util.List"%>
<%@page import="net.sourceforge.fenixedu.domain.accounting.Event"%>
<%@page import="java.util.Set"%>
<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml />

<h3><bean:message bundle="SCIENTIFIC_COUNCIL_RESOURCES" key="label.thesis.search.student"/></h3> 

<p>
	<fr:form action="<%= "/manageSecondCycleThesis.do?method=searchPerson" %>">
		<table><tr>
			<td>
				<fr:edit id="searchPersonForm" name="manageSecondCycleThesisSearchBean">
					<fr:schema type="net.sourceforge.fenixedu.presentationTier.Action.scientificCouncil.thesis.ManageSecondCycleThesisSearchBean"
							   bundle="SCIENTIFIC_COUNCIL_RESOURCES">
						<fr:slot name="searchString" key="label.thesis.search.student.string.field">
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
					<bean:message bundle="SCIENTIFIC_COUNCIL_RESOURCES" key="label.thesis.search.student.view" />
				</html:submit>
			</td>
		</tr></table>
	</fr:form>
</p>

<logic:notEmpty name="manageSecondCycleThesisSearchBean" property="searchString">
	<logic:present name="people">
		<logic:empty name="people">
			<span class="error0">
				<bean:message bundle="SCIENTIFIC_COUNCIL_RESOURCES" key="label.thesis.search.person.none" />
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
								<html:link action="/manageSecondCycleThesis.do?method=showPersonThesisDetails" paramId="personOid" paramName="person" paramProperty="externalId">
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
