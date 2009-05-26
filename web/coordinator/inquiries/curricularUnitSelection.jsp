<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<html:xhtml />

<style>

div.progress-container {
  border: 1px solid #ccc; 
  width: 100px; 
  margin: 2px 5px 2px 0; 
  padding: 1px; 
  float: left; 
  background: white;
}

div.progress-container > div {
  background-color: #ACE97C; 
  height: 12px
}

</style>

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
<table>
    <th></th><th colspan="2">Relatórios de Docência<br/>Preenchidos</th><th colspan="2">
    <logic:iterate id="executionCourse" name="executionCoursesToImproove">
        <bean:define id="executionCourseID" name="executionCourse" property="idInternal" />
        <bean:define id="executionDegreeID" name="executionDegreeID" />
        <tr>
            <td class="width400px">
                <html:link page="<%= "/viewInquiriesResults.do?method=selectExecutionCourse&executionCourseID=" + executionCourseID  + "&executionDegreeID=" + executionDegreeID + "&amp;degreeCurricularPlanID=" + request.getAttribute("degreeCurricularPlanID") %>" >
                    <bean:write name="executionCourse" property="nome"/>
                </html:link>
            </td>
            <td> 
                <fmt:formatNumber maxFractionDigits="0" value="${(executionCourse.answeredTeachingInquiriesCount / executionCourse.professorshipsCount) * 100}" var="ratio"/> 
                <bean:define id="ratio" name="ratio" />
                <div class="progress-container">          
                    <div style="width: <%= ratio %>%"></div>
                </div>
            </td>
            <td>
                <bean:write name="executionCourse" property="answeredTeachingInquiriesCount"/> / <bean:write name="executionCourse" property="professorshipsCount"/>
            </td>
        </tr>
    </logic:iterate>
</table>
    
<p class="separator2 mtop25"><b><bean:message key="title.coordinationReport.excellentResults" bundle="INQUIRIES_RESOURCES"/></b></p>
<table>
    <th></th><th colspan="2">Relatórios de Docência<br/>Preenchidos</th><th colspan="2">
    <logic:iterate id="executionCourse" name="excelentExecutionCourses">
        <bean:define id="executionCourseID" name="executionCourse" property="idInternal" />
        <bean:define id="executionDegreeID" name="executionDegreeID" />
        <tr>
            <td class="width400px">
                <html:link page="<%= "/viewInquiriesResults.do?method=selectExecutionCourse&executionCourseID=" + executionCourseID  + "&executionDegreeID=" + executionDegreeID + "&amp;degreeCurricularPlanID=" + request.getAttribute("degreeCurricularPlanID") %>" >
                    <bean:write name="executionCourse" property="nome"/>
                </html:link>
            </td>
            <td> 
                <fmt:formatNumber maxFractionDigits="0" value="${(executionCourse.answeredTeachingInquiriesCount / executionCourse.professorshipsCount) * 100}" var="ratio"/> 
                <bean:define id="ratio" name="ratio" />
                <div class="progress-container">          
                    <div style="width: <%= ratio %>%"></div>
                </div>
            </td>
            <td>
                <bean:write name="executionCourse" property="answeredTeachingInquiriesCount"/> / <bean:write name="executionCourse" property="professorshipsCount"/>
            </td>
        </tr>
    </logic:iterate>
</table>

<p class="separator2 mtop25"><b><bean:message key="title.coordinationReport.otherResults" bundle="INQUIRIES_RESOURCES"/></b></p>
<table>
    <th></th><th colspan="2">Relatórios de Docência<br/>Preenchidos</th><th colspan="2">
    <logic:iterate id="executionCourse" name="otherExecutionCourses">
        <bean:define id="executionCourseID" name="executionCourse" property="idInternal" />
        <bean:define id="executionDegreeID" name="executionDegreeID" />
        <tr>
            <td class="width400px">
                <html:link page="<%= "/viewInquiriesResults.do?method=selectExecutionCourse&executionCourseID=" + executionCourseID  + "&executionDegreeID=" + executionDegreeID + "&amp;degreeCurricularPlanID=" + request.getAttribute("degreeCurricularPlanID") %>" >
                    <bean:write name="executionCourse" property="nome"/>
                </html:link>
            </td>
            <td> 
                <fmt:formatNumber maxFractionDigits="0" value="${(executionCourse.answeredTeachingInquiriesCount / executionCourse.professorshipsCount) * 100}" var="ratio"/> 
                <bean:define id="ratio" name="ratio" />
                <div class="progress-container">          
                    <div style="width: <%= ratio %>%"></div>
                </div>
            </td>
            <td>
                <bean:write name="executionCourse" property="answeredTeachingInquiriesCount"/> / <bean:write name="executionCourse" property="professorshipsCount"/>
            </td>
        </tr>
    </logic:iterate>
</table>

<p class="separator2 mtop25"><b><bean:message key="title.coordinatorExecutionDegreeCoursesReport" bundle="INQUIRIES_RESOURCES"/></b></p>

<logic:equal name="canComment" value="true">
    <html:form action="/viewInquiriesResults.do">
        <html:hidden property="method" value="selectexecutionSemester"/>
        <html:hidden property="degreeCurricularPlanID"/>
        <html:hidden property="executionDegreeID"/>
        <html:hidden property="executionSemesterID"/>
        <fr:edit name="executionDegreeCoursesReport" schema="executionDegree.coordinatorExecutionDegreeCoursesReport" >
            <fr:layout name="tabular-editable">
                <fr:property name="classes" value="tstyle2 thlight thleft thtop"/>
                <fr:property name="columnClasses" value="width300px,,"/>
           </fr:layout>
        </fr:edit>
        <html:submit><bean:message key="label.edit" bundle="APPLICATION_RESOURCES"/></html:submit>
    </html:form>
</logic:equal>
<logic:equal name="canComment" value="false">
    <logic:notEmpty name="executionDegreeCoursesReport">
        <fr:view name="executionDegreeCoursesReport" schema="executionDegree.coordinatorExecutionDegreeCoursesReport" >
            <fr:layout name="tabular">
                <fr:property name="classes" value="tstyle2 thlight thleft thtop width300px"/>
           </fr:layout>        
        </fr:view>
    </logic:notEmpty>
</logic:equal>
