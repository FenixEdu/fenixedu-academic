<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.precedences.InfoRestriction" %>

<h3><bean:message bundle="MANAGER_RESOURCES" key="label.manager.precedences.management"/></h3>

<span class="error"><html:errors/></span>

<logic:present name="precedences" scope="request">
<logic:notEmpty name="precedences" scope="request">

<h4><bean:message bundle="MANAGER_RESOURCES" key="label.manager.precedences.conjunction.selectSecondPrecedence"/></h4>

<html:form action="/makePrecedenceConjunction.do">

	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="2"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeCurricularPlanId" property="degreeCurricularPlanId" value="<%= request.getParameter("degreeCurricularPlanId") %>"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeId" property="degreeId" value="<%= request.getParameter("degreeId") %>"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="merge"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.firstPrecedence" property="firstPrecedence"/>

	<logic:iterate id="element" name="precedences">
		<bean:define id="infoCurricularCourse" name="element" property="key"/>
		<bean:define id="infoPrecedences" name="element" property="value"/>

		<table border="0">
			<tr>
				<th class="listClasses-header" colspan="2">
					<bean:message bundle="MANAGER_RESOURCES" key="message.manager.this.course"/>
						<bean:write name="infoCurricularCourse" property="name"/>
					<bean:message bundle="MANAGER_RESOURCES" key="message.manager.has.precedence"/>
				</th>
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
										<bean:message bundle="MANAGER_RESOURCES" name="restriction" property="restrictionKindResourceKey" arg0="<%= ((InfoRestriction) restriction).getArg() %>"/>
										<% if ((restrictionsLength.intValue() + 1 ) < infoRestrictionsSize.intValue()) { %>
											<bean:message bundle="MANAGER_RESOURCES" key="message.manager.and"/>
										<% } %>
									</td>
								</tr>
							</logic:iterate>
						</table>
					</td>
					<td class="listClasses">
						<bean:define id="precedenceID" name="precedence" property="idInternal"/>
						<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.secondPrecedence" property="secondPrecedence" value="<%= precedenceID.toString() %>" onclick="form.submit()"/>
					</td>
				</tr>
				<% if ((precedencesLength.intValue() + 1 ) < infoPrecedencesSize.intValue()) { %>
					<tr>
						<th class="listClasses-header" colspan="2"><bean:message bundle="MANAGER_RESOURCES" key="message.manager.or"/></th>
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
