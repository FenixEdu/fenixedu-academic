<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/string-1.0.1" prefix="str"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@page import="net.sourceforge.fenixedu.domain.Person"%>
<%@page import="net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice" %>
<%@page import="net.sourceforge.fenixedu.presentationTier.docs.academicAdministrativeOffice.DegreeFinalizationCertificate"%>
<%@page import="pt.utl.ist.fenix.tools.util.i18n.Language"%>
<%@page import="org.joda.time.LocalDate"%>

<html:xhtml />

<bean:define id="registration" name="registration" type="net.sourceforge.fenixedu.domain.student.Registration"/>
<bean:define id="registrationConclusionBean" name="registrationConclusionBean" type="net.sourceforge.fenixedu.dataTransferObject.student.RegistrationConclusionBean"/>

<table width="90%">
	<tr>
		<td align="center">
		<h2><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="conclusion.document"/></h2>
		<br />
		<br />
		<br />
		</td>
	<tr>
		<td><b><bean:write name="degreePresentationName"/></b></td>
	<tr>
		<td><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="conclusion.document.the.student"/> <b><bean:write name="registration" property="person.name"/></b><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="conclusion.document.on.registration.number"/><b><bean:write name="registration" property="number"/></b></td>
	<tr>
		<td>
			<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="conclusion.document.concluded.lowercase"/>
			<logic:equal name="registration" property="degreeType.administrativeOfficeType.qualifiedName" value="AdministrativeOfficeType.MASTER_DEGREE"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="conclusion.document.scholarship.of"/></logic:equal><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="conclusion.document.degree.of"/>
			<bean:write name="registration" property="degreeType.localizedName"/> 
			<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="conclusion.document.metioned.above.with.following.units"/>:
			<br/>
			<br/>
		</td>
	</tr>
</table>


<logic:equal name="registrationConclusionBean" property="curriculumForConclusion.studentCurricularPlan.boxStructure" value="true">
	<fr:view name="registrationConclusionBean" property="curriculumForConclusion">
		<fr:layout>
			<fr:property name="visibleCurricularYearEntries" value="false" />
		</fr:layout>
	</fr:view>
</logic:equal>

<logic:equal name="registrationConclusionBean" property="curriculumForConclusion.studentCurricularPlan.boxStructure" value="false">
	<bean:define id="curriculumEntries" name="registrationConclusionBean" property="curriculumForConclusion.curriculumEntries"/>
		<table class="scplan">
			<tr class="scplangroup">
				<td class=" scplancolcurricularcourse" rowspan="2" colspan="2">
					<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.curricular.course.from.curriculum"/>
				</th>
				<td class=" scplancolgrade"colspan="3">
					<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="degree.average"/>
				</th>
				<td class=" scplancolgrade">
					<bean:message key="curricular.year" bundle="ACADEMIC_OFFICE_RESOURCES"/>
				</td>
			</tr>
			<tr class="scplangroup">
				<td class=" scplancolgrade">
					<bean:message bundle="APPLICATION_RESOURCES" key="label.grade"/>
				</td>
				<td class=" scplancolgrade">
					<bean:message bundle="APPLICATION_RESOURCES" key="label.weight"/>
				</td>
				<td class=" scplancolgrade" style="width: 100px;">
					(<bean:message bundle="APPLICATION_RESOURCES" key="label.weight"/> x <bean:message bundle="APPLICATION_RESOURCES" key="label.grade"/>)
				</td>
				<td  class=" scplancolgrade" style="width: 100px;">
					<bean:message bundle="APPLICATION_RESOURCES" key="label.credits"/>
				</th>
			</tr>
			<logic:iterate id="curriculumEntry" name="curriculumEntries">
				<logic:equal name="curriculumEntry" property="class.name" value="net.sourceforge.fenixedu.domain.student.curriculum.NotInDegreeCurriculumCurriculumEntry">
					<tr class="scplanenrollment">
						<td><bean:write name="curriculumEntry" property="enrolment.curricularCourse.code"/></td>
						<td class=" scplancolcurricularcourse"><bean:write name="curriculumEntry" property="enrolment.curricularCourse.name"/></td>
						<td class=" scplancolgrade"><bean:write name="curriculumEntry" property="enrolment.latestEnrolmentEvaluation.gradeValue"/></td>						
						<td class=" scplancolweight"><bean:write name="curriculumEntry" property="weigthForCurriculum"/></td>
						<td class=" scplancolweight">
							<logic:empty name="curriculumEntry" property="weigthTimesGrade">
								-
							</logic:empty>
							<logic:notEmpty name="curriculumEntry" property="weigthTimesGrade">
								<bean:write name="curriculumEntry" property="weigthTimesGrade"/>
							</logic:notEmpty>
						</td>
						<td class=" scplancolects">
							<logic:empty name="curriculumEntry" property="ectsCreditsForCurriculum">
								-
							</logic:empty>
							<logic:notEmpty name="curriculumEntry" property="ectsCreditsForCurriculum">
								<bean:write name="curriculumEntry" property="ectsCreditsForCurriculum"/>
							</logic:notEmpty>
						</td>
					</tr>
				</logic:equal>
			</logic:iterate>				
			<logic:iterate id="curriculumEntry" name="curriculumEntries">
				<logic:equal name="curriculumEntry" property="class.name" value="net.sourceforge.fenixedu.domain.student.curriculum.GivenCreditsEntry">
					<tr class="scplanenrollment">
						<td class="acenter">-</td>
						<td class=" scplancolcurricularcourse"><bean:message bundle="APPLICATION_RESOURCES" key="label.givenCredits"/></td>
						<td class=" scplancolgrade">-</td>						
						<td class=" scplancolweight">-</td>
						<td class=" scplancolweight">-</td>
						<td class=" scplancolects">
							<logic:empty name="curriculumEntry" property="ectsCreditsForCurriculum">
								-
							</logic:empty>
							<logic:notEmpty name="curriculumEntry" property="ectsCreditsForCurriculum">
								<bean:write name="curriculumEntry" property="ectsCreditsForCurriculum"/>
							</logic:notEmpty>
						</td>
					</tr>
				</logic:equal>
			</logic:iterate>				
			<tr class="scplanenrollment">
				<td colspan="3" style="text-align: right;">
					Somatórios
				</td>
				<td class=" scplancolweight">
					<bean:write name="registrationConclusionBean" property="curriculumForConclusion.sumPi"/>
				</td>
				<td class=" scplancolweight">
					<bean:write name="registrationConclusionBean" property="curriculumForConclusion.sumPiCi"/>
				</td>
				<td class=" scplancolects">
					<bean:write name="registrationConclusionBean" property="curriculumForConclusion.sumEctsCredits"/>
				</td>
			</tr>
		</table>

</logic:equal>	

<%
	request.setAttribute("degreeFinalizationDate", registrationConclusionBean.getConclusionDate().toString("dd 'de' MMMM 'de' yyyy", Language.getLocale()));
	final Integer finalAverage = registrationConclusionBean.getFinalAverage();	
	request.setAttribute("finalAverage", finalAverage);
	request.setAttribute("degreeFinalizationGrade", DegreeFinalizationCertificate.getDegreeFinalizationGrade(finalAverage));
	request.setAttribute("degreeFinalizationEcts", String.valueOf(registrationConclusionBean.getEctsCredits()));
	request.setAttribute("creditsDescription", registration.getDegreeType().getCreditsDescription());
	
	AdministrativeOffice office = registration.getDegree().getAdministrativeOffice();
	final Person administrativeOfficeCoordinator = office.getUnit().getActiveUnitCoordinator();
	request.setAttribute("administrativeOfficeCoordinator", administrativeOfficeCoordinator);
	request.setAttribute("administrativeOfficeCoordinatorGender", administrativeOfficeCoordinator.isMale() ? "" : "a");
	request.setAttribute("administrativeOfficeName", office.getUnit().getName());
	request.setAttribute("day", new LocalDate().toString("dd 'de' MMMM 'de' yyyy", Language.getLocale()));
%>

<table class="apura-final mtop2" width="90%" cellspacing="0" border="0">
	<tr>
		<td colspan="6" style="color: #333; background: #ccc; padding: 5px; border-bottom: 1px solid #333;"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="conclusion.document.average.atribution"/></td>
	</tr>
	<tr>
		<td style="padding: 5px;"><bean:message bundle="APPLICATION_RESOURCES" key="label.net.sourceforge.fenixedu.dataTransferObject.student.RegistrationConclusionBean.average"/></td>
		<td style="padding: 5px;"><bean:write name="registrationConclusionBean" property="average"/></td>		
		
		<logic:equal name="registration" property="degreeType" value="MASTER_DEGREE">
			<td width="50%" style="padding: 5px; padding-left: 15em;"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="conclusion.document.degree.coordinator"/>,</td>
		</logic:equal>
	</tr>
	<tr>
		<td style="padding: 5px;"><bean:message bundle="APPLICATION_RESOURCES" key="label.net.sourceforge.fenixedu.dataTransferObject.student.RegistrationConclusionBean.registration.averageType"/></td>
		<bean:define id="averageType">
			<bean:write name="registrationConclusionBean" property="registration.averageType.qualifiedName"/>
		</bean:define>	
		<td style="padding: 5px;"><bean:message bundle="ENUMERATION_RESOURCES" key="<%=averageType%>"/></td>
	</tr>
	<tr>
		<td style="padding: 5px;"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="final.degree.average"/></td>
		<td style="padding: 5px;"><bean:write name="finalAverage"/> <bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="values"/></td>
		<td style="padding: 5px; padding-left: 100px;">	</td>
	</tr>
	<tr>
		<td colspan="2" style="padding: 5px;"></td>
		<logic:equal name="registration" property="degreeType" value="MASTER_DEGREE">
			<td width="50%" style="padding: 5px; padding-left: 15em;">_____________________________</td>
		</logic:equal>
	</tr>
</table>

<br/>

<table class="apura-final mtop2" width="90%" cellspacing="0" border="0">
	<tr>
		<td colspan="2" style="color: #333; background: #ccc; padding: 5px; border-bottom: 1px solid #333;"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.information"/></td>
	</tr>
	<tr>
		<td colspan="2" style="padding: 5px;">
			<p class="apura-pt9">
				<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="conclusion.document.concluded.uppercase"/>  
				<logic:equal name="registration" property="degreeType.administrativeOfficeType.qualifiedName" value="AdministrativeOfficeType.MASTER_DEGREE">
					<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="conclusion.document.scholarship.of"/>
				</logic:equal>
				o <bean:write name="registrationConclusionBean" property="conclusionDegreeDescription"/> <bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.in"/> 
				<bean:write name="degreeFinalizationDate"/><bean:write name="degreeFinalizationGrade"/>, 
				tendo obtido o total de <bean:write name="degreeFinalizationEcts"/><bean:write name="creditsDescription"/>.
			</p>
		</td>
	</tr>
	<tr>
		<td style="text-align: center;">
			Conferido em <bean:write name="day"/>
		</td>
		<td style="text-align: center;">
			<div class="homologo">Homologo,</div>
		</td>
	</tr>
	<tr>
		<td colspan="2">&nbsp;</td>
	</tr>
	<tr>
		<td style="text-align: center;">_____________________________</td>
		<td style="text-align: center;">_____________________________</td>
	</tr>
	<tr>
		<td style="text-align: center;"></td>
		<td style="text-align: center;"><bean:write name="administrativeOfficeCoordinator" property="name"/></td>
	</tr>
	<tr>
		<td style="text-align: center;"></td>
		<td style="text-align: center;"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.coordinator"/><bean:write name="administrativeOfficeCoordinatorGender"/> <bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.of.the.hermaphrodite"/> <bean:write name="administrativeOfficeName"/></td>
	</tr>
</table>
