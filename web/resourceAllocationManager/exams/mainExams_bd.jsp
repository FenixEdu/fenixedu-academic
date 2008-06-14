<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<em><bean:message key="title.resourceAllocationManager.management"/></em>
<h2>Avaliações Escritas</h2>
<p>
	Seleccione a op&ccedil;&atilde;o pretendida para criar, editar ou visualisar a calendariza&ccedil&atilde;o das avaliações escritas.
	Em baixo pode alterar o período de execu&ccedil&atilde;o seleccionado.
</p>

<p>
	<span class="error"><!-- Error messages go here --><html:errors /></span>
</p>

<html:form action="/mainExamsNew">

	<table class="tstyle5 thlight thmiddle mvert05">
	  <tr>
	    <th><bean:message key="property.executionPeriod"/>:</th>
	    <td><jsp:include page="../selectExecutionPeriodList.jsp"/></td>
	  </tr>
	</table>

	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="choose"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
	<p class="mtop05">
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
			<bean:message key="label.choose"/>
		</html:submit>
	</p>
</html:form>

<br/>

<html:form action="/publishExams">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="switchPublishedState"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
	<html:hidden alt="<%= net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.SessionConstants.EXECUTION_PERIOD_OID %>" property="<%= net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.SessionConstants.EXECUTION_PERIOD_OID %>"
				 value="<%= pageContext.findAttribute("executionPeriodOID").toString() %>"/>

	<p>
	<bean:message key="publish.exams.map" />
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
		<bean:message key="label.change.published.state"/>
	</html:submit>
	</p>
	
	<logic:present name="executionDegrees">
		<table class="tstyle4">
			<tr>
				<th>
					<bean:message key="label.manager.degree.tipoCurso"/>
				</th>
				<th>
					<bean:message key="label.degree"/>
				</th>
				<th>
					<bean:message key="label.exams.map.temp.state"/>
				</th>
			</tr>
			<logic:iterate id="executionDegree" name="executionDegrees">
				<tr>
					<td>
						<bean:message name="executionDegree" property="degreeCurricularPlan.degree.tipoCurso.name" bundle="ENUMERATION_RESOURCES"/>
					</td>
					<td>
						<bean:write name="executionDegree" property="degreeCurricularPlan.degree.nome"/>
					</td>
					<td>
						<logic:equal name="executionDegree" property="temporaryExamMap" value="true">
							<bean:message key="label.change.published.state.temp"/>
						</logic:equal>
						<logic:equal name="executionDegree" property="temporaryExamMap" value="false">
							<bean:message key="label.change.published.state.published"/>
						</logic:equal>
					</td>
				</tr>
			</logic:iterate>
		</table>
	</logic:present>

</html:form>
