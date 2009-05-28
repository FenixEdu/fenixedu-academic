<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<html:xhtml/>

<h2><bean:message key="link.reports.by.degree.type" bundle="GEP_RESOURCES" /></h2>

<logic:notEmpty name="queueJobList">

	<h3 class="mtop15 mbottom05"><bean:message key="label.gep.latest.requests" bundle="GEP_RESOURCES" /></h3>
	
	<fr:view name="queueJobList" schema="latestJobs">
    	<fr:layout name="tabular">
    		<fr:property name="classes" value="tstyle1 mtop05" />
    		<fr:property name="columnClasses" value=",,,acenter,,,,,," />
			<fr:property name="link(Download)" value="/downloadQueuedJob.do?method=downloadFile"/>
			<fr:property name="bundle(Download)" value="GEP_RESOURCES"/>
			<fr:property name="param(Download)" value="idInternal/id"/>
			<fr:property name="visibleIf(Download)" value="done"/>
			<fr:property name="module(Download)" value=""/>
		</fr:layout>
	</fr:view>
</logic:notEmpty>

<logic:present name="reportBean">
	
	<h3 class="mtop15 mbottom05">Novo pedido</h3>
	
	<fr:form action="/reportsByDegreeType.do?method=selectDegreeType">
		<fr:edit name="reportBean" id="reportBean" type="net.sourceforge.fenixedu.presentationTier.Action.gep.ReportsByDegreeTypeDA$ReportBean" 
				schema="select.degree.type">
			<fr:layout>
				<fr:property name="classes" value="tstyle5 thlight thmiddle mbottom1 mtop05"/>
			</fr:layout>
		</fr:edit>
	</fr:form>

<logic:present name="job">
	<bean:define id="job" name="job" type="net.sourceforge.fenixedu.domain.reports.GepReportFile"/>
	<p>
		<div class="success0" style="width:600px;">
		<logic:present name="reportBean" property="executionYear">
		<logic:present name="reportBean" property="degreeType">
			<p class="mvert05"><bean:message key="label.gep.listing.confirmation" bundle="GEP_RESOURCES" arg0="<%= job.getJobName().toString() %>" arg1="<%= job.getExecutionYear().getYear().toString() %>" arg2="<%= job.getDegreeType().getLocalizedName().toString() %>"/></p>
			<p class="mvert05"><bean:message key="label.gep.email.notice" bundle="GEP_RESOURCES" /></p>
		</logic:present>
		</logic:present>

		<logic:notPresent name="reportBean" property="executionYear">
			<p class="mvert05"><bean:message key="label.gep.listing.confirmation.graduation" bundle="GEP_RESOURCES" arg0="<%= job.getJobName().toString() %>"/></p>
			<p class="mvert05"><bean:message key="label.gep.email.notice" bundle="GEP_RESOURCES" /></p>
		</logic:notPresent>
		
		<logic:present name="reportBean" property="executionYear">
		<logic:notPresent name="reportBean" property="degreeType">
			<p class="mvert05"><bean:message key="label.gep.listing.confirmation.graduation" bundle="GEP_RESOURCES" arg0="<%= job.getJobName().toString() %>"/></p>
			<p class="mvert05"><bean:message key="label.gep.email.notice" bundle="GEP_RESOURCES" /></p>
		</logic:notPresent>
		</logic:present>

		</div>
	</p>
</logic:present>

<bean:define id="args" type="java.lang.String">degreeType=<bean:write name="reportBean" property="degreeType"/>&amp;executionYearID=<bean:write name="reportBean" property="executionYearOID"/></bean:define>

<logic:present name="reportBean" property="executionYear">
<logic:present name="reportBean" property="degreeType">

<bean:define id="executionYearID" name="reportBean" property="executionYear.idInternal"/>
<bean:define id="degreeType" name="reportBean" property="degreeType"/>


<bean:define id="args" type="java.lang.String">degreeType=<bean:write name="degreeType"/>&amp;executionYearID=<bean:write name="executionYearID"/></bean:define>
 
	<p class="mtop2 mbottom05">
		<bean:message key="label.available.reports" bundle="GEP_RESOURCES" />
	</p>
	<bean:define id="urlEurAce" type="java.lang.String">/reportsByDegreeType.do?method=downloadEurAce&amp;<bean:write name="args" filter="false"/></bean:define>
	<bean:define id="urlEctsLabelForDegrees" type="java.lang.String">/reportsByDegreeType.do?method=downloadEctsLabelForDegrees&amp;<bean:write name="args" filter="false"/></bean:define>
	<bean:define id="urlEctsLabelForCurricularCourses" type="java.lang.String">/reportsByDegreeType.do?method=downloadEctsLabelForCurricularCourses&amp;<bean:write name="args" filter="false"/></bean:define>
	<bean:define id="urlStatusAndAproval" type="java.lang.String">/reportsByDegreeType.do?method=downloadStatusAndAproval&amp;<bean:write name="args" filter="false"/></bean:define>
	<bean:define id="urlEti" type="java.lang.String">/reportsByDegreeType.do?method=downloadEti&amp;<bean:write name="args" filter="false"/></bean:define>
	<bean:define id="urlRegistrations" type="java.lang.String">/reportsByDegreeType.do?method=downloadRegistrations&amp;<bean:write name="args" filter="false"/></bean:define>
	<bean:define id="urlFlunked" type="java.lang.String">/reportsByDegreeType.do?method=downloadFlunked&amp;<bean:write name="args" filter="false"/></bean:define>
	<bean:define id="urlTeachersByShift" type="java.lang.String">/reportsByDegreeType.do?method=downloadTeachersByShift&amp;<bean:write name="args" filter="false"/></bean:define>
	<bean:define id="urlCourseLoads" type="java.lang.String">/reportsByDegreeType.do?method=downloadCourseLoads&amp;<bean:write name="args" filter="false"/></bean:define>
	<bean:define id="urlTutorshipProgram" type="java.lang.String">/reportsByDegreeType.do?method=downloadTutorshipProgram&amp;<bean:write name="args" filter="false"/></bean:define>
	<bean:define id="viewReports" type="java.lang.String">/reportsByDegreeType.do?method=viewReports&amp;<bean:write name="args" filter="false"/></bean:define>
	
			<table class="tstyle1 thleft thlight mtop05">
				<tr>
					<td style="width: 350px;">
						<bean:message key="label.report.eur.ace" bundle="GEP_RESOURCES" />
					</td>
					<td>
						<bean:define id="urlEurAceCsv" type="java.lang.String"><bean:write name="urlEurAce" filter="false"/>&amp;format=csv</bean:define>
						<html:link page="<%= urlEurAceCsv %>">
							<bean:message key="label.request.csv" bundle="GEP_RESOURCES" />
						</html:link>
						|
						<bean:define id="urlEurAceXls" type="java.lang.String"><bean:write name="urlEurAce" filter="false"/>&amp;format=xls</bean:define>
						<html:link page="<%= urlEurAceXls %>">
							<bean:message key="label.request.xls" bundle="GEP_RESOURCES" />
						</html:link>
					</td>
					<td>
						<html:link page="<%= viewReports + "&type=1" %>">
							<bean:message key="label.view.requests.done" bundle="GEP_RESOURCES" />
						</html:link>
					</td>
				</tr>
				<tr>
					<td>
						<bean:message key="label.report.ects.label.degrees" bundle="GEP_RESOURCES" />
					</td>
					<td>
						<bean:define id="urlEctsLabelDegreesCsv" type="java.lang.String"><bean:write name="urlEctsLabelForDegrees" filter="false"/>&amp;format=csv</bean:define>
						<html:link page="<%= urlEctsLabelDegreesCsv %>">
							<bean:message key="label.request.csv" bundle="GEP_RESOURCES" />
						</html:link>
						|
						<bean:define id="urlEctsLabelDegreesXls" type="java.lang.String"><bean:write name="urlEctsLabelForDegrees" filter="false"/>&amp;format=xls</bean:define>
						<html:link page="<%= urlEctsLabelDegreesXls %>">
							<bean:message key="label.request.xls" bundle="GEP_RESOURCES" />
						</html:link>
					</td>
					<td>
						<html:link page="<%= viewReports + "&type=2" %>">
							<bean:message key="label.view.requests.done" bundle="GEP_RESOURCES" />
						</html:link>
					</td>
				</tr>
				<tr>
					<td>
						<bean:message key="label.report.ects.label.curricularCourses" bundle="GEP_RESOURCES" />
					</td>
					<td>
						<bean:define id="urlEctsLabelCurricularCoursesCsv" type="java.lang.String"><bean:write name="urlEctsLabelForCurricularCourses" filter="false"/>&amp;format=csv</bean:define>
						<html:link page="<%= urlEctsLabelCurricularCoursesCsv %>">
							<bean:message key="label.request.csv" bundle="GEP_RESOURCES" />
						</html:link>
						|
						<bean:define id="urlEctsLabelCurricularCoursesXls" type="java.lang.String"><bean:write name="urlEctsLabelForCurricularCourses" filter="false"/>&amp;format=xls</bean:define>
						<html:link page="<%= urlEctsLabelCurricularCoursesXls %>">
							<bean:message key="label.request.xls" bundle="GEP_RESOURCES" />
						</html:link>
					</td>
					<td>
						<html:link page="<%= viewReports + "&type=3" %>">
							<bean:message key="label.view.requests.done" bundle="GEP_RESOURCES" />
						</html:link>
					</td>
				</tr>
				<tr>
					<td>
						<bean:define id="year1" type="java.lang.String" name="reportBean" property="executionYearFourYearsBack.year"/>
						<bean:define id="year2" type="java.lang.String" name="reportBean" property="executionYear.year"/>
						<bean:message key="label.report.status.and.aprovals" bundle="GEP_RESOURCES" arg0="2003/2004" arg1="<%= year2 %>"/>
					</td>
					<td>
						<bean:define id="urlStatusAndAprovalCsv" type="java.lang.String"><bean:write name="urlStatusAndAproval" filter="false"/>&amp;format=csv</bean:define>
						<html:link page="<%= urlStatusAndAprovalCsv %>">
							<bean:message key="label.request.csv" bundle="GEP_RESOURCES" />
						</html:link>
						|
						<bean:define id="urlStatusAndAprovalXls" type="java.lang.String"><bean:write name="urlStatusAndAproval" filter="false"/>&amp;format=xls</bean:define>
						<html:link page="<%= urlStatusAndAprovalXls %>">
							<bean:message key="label.request.xls" bundle="GEP_RESOURCES" />
						</html:link>
					</td>
					<td>
						<html:link page="<%= viewReports + "&type=4" %>">
							<bean:message key="label.view.requests.done" bundle="GEP_RESOURCES" />
						</html:link>
					</td>
				</tr>
				<tr>
					<td>
						<bean:message key="label.report.eti" bundle="GEP_RESOURCES"/>
					</td>
					<td>
						<bean:define id="urlEtiCsv" type="java.lang.String"><bean:write name="urlEti" filter="false"/>&amp;format=csv</bean:define>
						<html:link page="<%= urlEtiCsv %>">
							<bean:message key="label.request.csv" bundle="GEP_RESOURCES" />
						</html:link>
						|
						<bean:define id="urlEtiXls" type="java.lang.String"><bean:write name="urlEti" filter="false"/>&amp;format=xls</bean:define>
						<html:link page="<%= urlEtiXls %>">
							<bean:message key="label.request.xls" bundle="GEP_RESOURCES" />
						</html:link>
					</td>
					<td>
						<html:link page="<%= viewReports + "&type=5" %>">
							<bean:message key="label.view.requests.done" bundle="GEP_RESOURCES" />
						</html:link>
					</td>
				</tr>
		<!-- 		<tr>
					<td>
						<bean:message key="label.report.registrations" bundle="GEP_RESOURCES"/>
					</td>
					<td>
						<bean:define id="urlRegistrationsCsv" type="java.lang.String"><bean:write name="urlRegistrations" filter="false"/>&amp;format=csv</bean:define>
						<html:link page="<%= urlRegistrationsCsv %>">
							<bean:message key="label.request.csv" bundle="GEP_RESOURCES" />
						</html:link>
						|
						<bean:define id="urlRegistrationsXls" type="java.lang.String"><bean:write name="urlRegistrations" filter="false"/>&amp;format=xls</bean:define>
						<html:link page="<%= urlRegistrationsXls %>">
							<bean:message key="label.request.xls" bundle="GEP_RESOURCES" />
						</html:link>
					</td>
					<td>
						<html:link page="<%= viewReports + "&type=6" %>">
							<bean:message key="label.view.requests.done" bundle="GEP_RESOURCES" />
						</html:link>
					</td>
				</tr>
                 -->
				<tr>
					<td>
						<bean:message key="label.report.flunked" bundle="GEP_RESOURCES"/>
					</td>
					<td>
						<bean:define id="urlFlunkedCsv" type="java.lang.String"><bean:write name="urlFlunked" filter="false"/>&amp;format=csv</bean:define>
						<html:link page="<%= urlFlunkedCsv %>">
							<bean:message key="label.request.csv" bundle="GEP_RESOURCES" />
						</html:link>
						|
						<bean:define id="urlFlunkedXls" type="java.lang.String"><bean:write name="urlFlunked" filter="false"/>&amp;format=xls</bean:define>
						<html:link page="<%= urlFlunkedXls %>">
							<bean:message key="label.request.xls" bundle="GEP_RESOURCES" />
						</html:link>
					</td>
					<td>
						<html:link page="<%= viewReports + "&type=7" %>">
							<bean:message key="label.view.requests.done" bundle="GEP_RESOURCES" />
						</html:link>
					</td>
				</tr>
				<tr>
					<td>
						<bean:message key="label.report.teachersByShift" bundle="GEP_RESOURCES"/>
					</td>
					<td>
						<bean:define id="urlTeachersByShiftCsv" type="java.lang.String"><bean:write name="urlTeachersByShift" filter="false"/>&amp;format=csv</bean:define>
						<html:link page="<%= urlTeachersByShiftCsv %>">
							<bean:message key="label.request.csv" bundle="GEP_RESOURCES" />
						</html:link>
						|
						<bean:define id="urlTeachersByShiftXls" type="java.lang.String"><bean:write name="urlTeachersByShift" filter="false"/>&amp;format=xls</bean:define>
						<html:link page="<%= urlTeachersByShiftXls %>">
							<bean:message key="label.request.xls" bundle="GEP_RESOURCES" />
						</html:link>
					</td>
					<td>
						<html:link page="<%= viewReports + "&type=8" %>">
							<bean:message key="label.view.requests.done" bundle="GEP_RESOURCES" />
						</html:link>
					</td>
				</tr>
				<tr>
					<td>
						<bean:message key="label.report.courseLoads" bundle="GEP_RESOURCES"/>
					</td>
					<td>
						<bean:define id="urlCourseLoadsCsv" type="java.lang.String"><bean:write name="urlCourseLoads" filter="false"/>&amp;format=csv</bean:define>
						<html:link page="<%= urlCourseLoadsCsv %>">
							<bean:message key="label.request.csv" bundle="GEP_RESOURCES" />
						</html:link>
						|
						<bean:define id="urlCourseLoadsXls" type="java.lang.String"><bean:write name="urlCourseLoads" filter="false"/>&amp;format=xls</bean:define>
						<html:link page="<%= urlCourseLoadsXls %>">
							<bean:message key="label.request.xls" bundle="GEP_RESOURCES" />
						</html:link>
					</td>
					<td>
						<html:link page="<%= viewReports + "&type=9" %>">
							<bean:message key="label.view.requests.done" bundle="GEP_RESOURCES" />
						</html:link>
					</td>
				</tr>

				<tr>
					<td>
						<bean:message key="label.report.tutorship.program" bundle="GEP_RESOURCES"/>
					</td>
					<td>
						<bean:define id="urlTutorshipProgramCsv" type="java.lang.String"><bean:write name="urlTutorshipProgram" filter="false"/>&amp;format=csv</bean:define>
						<html:link page="<%= urlTutorshipProgramCsv %>">
							<bean:message key="label.request.csv" bundle="GEP_RESOURCES" />
						</html:link>
						|
						<bean:define id="urlTutorshipProgramXls" type="java.lang.String"><bean:write name="urlTutorshipProgram" filter="false"/>&amp;format=xls</bean:define>
						<html:link page="<%= urlTutorshipProgramXls %>">
							<bean:message key="label.request.xls" bundle="GEP_RESOURCES" />
						</html:link>
					</td>
					<td>
						<html:link page="<%= viewReports + "&type=9" %>">
							<bean:message key="label.view.requests.done" bundle="GEP_RESOURCES" />
						</html:link>
					</td>
				</tr>
			</table>
</logic:present>
</logic:present>

</logic:present>

<bean:define id="urlGraduations" type="java.lang.String">/reportsByDegreeType.do?method=downloadGraduations&amp;<bean:write name="args" filter="false"/></bean:define>
<bean:define id="viewReports" type="java.lang.String">/reportsByDegreeType.do?method=viewReports&amp;<bean:write name="args" filter="false"/></bean:define>
<table class="tstyle1 thleft thlight mtop05">
	<tr>
		<td style="width: 350px;">
			<bean:message key="label.report.graduations" bundle="GEP_RESOURCES"/>
		</td>
		<td>
			<bean:define id="urlGraduationsCsv" type="java.lang.String"><bean:write name="urlGraduations" filter="false"/>&amp;format=csv</bean:define>
			<html:link page="<%= urlGraduationsCsv %>">
				<bean:message key="label.request.csv" bundle="GEP_RESOURCES" />
			</html:link>
			|
			<bean:define id="urlGraduationsXls" type="java.lang.String"><bean:write name="urlGraduations" filter="false"/>&amp;format=xls</bean:define>
			<html:link page="<%= urlGraduationsXls %>">
				<bean:message key="label.request.xls" bundle="GEP_RESOURCES" />
			</html:link>
		</td>
		<td>
			<html:link page="<%= viewReports + "&type=10" %>">
				<bean:message key="label.view.requests.done" bundle="GEP_RESOURCES" />
			</html:link>
		</td>
	</tr>
</table>