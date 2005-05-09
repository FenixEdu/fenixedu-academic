<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<h2>Gest&atilde;o de Exames</h2>
<p>Seleccione a op&ccedil;&atilde;o pretendida para criar, editar ou visualisar a calendariza&ccedil&atilde;o dos exames. <br />
Pode alterar em baixo o período de execu&ccedil&atilde;o seleccionado.</p>
<span class="error"><html:errors /></span>
<html:form action="/mainExamsNew">
<table border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td nowrap="nowrap" width="125"><bean:message key="property.executionPeriod"/>:</td>
    <td nowrap="nowrap"><jsp:include page="../selectExecutionPeriodList.jsp"/></td>
  </tr>
</table>
<br />
<html:hidden property="method" value="choose"/>
<html:hidden property="page" value="1"/>
<html:submit styleClass="inputbutton">
<bean:message key="label.choose"/>
</html:submit>
</html:form>

<br/>

<html:form action="/publishExams">
	<html:hidden property="method" value="switchPublishedState"/>
	<html:hidden property="page" value="1"/>
	<html:hidden property="<%= net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants.EXECUTION_PERIOD_OID %>"
				 value="<%= pageContext.findAttribute("executionPeriodOID").toString() %>"/>

	<bean:message key="publish.exams.map" />

	<logic:present name="executionDegrees">
		<logic:notEmpty name="executionDegrees">
			<logic:iterate id="executionDegree" name="executionDegrees" length="1">
				<logic:equal name="executionDegree" property="temporaryExamMap" value="true">
					<html:checkbox property="switchPublishedState" value="on" onchange="this.form.submit();"/>
				</logic:equal>
				<logic:notEqual name="executionDegree" property="temporaryExamMap" value="true">
					<html:checkbox property="switchPublishedState" value="off" onchange="this.form.submit();"/>
				</logic:notEqual>
			</logic:iterate>
		</logic:notEmpty>
		<logic:empty name="executionDegrees">
			<html:checkbox property="switchPublishedState" value="off" onchange="this.form.submit();"/>
		</logic:empty>
	</logic:present>

	<bean:write name="executionPeriodOID"/>

</html:form>
