<%@ page language="java" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

	
<logic:present name="executionDegree">
	<bean:write name="executionDegree" property="infoDegreeCurricularPlan.infoDegree.tipoCurso"/> em 
	<bean:write name="executionDegree" property="infoDegreeCurricularPlan.infoDegree.nome"/>
	<br/>
	<bean:define id="curricularYearValue"
				 name="curricularYear"
				 property="year"
				 toScope="request"
				 scope="request"/>
	<bean:message key="label.year" arg0="<%= pageContext.findAttribute("curricularYearValue").toString() %>" /> - 
	<bean:write name="executionPeriod" property="name" scope="request"/> -
	<bean:write name="executionPeriod" property="infoExecutionYear.year" scope="request"/>
</logic:present>
		
<logic:present name="<%= SessionConstants.CLASS_VIEW %>"  >
	<bean:define id="infoTurma" name="<%= SessionConstants.CLASS_VIEW %>" scope="request"/>
	<br/>
	<bean:message key="label.class"/> <jsp:getProperty name="infoTurma" property="nome" />
	<br/>
</logic:present>

<logic:present name="<%= SessionConstants.EXECUTION_COURSE_KEY %>"  >
	<bean:define id="infoDisciplinaExecucao" name="<%= SessionConstants.EXECUTION_COURSE_KEY %>" scope="request"/>
	<br/>
	<bean:message key="property.course"/>: <jsp:getProperty name="infoDisciplinaExecucao" property="nome" />
	<br/>
</logic:present>
