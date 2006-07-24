<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>

<h2><bean:message bundle="MANAGER_RESOURCES" key="title.manage.enrolement.period"/></h2>

<span class="error"><html:errors/></span>

<br />
<br />

<html:form action="/manageEnrolementPeriods">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="prepare"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="0"/>
	<table>
		<tr>
			<th class="listClasses-header">
				<bean:message bundle="MANAGER_RESOURCES" key="label.choose.execution.period"/>
			</th>
			<td class="listClasses">
				<html:select bundle="HTMLALT_RESOURCES" altKey="select.executionPeriodID" property="executionPeriodID" onchange="this.form.submit();">
					<html:option value=""/>
					<html:options collection="infoExecutionPeriods" labelProperty="description" property="idInternal"/>
				</html:select>
				<html:submit styleId="javascriptButtonID" styleClass="altJavaScriptSubmitButton" bundle="HTMLALT_RESOURCES" altKey="submit.submit">
					<bean:message key="button.submit"/>
				</html:submit>
			</td>
		</tr>
	</table>
</html:form>

<br />
<br />

<html:form action="/manageEnrolementPeriods">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="createPeriods"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="0"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionPeriodID" property="executionPeriodID"/>
	<table>
		<tr>
			<th class="listClasses-header">
			</th>
			<td class="listClasses">
				<html:select bundle="HTMLALT_RESOURCES" altKey="select.degreeType" property="degreeType">
					<html:option bundle="MANAGER_RESOURCES" key="label.all" value=""/>
					<html:option bundle="ENUMERATION_RESOURCES" key="DEGREE" value="DEGREE"/>
					<html:option bundle="ENUMERATION_RESOURCES" key="MASTER_DEGREE" value="MASTER_DEGREE"/>
				</html:select>
			</td>
		</tr>
		<tr>
			<th class="listClasses-header">
			</th>
			<td class="listClasses">
				<html:select bundle="HTMLALT_RESOURCES" altKey="select.enrolmentPeriodClass" property="enrolmentPeriodClass">
					<html:option bundle="MANAGER_RESOURCES" key="label.class.enrolment.period.in.curricular.course"
							value="net.sourceforge.fenixedu.domain.EnrolmentPeriodInCurricularCourses"/>
					<html:option bundle="MANAGER_RESOURCES" key="label.class.enrolment.period.in.classes"
							value="net.sourceforge.fenixedu.domain.EnrolmentPeriodInClasses"/>
					<html:option bundle="MANAGER_RESOURCES" key="label.class.enrolment.period.in.curricular.course.special.season"
							value="net.sourceforge.fenixedu.domain.EnrolmentPeriodInCurricularCoursesSpecialSeason"/>
				</html:select>
			</td>
		</tr>
		<tr>
			<td class="listClasses">
			</td>
			<td class="listClasses">
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.startDate" property="startDate" size="8"/> <html:text bundle="HTMLALT_RESOURCES" altKey="text.endDate" property="endDate" size="8"/>
			</td>
		</tr>
	</table>

	<br />
	<br />

	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
		<bean:message bundle="MANAGER_RESOURCES" key="button.change"/>
	</html:submit>

</html:form>

	<logic:present name="infoEnrolmentPeriods">
		<br />
		<br />

		<table>
			<tr>
				<th class="listClasses-header">
					<bean:message bundle="MANAGER_RESOURCES" key="label.choose.execution.period"/>
				</th>
				<th class="listClasses-header">
					<bean:message bundle="MANAGER_RESOURCES" key="label.manager.degree.tipoCurso"/>
				</th>
				<th class="listClasses-header">
					<bean:message bundle="MANAGER_RESOURCES" key="label.enrolment.period.type"/>
				</th>
				<th class="listClasses-header">
					<bean:message bundle="MANAGER_RESOURCES" key="label.enrolment.period.startDate"/>
				</th>
				<th class="listClasses-header">
					<bean:message bundle="MANAGER_RESOURCES" key="label.enrolment.period.endDate"/>
				</th>
				<th class="listClasses-header">
				</th>
			</tr>
			<logic:iterate id="infoEnrolmentPeriod" name="infoEnrolmentPeriods">
				<html:form action="/manageEnrolementPeriods">
					<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="changePeriodValues"/>
					<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="0"/>
					<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionPeriodID" property="executionPeriodID"/>
					<bean:define id="enrolmentPeriodID" type="java.lang.Integer" name="infoEnrolmentPeriod" property="idInternal"/>
					<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.enrolmentPeriodID" property="enrolmentPeriodID" value="<%= enrolmentPeriodID.toString() %>"/>

					<tr>
						<td class="listClasses">
							<bean:write name="infoEnrolmentPeriod" property="infoDegreeCurricularPlan.infoDegree.tipoCurso"/>
						</td>
						<td class="listClasses">
							<bean:write name="infoEnrolmentPeriod" property="infoDegreeCurricularPlan.infoDegree.nome"/>
						</td>
						<td class="listClasses">
							<bean:message bundle="MANAGER_RESOURCES" name="infoEnrolmentPeriod" property="class.simpleName"/>
						</td>
						<td class="listClasses">
							<bean:define id="startDate" type="java.lang.String"><dt:format pattern="yyyy/MM/dd"><bean:write name="infoEnrolmentPeriod" property="startDate.time"/></dt:format></bean:define>
							<html:text bundle="HTMLALT_RESOURCES" altKey="text.startDate" property="startDate" value="<%= startDate.toString() %>" size="8"/>
						</td>
						<td class="listClasses">
							<bean:define id="endDate" type="java.lang.String"><dt:format pattern="yyyy/MM/dd"><bean:write name="infoEnrolmentPeriod" property="endDate.time"/></dt:format></bean:define>
							<html:text bundle="HTMLALT_RESOURCES" altKey="text.endDate" property="endDate" value="<%= endDate.toString() %>" size="8"/>
						</td>
						<td class="listClasses">
							<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
								<bean:message bundle="MANAGER_RESOURCES" key="button.change"/>
							</html:submit>
						</td>
					</tr>
				</html:form>
			</logic:iterate>
		</table>
	</logic:present>