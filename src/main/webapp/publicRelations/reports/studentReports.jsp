<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<html:xhtml/>

<em><bean:message key="label.publicRelationOffice" bundle="APPLICATION_RESOURCES"/></em>
<h2><bean:message key="title.student.reports" bundle="APPLICATION_RESOURCES"/></h2>

<logic:notEmpty name="queueJobList">

	<h3 class="mtop15 mbottom05"><bean:message key="label.grp.latest.requests" bundle="APPLICATION_RESOURCES" /></h3>
	
	<fr:view name="queueJobList" schema="latestGRPJobs">
    	<fr:layout name="tabular">
    		<fr:property name="classes" value="tstyle1 mtop05" />
    		<fr:property name="columnClasses" value=",,,acenter,,,,,," />
			<fr:property name="link(Download)" value="/downloadQueuedJob.do?method=downloadFile"/>
			<fr:property name="bundle(Download)" value="GRP_RESOURCES"/>
			<fr:property name="param(Download)" value="externalId/id"/>
			<fr:property name="visibleIf(Download)" value="done"/>
			<fr:property name="module(Download)" value=""/>
		</fr:layout>
	</fr:view>
</logic:notEmpty>

<fr:edit name="studentReportPredicate"
		 schema="student.report.predicate"
		 type="net.sourceforge.fenixedu.applicationTier.Servico.student.reports.GenerateStudentReport$StudentReportPredicate"
		 action="/studentReports.do?method=search"
		 >
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle5 thlight thright mtop1"/>
		<fr:property name="columnClasses" value=",,tderror1 tdclear"/>
	</fr:layout>
</fr:edit>

<br/>
<br/>

<logic:present name="job">
	<bean:define id="job" name="job" type="net.sourceforge.fenixedu.domain.PublicRelationsStudentListQueueJob"/>
	<div class="success0" style="width:440px;">
		<logic:present name="job" property="degreeType">
			<p class="mvert05"><bean:message key="label.grp.request.listing.success.with.degreetype" bundle="APPLICATION_RESOURCES" arg0="<%= job.getExecutionYear().getYear().toString() %>" arg1="<%= job.getDegreeType().getLocalizedName().toString() %>"/></p>
			<p class="mvert05"><bean:message key="label.gep.email.notice" bundle="GEP_RESOURCES" /></p>
		</logic:present>

		<logic:notPresent name="job" property="degreeType">
			<p class="mvert05"><bean:message key="label.grp.request.listing.success" bundle="APPLICATION_RESOURCES" arg0="<%= job.getExecutionYear().getYear().toString() %>"/></p>
			<p class="mvert05"><bean:message key="label.gep.email.notice" bundle="GEP_RESOURCES" /></p>
		</logic:notPresent>

		</div>
</logic:present>

<logic:present name="showLink">

<bean:define id="executionYearID" name="studentReportPredicate" property="executionYear.idInternal"/>
<bean:define id="active" name="studentReportPredicate" property="active"/>
<bean:define id="concluded" name="studentReportPredicate" property="concluded"/>
<bean:define id="args" type="java.lang.String">executionYearID=<bean:write name="executionYearID"/>&amp;active=<bean:write name="active"/>&amp;concluded=<bean:write name="concluded"/></bean:define>
<logic:notEmpty name="studentReportPredicate" property="degreeType">
	<bean:define id="degreeType" name="studentReportPredicate" property="degreeType"/>
	<bean:define id="args" type="java.lang.String">degreeType=<bean:write name="degreeType"/>&amp;executionYearID=<bean:write name="executionYearID"/>&amp;active=<bean:write name="active"/>&amp;concluded=<bean:write name="concluded"/></bean:define>
</logic:notEmpty>
<bean:define id="requestJob" type="java.lang.String">/studentReports.do?method=requestJob&amp;<bean:write name="args" filter="false"/></bean:define>
<bean:define id="viewJobs" type="java.lang.String">/studentReports.do?method=viewJobs&amp;<bean:write name="args" filter="false"/></bean:define>

	<html:link page="<%= requestJob %>">
		<bean:message key="label.grp.request.listing" bundle="APPLICATION_RESOURCES" />
	</html:link>
|
	<html:link page="<%= viewJobs %>">
		<bean:message key="label.view.requests.done" bundle="GEP_RESOURCES" />
	</html:link>
</logic:present>