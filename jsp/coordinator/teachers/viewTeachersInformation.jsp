<%@ page language="java" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt"%>

<h2><bean:message key="title.teachersInformation"/>(<dt:format pattern="dd/MM/yyyy"><dt:currentTime/></dt:format>)</h2>

<logic:present name="infoSiteTeachersInformation">
	<table width="90%" border="0" cellspacing="1" style="margin-top:10px">
		<tr>
			<td class="listClasses-header"><bean:message key="label.teacher" /></td>
			<td class="listClasses-header"><bean:message key="label.teachersInformation.number" /></td>
			<td class="listClasses-header"><bean:message key="label.teacher.category" /></td>
			<td class="listClasses-header"><bean:message key="label.teachersInformation.associatedLecturingCourses" /></td>
			<td class="listClasses-header"><bean:message key="label.teachersInformation.associatedLecturingCourses.degrees" /></td>
			<td class="listClasses-header"><bean:message key="label.teachersInformation.lastModificationDate" /></td>
		</tr>
		<logic:iterate id="infoSiteTeacherInformation" name="infoSiteTeachersInformation">
			<bean:size id="numberCourses" name="infoSiteTeacherInformation" property="infoLecturingExecutionCourses"/> 
				<logic:iterate id="infoExecutionCourse" name="infoSiteTeacherInformation" property="infoLecturingExecutionCourses" length="1"> 
					<tr>
						<td class="listClasses" rowspan="<%=  pageContext.findAttribute("numberCourses") %>">&nbsp;
							<html:link page="/readTeacherInformation.do" 
									   paramId="username" 
									   paramName="infoSiteTeacherInformation" 
					    				paramProperty="infoTeacher.infoPerson.username">
					    		<bean:write name="infoSiteTeacherInformation" 
					    					property="infoTeacher.infoPerson.nome"/> 
					    	</html:link> 
					    </td>
					    <td class="listClasses" rowspan="<%=  pageContext.findAttribute("numberCourses") %>">&nbsp; 
					    	<html:link page="/readTeacherInformation.do" 
					    			   paramId="username" 
					    			   paramName="infoSiteTeacherInformation" 
					    			   paramProperty="infoTeacher.infoPerson.username"> 
					    		<bean:write name="infoSiteTeacherInformation"   
					    					property="infoTeacher.teacherNumber"/> 
					    	</html:link> 
					    </td>
					    <td class="listClasses" rowspan="<%=  pageContext.findAttribute("numberCourses") %>">&nbsp; 
					    	<bean:write name="infoSiteTeacherInformation" property="infoTeacher.infoCategory.shortName"/> 
					    </td>
					    <td  class="listClasses" >
					    	<bean:write name="infoExecutionCourse" property="nome" />
					    	 <% if (
					    	 ((DataBeans.teacher.InfoSiteTeacherInformation)infoSiteTeacherInformation).getInfoResponsibleExecutionCourses().contains(infoExecutionCourse)
					    	 ){%>
					    	 (<bean:message key="label.teachersInformation.responsible"/>) <% }  %> 
					    	 </td>
					    <td  class="listClasses" >
					    	 <logic:iterate id="curricularCourse" name="infoExecutionCourse" property="associatedInfoCurricularCourses">
					    	 	<bean:write name="curricularCourse" property="infoDegreeCurricularPlan.infoDegree.sigla"/>&nbsp;
					    	 </logic:iterate>
					    </td> 
					    <td class="listClasses" rowspan="<%=  pageContext.findAttribute("numberCourses") %>">&nbsp; 
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
				    	<td  class="listClasses" >
				    		<bean:write name="infoExecutionCourse" property="nome" />
					    	 <% if (
					    	 ((DataBeans.teacher.InfoSiteTeacherInformation)infoSiteTeacherInformation).getInfoResponsibleExecutionCourses().contains(infoExecutionCourse)
					    	 ){%>
					    	 (<bean:message key="label.teachersInformation.responsible"/>) <% }  %> 
				    	 </td> 
				    	<td  class="listClasses" >
					    	<logic:iterate id="curricularCourse" name="infoExecutionCourse" property="associatedInfoCurricularCourses">
					    	 	<bean:write name="curricularCourse" property="infoDegreeCurricularPlan.infoDegree.sigla"/>&nbsp;
					    	</logic:iterate>
				     	</td> 
				     </tr>
			   	</logic:iterate>
		</logic:iterate>
	</table>
	<br/>
	<h2><bean:message key="label.teachersInformation.statistics"/>:</h2>
	<bean:size id="length" name="infoSiteTeachersInformation"/>
	<bean:message key="label.teachersInformation.numberOfTeachers"/>:
	<bean:write name="length"/>
	<% int filled = 0; %>
	<logic:iterate id="infoSiteTeacherInformation" name="infoSiteTeachersInformation">
		<logic:present name="infoSiteTeacherInformation" property="lastModificationDate">
			<% filled++; %>
		</logic:present>
	</logic:iterate>
	<% int stats = (int) (((double) filled / length.doubleValue()) * 100); %>
	<br />
	<bean:message key="label.teachersInformation.filled"/>: <%= filled %>
	<br/>
	<%--<bean:message key="label.teachersInformation.stats"/>: <%= stats %>%--%>
</logic:present>
</logic:present>