<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt"%>
<%@ page import="java.util.Calendar" %>
<logic:present name="infoSiteTeachersInformation">
	<logic:iterate id="infoSiteTeacherInformation" name="infoSiteTeachersInformation">
		<h2 class="break-before"><bean:message key="label.acred.teacherInfo" bundle="GEP_RESOURCES"/></h2>
		<logic:present name="infoExecutionDegree">
			<h2>
				<bean:message key="label.acred.degree" bundle="GEP_RESOURCES"/>
				<bean:write name="infoExecutionDegree" property="infoDegreeCurricularPlan.infoDegree.nome"/>
			</h2>
		</logic:present>
		<table class="infoselected" width="100%">
			<tr>
				<td width="70%"><bean:message key="label.acred.teacherInformation.name" bundle="GEP_RESOURCES"/>
					&nbsp;<bean:write name="infoSiteTeacherInformation" property="infoTeacher.infoPerson.nome" /> </td> 
				<td width="30%"><bean:message key="label.acred.teacherInformation.birthDate" bundle="GEP_RESOURCES"/>
					&nbsp;<bean:write name="infoSiteTeacherInformation" property="infoTeacher.infoPerson.nascimento" /> </td>	
			</tr>
			<tr>
				<td><bean:message key="message.teacherInformation.category" bundle="GEP_RESOURCES"/>
					&nbsp;<bean:write name="infoSiteTeacherInformation" property="infoTeacher.infoCategory.shortName" /></td>
			</tr>
		</table>
		<br />
		<p class="infoop"><span class="emphasis-box">1</span>
		<bean:message key="message.teacherInformation.qualifications" bundle="GEP_RESOURCES"/></p>
		<logic:notEmpty name="infoSiteTeacherInformation" property="infoQualifications">
			<table width="100%" border="0" cellspacing="1" style="margin-top:10px">	
				<tr>
					<th class="listClasses-header"><bean:message key="message.teacherInformation.year" bundle="GEP_RESOURCES" /></th>
					<th class="listClasses-header"><bean:message key="message.teacherInformation.school" bundle="GEP_RESOURCES"/></th>
					<th class="listClasses-header"><bean:message key="message.teacherInformation.qualificationsDegree" bundle="GEP_RESOURCES"/></th>
					<th class="listClasses-header"><bean:message key="message.teacherInformation.qualificationsTitle" bundle="GEP_RESOURCES"/></th>
					<th class="listClasses-header"><bean:message key="message.teacherInformation.qualificationsMark" bundle="GEP_RESOURCES"/></th>
				</tr>
				<logic:iterate id="infoQualification" name="infoSiteTeacherInformation" property="infoQualifications">
					<tr>
						<td class="listClasses">
							<logic:present name="infoQualification" property="date">
		 							<dt:format pattern="yyyy">
										<bean:write name="infoQualification" property="date.time" />	
									</dt:format>
							</logic:present>
							<logic:notPresent name="infoQualification" property="date">-</logic:notPresent>
						</td>
						<td class="listClasses"><bean:write name="infoQualification" property="school" /></td>
						<td class="listClasses"><bean:write name="infoQualification" property="degree" /></td>
						<td class="listClasses"><bean:write name="infoQualification" property="title" /></td>
						<td class="listClasses">&nbsp;<bean:write name="infoQualification" property="mark" /></td>
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
		<p class="infoop"><span class="emphasis-box">2</span>
		<bean:message key="message.teacherInformation.teachingCareer" /></p>
		<logic:notEmpty name="infoSiteTeacherInformation" property="infoTeachingCareers">
			<table width="100%" border="0" cellspacing="1" style="margin-top:10px">	
				<tr>
					<th class="listClasses-header"><bean:message key="message.teacherInformation.years" bundle="GEP_RESOURCES" /></th>
					<th class="listClasses-header"><bean:message key="message.teacherInformation.careerCategory" bundle="GEP_RESOURCES" /></th>
					<th class="listClasses-header"><bean:message key="message.teacherInformation.careerPositions" bundle="GEP_RESOURCES" /></th>
				</tr>
				<logic:iterate id="infoTeachingCareer" name="infoSiteTeacherInformation" property="infoTeachingCareers">
				<tr>
					<td class="listClasses"><bean:write name="infoTeachingCareer" property="beginYear"/>
						-<bean:write name="infoTeachingCareer" property="endYear"/>
					</td>
					<td class="listClasses">
						&nbsp;
						<logic:notEmpty name="infoTeachingCareer" property="infoCategory">
							<bean:write name="infoTeachingCareer" property="infoCategory.shortName" />
						</logic:notEmpty>
					</td>
					<td class="listClasses"><bean:write name="infoTeachingCareer" property="courseOrPosition" /></td>
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
		<p class="infoop"><span class="emphasis-box">3</span>
		<bean:message key="message.teacherInformation.professionalCareer" /></p>
		<logic:notEmpty name="infoSiteTeacherInformation" property="infoProfessionalCareers">
			<table width="100%" border="0" cellspacing="1" style="margin-top:10px">	
				<tr>
					<th class="listClasses-header"><bean:message key="message.teacherInformation.years" bundle="GEP_RESOURCES"/></th>
					<th class="listClasses-header"><bean:message key="message.teacherInformation.entity" bundle="GEP_RESOURCES"/></th>
					<th class="listClasses-header"><bean:message key="message.teacherInformation.functions" bundle="GEP_RESOURCES"/></th>
				</tr>
				<logic:iterate id="infoProfessionalCareer" 
							   name="infoSiteTeacherInformation" 
							   property="infoProfessionalCareers">
					<tr>
						<td class="listClasses">
							<bean:write name="infoProfessionalCareer" property="beginYear"/>
							-<bean:write name="infoProfessionalCareer" property="endYear"/>
						</td>
						<td class="listClasses"><bean:write name="infoProfessionalCareer" property="entity" /></td>
						<td class="listClasses"><bean:write name="infoProfessionalCareer" property="function" /></td>
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
		<p class="infoop"><span class="emphasis-box">4</span>
		<bean:message key="message.teacherInformation.serviceRegime" bundle="GEP_RESOURCES" /></p>
		<logic:present name="infoSiteTeacherInformation" property="infoServiceProviderRegime.providerRegimeType">
			<table width="100%" border="0" cellspacing="1" style="margin-top:10px">	
				<tr>
					<td>
						<bean:message name="infoSiteTeacherInformation" 
									  property="infoServiceProviderRegime.providerRegimeType.name" 
									  bundle="ENUMERATION_RESOURCES"/>
					</td>
				</tr>
			</table>
		</logic:present>
		<logic:notPresent name="infoSiteTeacherInformation" property="infoServiceProviderRegime.providerRegimeType">
			<br/>&nbsp;<br/>
			<bean:message key="label.teachersInformation.notModified" />
			<br/>&nbsp;<br/>
		</logic:notPresent>
		<br />
		<p class="infoop"><span class="emphasis-box">5</span>
			<bean:message key="message.teacherInformation.externalActivities" bundle="GEP_RESOURCES"/></p>
		<logic:notEmpty name="infoSiteTeacherInformation" property="infoExternalActivities">
			<br/>
			<table width="100%">	
				<logic:iterate id="infoExternalActivity" name="infoSiteTeacherInformation" property="infoExternalActivities">
					<tr>
						<td class="listClasses" style="text-align:left">
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
		<p class="infoop"><span class="emphasis-box">6</span>
				<bean:message key="message.teacherInformation.numberOfPublications" bundle="GEP_RESOURCES"/></p>
		<table width="100%" border="0" cellspacing="1" style="margin-top:10px">
			<tr>
				<th class="listClasses-header"><bean:message key="message.teacherInformation.number" bundle="GEP_RESOURCES"/></th>
				<th class="listClasses-header"><bean:message key="message.teacherInformation.national" bundle="GEP_RESOURCES"/></th>
				<th class="listClasses-header"><bean:message key="message.teacherInformation.international" bundle="GEP_RESOURCES"/></th>
			</tr>
			<tr>
				<td class="listClasses"><bean:message key="message.teacherInformation.comunicationsPublications" bundle="GEP_RESOURCES"/></td>
				<td class="listClasses">&nbsp;
					<bean:write name="infoSiteTeacherInformation" property="infoComunicationPublicationsNumber.national" />
				</td>
				<td class="listClasses">&nbsp;
					<bean:write name="infoSiteTeacherInformation" property="infoComunicationPublicationsNumber.international" />
				</td>
			</tr>
			<tr>
				<td class="listClasses"><bean:message key="message.teacherInformation.articlesPublications" bundle="GEP_RESOURCES"/></td>
				<td class="listClasses">&nbsp;
					<bean:write name="infoSiteTeacherInformation" property="infoMagArticlePublicationsNumber.national" />
				</td>
				<td class="listClasses">&nbsp;
					<bean:write name="infoSiteTeacherInformation" property="infoMagArticlePublicationsNumber.international" />
				</td>
			</tr>
			<tr>
				<td class="listClasses"><bean:message key="message.teacherInformation.bookAuthorPublications" bundle="GEP_RESOURCES"/></td>
				<td class="listClasses">&nbsp;
					<bean:write name="infoSiteTeacherInformation" property="infoAuthorBookPublicationsNumber.national" />
				</td>
				<td class="listClasses">&nbsp;
					<bean:write name="infoSiteTeacherInformation" property="infoAuthorBookPublicationsNumber.international" />
				</td>
			</tr>
			<tr>
				<td class="listClasses"><bean:message key="message.teacherInformation.bookEditorPublications" bundle="GEP_RESOURCES"/></td>
				<td class="listClasses">&nbsp;
					<bean:write name="infoSiteTeacherInformation" property="infoEditBookPublicationsNumber.national" />
				</td>
				<td class="listClasses">&nbsp;
					<bean:write name="infoSiteTeacherInformation" property="infoEditBookPublicationsNumber.international" />
				</td>
			</tr>
			<tr>
				<td class="listClasses"><bean:message key="message.teacherInformation.articlesAndChaptersPublications" bundle="GEP_RESOURCES"/></td>
				<td class="listClasses">&nbsp;
					<bean:write name="infoSiteTeacherInformation" property="infoArticleChapterPublicationsNumber.national" />
				</td>
				<td class="listClasses">&nbsp;
					<bean:write name="infoSiteTeacherInformation" property="infoArticleChapterPublicationsNumber.international" />
				</td>
			</tr>
		</table>
		<br/>
		<p class="infoop">
			<span class="emphasis-box">7</span>
			<bean:message key="message.teacherInformation.ownPublications" />
		</p>
		
		<logic:notEmpty name="infoSiteTeacherInformation" property="infoDidaticPublications">
			<table width="100%" border="0" cellspacing="1" style="margin-top:10px">	
				<logic:iterate id="infoPublicationDidatic" name="infoSiteTeacherInformation" property="infoDidaticPublications">
					<tr>
						<td class="listClasses" style="text-align:left">
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
		
		<br />
		<p class="infoop"><span class="emphasis-box">8</span>
			<bean:message key="message.teacherInformation.cientificPublications" />
		</p>
		<logic:notEmpty name="infoSiteTeacherInformation" property="infoCientificPublications">
			<table width="100%" border="0" cellspacing="1" style="margin-top:10px">	
				<logic:iterate id="infoPublicationCientific" name="infoSiteTeacherInformation" property="infoCientificPublications">
					<tr>
						<td class="listClasses" style="text-align:left">
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
		<br />
		<br />
	</logic:iterate>
 	<% String today = Calendar.getInstance().getTime().toGMTString(); %>
	<p align="right"><%=today%></p>
</logic:present>
