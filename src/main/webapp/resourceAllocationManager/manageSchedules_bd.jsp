<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>

<h2><bean:message key="title.manage.schedule"/> <span class="small">${context_selection_bean.academicInterval.pathName}</span></h2>


<h4>${context_selection_bean.executionDegree.presentationName} <span class="small">${context_selection_bean.curricularYear.year}º ano</span></h4>

<jsp:include page="context.jsp"/>


<div class="row">
<br />
	<div class="col-sm-offset-3 col-sm-3">
		<html:link styleClass="btn btn-primary" page="/manageClasses.do?method=listClasses&academicInterval=${academicInterval}&curricular_year_oid=${context_selection_bean.curricularYear.externalId}&execution_degree_oid=${context_selection_bean.executionDegree.externalId}">
			<bean:message key="link.manage.turmas" />
		</html:link>
	</div>

	<div class="col-sm-3">
		<html:link styleClass="btn btn-primary" page="/manageShifts.do?method=listShifts&academicInterval=${academicInterval}&curricular_year_oid=${context_selection_bean.curricularYear.externalId}&execution_degree_oid=${context_selection_bean.executionDegree.externalId}">
			<bean:message key="link.manage.turnos" />
		</html:link>
	</div>

</div>
