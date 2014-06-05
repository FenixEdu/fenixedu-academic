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
<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3.TimeTableType" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/struts-example-1.0" prefix="app" %>

<link href="<%= request.getContextPath() %>/CSS/dotist_timetables.css" rel="stylesheet" type="text/css" />

<jsp:include page="/commons/contextClassAndExecutionDegreeAndCurricularYear.jsp" />

<h2><bean:message key="link.manage.turmas"/> <span class="small">${executionDegree.executionDegree.degreeCurricularPlan.name}</span></h2>

<h3>Manipular Turma <span class="small">${className}</span></h3>


<html:link styleClass="btn btn-primary btn-sm" page="/manageClass.do?method=prepareAddShifts&academicInterval=${academicInterval}&execution_degree_oid=${execution_degree_oid}&curricular_year_oid=${curricularYearOID}&class_oid=${classOID}">
	<bean:message key="label.shifts.add"/>
</html:link>

<html:link styleClass="btn btn-primary btn-sm" page="/manageClass.do?method=prepare&academicInterval=${academicInterval}&execution_degree_oid=${execution_degree_oid}&curricular_year_oid=${curricularYearOID}&class_oid=${classOID}">
	<bean:message key="label.view.shifts"/>
</html:link>

<html:link styleClass="btn btn-primary btn-sm" page="/manageClasses.do?method=listClasses&academicInterval=${academicInterval}&execution_degree_oid=${execution_degree_oid}&curricular_year_oid=${curricularYearOID}">
	<bean:message key="label.return"/>
</html:link>

<h3 class="mtop15">Horário da Turma</h3>
<div align="center">
	<app:gerarHorario name="<%= PresentationConstants.LESSON_LIST_ATT %>"
					  type="<%= TimeTableType.SOP_CLASS_TIMETABLE %>"/>
</div>
