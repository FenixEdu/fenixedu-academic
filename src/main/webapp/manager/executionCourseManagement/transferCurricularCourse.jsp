<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%-- TO BE DELETED --%>

<h2><bean:message bundle="MANAGER_RESOURCES" key="label.manager.executionCourseManagement.edit.executionCourse"/></h2>

<logic:messagesPresent message="true" property="success">
	<p>
		<span class="success0">
			<html:messages id="messages" message="true" bundle="MANAGER_RESOURCES" property="success">
				<bean:write name="messages" />
			</html:messages>
		</span>
	</p>
</logic:messagesPresent>
<span class="error"><!-- Error messages go here --><html:errors /></span>
<logic:messagesPresent message="true" property="error">
	<p>
		<span class="error0">
			<html:messages id="messages" message="true" bundle="MANAGER_RESOURCES" property="error">
				<bean:write name="messages" />
			</html:messages>
		</span>
	</p>
</logic:messagesPresent>

Nome - Sigla Disciplina Execução: <span class="infoop4"><bean:write name="infoExecutionCourse" property="nome"/>
 - <bean:write name="infoExecutionCourse" property="sigla"/></span>
<br />
Nome - Código - Plano Curricular Disciplina Curricular: <span class="infoop4"><bean:write name="infoCurricularCourse" property="name"/>
 - <bean:write name="infoCurricularCourse" property="code"/>
 - <bean:write name="infoCurricularCourse" property="infoDegreeCurricularPlan.name"/></span>

<br />
<br />
<html:form action="/editExecutionCourseTransferCurricularCourses" focus="name">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="transferCurricularCourse"/>

	ID da disciplina execução: <bean:write name="executionCourseId"/><br />
	ID da disciplina curricular: <bean:write name="curricularCourseId"/><br />

	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionCourseId" property="executionCourseId" value="<%= pageContext.findAttribute("executionCourseId").toString() %>"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.curricularCourseId" property="curricularCourseId" value="<%= pageContext.findAttribute("curricularCourseId").toString() %>"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionPeriodId" property="executionPeriod" value="<%= pageContext.findAttribute("executionPeriodName") + "~" + pageContext.findAttribute("executionPeriodId").toString() %>"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionDegreeId" property="executionDegree" value="<%= pageContext.findAttribute("executionDegreeName") + "~" + pageContext.findAttribute("executionDegreeId").toString() %>"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.curricularYearId" property="curYear" value="<%= pageContext.findAttribute("curYearName").toString() + "~" + pageContext.findAttribute("curYearId").toString() %>"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionCoursesNotLinked" property="executionCoursesNotLinked" value="<%= pageContext.findAttribute("executionCoursesNotLinked").toString() %>"/>

	Seleccionar curso destino:
	<html:select bundle="HTMLALT_RESOURCES" altKey="select.destinationExecutionDegreeId" property="destinationExecutionDegreeId" size="1"
			onchange="this.form.method.value='selectExecutionDegree';this.form.submit();">
		<html:options collection="executionDegrees" labelProperty="label" property="value" />
	</html:select>
	<html:submit styleId="javascriptButtonID" styleClass="altJavaScriptSubmitButton" bundle="HTMLALT_RESOURCES" altKey="submit.submit">
		<bean:message key="button.submit"/>
	</html:submit>
	<br/>

	Seleccionar ano curricular:
	<html:select bundle="HTMLALT_RESOURCES" altKey="select.curricularYear" property="curricularYear" size="1"
			onchange="this.form.method.value='selectExecutionDegree';this.form.submit();">
		<html:options collection="curricularYears" labelProperty="label" property="value" />
	</html:select>
	<html:submit styleId="javascriptButtonID2" styleClass="altJavaScriptSubmitButton" bundle="HTMLALT_RESOURCES" altKey="submit.submit">
		<bean:message key="button.submit"/>
	</html:submit>
	<br/>

	<br />
	<br />
	<logic:present name="executionCourses">
	<logic:notEmpty name="executionCourses">
		Seleccionar Disciplina Execução destino:
		<table>
			<tr>
				<th class="listClasses-header">externalId
				</th>
				<th class="listClasses-header">nome
				</th>
				<th class="listClasses-header">sigla
				</th>
			</tr>
			<logic:iterate id="executionCourse" name="executionCourses">
				<bean:define id="destinationExecutionCourseId" name="executionCourse" property="externalId"/>
				<tr>
					<td class="listClasses">
						<bean:write name="executionCourse" property="externalId"/>
						<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.destinationExecutionCourseId" property="destinationExecutionCourseId"
								value="<%= pageContext.findAttribute("destinationExecutionCourseId").toString() %>" />
					</td>
					<td class="listClasses">
						<bean:write name="executionCourse" property="nome"/>
					</td>
					<td class="listClasses">
						<bean:write name="executionCourse" property="sigla"/>
					</td>
				</tr>
			</logic:iterate>		
		</table>
	</logic:notEmpty>
	<logic:empty name="executionCourses">
		Não há disciplinas execução para o curso e ano curricular escolhido.
	</logic:empty>
	</logic:present>

	<br />
	<br />
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
		<bean:message bundle="MANAGER_RESOURCES" key="button.transfer"/>
	</html:submit>
</html:form>
<%-- BEGIN information needed to build link for returning to edit --%>
<bean:define id="linkGetRequestBigMessage" value="" />
<logic:equal name="sessionBean" property="chooseNotLinked" value="false">
	<bean:define id="linkGetRequestBigMessage"
		value="<%= "&executionPeriod=" + pageContext.findAttribute("executionPeriodName") + "~" + pageContext.findAttribute("executionPeriodId")
				+ "&executionDegree=" + pageContext.findAttribute("executionDegreeName") + "~" + pageContext.findAttribute("executionDegreeId")
				+ "&curYear=" + pageContext.findAttribute("curYearName") + "~" + pageContext.findAttribute("curYearId")
				+ "&executionCoursesNotLinked=null"%>" />
</logic:equal>
<logic:equal name="sessionBean" property="chooseNotLinked" value="true">
	<bean:define id="linkGetRequestBigMessage"
		value="<%= "&executionPeriod=" + pageContext.findAttribute("executionPeriodName") + "~" + pageContext.findAttribute("executionPeriodId")
		+ "&executionDegree=null~null"
		+ "&curYear=null~null"
		+ "&executionCoursesNotLinked=" + pageContext.findAttribute("executionCoursesNotLinked") %>" />
</logic:equal>
<%-- END information needed to build link for returning to edit --%>
<fr:form action="<%="/editExecutionCourse.do?method=editExecutionCourse&executionCourseId=" + pageContext.findAttribute("executionCourseId").toString() + linkGetRequestBigMessage.toString() %>">
	<fr:edit id="sessionBeanJSP" name="sessionBean" visible="false"/>
	<html:submit>
		<bean:message bundle="MANAGER_RESOURCES" key="label.return"/>
	</html:submit>
</fr:form>