<%@ page language="java" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ page import="DataBeans.CurricularYearAndSemesterAndInfoExecutionDegree" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

    	<bean:define id="licContext" name="<%= SessionConstants.CONTEXT_KEY %>" scope="session" type="CurricularYearAndSemesterAndInfoExecutionDegree"/>
    	<bean:define id="infoLic" name="<%= SessionConstants.CONTEXT_KEY %>" property="infoLicenciaturaExecucao.infoDegreeCurricularPlan.infoDegree" scope="session" />	
		<jsp:getProperty name="infoLic" property="nome" />
		<br/>
		<bean:message key="label.year" arg0="<%= licContext.getAnoCurricular().toString() %>" /> -
		
		<bean:message key="label.period" arg0="<%= licContext.getSemestre().toString() %>" />
		
		
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
