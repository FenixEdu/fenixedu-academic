<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="DataBeans.precedences.InfoRestriction" %>

<h3><bean:message key="label.manager.precedences.management"/></h3>

<ul style="list-style-type: square;">
	<li><html:link page='<%="/makeSimplePrecedence.do?method=showAllRestrictions&degreeId=" + request.getParameter("degreeId") + "&amp;degreeCurricularPlanId=" + request.getParameter("degreeCurricularPlanId")%>'><bean:message key="label.manager.insert.simple.precedence"/></html:link></li>
	<li><html:link page='<%="/makePrecedenceConjunction.do?method=start&degreeId=" + request.getParameter("degreeId") + "&amp;degreeCurricularPlanId=" + request.getParameter("degreeCurricularPlanId")%>'><bean:message key="label.manager.insert.conjunction.precedence"/></html:link></li>
</ul>

<br/>
<span class="error"><html:errors/></span>
<br/>

<logic:present name="precedences" scope="request">
<logic:notEmpty name="precedences" scope="request">
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
									<logic:greaterThan name="infoRestrictionsSize" value="1">
										<td class="listClasses">
											<bean:define id="restrictionID" name="restriction" property="idInternal"/>
											<html:link page='<%="/deleteRestriction.do?degreeId=" + request.getParameter("degreeId") + "&amp;degreeCurricularPlanId=" + request.getParameter("degreeCurricularPlanId") + "&amp;restrictionId=" + restrictionID.toString()%>'>
												<bean:message key="message.manager.delete"/>
											</html:link>
										</td>
									</logic:greaterThan>
								</tr>
							</logic:iterate>
						</table>
					</td>
					<td class="listClasses">
						<bean:define id="precedenceID" name="precedence" property="idInternal"/>
						<html:link page='<%="/deletePrecedence.do?degreeId=" + request.getParameter("degreeId") + "&amp;degreeCurricularPlanId=" + request.getParameter("degreeCurricularPlanId") + "&amp;precedenceId=" + precedenceID.toString()%>'>
							<bean:message key="message.manager.delete"/>
						</html:link>
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
</logic:notEmpty>
</logic:present>
