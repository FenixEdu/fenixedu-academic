<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<br/>
<table width="80%" border="0" cellpadding="0" cellspacing="0" align="center">
	<tr>
		<td bgcolor="#FFFFFF" class="infoselected"><p>A licenciatura seleccionada
        	&eacute;:</p>
			<strong><jsp:include page="examsMapContext.jsp"/></strong>
         </td>
    </tr>
</table>
<br/>
<bean:define id="infoExecutionDegree" name="exeDegree" />
<logic:equal name="infoExecutionDegree" property="temporaryExamMap" value="true">
	<center>
	<font color="red" size="12"><bean:message key="label.temporary.exam.map"/><font>
	<center/>
</logic:equal>

<br/>
<app:generateExamsMap name="infoExamsMap" user="public"/>
