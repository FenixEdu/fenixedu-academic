<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<br/>
<%-- <bean:define id="component" name="siteView" property="component"/> --%>
<table width="80%" align="center">
	<tr>
		<td class="infoselected"><p><bean:message key="title.selected.degree"/></p>
			<strong>
				<bean:define id="executionDegree" name="<%= SessionConstants.EXECUTION_DEGREE%>" />
				<logic:present name="executionDegree"  >
					<bean:define id="infoDegree" name="executionDegree" property="infoDegreeCurricularPlan.infoDegree" />
					<jsp:getProperty name="infoDegree" property="tipoCurso" /> em 
					<jsp:getProperty name="infoDegree" property="nome" />
					<br/>
					<bean:write name="<%= SessionConstants.EXECUTION_PERIOD%>" property="name" scope="request"/>
				</logic:present>						
			</strong>
         </td>
    </tr>
</table>
<br />
<center>
<font color="red" size="3">Atenção aos comentários em baixo!<font>
<center/>
<br />
<%-- <bean:define id="infoExecutionDegree" name="component" property="infoExecutionDegree" />
<logic:equal name="infoExecutionDegree" property="temporaryExamMap" value="true">
	<center>
	<font color="red" size="12"><bean:message key="label.temporary.exam.map"/><font>
	<center/>
</logic:equal>
<bean:define id="infoExamsMap" name="component" property="infoExamsMap"/>
<br/>
<app:generateNewExamsMap name="infoExamsMap" user="public" mapType=" "/> --%>

<app:generateNewExamsMap name="<%= SessionConstants.INFO_EXAMS_MAP %>" user="public" mapType=" "/>
