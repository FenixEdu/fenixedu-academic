<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<html:xhtml/>

<h2><bean:message key="title.student.enrollment" bundle="STUDENT_RESOURCES"/></h2>

<span class="error"><!-- Error messages go here --><html:errors /></span>
<logic:messagesPresent message="true">
	<ul>
		<html:messages id="messages" message="true">
			<li><span class="error0"><bean:write name="messages" /></span></li>
		</html:messages>
	</ul>
	<br />
</logic:messagesPresent>

<html:form action="/curricularCoursesEnrollment">

	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="prepareEnrollmentChooseAreas" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="2" />
	
	<bean:define id="executionPeriod" name="executionPeriod" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionPeriod" property="executionPeriod" value="<%= executionPeriod.toString() %>"/>
	
	<bean:define id="executionYear" name="executionYear" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionYear" property="executionYear" value="<%= executionYear.toString() %>"/>
	
	<bean:define id="studentNumber" name="studentCurricularPlan" property="student.number" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.studentNumber" property="studentNumber" value="<%= studentNumber.toString() %>" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeCurricularPlanID" property="degreeCurricularPlanID" />
	
	<logic:present name="executionDegreeId">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionDegreeId" property="executionDegreeId" value="<%=pageContext.findAttribute("executionDegreeId").toString()%>"/>
	</logic:present>
	

<div class="infoop2">
	<p>
		<b><bean:message key="label.student.enrollment.number"/>:</b>
		<bean:write name="studentCurricularPlan" property="student.number" />&nbsp;-&nbsp;
		<bean:write name="studentCurricularPlan" property="student.person.name" />
	</p>
	<p>
		<b><bean:message key="label.student.enrollment.executionPeriod"/>:</b>
		<bean:write name="executionPeriod" />&nbsp;				
		<bean:write name="executionYear" />
	</p>
</div>


<div class="infoop mvert15">
	<bean:message key="message.student.enrollment.help" />
</div>

	<table class="mtop15">
		<tr>
			<td>
				<bean:message key="label.student.enrollment.specializationArea" /> /<bean:message key="label.student.enrollment.branch"  bundle="STUDENT_RESOURCES"/>
			</td>
			<td>
				<html:select bundle="HTMLALT_RESOURCES" altKey="select.specializationArea" property="specializationArea">
					<html:option value="" key="label.student.enrollment.no.area">
						<bean:message key="label.student.enrollment.no.area"/>
					</html:option>
					<html:options collection="specializationAreas" property="idInternal" labelProperty="name"/>
				</html:select>
			</td>
		</tr>
		<tr>
			<td>
				<bean:message key="label.student.enrollment.secondaryArea" />
			</td>
			<td>
				<html:select bundle="HTMLALT_RESOURCES" altKey="select.secondaryArea" property="secondaryArea">
					<html:option value="" key="label.student.enrollment.no.area">
						<bean:message key="label.student.enrollment.no.area"/>
					</html:option>
					<html:options collection="secondaryAreas" property="idInternal" labelProperty="name"/>
				</html:select>
			</td>
		</tr>
	</table>
	

	<p class="mtop2">
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
			<bean:message key="button.student.modify"/>
		</html:submit>
	</p>
</html:form>