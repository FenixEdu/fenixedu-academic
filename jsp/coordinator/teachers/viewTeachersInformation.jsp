<%@ page language="java" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt"%>

<h2><bean:message key="title.teachersInformation"/>(<dt:format pattern="dd/MM/yyyy"><dt:currentTime/></dt:format>)</h2>

<logic:present name="infoSiteTeachersInformation">
	<table width="100%" border="0" cellspacing="1" style="margin-top:10px">
		<tr>
			<td class="listClasses-header"><bean:message key="label.teacher" /></td>
			<td class="listClasses-header"><bean:message key="label.teachersInformation.number" /></td>
			<td class="listClasses-header"><bean:message key="label.teacher.category" /></td>
			<td class="listClasses-header"><bean:message key="label.teachersInformation.associatedResponsibleCourses" /></td>
			<td class="listClasses-header"><bean:message key="label.teachersInformation.associatedLecturingCourses" /></td>
			<td class="listClasses-header"><bean:message key="label.teachersInformation.lastModificationDate" /></td>
		</tr>
		<logic:iterate id="infoSiteTeacherInformation" name="infoSiteTeachersInformation">
			<tr>
				<td class="listClasses">&nbsp;
					<html:link page="/readTeacherInformation.do" 
						       paramId="username" 
						       paramName="infoSiteTeacherInformation"
						       paramProperty="infoTeacher.infoPerson.username">
   				         	 <bean:write name="infoSiteTeacherInformation" property="infoTeacher.infoPerson.nome"/>
		         	</html:link>
	         	</td>     	
				<td class="listClasses">&nbsp;
					<html:link page="/readTeacherInformation.do" 
						       paramId="username" 
						       paramName="infoSiteTeacherInformation"
						       paramProperty="infoTeacher.infoPerson.username">
   					       <bean:write name="infoSiteTeacherInformation" property="infoTeacher.teacherNumber"/>
			       </html:link>
		       	</td>
		       	<td class="listClasses">&nbsp;
		       		<bean:write name="infoSiteTeacherInformation" property="infoTeacher.infoCategory.shortName"/>
		       	</td>
				<td class="listClasses">&nbsp;
					<logic:iterate id="infoExecutionCourse" name="infoSiteTeacherInformation" property="infoResponsibleExecutionCourses">
						<bean:write name="infoExecutionCourse" property="nome" />
						<br />
					</logic:iterate>
				</td>
				<td class="listClasses">&nbsp;
					<logic:iterate id="infoExecutionCourse" name="infoSiteTeacherInformation" property="infoLecturingExecutionCourses">
						<bean:write name="infoExecutionCourse" property="nome" />
						<br />
					</logic:iterate>
				</td>
				<td class="listClasses">&nbsp;
					<logic:present name="infoSiteTeacherInformation" property="lastModificationDate"> 
						<dt:format pattern="dd/MM/yyyy HH:mm">
							<bean:write name="infoSiteTeacherInformation" property="lastModificationDate.time"/>
						</dt:format>
					</logic:present>
					<logic:notPresent name="infoSiteTeacherInformation" property="lastModificationDate"> 
						<bean:message key="label.teachersInformation.notModified" />
					</logic:notPresent>
				</td>
			</tr>
		</logic:iterate>
	</table>
</logic:present>