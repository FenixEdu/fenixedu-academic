<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt"%>

<h2><bean:message key="title.teachersInformation"/> (<dt:format pattern="dd/MM/yyyy"><dt:currentTime/></dt:format>)</h2>

<bean:define id="degreeCurricularPlanID" name="degreeCurricularPlanID" scope="request" />

<html:form action="/teachersInformation.do">	
	<p class="mtop2">
		<bean:message key="link.masterDegree.administrativeOffice.gratuity.chosenYear"/>: 
		<html:select bundle="HTMLALT_RESOURCES" altKey="select.yearString" property="yearString" size="1" onchange="this.form.submit();">
			<html:options property="value" labelProperty="label" collection="executionYearList" />
		</html:select>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeCurricularPlanID" property="degreeCurricularPlanID" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionDegreeId" property="executionDegreeId" />		
		<html:submit styleId="javascriptButtonID" styleClass="altJavaScriptSubmitButton" bundle="HTMLALT_RESOURCES" altKey="submit.submit">
			<bean:message key="button.submit"/>
		</html:submit>
	</p>
</html:form>


<logic:present name="infoSiteTeachersInformation">
	<table class="tstyle4">
		<tr>
			<th><bean:message key="label.teacher" /></th>
			<th><bean:message key="label.teachersInformation.number" /></th>
			<th><bean:message key="label.teacher.category" /></th>
			<th><bean:message key="label.teachersInformation.associatedLecturingCourses" /></th>
			<th><bean:message key="label.teachersInformation.associatedLecturingCourses.degrees" /></th>
			<th><bean:message key="label.teachersInformation.lastModificationDate" /></th>
		</tr>
		<logic:iterate id="infoSiteTeacherInformation" name="infoSiteTeachersInformation">
			<bean:size id="numberCourses" name="infoSiteTeacherInformation" property="infoLecturingExecutionCourses"/> 
				<logic:iterate id="infoExecutionCourse" name="infoSiteTeacherInformation" property="infoLecturingExecutionCourses" length="1"> 
				<bean:define id="teacherUsername" name="infoSiteTeacherInformation" property="infoTeacher.infoPerson.username" />
					<tr>
						<td class="nowrap" rowspan="<%=  pageContext.findAttribute("numberCourses") %>">
							<html:link page="<%= "/readTeacherInformation.do?username=" + teacherUsername + "&degreeCurricularPlanID=" + degreeCurricularPlanID %>">
					    		<bean:write name="infoSiteTeacherInformation" 
					    					property="infoTeacher.infoPerson.nome"/> 
					    	</html:link> 
					    </td>
					    <td class="acenter" rowspan="<%=  pageContext.findAttribute("numberCourses") %>"> 
					    	<html:link page="<%= "/readTeacherInformation.do?username=" + teacherUsername + "&degreeCurricularPlanID=" + degreeCurricularPlanID %>">
					    		<bean:write name="infoSiteTeacherInformation"   
					    					property="infoTeacher.teacherNumber"/> 
					    	</html:link> 
					    </td>
					    <td rowspan="<%=  pageContext.findAttribute("numberCourses") %>"> 
					    	<bean:write name="infoSiteTeacherInformation" property="infoTeacher.infoCategory.shortName"/> 
					    </td>
					    <td  >
					    	<bean:write name="infoExecutionCourse" property="nome" />
					    	 <% if (
					    	 ((net.sourceforge.fenixedu.dataTransferObject.teacher.InfoSiteTeacherInformation)infoSiteTeacherInformation).getInfoResponsibleExecutionCourses().contains(infoExecutionCourse)
					    	 ){%>
					    	 (<bean:message key="label.teachersInformation.responsible"/>) <% }  %> 
					    	 </td>
					    <td  >
					    	 <logic:iterate id="curricularCourse" name="infoExecutionCourse" property="associatedInfoCurricularCourses">
					    	 	<bean:write name="curricularCourse" property="infoDegreeCurricularPlan.infoDegree.sigla"/>
					    	 </logic:iterate>
					    </td> 
					    <td class="nowrap" rowspan="<%=  pageContext.findAttribute("numberCourses") %>"> 
					    	<logic:present  name="infoSiteTeacherInformation" property="lastModificationDate">
						    	<dt:format pattern="dd/MM/yyyy HH:mm"> 
						    		<bean:write name="infoSiteTeacherInformation" property="lastModificationDate.time"/>
						    	</dt:format>
					    	</logic:present> 
					    	<logic:notPresent name="infoSiteTeacherInformation" property="lastModificationDate">
					    	 <bean:message key="label.teachersInformation.notModified"/> 
					    	</logic:notPresent> 
					    </td>
				    </tr>
    			</logic:iterate>
				<logic:iterate  id="infoExecutionCourse" name="infoSiteTeacherInformation" property="infoLecturingExecutionCourses" offset="1">
					<tr>
				    	<td  >
				    		<bean:write name="infoExecutionCourse" property="nome" />
					    	 <% if (
					    	 ((net.sourceforge.fenixedu.dataTransferObject.teacher.InfoSiteTeacherInformation)infoSiteTeacherInformation).getInfoResponsibleExecutionCourses().contains(infoExecutionCourse)
					    	 ){%>
					    	 (<bean:message key="label.teachersInformation.responsible"/>) <% }  %> 
				    	 </td> 
				    	<td  >
					    	<logic:iterate id="curricularCourse" name="infoExecutionCourse" property="associatedInfoCurricularCourses">
					    	 	<bean:write name="curricularCourse" property="infoDegreeCurricularPlan.infoDegree.sigla"/>
					    	</logic:iterate>
				     	</td> 
				     </tr>
			   	</logic:iterate>
		</logic:iterate>
	</table>



	<h2><bean:message key="label.teachersInformation.statistics"/></h2>

	<bean:size id="length" name="infoSiteTeachersInformation"/>
	<p>
		<bean:message key="label.teachersInformation.numberOfTeachers"/>:
		<strong><bean:write name="length"/></strong>
		<% int filled = 0; %>
		<logic:iterate id="infoSiteTeacherInformation" name="infoSiteTeachersInformation">
			<logic:present name="infoSiteTeacherInformation" property="lastModificationDate">
				<strong><% filled++; %></strong>
			</logic:present>
		</logic:iterate>
		<% int stats = (int) (((double) filled / length.doubleValue()) * 100); %>
	</p>
	
	<p>
		<bean:message key="label.teachersInformation.filled"/>: <strong><%= filled %></strong>
	</p>

	<%--<bean:message key="label.teachersInformation.stats"/>: <%= stats %>%--%>
</logic:present>