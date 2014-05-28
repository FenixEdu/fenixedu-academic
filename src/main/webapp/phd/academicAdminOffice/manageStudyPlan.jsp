<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page isELIgnored="true"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>

<%@page import="net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramDocumentType"%><html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<bean:define id="processId" name="process" property="externalId" />

<%-- ### Title #### --%>
<h2><bean:message key="label.phd.studyPlanAndQualificationExams" bundle="PHD_RESOURCES" /></h2>
<%-- ### End of Title ### --%>


<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>
<html:link action="<%= "/phdIndividualProgramProcess.do?method=viewProcess&processId=" + processId.toString() %>">
	<bean:message bundle="PHD_RESOURCES" key="label.back"/>
</html:link>
<br/><br/>
<%--  ### Return Links / Steps Information (for multistep forms)  ### --%>

<%--  ### Error Messages  ### --%>
<jsp:include page="/phd/errorsAndMessages.jsp" />
<%--  ### End of Error Messages  ### --%>


<%--  ### Context Information (e.g. Person Information, Registration Information)  ### --%>
<strong><bean:message  key="label.phd.process" bundle="PHD_RESOURCES"/></strong>
<fr:view schema="AcademicAdminOffice.PhdIndividualProgramProcess.view" name="process">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thlight mtop15" />
	</fr:layout>
</fr:view>
<%--  ### End Of Context Information  ### --%>

<br/>

<%--  ### Operation Area (e.g. Create Candidacy)  ### --%>

<%--  Qualification Exams --%>
<strong><bean:message  key="label.phd.qualification.exams" bundle="PHD_RESOURCES"/></strong><br/>
<fr:view schema="PhdIndividualProgramProcess.view.qualification.exams.information" name="process">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thlight mtop15" />
	</fr:layout>
</fr:view>
<html:link action="/phdIndividualProgramProcess.do?method=prepareEditQualificationExams" paramId="processId" paramName="process" paramProperty="externalId"> 
	<bean:message bundle="PHD_RESOURCES" key="label.edit"/>
</html:link>

<br/><br/>

<%--  Study Plan Relevant Documents --%>
<strong><bean:message  key="label.phd.relevant.documents" bundle="PHD_RESOURCES"/></strong><br/>
<logic:empty name="process" property="studyPlanRelevantDocuments">
	<bean:message  key="label.phd.noDocuments" bundle="PHD_RESOURCES"/>
</logic:empty> 
<logic:notEmpty name="process" property="studyPlanRelevantDocuments">
	<fr:view schema="PhdProgramProcessDocument.view.without.type" name="process" property="studyPlanRelevantDocuments">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight mtop15" />
			
			<fr:property name="linkFormat(view)" value="${downloadUrl}"/>
			<fr:property name="key(view)" value="label.view"/>
			<fr:property name="bundle(view)" value="PHD_RESOURCES"/>
			<fr:property name="order(view)" value="0" />
			<fr:property name="hasContext(view)" value="false" />
			<fr:property name="contextRelative(view)" value="false" />
			
			<fr:property name="sortBy" value="uploadTime=desc" />
		</fr:layout>
	</fr:view>
</logic:notEmpty>

<br/><br/>

<%--  Study Plan --%>
<strong><bean:message  key="label.phd.studyPlan" bundle="PHD_RESOURCES"/></strong><br/>
<logic:empty name="process" property="studyPlan">
	<bean:message  key="label.phd.noStudyPlan" bundle="PHD_RESOURCES"/><br/><br/>
	<html:link action="/phdIndividualProgramProcess.do?method=prepareCreateStudyPlan" paramId="processId" paramName="process" paramProperty="externalId">
		<bean:message bundle="PHD_RESOURCES" key="label.phd.createStudyPlan"/>
	</html:link>
</logic:empty>

<logic:notEmpty name="process" property="studyPlan">
	
	<fr:view schema="PhdStudyPlan.view" name="process" property="studyPlan">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight mtop15" />
		</fr:layout>
	</fr:view>
	<html:link action="/phdIndividualProgramProcess.do?method=prepareEditStudyPlan" paramId="processId" paramName="process" paramProperty="externalId"> 
		<bean:message bundle="PHD_RESOURCES" key="label.edit"/>
	</html:link>, 
	<bean:define id="confirmationMessage"><bean:message  key="label.confirmation.delete.message" bundle="PHD_RESOURCES"/></bean:define>
	<html:link action="/phdIndividualProgramProcess.do?method=deleteStudyPlan" paramId="processId" paramName="process" paramProperty="externalId" onclick="<%= "return confirm('" + confirmationMessage.toString() + "')" %>"> 
		<bean:message bundle="PHD_RESOURCES" key="label.delete"/>
	</html:link>
	
	<logic:equal name="process" property="studyPlan.exempted" value="false">
		<br/><br/>
		<strong><bean:message  key="label.phd.normalCourses" bundle="PHD_RESOURCES"/></strong><br/>
		<bean:define id="studyPlan" name="process" property="studyPlan" />
		<logic:empty name="studyPlan" property="normalEntries">
			<bean:message  key="label.phd.noCourses" bundle="PHD_RESOURCES"/>
		</logic:empty>
		<logic:notEmpty name="studyPlan" property="normalEntries">
			<fr:view schema="PhdStudyPlanEntry.view" name="studyPlan" property="normalEntries">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle2 thlight mtop15" />
					<fr:property name="linkFormat(delete)" value="/phdIndividualProgramProcess.do?method=deleteStudyPlanEntry&studyPlanEntryId=${externalId}&processId=${studyPlan.process.externalId}"/>
					<fr:property name="key(delete)" value="label.delete"/>
					<fr:property name="bundle(delete)" value="PHD_RESOURCES"/>
					<fr:property name="confirmationKey(delete)" value="label.confirmation.delete.message" />
					<fr:property name="confirmationBundle(delete)" value="PHD_RESOURCES" />
				</fr:layout>
			</fr:view>
		</logic:notEmpty>	
		
		<br/><br/>
		<strong><bean:message  key="label.phd.propaedeuticsCourses" bundle="PHD_RESOURCES"/></strong><br/>
		<logic:empty name="studyPlan" property="propaedeuticEntries">
			<bean:message  key="label.phd.noCourses" bundle="PHD_RESOURCES"/>
		</logic:empty>
		<logic:notEmpty name="studyPlan" property="propaedeuticEntries">
			<fr:view schema="PhdStudyPlanEntry.view" name="studyPlan" property="propaedeuticEntries">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle2 thlight mtop15" />
					<fr:property name="linkFormat(delete)" value="/phdIndividualProgramProcess.do?method=deleteStudyPlanEntry&studyPlanEntryId=${externalId}&processId=${studyPlan.process.externalId}"/>
					<fr:property name="key(delete)" value="label.delete"/>
					<fr:property name="bundle(delete)" value="PHD_RESOURCES"/>
					<fr:property name="confirmationKey(delete)" value="label.confirmation.delete.message" />
					<fr:property name="confirmationBundle(delete)" value="PHD_RESOURCES" />
				</fr:layout>
			</fr:view>
		</logic:notEmpty>

		<br/><br/>
		<strong><bean:message  key="label.phd.extraCurricularCourses" bundle="PHD_RESOURCES"/></strong><br/>
		<logic:empty name="studyPlan" property="extraCurricularEntries">
			<bean:message  key="label.phd.noCourses" bundle="PHD_RESOURCES"/>
		</logic:empty>
		<logic:notEmpty name="studyPlan" property="extraCurricularEntries">
			<fr:view schema="PhdStudyPlanEntry.view" name="studyPlan" property="extraCurricularEntries">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle2 thlight mtop15" />
					<fr:property name="linkFormat(delete)" value="/phdIndividualProgramProcess.do?method=deleteStudyPlanEntry&studyPlanEntryId=${externalId}&processId=${studyPlan.process.externalId}"/>
					<fr:property name="key(delete)" value="label.delete"/>
					<fr:property name="bundle(delete)" value="PHD_RESOURCES"/>
					<fr:property name="confirmationKey(delete)" value="label.confirmation.delete.message" />
					<fr:property name="confirmationBundle(delete)" value="PHD_RESOURCES" />
				</fr:layout>
			</fr:view>
		</logic:notEmpty>
		
		<br/><br/>
		<p>
			<html:link action="/phdIndividualProgramProcess.do?method=prepareCreateStudyPlanEntry" paramId="processId" paramName="process" paramProperty="externalId">
				<bean:message bundle="PHD_RESOURCES" key="label.phd.createStudyPlanEntry"/>
			</html:link>
		</p>
	</logic:equal>
	
</logic:notEmpty>

<%--  ### End of Operation Area  ### --%>



<%--  ### Buttons (e.g. Submit)  ### --%>

<%--  ### End of Buttons (e.g. Submit)  ### --%>

