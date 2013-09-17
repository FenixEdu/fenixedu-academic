<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt" %>
<html:xhtml/>

<h2><bean:message bundle="MANAGER_RESOURCES" key="label.view.viewCurriculumLineLogStatistics"/></h2>

<h3>
	<bean:message bundle="MANAGER_RESOURCES" key="label.view.viewCurriculumLineLogStatistics.executionSemester"/>:
	<bean:write name="executionSemester" property="qualifiedName"/>
</h3>

<h3>
	<bean:message bundle="MANAGER_RESOURCES" key="label.view.viewCurriculumLineLogStatistics.enrolmentInterval"/>:
	<bean:define id="interval" name="curriculumLineLogStatisticsCalculator" property="enrolmentPeriod"/>
	<dt:format pattern="yyyy-MM-dd HH:mm:ss"><bean:write name="interval" property="start.millis"/></dt:format>
	-
	<dt:format pattern="yyyy-MM-dd HH:mm:ss"><bean:write name="interval" property="end.millis"/></dt:format>
</h3>

<bean:define id="url" type="java.lang.String"><%= request.getContextPath() %>/manager/curriculumLineLogs.do?method=viewCurriculumLineLogStatisticsChartOperations&amp;executionSemesterId=<bean:write name="executionSemester" property="externalId"/></bean:define>
<html:img align="middle" src="<%= url %>" altKey="clip_image002" bundle="IMAGE_RESOURCES" />

