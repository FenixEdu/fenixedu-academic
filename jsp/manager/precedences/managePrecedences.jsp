<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<h3><bean:message key="label.manager.precedences.management"/></h3>

<ul style="list-style-type: square;">
	<li><html:link page='<%="/makeSimplePrecedence.do?degreeId=" + request.getParameter("degreeId") + "&amp;degreeCurricularPlanId=" + request.getParameter("degreeCurricularPlanId")%>'><bean:message key="label.manager.insert.simple.precedence"/></html:link></li>
	<li><html:link page='<%="/makePrecedenceConjunction.do?method=start&degreeId=" + request.getParameter("degreeId") + "&amp;degreeCurricularPlanId=" + request.getParameter("degreeCurricularPlanId")%>'><bean:message key="label.manager.insert.conjunction.precedence"/></html:link></li>
	<li><html:link page='<%="/makePrecedenceDijunction.do?method=start&degreeId=" + request.getParameter("degreeId") + "&amp;degreeCurricularPlanId=" + request.getParameter("degreeCurricularPlanId")%>'><bean:message key="label.manager.insert.dijunction.precedence"/></html:link></li>
</ul>

<span class="error"><html:errors/></span>

<logic:present name="precedences" scope="request">
	<logic:iterate id="element" name="precedences">
		<bean:define id="infoCurricularCourse" name="element" property="key"/>
		<bean:define id="infoPrecedences" name="element" property="value"/>

		<table width="100%" border="1" cellpadding="0" cellspacing="0">
			<tr>
				<td colspan="2">
					<bean:message key="message.manager.this.course"/>
					<bean:write name="infoCurricularCourse" property="name"/>
					<bean:message key="message.manager.has.precedence"/>
				</td>
			</tr>
		</table>
	</logic:iterate>
</logic:present>
