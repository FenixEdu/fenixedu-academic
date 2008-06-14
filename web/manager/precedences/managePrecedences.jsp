<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.precedences.InfoRestriction" %>

<h3><bean:message bundle="MANAGER_RESOURCES" key="label.manager.precedences.management"/></h3>

<ul style="list-style-type: square;">
	<li><html:link module="/manager" page='<%="/makeSimplePrecedence.do?method=showAllRestrictions&amp;page=0&amp;degreeId=" + request.getParameter("degreeId") + "&amp;degreeCurricularPlanId=" + request.getParameter("degreeCurricularPlanId")%>'><bean:message bundle="MANAGER_RESOURCES" key="label.manager.insert.simple.precedence"/></html:link></li>
	<li><html:link module="/manager" page='<%="/makePrecedenceConjunction.do?method=showFirstPage&degreeId=" + request.getParameter("degreeId") + "&amp;degreeCurricularPlanId=" + request.getParameter("degreeCurricularPlanId")%>'><bean:message bundle="MANAGER_RESOURCES" key="label.manager.insert.conjunction.precedence"/></html:link></li>
</ul>

<logic:present name="precedences" scope="request">
<logic:notEmpty name="precedences" scope="request">
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
									<logic:greaterThan name="infoRestrictionsSize" value="1">
										<td class="listClasses">
											<bean:define id="restrictionID" name="restriction" property="idInternal"/>
											<html:link module="/manager" page='<%="/deleteRestriction.do?degreeId=" + request.getParameter("degreeId") + "&amp;degreeCurricularPlanId=" + request.getParameter("degreeCurricularPlanId") + "&amp;restrictionId=" + restrictionID.toString()%>'>
												<bean:message bundle="MANAGER_RESOURCES" key="message.manager.delete"/>
											</html:link>
										</td>
									</logic:greaterThan>
								</tr>
							</logic:iterate>
						</table>
					</td>
					<td class="listClasses">
						<bean:define id="precedenceID" name="precedence" property="idInternal"/>
						<html:link module="/manager" page='<%="/deletePrecedence.do?degreeId=" + request.getParameter("degreeId") + "&amp;degreeCurricularPlanId=" + request.getParameter("degreeCurricularPlanId") + "&amp;precedenceId=" + precedenceID.toString()%>'>
							<bean:message bundle="MANAGER_RESOURCES" key="message.manager.delete"/>
						</html:link>
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
</logic:notEmpty>
</logic:present>
