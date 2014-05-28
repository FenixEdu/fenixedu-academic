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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/phd" prefix="phd" %>

<%@page import="net.sourceforge.fenixedu.domain.phd.seminar.PublicPresentationSeminarProcess"%>

<logic:notEmpty name="process" property="seminarProcess">
<logic:equal name="process" property="activeState.active" value="true">
	<bean:define id="seminarProcess" name="process" property="seminarProcess" />

	<br/>
	<strong><bean:message  key="label.phd.publicPresentationSeminarProcess" bundle="PHD_RESOURCES"/></strong>
	<table>
		<tr>
		    <td>
				<fr:view schema="PublicPresentationSeminarProcess.view" name="process" property="seminarProcess">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle2 thlight mtop10 thleft" />
					</fr:layout>
				</fr:view>
			</td>
			<td>
				<ul class="operations" >
					<logic:equal value="true" name="process" property="currentUserAllowedToManageProcessState">
					<li >
						<html:link action="/publicPresentationSeminarProcess.do?method=manageStates" paramId="processId" paramName="process" paramProperty="seminarProcess.externalId">
							<bean:message bundle="PHD_RESOURCES" key="label.phd.manage.states"/>
						</html:link>
					</li>
					</logic:equal>
				<phd:activityAvailable process="<%= seminarProcess  %>" activity="<%= PublicPresentationSeminarProcess.EditProcessAttributes.class %>">
					<li >
						<html:link action="/publicPresentationSeminarProcess.do?method=prepareEditProcessAttributes" paramId="processId" paramName="process" paramProperty="seminarProcess.externalId">
							<bean:message bundle="PHD_RESOURCES" key="label.phd.edit.attributes"/>
						</html:link>
					</li>
				</phd:activityAvailable>	
				</ul>
			</td>
		</tr>
	</table>
	
	<ul class="operations">
		
		<phd:activityAvailable process="<%= seminarProcess  %>" activity="<%= PublicPresentationSeminarProcess.SubmitComission.class %>">
		<li style="display: inline;">
			<html:link action="/publicPresentationSeminarProcess.do?method=prepareSubmitComission" paramId="processId" paramName="process" paramProperty="seminarProcess.externalId">
				<bean:message bundle="PHD_RESOURCES" key="label.phd.submit.public.presentation.seminar.comission"/>
			</html:link>
		</li>
		</phd:activityAvailable>
		
		<phd:activityAvailable process="<%= seminarProcess  %>" activity="<%= PublicPresentationSeminarProcess.ValidateComission.class %>">
		<li style="display: inline;">
			<html:link action="/publicPresentationSeminarProcess.do?method=prepareValidateComission" paramId="processId" paramName="process" paramProperty="seminarProcess.externalId">
				<bean:message bundle="PHD_RESOURCES" key="label.phd.validate.public.presentation.seminar.comission"/>
			</html:link>
		</li>
		</phd:activityAvailable>
		
		<phd:activityAvailable process="<%= seminarProcess  %>" activity="<%= PublicPresentationSeminarProcess.SchedulePresentationDate.class %>">
		<li style="display: inline;">
			<html:link action="/publicPresentationSeminarProcess.do?method=prepareSchedulePresentationDate" paramId="processId" paramName="process" paramProperty="seminarProcess.externalId">
				<bean:message bundle="PHD_RESOURCES" key="label.phd.schedule.public.presentation.seminar.date"/>
			</html:link>
		</li>
		</phd:activityAvailable>
		
		<phd:activityAvailable process="<%= seminarProcess  %>" activity="<%= PublicPresentationSeminarProcess.UploadReport.class %>">
		<li style="display: inline;">
			<html:link action="/publicPresentationSeminarProcess.do?method=prepareUploadReport" paramId="processId" paramName="process" paramProperty="seminarProcess.externalId">
				<bean:message bundle="PHD_RESOURCES" key="label.phd.upload.public.presentation.seminar.report"/>
			</html:link>
		</li>
		</phd:activityAvailable>
		
		<phd:activityAvailable process="<%= seminarProcess  %>" activity="<%= PublicPresentationSeminarProcess.ValidateReport.class %>">
		<li style="display: inline;">
			<html:link action="/publicPresentationSeminarProcess.do?method=prepareValidateReport" paramId="processId" paramName="process" paramProperty="seminarProcess.externalId">
				<bean:message bundle="PHD_RESOURCES" key="label.phd.validate.public.presentation.seminar.report"/>
			</html:link>
		</li>
		</phd:activityAvailable>
		
		<phd:activityAvailable process="<%= seminarProcess  %>" activity="<%= PublicPresentationSeminarProcess.DownloadComissionDocument.class %>">
		<li style="display: inline;">
			<bean:define id="comissionDocumentUrl" name="seminarProcess" property="comissionDocument.downloadUrl" />
			<a href="<%= comissionDocumentUrl.toString() %>"><bean:message  key="label.phd.public.presentation.seminar.comission.document" bundle="PHD_RESOURCES"/></a>
		</li>
		</phd:activityAvailable>
		
		<phd:activityAvailable process="<%= seminarProcess  %>" activity="<%= PublicPresentationSeminarProcess.DownloadReportDocument.class %>">
		<li style="display: inline;">
			<bean:define id="reportDocumentUrl" name="seminarProcess" property="reportDocument.downloadUrl" />
			<a href="<%= reportDocumentUrl.toString() %>"><bean:message  key="label.phd.public.presentation.seminar.report.document" bundle="PHD_RESOURCES"/></a>
		</li>
		</phd:activityAvailable>

		<phd:activityAvailable process="<%= seminarProcess %>" activity="<%= PublicPresentationSeminarProcess.RevertToWaitingForComissionConstitution.class %>" >
		<li style="display: inline;">
			<html:link action="/publicPresentationSeminarProcess.do?method=revertToWaitingForComissionConstitution" paramId="processId" paramName="process" paramProperty="seminarProcess.externalId">
				<bean:message bundle="PHD_RESOURCES" key="label.phd.revert.public.presentation.seminar.to.waiting.for.comission.constitution" />
			</html:link>
		</li>
		</phd:activityAvailable>
		
		<phd:activityAvailable process="<%= seminarProcess %>" activity="<%= PublicPresentationSeminarProcess.RevertToWaitingComissionForValidation.class %>" >
		<li style="display: inline;">
			<html:link action="/publicPresentationSeminarProcess.do?method=revertToWaitingComissionValidation" paramId="processId" paramName="process" paramProperty="seminarProcess.externalId">
				<bean:message bundle="PHD_RESOURCES" key="label.phd.revert.public.presentation.seminar.to.waiting.comission.for.validation" />
			</html:link>
		</li>		
		</phd:activityAvailable>
	
	</ul>
</logic:equal>
<br/>
</logic:notEmpty>
