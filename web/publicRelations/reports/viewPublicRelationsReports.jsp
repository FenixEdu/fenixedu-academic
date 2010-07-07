<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<html:xhtml/>

<em><bean:message key="label.publicRelationOffice" bundle="APPLICATION_RESOURCES"/></em>
<h2><bean:message key="title.student.reports" bundle="APPLICATION_RESOURCES"/></h2>

<bean:define id="studentReportPredicate" name="studentReportPredicate" type="net.sourceforge.fenixedu.applicationTier.Servico.student.reports.GenerateStudentReport.StudentReportPredicate"/>
<bean:define id="year" name="studentReportPredicate" property="executionYear.year"/>
<bean:define id="executionYearID" name="studentReportPredicate" property="executionYear.idInternal"/>
<bean:define id="active" name="studentReportPredicate" property="active"/>
<bean:define id="concluded" name="studentReportPredicate" property="concluded"/>
<bean:define id="args" type="java.lang.String">executionYearID=<bean:write name="executionYearID"/>&amp;active=<bean:write name="active"/>&amp;concluded=<bean:write name="concluded"/></bean:define>
<logic:notEmpty name="studentReportPredicate" property="degreeType">
	<bean:define id="degreeType" name="studentReportPredicate" property="degreeType"/>
	<bean:define id="degreeTypeName" name="degreeType" property="localizedName"/>
	<bean:define id="args" type="java.lang.String">degreeType=<bean:write name="degreeType"/>&amp;executionYearID=<bean:write name="executionYearID"/>&amp;active=<bean:write name="active"/>&amp;concluded=<bean:write name="concluded"/></bean:define>
</logic:notEmpty>

<ul>
	<li><a href="<%= request.getContextPath() + "/publicRelations/studentReports.do?method=search&"+ args %>"><bean:message bundle="GEP_RESOURCES" key="label.gep.back"/></a></li>
</ul>

<logic:notEmpty name="queueJobList">

	<h3 class="mtop15 mbottom05">
	<logic:present name="degreeType">
		<bean:define id="degreeType" name="studentReportPredicate" property="degreeType"/>
		<bean:define id="degreeTypeName" name="degreeType" property="localizedName"/>
		<bean:message key="label.grp.latest.requests.done.with.degreetype" bundle="APPLICATION_RESOURCES" arg0="<%= year.toString() %>" arg1="<%= degreeTypeName.toString() %>"/>
	</logic:present>
	<logic:notPresent name="degreeType">
		<bean:message key="label.grp.latest.requests.done" bundle="APPLICATION_RESOURCES" arg0="<%= year.toString() %>"/>
	</logic:notPresent>
	</h3>
	
	<b><bean:message key="label.grp.options" bundle="APPLICATION_RESOURCES"/>:</b>
	<logic:equal name="concluded" value="true" >
		<bean:message key="label.grp.concluded" bundle="APPLICATION_RESOURCES"/>;
	</logic:equal>
	<logic:equal name="active" value="true" >
	<bean:message key="label.grp.active" bundle="APPLICATION_RESOURCES"/>
	</logic:equal>
	
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

<logic:empty name="queueJobList">
	<h3 class="mtop15 mbottom05"><bean:message key="label.gep.listing.type.non.existing" bundle="GEP_RESOURCES" /></h3>
</logic:empty>