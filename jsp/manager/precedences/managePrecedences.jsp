<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<h3><bean:message key="label.manager.precedences.management"/></h3>

<ul style="list-style-type: square;">
	<li><html:link page='<%="/makeSimplePrecedence.do?method=start&degreeId=" + request.getParameter("degreeId") + "&amp;degreeCurricularPlanId=" + request.getParameter("degreeCurricularPlanId")%>'><bean:message key="label.manager.insert.simple.precedence"/></html:link></li>
	<li><html:link page='<%="/makePrecedenceConjunction.do?method=start&degreeId=" + request.getParameter("degreeId") + "&amp;degreeCurricularPlanId=" + request.getParameter("degreeCurricularPlanId")%>'><bean:message key="label.manager.insert.conjunction.precedence"/></html:link></li>
	<li><html:link page='<%="/makePrecedenceDijunction.do?method=start&degreeId=" + request.getParameter("degreeId") + "&amp;degreeCurricularPlanId=" + request.getParameter("degreeCurricularPlanId")%>'><bean:message key="label.manager.insert.dijunction.precedence"/></html:link></li>
</ul>

<span class="error"><html:errors/></span>

