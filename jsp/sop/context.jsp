<%@ page language="java" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

	
	<bean:define id="infoDegree" name="<%= SessionConstants.INFO_EXECUTION_DEGREE_KEY %>" property="infoDegreeCurricularPlan.infoDegree"scope="session" />
    	<bean:define id="infoExecutionPeriod" name="<%= SessionConstants.INFO_EXECUTION_PERIOD_KEY %>" scope="session"/>
    	
    	<bean:define id="curricularYear" name="<%= SessionConstants.CURRICULAR_YEAR_KEY %>" scope="session"/>
		<jsp:getProperty name="infoDegree" property="nome" />
		<br/>
		<bean:message key="label.year" arg0="<%= curricularYear.toString() %>" /> -
		
		<jsp:getProperty name="infoExecutionPeriod" property="name"/>
		<logic:present name="<%= SessionConstants.CLASS_VIEW %>"  >
			<bean:define id="infoTurma" name="<%= SessionConstants.CLASS_VIEW %>" scope="session"/>
    			<br/>
    			<bean:message key="label.class"/> <jsp:getProperty name="infoTurma" property="nome" />
			<br/>
		</logic:present>

		<logic:present name="<%= SessionConstants.EXECUTION_COURSE_KEY %>"  >
			<bean:define id="infoDisciplinaExecucao" name="<%= SessionConstants.EXECUTION_COURSE_KEY %>" scope="session"/>
    			<br/>
    			<bean:message key="property.course"/>: <jsp:getProperty name="infoDisciplinaExecucao" property="nome" />
				<br/>
		</logic:present>
