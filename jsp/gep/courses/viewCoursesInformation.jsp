<%@ page language="java" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt"%>
<logic:present name="infoSiteCoursesInformation">
	<bean:define id="degreeCurricularPlanId" name="infoExecutionDegree" property="infoDegreeCurricularPlan.idInternal"/>
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
		<bean:message key="link.gep.executionCoursesInformation"
					  bundle="GEP_RESOURCES"/>
  			(<dt:format pattern="dd/MM/yyyy">
  				<dt:currentTime/>
  			</dt:format>)
  	</h2>
	<table width="100%" border="0" cellspacing="1" style="margin-top:10px">
		<tr>
			<td class="listClasses-header">
				<bean:message key="label.gep.degreeYear" bundle="GEP_RESOURCES"/>
			</td>
			<td class="listClasses-header">
				<bean:message key="label.gep.degreeSemester" bundle="GEP_RESOURCES"/>
			</td>
			<td class="listClasses-header">
				<bean:message key="label.gep.degreeName" bundle="GEP_RESOURCES"/>
			</td>
			<td class="listClasses-header">
				<bean:message key="label.gep.courseInformation.basic" bundle="GEP_RESOURCES"/>
			</td>
			<td class="listClasses-header">
				<bean:message key="label.gep.courseInformation.responsible" bundle="GEP_RESOURCES"/>
			</td>
			<td class="listClasses-header">
				<bean:message key="label.gep.courseInformation.professorships" bundle="GEP_RESOURCES"/>
			</td>
			<td class="listClasses-header">
				<bean:message key="label.gep.courseInformation.lastModificationDate" bundle="GEP_RESOURCES"/>
			</td>
		</tr>
		<logic:iterate id="infoSiteCourseInformation" name="infoSiteCoursesInformation">
			<logic:iterate id="infoCurricularCourse" name="infoSiteCourseInformation" property="infoCurricularCourses">
				<logic:equal name="infoCurricularCourse" 
  						 property="infoDegreeCurricularPlan.idInternal" 
  						 value="<%= degreeCurricularPlanId.toString() %>">
					<logic:iterate id="infoCurricularCourseScope" name="infoCurricularCourse" property="infoScopes">
						<tr>
							<td class="listClasses">&nbsp;
	   				         	 <bean:write name="infoCurricularCourseScope" property="infoCurricularSemester.infoCurricularYear.year"/>
				         	</td>
				         	<td class="listClasses">&nbsp;
	   				         	 <bean:write name="infoCurricularCourseScope" property="infoCurricularSemester.semester"/>
				         	</td>       	
							<td class="listClasses">&nbsp;
								<bean:define id="idInternal" name="infoExecutionDegree" property="idInternal"/>
								<html:link page='<%= "/readCourseInformation.do?executionDegreeId=" + idInternal %>'
									       paramId="executionCourseId" 
									       paramName="infoSiteCourseInformation"
									       paramProperty="infoExecutionCourse.idInternal">
			   					        <bean:write name="infoSiteCourseInformation" property="infoExecutionCourse.nome"/>-
					       		   		<bean:write name="infoSiteCourseInformation" property="infoExecutionCourse.sigla"/>
						      	</html:link>
					       	</td>
					       	<td class="listClasses">
					       		<logic:equal name="infoCurricularCourse" property="basic" value="true">
					       			<bean:message key="label.yes" bundle="GEP_RESOURCES"/>
				       			</logic:equal>
   								<logic:notEqual name="infoCurricularCourse" property="basic" value="true">
					       			<bean:message key="label.no" bundle="GEP_RESOURCES"/>
				       			</logic:notEqual>
				       		</td>
							<td class="listClasses">&nbsp;
								<logic:iterate id="infoTeacher" name="infoSiteCourseInformation" property="infoResponsibleTeachers">
									<bean:write name="infoTeacher" property="infoPerson.nome" />
									<br />
								</logic:iterate>
							</td>
							<td class="listClasses">&nbsp;
								<logic:iterate id="infoTeacher" name="infoSiteCourseInformation" property="infoLecturingTeachers">
									<bean:write name="infoTeacher" property="infoPerson.nome" />
									<br />
								</logic:iterate>
							</td>
							<td class="listClasses">&nbsp;
								<logic:present name="infoSiteCourseInformation" property="lastModificationDate"> 
									<dt:format pattern="dd/MM/yyyy HH:mm">
										<bean:write name="infoSiteCourseInformation" property="lastModificationDate.time"/>
									</dt:format>
								</logic:present>
								<logic:notPresent name="infoSiteCourseInformation" property="lastModificationDate"> 
									<bean:message key="label.gep.courseInformation.notModified" 
												  bundle="GEP_RESOURCES"/>
								</logic:notPresent>
							</td>
						</tr>
					</logic:iterate>
				</logic:equal>
			</logic:iterate>
		</logic:iterate>
	</table>
</logic:present>