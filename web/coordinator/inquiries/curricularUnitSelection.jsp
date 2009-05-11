<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<html:xhtml />

<h2><bean:message key="title.inquiries.resultsWithDescription" bundle="INQUIRIES_RESOURCES"/></h2>

<p>
	<bean:message key="message.inquiries.coordinator.instructions" bundle="INQUIRIES_RESOURCES"/>
</p>

<html:form action="/viewInquiriesResults.do">
	<html:hidden property="method" value="prepare"/>
	<html:hidden property="degreeCurricularPlanID"/>
	<html:hidden property="executionDegreeID"/>
	<table class="tstyle5 thlight thright mvert05">
	<tr>
		<th>
		<label for="executionSemesterID"><bean:message key="label.inquiries.semesterAndYear" bundle="INQUIRIES_RESOURCES"/>:</label>
		</th>
		<td>
		<html:select property="executionSemesterID" onchange="this.form.method.value='selectexecutionSemester';this.form.submit();">
			<html:option value=""><bean:message key="label.inquiries.chooseAnOption" bundle="INQUIRIES_RESOURCES"/></html:option>
	 		<html:options collection="executionPeriods" property="idInternal" labelProperty="qualifiedName"/>
		</html:select>
		</td>
	</tr>
	</table>
</html:form>

<p class="separator2 mtop25"><b><bean:message key="title.coordinationReport.resultsToImprove" bundle="INQUIRIES_RESOURCES"/></b></p>
<logic:iterate id="executionCourse" name="executionCoursesToImproove">
    <bean:define id="executionCourseID" name="executionCourse" property="idInternal" />
    <bean:define id="executionDegreeID" name="executionDegreeID" />
    <html:link page="<%= "/viewInquiriesResults.do?method=selectExecutionCourse&executionCourseID=" + executionCourseID  + "&executionDegreeID=" + executionDegreeID%>" >
        <bean:write name="executionCourse" property="nome"/><br/>
    </html:link>
</logic:iterate>
	
<p class="separator2 mtop25"><b><bean:message key="title.coordinationReport.excellentResults" bundle="INQUIRIES_RESOURCES"/></b></p>
<logic:iterate id="executionCourse" name="excelentExecutionCourses">
    <bean:define id="executionCourseID" name="executionCourse" property="idInternal" />
    <bean:define id="executionDegreeID" name="executionDegreeID" />
    <html:link page="<%= "/viewInquiriesResults.do?method=selectExecutionCourse&executionCourseID=" + executionCourseID  + "&executionDegreeID=" + executionDegreeID%>" >
        <bean:write name="executionCourse" property="nome"/><br/>
    </html:link>
</logic:iterate>

<p class="separator2 mtop25"><b><bean:message key="title.coordinationReport.otherResults" bundle="INQUIRIES_RESOURCES"/></b></p>
<logic:iterate id="executionCourse" name="otherExecutionCourses">
    <bean:define id="executionCourseID" name="executionCourse" property="idInternal" />
    <bean:define id="executionDegreeID" name="executionDegreeID" />
    <html:link page="<%= "/viewInquiriesResults.do?method=selectExecutionCourse&executionCourseID=" + executionCourseID  + "&executionDegreeID=" + executionDegreeID%>" >
        <bean:write name="executionCourse" property="nome"/><br/>
    </html:link>
</logic:iterate>