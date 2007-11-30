<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/taglibs-string.tld" prefix="str"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@page import="net.sourceforge.fenixedu.domain.Person"%>
<%@page import="net.sourceforge.fenixedu.domain.Employee"%>
<%@page import="net.sourceforge.fenixedu.injectionCode.AccessControl"%>
<%@page import="net.sourceforge.fenixedu.presentationTier.docs.academicAdministrativeOffice.DegreeFinalizationCertificate"%>
<%@page import="net.sourceforge.fenixedu.util.LanguageUtils"%>
<%@page import="org.joda.time.YearMonthDay"%>

<html:xhtml />

<bean:define id="registration" name="registration" type="net.sourceforge.fenixedu.domain.student.Registration"/>
<bean:define id="registrationConclusionBean" name="registrationConclusionBean" type="net.sourceforge.fenixedu.dataTransferObject.student.RegistrationConclusionBean"/>

<table width="90%">
	<tr>
		<td align="center">
		<h2>Folha de Apuramento Final</h2>
		<br />
		<br />
		<br />
		</td>
	<tr>
		<td><b><bean:write name="registration" property="degree.presentationName"/></b></td>
	<tr>
		<td>O Aluno <b><bean:write name="registration" property="person.name"/></b>, na Matrícula nº <b><bean:write name="registration" property="number"/></b></td>
	<tr>
		<td>
			concluiu 
			<logic:equal name="registration" property="degreeType.administrativeOfficeType" value="AdministrativeOfficeType.MASTER_DEGREE">
				a parte escolar d
			</logic:equal>
			o curso de <bean:write name="registration" property="degreeType.localizedName"/> 
			acima indicado, constituído pelas seguintes unidades curriculares e classificações:
			<br/>
			<br/>
		</td>
	</tr>
</table>

<fr:view name="registration" property="curriculum">
	<fr:layout>
		<fr:property name="visibleCurricularYearEntries" value="false" />
	</fr:layout>
</fr:view>

<%
	request.setAttribute("degreeFinalizationDate", registrationConclusionBean.getConclusionDate().toString("dd 'de' MMMM 'de' yyyy", LanguageUtils.getLocale()));
	final Integer finalAverage = registrationConclusionBean.getFinalAverage();	
	request.setAttribute("finalAverage", finalAverage);
	request.setAttribute("degreeFinalizationGrade", DegreeFinalizationCertificate.getDegreeFinalizationGrade(finalAverage));
	request.setAttribute("degreeFinalizationEcts", String.valueOf(registrationConclusionBean.getEctsCredits()));
	request.setAttribute("creditsDescription", registration.getDegreeType().getCreditsDescription());
	
	final Employee employee = AccessControl.getPerson().getEmployee();
	final Person administrativeOfficeCoordinator = employee.getCurrentWorkingPlace().getActiveUnitCoordinator();
	request.setAttribute("administrativeOfficeCoordinator", administrativeOfficeCoordinator);
	request.setAttribute("administrativeOfficeCoordinatorGender", administrativeOfficeCoordinator.isMale() ? "" : "a");
	request.setAttribute("administrativeOfficeName", employee.getCurrentWorkingPlace().getName());
	request.setAttribute("day", new YearMonthDay().toString("dd 'de' MMMM 'de' yyyy", LanguageUtils.getLocale()));
%>

<table class="apura-final" width="90%" cellspacing="0" border="0">
	<tr>
		<td colspan="3" style="color: #333; background: #ccc; padding: 5px; border-bottom: 1px solid #333;">Atribuição da Média</td>
	</tr>
	<tr>
		<td style="padding: 5px;">Média Ponderada</td>
		<td style="padding: 5px;"><bean:write name="registration" property="average"/></td>		
		<logic:equal name="registration" property="degreeType.administrativeOfficeType" value="AdministrativeOfficeType.MASTER_DEGREE">
			<td width="50%" style="padding: 5px; padding-left: 15em;">O coordenador do curso,</td>
		</logic:equal>
	</tr>
	<tr>
		<td style="padding: 5px;">Média Final</td>
		<td style="padding: 5px;"><bean:write name="finalAverage"/> valores</td>
		<td style="padding: 5px; padding-left: 100px;">	</td>
	</tr>
	<tr>
		<td colspan="2" style="padding: 5px;"></td>
		<logic:equal name="registration" property="degreeType.administrativeOfficeType" value="AdministrativeOfficeType.MASTER_DEGREE">
			<td width="50%" style="padding: 5px; padding-left: 15em;">_____________________________</td>
		</logic:equal>
	</tr>
</table>

<br/>

<table class="apura-final" width="90%" cellspacing="0" border="0">
	<tr>
		<td colspan="2" style="color: #333; background: #ccc; padding: 5px; border-bottom: 1px solid #333;">Informação</td>
	</tr>
	<tr>
		<td colspan="2" style="padding: 5px;">
			<p class="apura-pt9">
				Concluiu 
				<logic:equal name="registration" property="degreeType.administrativeOfficeType" value="AdministrativeOfficeType.MASTER_DEGREE">
					a parte escolar d
				</logic:equal>
				o <bean:write name="registration" property="degreeDescription"/>
				em <bean:write name="degreeFinalizationDate"/><bean:write name="degreeFinalizationGrade"/>, 
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
		<td style="text-align: center;">Coordenador<bean:write name="administrativeOfficeCoordinatorGender"/> do <bean:write name="administrativeOfficeName"/></td>
	</tr>
</table>
