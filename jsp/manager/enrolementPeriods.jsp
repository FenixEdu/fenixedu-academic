<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>

<h2><bean:message bundle="MANAGER_RESOURCES" key="title.manage.enrolement.period"/></h2>

<span class="error"><html:errors/></span>

<br />
<br />

<html:form action="/manageEnrolementPeriods">
	<html:hidden property="method" value="prepare"/>
	<html:hidden property="page" value="0"/>
	<table>
		<tr>
			<td class="listClasses-header">
				<bean:message bundle="MANAGER_RESOURCES" key="label.choose.execution.period"/>
			</td>
			<td class="listClasses">
				<html:select property="executionPeriodID" onchange="this.form.submit();">
					<html:option value=""/>
					<html:options collection="infoExecutionPeriods" labelProperty="description" property="idInternal"/>
				</html:select>
			</td>
		</tr>
	</table>
</html:form>

<br />
<br />

<html:form action="/manageEnrolementPeriods">
	<html:hidden property="method" value="createPeriods"/>
	<html:hidden property="page" value="0"/>
	<html:hidden property="executionPeriodID"/>
	<table>
		<tr>
			<td class="listClasses-header">
			</td>
			<td class="listClasses">
				<html:select property="degreeType">
					<html:option bundle="MANAGER_RESOURCES" key="label.all" value=""/>
					<html:option bundle="ENUMERATION_RESOURCES" key="DEGREE" value="DEGREE"/>
					<html:option bundle="ENUMERATION_RESOURCES" key="MASTER_DEGREE" value="MASTER_DEGREE"/>
				</html:select>
			</td>
		</tr>
		<tr>
			<td class="listClasses-header">
			</td>
			<td class="listClasses">
				<html:select property="enrolmentPeriodClass">
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
				<html:text property="startDate" size="8"/> <html:text property="endDate" size="8"/>
			</td>
		</tr>
	</table>

	<br />
	<br />

	<html:submit styleClass="inputbutton">
		<bean:message bundle="MANAGER_RESOURCES" key="button.change"/>
	</html:submit>

</html:form>

	<logic:present name="infoEnrolmentPeriods">
		<br />
		<br />

		<table>
			<tr>
				<td class="listClasses-header">
					<bean:message bundle="MANAGER_RESOURCES" key="label.choose.execution.period"/>
				</td>
				<td class="listClasses-header">
					<bean:message bundle="MANAGER_RESOURCES" key="label.manager.degree.tipoCurso"/>
				</td>
				<td class="listClasses-header">
					<bean:message bundle="MANAGER_RESOURCES" key="label.enrolment.period.type"/>
				</td>
				<td class="listClasses-header">
					<bean:message bundle="MANAGER_RESOURCES" key="label.enrolment.period.startDate"/>
				</td>
				<td class="listClasses-header">
					<bean:message bundle="MANAGER_RESOURCES" key="label.enrolment.period.endDate"/>
				</td>
				<td class="listClasses-header">
				</td>
			</tr>
			<logic:iterate id="infoEnrolmentPeriod" name="infoEnrolmentPeriods">
				<html:form action="/manageEnrolementPeriods">
					<html:hidden property="method" value="changePeriodValues"/>
					<html:hidden property="page" value="0"/>
					<html:hidden property="executionPeriodID"/>
					<bean:define id="enrolmentPeriodID" type="java.lang.Integer" name="infoEnrolmentPeriod" property="idInternal"/>
					<html:hidden property="enrolmentPeriodID" value="<%= enrolmentPeriodID.toString() %>"/>

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
							<html:text property="startDate" value="<%= startDate.toString() %>" size="8"/>
						</td>
						<td class="listClasses">
							<bean:define id="endDate" type="java.lang.String"><dt:format pattern="yyyy/MM/dd"><bean:write name="infoEnrolmentPeriod" property="endDate.time"/></dt:format></bean:define>
							<html:text property="endDate" value="<%= endDate.toString() %>" size="8"/>
						</td>
						<td class="listClasses">
							<html:submit styleClass="inputbutton">
								<bean:message bundle="MANAGER_RESOURCES" key="button.change"/>
							</html:submit>
						</td>
					</tr>
				</html:form>
			</logic:iterate>
		</table>
	</logic:present>