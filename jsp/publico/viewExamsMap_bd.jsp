<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ page import="Util.TipoCurso" %>

<logic:present name="infoDegreeCurricularPlan" >
	<bean:define id="degreeCurricularPlanID" name="infoDegreeCurricularPlan" property="idInternal" />
	<div class="breadcumbs"><a href="http://www.ist.utl.pt/index.shtml"><bean:message key="label.school" /></a>
		<bean:define id="degreeType" name="infoDegreeCurricularPlan" property="infoDegree.tipoCurso" />	
		&nbsp;&gt;&nbsp;<a href="http://www.ist.utl.pt/html/ensino/index.shtml"><bean:message key="label.education" /></a>	
		&nbsp;&gt;&nbsp;
		<html:link page="<%= "/showDegreeSite.do?method=showDescription&amp;executionPeriodOID=" + request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID) + "&amp;degreeID=" + request.getAttribute("degreeID").toString()  %>">
			<bean:write name="infoDegreeCurricularPlan" property="infoDegree.sigla" />
		</html:link>
		&nbsp;&gt;&nbsp;
		<html:link page="<%= "/showDegreeSite.do?method=showCurricularPlan&amp;degreeID=" + request.getAttribute("degreeID") + "&amp;degreeCurricularPlanID=" + pageContext.findAttribute("degreeCurricularPlanID").toString() + "&amp;executionPeriodOID=" + request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID)  %>" >
			<bean:message key="label.curricularPlan"/>
		</html:link>
		&nbsp;&gt;&nbsp;<bean:message key="label.exams"/> 
	</div>	
<%--

<!-- P�GINA EM INGL�S -->
	<div class="version">
		<span class="px10">
			<html:link page="<%= "/showDegreeSite.do?method=showCurricularPlan&amp;inEnglish=true&amp;executionPeriodOID=" + request.getAttribute(SessionConstants.EXECUTION_PERIOD_OID) + "&amp;degreeID=" +  request.getAttribute("degreeID") %>" >
				<bean:message key="label.version.english" />
			</html:link>
			<img src="<%= request.getContextPath() %>/images/icon_uk.gif" alt="Icon: English version!" width="16" height="12" />
	</span>	
	</div> --%>
	
	<h1>
		<bean:write name="infoDegreeCurricularPlan" property="infoDegree.tipoCurso" />
			<bean:message key="label.in" />
		<bean:write name="infoDegreeCurricularPlan" property="infoDegree.nome" />
	</h1>

	<h2 class="greytxt">
		<bean:write name="<%=SessionConstants.EXECUTION_PERIOD%>" property="semester" /><bean:message key="label.ordinal.semester.abbreviation" />
		<bean:write name="<%=SessionConstants.EXECUTION_DEGREE%>" property="infoExecutionYear.year"/>
	</h2>

	<logic:present name="<%= SessionConstants.INFO_EXAMS_MAP_LIST %>" >
		<bean:define id="infoExamsMaps" name="<%= SessionConstants.INFO_EXAMS_MAP_LIST %>" scope="request" />
		<logic:iterate id="infoExamsMap" name="infoExamsMaps" >
			<div><app:generateNewExamsMap name="infoExamsMap" user="public" mapType=" "/></div>
		</logic:iterate>   
	</logic:present>

	<logic:notPresent name="<%= SessionConstants.INFO_EXAMS_MAP_LIST %>" >
		<div>
			<app:generateNewExamsMap name="<%= SessionConstants.INFO_EXAMS_MAP %>" user="public" mapType=" "/>
		</div>
	</logic:notPresent>

</logic:present>

