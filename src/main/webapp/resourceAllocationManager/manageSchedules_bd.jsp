<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<h2><bean:message key="title.manage.schedule"/> <span class="small">${context_selection_bean.academicInterval.pathName}</span></h2>


<h4>${context_selection_bean.executionDegree.presentationName} <span class="small">${context_selection_bean.curricularYear.year}º ano</span></h4>

<fr:form action="/chooseContext.do">
	<fr:edit name="context_selection_bean" schema="degreeContext.choose">
		<fr:destination name="degreePostBack" path="/chooseContext.do?method=choosePostBackToContext" />
		<fr:destination name="yearPostBack" path="/chooseContext.do?method=choosePostBackToContext" />
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thright mtop05 mbottom0 thmiddle" />
			<fr:property name="columnClasses" value="width12em,width800px,tdclear tderror1" />
		</fr:layout>
	</fr:edit>
</fr:form>

<fr:form action="/chooseExecutionPeriod.do">
	<fr:edit schema="academicIntervalSelectionBean.choosePostBack"
		name="context_selection_bean">
		<fr:destination name="intervalPostBack" path="/chooseExecutionPeriod.do?method=choose" />
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thright mtop0 thmiddle" />
			<fr:property name="columnClasses" value="width12em,width800px,tdclear tderror1" />
		</fr:layout>
	</fr:edit>
</fr:form>

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
