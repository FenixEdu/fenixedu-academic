<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt"%>
<bean:define id="datePattern" value="dd-MM-yyyy"/>

<em><bean:message key="label.teacherPortal"/></em>
<h2><bean:message key="title.teacherInformation"/></h2>


<logic:present name="infoSiteTeacherInformation"> 
	<html:form action="/teacherInformation">
		<logic:messagesPresent>
			<p>
				<span class="error0"><!-- Error messages go here -->
					<html:errors/>
				</span>
			</p>
		</logic:messagesPresent>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.teacherId" property="teacherId"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.weeklyOcupationId" property="weeklyOcupationId"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.serviceProviderRegimeId" property="serviceProviderRegimeId"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeOrientationId" property="degreeOrientationId"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.masterOrientationId" property="masterOrientationId"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.phdOrientationId" property="phdOrientationId"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.comunicationPublicationsNumberId" property="comunicationPublicationsNumberId"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.magArticlePublicationsNumberId" property="magArticlePublicationsNumberId"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.authorBookPublicationsNumberId" property="authorBookPublicationsNumberId"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.editorBookPublicationsNumberId" property="editorBookPublicationsNumberId"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.articlesChaptersPublicationsNumberId" property="articlesChaptersPublicationsNumberId"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="edit"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
		
		<div class="infoop2">
			<bean:message key="message.teacherInformation.warning"/>
		</div>

<table class="tstyle4" class="tstyle2 thlight">
	<tr>
		<td><bean:message key="message.teacherInformation.name" /></td>
		<td><bean:write name="infoSiteTeacherInformation" property="infoTeacher.infoPerson.nome" /></td>
	</tr>
	<tr>
		<td><bean:message key="message.teacherInformation.birthDate" /></td>
		<td><dt:format patternId="datePattern"><bean:write name="infoSiteTeacherInformation" property="infoTeacher.infoPerson.nascimento.time" /></dt:format></td>
	</tr>
	<tr>
		<td>
			<bean:message key="message.teacherInformation.category" />
		</td>
		<td>
			<logic:present name="infoSiteTeacherInformation" property="infoTeacher">
				<logic:present name="infoSiteTeacherInformation" property="infoTeacher.infoCategory">
					<bean:write name="infoSiteTeacherInformation" property="infoTeacher.infoCategory.shortName" />
				</logic:present>
			</logic:present>
		</td>
	</tr>
</table>

	
		<p class="infoop mtop15"><span class="emphasis-box">1</span>
		<bean:message key="message.teacherInformation.qualifications" /></p>		
		<logic:notEmpty name="infoSiteTeacherInformation" property="infoQualifications">
			<table class="tstyle4" class="tstyle4" width="100%" style="margin-top:10px">	
				<tr>
					<th><bean:message key="message.teacherInformation.year" bundle="GEP_RESOURCES" /></th>
					<th><bean:message key="message.teacherInformation.school" bundle="GEP_RESOURCES"/></th>
					<th><bean:message key="message.teacherInformation.qualificationsDegree" bundle="GEP_RESOURCES"/></th>
					<th><bean:message key="message.teacherInformation.qualificationsTitle" bundle="GEP_RESOURCES"/></th>
					<th><bean:message key="message.teacherInformation.qualificationsMark" bundle="GEP_RESOURCES"/></th>
				</tr>
				<logic:iterate id="infoQualification" name="infoSiteTeacherInformation" property="infoQualifications">
					<tr>
						<td>				
							<logic:notEmpty name="infoQualification" property="year">
								<bean:write name="infoQualification" property="year" />							
							</logic:notEmpty>			 								
							<logic:empty name="infoQualification" property="year">
								<logic:notEmpty name="infoQualification" property="date">
									<dt:format pattern="yyyy">
										<bean:write name="infoQualification" property="date.time" />							
									</dt:format>
								</logic:notEmpty>
							</logic:empty>
							<logic:empty name="infoQualification" property="year">
								<logic:empty name="infoQualification" property="date">
									--
								</logic:empty>
							</logic:empty>
						</td>												
						<td><bean:write name="infoQualification" property="school" /></td>
						<td><bean:write name="infoQualification" property="degree" /></td>
						<logic:notEmpty name="infoQualification" property="type">												
							<bean:define id="qualificationType" name="infoQualification" property="type.name" />
							<td><bean:message name="qualificationType" bundle="ENUMERATION_RESOURCES"/></td>
						</logic:notEmpty>
						<logic:empty name="infoQualification" property="type">																			
							<td><bean:write name="infoQualification" property="title"/></td>
						</logic:empty>	
						<logic:present name="infoQualification" property="mark">												
							<td>&nbsp;<bean:write name="infoQualification" property="mark" /></td>																
						</logic:present>
						<logic:notPresent name="infoQualification" property="mark">												
							<td>--</td>																
						</logic:notPresent>
					</tr>
				</logic:iterate>
			</table>
		</logic:notEmpty>
		<logic:empty name="infoSiteTeacherInformation" property="infoQualifications">
			<br/>&nbsp;<br/>
			<bean:message key="label.teachersInformation.notModified" />
			<br/>&nbsp;<br/>			
		</logic:empty>
		<br />
		<p class="infoop mtop15"><span class="emphasis-box">2</span>
		<bean:message key="message.teacherInformation.teachingCareer" /></p>
		<div class="gen-button">
			<html:link page="<%= "/readCareers.do?careerType=" + net.sourceforge.fenixedu.domain.CareerType.TEACHING.toString() + "&amp;page=0" %>">
				<bean:message key="label.teacherInformation.manage" />
			</html:link>
		</div>
		<logic:notEmpty name="infoSiteTeacherInformation" property="infoTeachingCareers">
			<table class="tstyle4" width="100%" style="margin-top:10px">	
				<tr>
					<th><bean:message key="message.teacherInformation.years" bundle="GEP_RESOURCES" /></th>
					<th><bean:message key="message.teacherInformation.careerCategory" bundle="GEP_RESOURCES" /></th>
					<th><bean:message key="message.teacherInformation.careerPositions" bundle="GEP_RESOURCES" /></th>
				</tr>
				<logic:iterate id="infoTeachingCareer" name="infoSiteTeacherInformation" property="infoTeachingCareers">
				<tr>
					<td><bean:write name="infoTeachingCareer" property="beginYear"/>
						-<bean:write name="infoTeachingCareer" property="endYear"/>
					</td>
					<td>
						&nbsp;
						<logic:notEmpty name="infoTeachingCareer" property="infoCategory">
							<bean:write name="infoTeachingCareer" property="infoCategory.shortName" />
						</logic:notEmpty>
					</td>
					<td><bean:write name="infoTeachingCareer" property="courseOrPosition" /></td>
				</tr>
				</logic:iterate>
			</table>
		</logic:notEmpty>
		<logic:empty name="infoSiteTeacherInformation" property="infoTeachingCareers">
			<br/>&nbsp;<br/>
			<bean:message key="label.teachersInformation.notModified" />
			<br/>&nbsp;<br/>
		</logic:empty>
		<br />
		<p class="infoop mtop15"><span class="emphasis-box">3</span>
		<bean:message key="message.teacherInformation.professionalCareer" /></p>
		<div class="gen-button">
			<html:link page="<%= "/readCareers.do?careerType=" + net.sourceforge.fenixedu.domain.CareerType.PROFESSIONAL.toString() + "&amp;page=0" %>">
				<bean:message key="label.teacherInformation.manage" />
			</html:link>
		</div>
		<logic:notEmpty name="infoSiteTeacherInformation" property="infoProfessionalCareers">
			<table class="tstyle4" width="100%"  style="margin-top:10px">	
				<tr>
					<th><bean:message key="message.teacherInformation.years" bundle="GEP_RESOURCES"/></th>
					<th><bean:message key="message.teacherInformation.entity" bundle="GEP_RESOURCES"/></th>
					<th><bean:message key="message.teacherInformation.functions" bundle="GEP_RESOURCES"/></th>
				</tr>
				<logic:iterate id="infoProfessionalCareer" 
							   name="infoSiteTeacherInformation" 
							   property="infoProfessionalCareers">
					<tr>
						<td>
							<bean:write name="infoProfessionalCareer" property="beginYear"/>
							-<bean:write name="infoProfessionalCareer" property="endYear"/>
						</td>
						<td><bean:write name="infoProfessionalCareer" property="entity" /></td>
						<td><bean:write name="infoProfessionalCareer" property="function" /></td>
					</tr>
				</logic:iterate>
			</table>
		</logic:notEmpty>
		<logic:empty name="infoSiteTeacherInformation" property="infoProfessionalCareers">
			<br/>&nbsp;<br/>
			<bean:message key="label.teachersInformation.notModified" />
			<br/>&nbsp;<br/>
		</logic:empty>
		<br />
		<p class="infoop mtop15"><span class="emphasis-box">4</span>
			<bean:message key="message.teacherInformation.externalActivities" />
			<bean:write name="infoSiteTeacherInformation" property="infoExecutionPeriod.infoExecutionYear.year" />
			<bean:message key="label.doublePoint" />
		</p>
		<div class="gen-button">
			<html:link page="/readExternalActivities.do">
				<bean:message key="label.teacherInformation.manage" />
			</html:link>
		</div>
		<logic:notEmpty name="infoSiteTeacherInformation" property="infoExternalActivities">
			<br/>
			<table class="tstyle4" width="100%">	
				<logic:iterate id="infoExternalActivity" name="infoSiteTeacherInformation" property="infoExternalActivities">
					<tr>
						<td style="text-align:left">
							<bean:write name="infoExternalActivity" property="activity" bundle="GEP_RESOURCES"/>
						</td>
					</tr>
				</logic:iterate>
			</table>
		</logic:notEmpty>
		<logic:empty name="infoSiteTeacherInformation" property="infoExternalActivities">
			<br/>&nbsp;<br/>
			<bean:message key="label.teachersInformation.notModified" />
			<br/>&nbsp;<br/>
		</logic:empty>
		<br />
		<!-- TJBF & PFON -->
		<p class="infoop mtop15"><span class="emphasis-box">5</span>
		<bean:message key="message.teacherInformation.ownPublications" />
		</p>
		<!-- DELETE (replace by resultpublications)  -->
		 <div class="gen-button">
			<html:link page="/readPublications.do?typePublication=Didatic&amp;page=0">
				<bean:message key="label.teacherInformation.manage" />
			</html:link>
		</div>
		<logic:notEmpty name="infoSiteTeacherInformation" property="infoDidaticPublications">
			<table class="tstyle4" width="100%"  style="margin-top:10px">	
				<logic:iterate id="infoPublicationDidatic" name="infoSiteTeacherInformation" property="infoDidaticPublications">
					<tr>
						<td style="text-align:left">
							<bean:write name="infoPublicationDidatic" property="publicationString" />
						</td>
					</tr>
				</logic:iterate>
			</table>
		</logic:notEmpty>
		<logic:empty name="infoSiteTeacherInformation" property="infoDidaticPublications">
			<br/>&nbsp;<br/>
			<bean:message key="label.teachersInformation.notModified" />
			<br/>&nbsp;<br/>
		</logic:empty>

		<!-- END DELETE  -->
		<!-- DIDATIC PUBLICATIONS (Sergio Patricio & Luis Santos)-->
		<%-- 
		<div class="gen-button">
			<html:link page="/resultTeacherManagement.do?method=readTeacherResults&amp;typeResult=Didatic">
				<bean:message key="label.teacherInformation.manage"/>
			</html:link>
		</div>
		<logic:notEmpty name="didaticResults">
			<table class="tstyle4" width="100%"  style="margin-top:10px">	
				<logic:iterate id="didaticResult" name="didaticResults">
					<tr>
						<td style="text-align:left">
							<bean:write name="didaticResult" property="resume"/>
						</td>
					</tr>
				</logic:iterate>
			</table>
		</logic:notEmpty>
		<logic:empty name="didaticResults">
			<br/>&nbsp;<br/>
			<bean:message key="label.teachersInformation.notModified" />
			<br/>&nbsp;<br/>
		</logic:empty>
		--%>
		<!--  END DIDATIC PUBLICATIONS -->
		<br />
		<p class="infoop mtop15"><span class="emphasis-box">6</span>
		<bean:message key="message.teacherInformation.cientificPublications" /></p>
		<!-- DELETE (replace by resultpublications)  -->
		
		<div class="gen-button">
			<html:link page="/readPublications.do?typePublication=Cientific&amp;page=0">
				<bean:message key="label.teacherInformation.manage" />
			</html:link>
		</div>
		<logic:notEmpty name="infoSiteTeacherInformation" property="infoCientificPublications">
			<table class="tstyle4" width="100%"  style="margin-top:10px">	
				<logic:iterate id="infoPublicationCientific" name="infoSiteTeacherInformation" property="infoCientificPublications">
					<tr>
						<td style="text-align:left">
							<bean:write name="infoPublicationCientific" property="publicationString" />
						</td>
					</tr>
				</logic:iterate>
			</table>
		</logic:notEmpty>
		<logic:empty name="infoSiteTeacherInformation" property="infoCientificPublications">
			<br/>&nbsp;<br/>
			<bean:message key="label.teachersInformation.notModified" />
			<br/>&nbsp;<br/>
		</logic:empty>
		
		<!-- END DELETE  -->
		<!-- CIENTIFIC PUBLICATIONS (Sergio Patricio & Luis Santos)-->
		<%-- 
		<div class="gen-button">
			<html:link page="/resultTeacherManagement.do?method=readTeacherResults&amp;typeResult=Cientific">
				<bean:message key="label.teacherInformation.manage"/>
			</html:link>
		</div>
		<logic:notEmpty name="cientificResults">
			<table class="tstyle4" width="100%"  style="margin-top:10px">	
				<logic:iterate id="cientificResult" name="cientificResults">
					<tr>
						<td style="text-align:left">
							<bean:write name="cientificResult" property="resume"/>
						</td>
					</tr>
				</logic:iterate>
			</table>
		</logic:notEmpty>
		<logic:empty name="cientificResults">
			<br/>&nbsp;<br/>
			<bean:message key="label.teachersInformation.notModified" />
			<br/>&nbsp;<br/>
		</logic:empty>
		--%>
		<!--  END CIENTIFIC PUBLICATIONS -->
		<br />
		<!-- TJBF & PFON -->
		<p class="infoop mtop15"><span class="emphasis-box">7</span>
			<bean:message key="message.teacherInformation.numberOfPublications" /></p>
		<table class="tstyle4" width="100%"  style="margin-top:10px">
			<tr>
				<th><bean:message key="message.teacherInformation.number" /></th>
				<th><bean:message key="message.teacherInformation.national" /></th>
				<th><bean:message key="message.teacherInformation.international" /></th>
			</tr>
			<tr>
				<td><bean:message key="message.teacherInformation.comunicationsPublications" /></td>
				<td class="acenter"><html:text bundle="HTMLALT_RESOURCES" altKey="text.comunicationNational" property="comunicationNational" /></td>
				<td class="acenter"><html:text bundle="HTMLALT_RESOURCES" altKey="text.comunicationInternational" property="comunicationInternational" /></td>
			</tr>
			<tr>
				<td><bean:message key="message.teacherInformation.articlesPublications" /></td>
				<td class="acenter"><html:text bundle="HTMLALT_RESOURCES" altKey="text.magArticleNational" property="magArticleNational" /></td>
				<td class="acenter"><html:text bundle="HTMLALT_RESOURCES" altKey="text.magArticleInternational" property="magArticleInternational" /></td>
			</tr>
			<tr>
				<td><bean:message key="message.teacherInformation.bookAuthorPublications" /></td>
				<td class="acenter"><html:text bundle="HTMLALT_RESOURCES" altKey="text.authorBookNational" property="authorBookNational" /></td>
				<td class="acenter"><html:text bundle="HTMLALT_RESOURCES" altKey="text.authorBookInternational" property="authorBookInternational" /></td>
			</tr>
			<tr>
				<td><bean:message key="message.teacherInformation.bookEditorPublications" /></td>
				<td class="acenter"><html:text bundle="HTMLALT_RESOURCES" altKey="text.editorBookNational" property="editorBookNational" /></td>
				<td class="acenter"><html:text bundle="HTMLALT_RESOURCES" altKey="text.editorBookInternational" property="editorBookInternational" /></td>
			</tr>
			<tr>
				<td><bean:message key="message.teacherInformation.articlesAndChaptersPublications" /></td>
				<td class="acenter"><html:text bundle="HTMLALT_RESOURCES" altKey="text.articlesChaptersNational" property="articlesChaptersNational" /></td>
				<td class="acenter"><html:text bundle="HTMLALT_RESOURCES" altKey="text.articlesChaptersInternational" property="articlesChaptersInternational" /></td>
			</tr>
		</table>
		<br />
		<p class="infoop mtop15"><span class="emphasis-box">8</span>
			<bean:message key="message.teacherInformation.serviceRegime" />
			<bean:write name="infoSiteTeacherInformation" property="infoExecutionPeriod.infoExecutionYear.year" />
			<bean:message key="label.doublePoint" />
		</p>
		<table class="tstyle4" width="100%" border="1" cellspacing="1">	
			<logic:iterate id="providerRegimeType" name="providerRegimeTypeList" scope="request">
				<tr>
					<td class="acenter"><html:radio bundle="HTMLALT_RESOURCES" altKey="radio.serviceProviderRegimeTypeName" property="serviceProviderRegimeTypeName"
					  		  idName="providerRegimeType"
							  value="name"/>
					</td>
					<td><bean:message name="providerRegimeType" property="name" bundle="ENUMERATION_RESOURCES"/></td>
				</tr>
			</logic:iterate>			
		</table>
		<br />
		<p class="infoop mtop15"><span class="emphasis-box">9</span>
			<bean:message key="message.teacherInformation.lectureCourses" />
			<bean:write name="infoSiteTeacherInformation" property="infoExecutionPeriod.infoExecutionYear.year" />
			<bean:message key="label.doublePoint" />
		</p>
		<logic:notEmpty name="infoSiteTeacherInformation" property="infoLecturingExecutionCourses">
			<table class="tstyle4" width="100%"  style="margin-top:10px">
				<tr>
					<th><bean:message key="message.teacherInformation.semester" /></th>
					<th><bean:message key="message.teacherInformation.lectureCourse" /></th>
					<th><bean:message key="message.teacherInformation.qualificationsDegree" /></th>
					<th><bean:message key="message.teacherInformation.typeOfClass" /></th>
					<th><bean:message key="message.teacherInformation.numberOfClass" /></th>
					<th><bean:message key="message.teacherInformation.numberOfWeeklyHours" /></th>
				</tr>
				<logic:iterate id="infoExecutionCourse" name="infoSiteTeacherInformation" property="infoLecturingExecutionCourses">
					<logic:iterate id="infoCurricularCourse" name="infoExecutionCourse" property="associatedInfoCurricularCourses">
						<tr>
								<td>
									<bean:write name="infoExecutionCourse" 
												property="infoExecutionPeriod.semester" />
								</td>
								<td><bean:write name="infoExecutionCourse" property="nome" /></td>
								<td>
									<bean:write name="infoCurricularCourse" 
												property="infoDegreeCurricularPlan.infoDegree.nome" />
								</td>
								<td>&nbsp;<%--<bean:write name="infoExecutionCourse" property="" />--%></td>
								<td>&nbsp;<%--<bean:write name="infoExecutionCourse" property="" />--%></td>
								<td>&nbsp;<%--<bean:write name="infoExecutionCourse" property="" />--%></td>
						</tr>
					</logic:iterate>
				</logic:iterate>
			</table>
		</logic:notEmpty>
		<logic:empty name="infoSiteTeacherInformation" property="infoLecturingExecutionCourses">
			<br/>&nbsp;<br/>
			<bean:message key="message.teacherInformation.noExecutionCourseLectured" />
			<br/>&nbsp;<br/>
		</logic:empty>
		<br />
		<p class="infoop mtop15"><span class="emphasis-box">10</span>
			<bean:message key="message.teacherInformation.orientations" />
			<bean:write name="infoSiteTeacherInformation" property="infoExecutionPeriod.infoExecutionYear.year" />
			<bean:message key="label.doublePoint" />
		</p>
		<table class="tstyle4" width="100%"  style="margin-top:10px">
			<tr>
				<th>&nbsp;</th>
				<th><bean:message key="message.teacherInformation.numberOfStudents" /></th>
				<th><bean:message key="message.teacherInformation.description" /></th>
			</tr>
			<tr>
				<td><bean:message key="message.teacherInformation.tfc" /></td>
				<td class="acenter"><html:text bundle="HTMLALT_RESOURCES" altKey="text.degreeStudentsNumber" property="degreeStudentsNumber" /></td>
				<td class="acenter"><html:text bundle="HTMLALT_RESOURCES" altKey="text.degreeDescription" property="degreeDescription" /></td>
			</tr>
			<tr>
				<td><bean:message key="message.teacherInformation.masterThesis" /></td>
				<td class="acenter"><html:text bundle="HTMLALT_RESOURCES" altKey="text.masterStudentsNumber" property="masterStudentsNumber" /></td>
				<td class="acenter"><html:text bundle="HTMLALT_RESOURCES" altKey="text.masterDescription" property="masterDescription" /></td>
			</tr>
			<tr>
				<td><bean:message key="message.teacherInformation.phdThesis" /></td>
				<td class="acenter"><html:text bundle="HTMLALT_RESOURCES" altKey="text.phdStudentsNumber" property="phdStudentsNumber" /></td>
				<td class="acenter"><html:text bundle="HTMLALT_RESOURCES" altKey="text.phdDescription" property="phdDescription" /></td>
			</tr>
		</table>
		<br />
		<p class="infoop mtop15"><span class="emphasis-box">11</span>
			<bean:message key="message.teacherInformation.weeklySpendTime" />
			<bean:write name="infoSiteTeacherInformation" property="infoExecutionPeriod.infoExecutionYear.year" />
			<bean:message key="label.doublePoint" />
		</p>
		<table class="tstyle4" width="100%"  style="margin-top:10px">
		<tr>
			<th>&nbsp;</th>
			<th><bean:message key="message.teacherInformation.teachers" /></th>
			<th><bean:message key="message.teacherInformation.supportLessons" /></th>
			<th><bean:message key="message.teacherInformation.investigation" /></th>
			<th><bean:message key="message.teacherInformation.managementWorks" /></th>
			<th><bean:message key="message.teacherInformation.others" /></th>
		</tr>
		<tr>
			<td><bean:message key="message.teacherInformation.numberOfHours" /></td>	
			<td class="acenter"><html:text bundle="HTMLALT_RESOURCES" altKey="text.lecture" property="lecture" /></td>
			<td class="acenter"><html:text bundle="HTMLALT_RESOURCES" altKey="text.support" property="support" /></td>
			<td class="acenter"><html:text bundle="HTMLALT_RESOURCES" altKey="text.research" property="research" /></td>
			<td class="acenter"><html:text bundle="HTMLALT_RESOURCES" altKey="text.management" property="management" /></td>
			<td class="acenter"><html:text bundle="HTMLALT_RESOURCES" altKey="text.other" property="other" /></td>
		</tr>
		</table>
		<br />		
		<p class="infoop mtop15"><span class="emphasis-box">12</span>
			<bean:message key="message.teacherInformation.managerPosition" />
			<bean:write name="infoSiteTeacherInformation" property="infoExecutionPeriod.infoExecutionYear.year" />
			<bean:message key="label.doublePoint" />
		</p>
		<table class="tstyle4" width="100%" style="margin-top:10px">	
			<bean:define id="datePattern" value="dd-MM-yyyy"/>
			<logic:notEmpty name="infoSiteTeacherInformation" property="personFunctions">		
				<tr>
					<th style="text-align:left"><bean:message key="label.managementPosition.position" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th>
					<th style="text-align:left"><bean:message key="label.managementPosition.unit" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th>										
					<th width="10%"><bean:message key="label.managementPosition.start" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th>					
					<th width="10%"><bean:message key="label.managementPosition.end" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></th>
				</tr>
				<logic:iterate id="personFunction" name="infoSiteTeacherInformation" property="personFunctions">
					<tr>
						<td style="text-align:left">
							<bean:write name="personFunction" property="function.name"/>
						</td>
						<td style="text-align:left">
							<bean:write name="personFunction" property="function.unit.presentationNameWithParents"/>							
						</td>				
						<td>
							<bean:define id="beginDate" type="org.joda.time.YearMonthDay" name="personFunction" property="beginDate"/>
							<bean:define id="beginDateTime" ><%= beginDate.toDateTimeAtCurrentTime().toDate().getTime() %></bean:define>
							<dt:format patternId="datePattern">
								<bean:write name="beginDateTime"/>
							</dt:format>
						</td>
						<logic:notEmpty name="personFunction" property="endDate">
							<td>
								<bean:define id="endDate" type="org.joda.time.YearMonthDay" name="personFunction" property="endDate"/>
								<bean:define id="endDateTime" ><%= endDate.toDateTimeAtCurrentTime().toDate().getTime() %></bean:define>
								<dt:format patternId="datePattern">
									<bean:write name="endDateTime"/>
								</dt:format>
							</td>
						</logic:notEmpty>
						<logic:empty name="personFunction" property="endDate">
						-			
						</logic:empty>
					</tr>
				</logic:iterate>
			</logic:notEmpty>
			<logic:empty name="infoSiteTeacherInformation" property="personFunctions">
				<tr>
					<td colspan="4"> 
						<i><bean:message key="message.managementPositions.noRegists" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></i>						
					</td>
				</tr>
			</logic:empty>		
		</table>

		<p class="mtop2">
			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.confirm" styleClass="inputbutton" property="confirm">
				<bean:message key="button.confirm"/>
			</html:submit>
			<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset" styleClass="inputbutton">
				<bean:message key="label.clear"/>
			</html:reset>
		</p>
		
	</html:form>
</logic:present>