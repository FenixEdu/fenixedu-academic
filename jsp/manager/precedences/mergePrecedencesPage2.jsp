<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="DataBeans.precedences.InfoRestriction" %>

<h3><bean:message key="label.manager.precedences.management"/></h3>

<span class="error"><html:errors/></span>

<logic:present name="precedences" scope="request">
<logic:notEmpty name="precedences" scope="request">

<h4><bean:message key="label.manager.precedences.conjunction.selectSecondPrecedence"/></h4>

<html:form action="/makePrecedenceConjunction.do">

	<html:hidden property="page" value="2"/>
	<html:hidden property="degreeCurricularPlanId" value="<%= request.getParameter("degreeCurricularPlanId") %>"/>
	<html:hidden property="degreeId" value="<%= request.getParameter("degreeId") %>"/>
	<html:hidden property="method" value="merge"/>
	<html:hidden property="firstPrecedence"/>

	<logic:iterate id="element" name="precedences">
		<bean:define id="infoCurricularCourse" name="element" property="key"/>
		<bean:define id="infoPrecedences" name="element" property="value"/>

		<table border="0">
			<tr>
				<td class="listClasses-header" colspan="2">
					<bean:message key="message.manager.this.course"/>
						<bean:write name="infoCurricularCourse" property="name"/>
					<bean:message key="message.manager.has.precedence"/>
				</td>
			</tr>
			<bean:size id="infoPrecedencesSize" name="infoPrecedences"/>
			<logic:iterate id="precedence" name="infoPrecedences" indexId="precedencesLength">
				<tr>
					<td class="listClasses">
						<table border="0" width="100%">
							<bean:size id="infoRestrictionsSize" name="precedence" property="infoRestrictions"/>
							<logic:iterate id="restriction" name="precedence" property="infoRestrictions" indexId="restrictionsLength">
								<tr>
									<td class="listClasses">
										<bean:message name="restriction" property="restrictionKindResourceKey" arg0="<%= ((InfoRestriction) restriction).getArg() %>"/>
										<% if ((restrictionsLength.intValue() + 1 ) < infoRestrictionsSize.intValue()) { %>
											<bean:message key="message.manager.and"/>
										<% } %>
									</td>
								</tr>
							</logic:iterate>
						</table>
					</td>
					<td class="listClasses">
						<bean:define id="precedenceID" name="precedence" property="idInternal"/>
						<html:radio property="secondPrecedence" value="<%= precedenceID.toString() %>" onclick="form.submit()"/>
					</td>
				</tr>
				<% if ((precedencesLength.intValue() + 1 ) < infoPrecedencesSize.intValue()) { %>
					<tr>
						<td class="listClasses-header" colspan="2"><bean:message key="message.manager.or"/></td>
					</tr>
				<% } %>
			</logic:iterate>
		</table>
		<br/>
		<br/>
	</logic:iterate>

</html:form>

</logic:notEmpty>
</logic:present>
