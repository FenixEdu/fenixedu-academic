<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt"%>
<logic:present name="infoSiteCoursesInformation">
	<logic:present name="infoExecutionDegree">
		<bean:define id="degreeCurricularPlanId" name="infoExecutionDegree" property="infoDegreeCurricularPlan.idInternal"/>
		<table width="90%" border="0" cellpadding="0" cellspacing="0">
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
	</logic:present>
	<br />
	<logic:present name="infoExecutionDegree">
		<logic:present name="basic">
			<div class="button">
				<html:link page="/listCoursesInformation.do?method=doSearch&amp;basic=basic" target="_blank"
						   paramId="executionDegreeId" 
						   paramName="infoExecutionDegree" 
						   paramProperty="idInternal">
					<bean:message key="label.list" bundle="GEP_RESOURCES" />
				</html:link>
			</div>
		</logic:present>
		<logic:notPresent name="basic">
			<div class="button">
				<html:link page="/listCoursesInformation.do?method=doSearch" target="_blank"
						   paramId="executionDegreeId" 
						   paramName="infoExecutionDegree" 
						   paramProperty="idInternal">
					<bean:message key="label.list" bundle="GEP_RESOURCES"/>
				</html:link>
			</div>
		</logic:notPresent>
	</logic:present>
	<h2>
		<bean:message key="link.gep.executionCoursesInformation"
					  bundle="GEP_RESOURCES"/>
  			(<dt:format pattern="dd/MM/yyyy">
  				<dt:currentTime/>
  			</dt:format>)
  	</h2>
	<table width="90%" border="0" cellspacing="1" style="margin-top:10px">
		<tr>
			<td class="listClasses-header">
				<bean:message key="label.gep.courseYear" bundle="GEP_RESOURCES"/><br />
				<bean:message key="label.gep.courseSemester" bundle="GEP_RESOURCES"/><br />
				<bean:message key="label.gep.branch" bundle="GEP_RESOURCES"/>
			</td>
			<td class="listClasses-header">
				<bean:message key="label.gep.courseName" bundle="GEP_RESOURCES"/>
			</td>
			<td class="listClasses-header">
				<bean:message key="label.gep.code" bundle="GEP_RESOURCES"/>
			</td>
			<td class="listClasses-header">
				<bean:message key="label.gep.executionPeriod" bundle="GEP_RESOURCES"/>
			</td>
			<td class="listClasses-header">
				<bean:message key="label.gep.courseInformation.basic" bundle="GEP_RESOURCES"/>
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
				<logic:present name="infoExecutionDegree">
					<bean:define id="degreeCurricularPlanId" name="infoExecutionDegree" property="infoDegreeCurricularPlan.idInternal"/>
					<logic:equal name="infoCurricularCourse" 
	  						 property="infoDegreeCurricularPlan.idInternal" 
	  						 value="<%= degreeCurricularPlanId.toString() %>">
						<tr>
							<td class="listClasses">
								<table>
									<logic:iterate id="infoCurricularCourseScope" name="infoCurricularCourse" property="infoScopes">
										<tr>
			   				         	 	<td><bean:write name="infoCurricularCourseScope" property="infoCurricularSemester.infoCurricularYear.year"/></td>
			   				         	 	<td><bean:write name="infoCurricularCourseScope" property="infoCurricularSemester.semester"/></td>
		   				         	 	 	<td><bean:write name="infoCurricularCourseScope" property="infoBranch.acronym"/>&nbsp;</td>
	   				         	 	 	</tr>
		   				         	 </logic:iterate>
	   				         	 </table>
   				         	</td>
   				         	<td class="listClasses" >&nbsp;
								<bean:define id="idInternal" name="infoExecutionDegree" property="idInternal"/>
								<html:link page='<%= "/readCourseInformation.do?executionDegreeId=" + idInternal %>'
									       paramId="executionCourseId" 
									       paramName="infoSiteCourseInformation"
									       paramProperty="infoExecutionCourse.idInternal">
			   					        <bean:write name="infoCurricularCourse" property="name"/>-
					       		   		<bean:write name="infoCurricularCourse" property="code"/>
						      	</html:link>
			       			</td>
			       			<td class="listClasses">&nbsp;
		   				   		<bean:write name="infoSiteCourseInformation" property="infoExecutionCourse.sigla"/>
				         	</td>
				         	<td class="listClasses" >&nbsp;
   				        		<bean:write name="infoSiteCourseInformation" property="infoExecutionCourse.infoExecutionPeriod.name"/>
			         		</td>
			         		<td class="listClasses" >
					       		<logic:equal name="infoCurricularCourse" property="basic" value="true">
					       			<bean:message key="label.yes" bundle="GEP_RESOURCES"/>
				       			</logic:equal>
	   							<logic:notEqual name="infoCurricularCourse" property="basic" value="true">
					       			<bean:message key="label.no" bundle="GEP_RESOURCES"/>
				       			</logic:notEqual>
				       		</td>
							<td class="listClasses" >&nbsp;
								<ul>
								<logic:iterate id="infoTeacher" name="infoSiteCourseInformation" property="infoLecturingTeachers">
									<li><bean:write name="infoTeacher" property="infoPerson.nome" />
									 <% if (
								    	 ((DataBeans.gesdis.InfoSiteCourseInformation)infoSiteCourseInformation).getInfoResponsibleTeachers().contains(infoTeacher)
								    	 ){%>
								    	 (<bean:message key="label.gep.responsible" bundle="GEP_RESOURCES"/>) <% }  %> 
									</li>
								</logic:iterate>
								</ul>
							</td>
							<td class="listClasses" >&nbsp;
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
		         	</logic:equal>
	         	</logic:present>
	         	<logic:notPresent name="infoExecutionDegree">
					<tr>
	         			<td class="listClasses">
	         				<table>
								<logic:iterate id="infoCurricularCourseScope" name="infoCurricularCourse" property="infoScopes">
									<tr>
		   				         	 	<td><bean:write name="infoCurricularCourseScope" property="infoCurricularSemester.infoCurricularYear.year"/></td>
		   				         	 	<td><bean:write name="infoCurricularCourseScope" property="infoCurricularSemester.semester"/></td>
	   				         	 	 	<td><bean:write name="infoCurricularCourseScope" property="infoBranch.acronym"/>&nbsp;</td>
   				         	 	 	</tr>
	   				         	 </logic:iterate>
   				         	 </table>
   				        </td>
   				        <td class="listClasses" >&nbsp;
							<html:link page="/readCourseInformation.do"
								       paramId="executionCourseId" 
								       paramName="infoSiteCourseInformation"
								       paramProperty="infoExecutionCourse.idInternal">
		   					        <bean:write name="infoSiteCourseInformation" property="infoExecutionCourse.nome"/>-
				       		   		<bean:write name="infoSiteCourseInformation" property="infoExecutionCourse.sigla"/>
					      	</html:link>
		       			</td>
		       			<td class="listClasses">&nbsp;
	   				   		<bean:write name="infoCurricularCourse" property="code"/>
			         	</td>
			         	<td class="listClasses" >&nbsp;
   				        		<bean:write name="infoSiteCourseInformation" property="infoExecutionCourse.infoExecutionPeriod.name"/>
		         		</td>
		         		<td class="listClasses" >
				       		<logic:equal name="infoCurricularCourse" property="basic" value="true">
				       			<bean:message key="label.yes" bundle="GEP_RESOURCES"/>
			       			</logic:equal>
	   							<logic:notEqual name="infoCurricularCourse" property="basic" value="true">
				       			<bean:message key="label.no" bundle="GEP_RESOURCES"/>
			       			</logic:notEqual>
			       		</td>
						<td class="listClasses" >&nbsp;
							<logic:iterate id="infoTeacher" name="infoSiteCourseInformation" property="infoResponsibleTeachers">
								<bean:write name="infoTeacher" property="infoPerson.nome" />
								<br />
							</logic:iterate>
						</td>
						<td class="listClasses" >&nbsp;
							<logic:iterate id="infoTeacher" name="infoSiteCourseInformation" property="infoLecturingTeachers">
								<bean:write name="infoTeacher" property="infoPerson.nome" />
								<br />
							</logic:iterate>
						</td>
						<td class="listClasses" >&nbsp;
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
	         	</logic:notPresent>
         	</logic:iterate>         	
 	  	</logic:iterate>
	</table>
	<br/>
	<h2><bean:message key="label.gep.statistics" bundle="GEP_RESOURCES"/>:</h2>
	<bean:size id="length" name="infoSiteCoursesInformation"/>
	<bean:message key="label.gep.numberOfCourses" bundle="GEP_RESOURCES"/>:
	<bean:write name="length"/>
	<% int filled = 0; %>
	<logic:iterate id="infoSiteCourseInformation" name="infoSiteCoursesInformation">
		<logic:present name="infoSiteCourseInformation" property="lastModificationDate">
			<% filled++; %>
		</logic:present>
	</logic:iterate>
	<% int stats = (int) (((double) filled / length.doubleValue()) * 100); %>
	<br />
	<bean:message key="label.gep.filled" bundle="GEP_RESOURCES"/>: <%= filled %>
	<br/>
	<%--<bean:message key="label.gep.stats" bundle="GEP_RESOURCES"/>: <%= stats %>%--%>
	<br />
	<br />
	<logic:present name="infoExecutionDegree">
		<logic:present name="basic">
			<div class="button">
				<html:link page="/listCoursesInformation.do?method=doSearch&amp;basic=basic" target="_blank"
						   paramId="executionDegreeId" 
						   paramName="infoExecutionDegree" 
						   paramProperty="idInternal">
					<bean:message key="label.list" bundle="GEP_RESOURCES" />
				</html:link>
			</div>
		</logic:present>
		<logic:notPresent name="basic">
			<div class="button">
				<html:link page="/listCoursesInformation.do?method=doSearch" target="_blank"
						   paramId="executionDegreeId" 
						   paramName="infoExecutionDegree" 
						   paramProperty="idInternal">
					<bean:message key="label.list" bundle="GEP_RESOURCES"/>
				</html:link>
			</div>
		</logic:notPresent>
	</logic:present>
</logic:present>