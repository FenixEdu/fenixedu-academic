<%@ page language="java" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt"%>
<logic:present name="infoSiteTeachersInformation">	
	<table width="100%" border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td align="center" class="infoselected">
				<p>
					<strong><bean:message key="title.gep.teachersInformationSelectedDegree"
										  bundle="GEP_RESOURCES"/>:</strong> 
					<bean:write name="infoExecutionDegree" property="infoDegreeCurricularPlan.infoDegree.nome"/>
					<br />
					<strong><bean:message key="title.gep.executionYear"
										  bundle="GEP_RESOURCES"/>:</strong>
					<bean:write name="infoExecutionDegree" property="infoExecutionYear.year"/>
				</p>			
			</td>
		</tr>
	</table>
	<h2>
		<bean:message key="title.gep.teachersInformation"
					  bundle="GEP_RESOURCES"/>
  			(<dt:format pattern="dd/MM/yyyy">
  				<dt:currentTime/>
  			</dt:format>)
  	</h2>
	<table width="100%" border="0" cellspacing="1" style="margin-top:10px">
		<tr>
			<td class="listClasses-header"><bean:message key="label.gep.teacher" 
														 bundle="GEP_RESOURCES"/></td>
			<td class="listClasses-header"><bean:message key="label.gep.teacher.number" 
														 bundle="GEP_RESOURCES"/></td>
			<td class="listClasses-header">
				<bean:message key="label.gep.teacher.category" bundle="GEP_RESOURCES"/>
			</td>
			<td class="listClasses-header"><bean:message key="label.gep.teachersInformation.associatedResponsibleCourses" 
														 bundle="GEP_RESOURCES"/></td>
			<td class="listClasses-header"><bean:message key="label.gep.teachersInformation.associatedLecturingCourses" 
														 bundle="GEP_RESOURCES"/></td>
			<td class="listClasses-header"><bean:message key="label.gep.teachersInformation.lastModificationDate" 
														 bundle="GEP_RESOURCES"/></td>
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
						<bean:message key="label.gep.teachersInformation.notModified" 
									  bundle="GEP_RESOURCES"/>
					</logic:notPresent>
				</td>
			</tr>
		</logic:iterate>
	</table>
	<br/>
	<h2><bean:message key="label.gep.statistics" bundle="GEP_RESOURCES"/>:</h2>
	<br/>
	<bean:size id="length" name="infoSiteTeachersInformation"/>
	<bean:message key="label.gep.numberOfTeachers" bundle="GEP_RESOURCES"/>:
	<bean:write name="length"/>
</logic:present>