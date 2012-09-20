<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt"%>
<%@ page
	import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants"%>
<%@ page import="java.util.List"%>

<em><bean:message key="label.manager.executionCourses" /></em>
<h2>Gestão de Disciplinas</h2>

<p><span class="error"><!-- Error messages go here --><html:errors /></span></p>

<fr:form action="/manageExecutionCourses.do?method=search">
	<div class="infoop3">
	<p class="mvert025">Nota: Na indicaçãodo nome pode ser fornecido apenas parte do nome da
	disciplina.</p>
	<p class="mvert025">Exemplo 1: Para selecionar todas as disciplinas que começam com a letra "A"
	escreva <strong>A%</strong></p>
	<p class="mvert025">Exemplo 2: Para selecionar todas as disciplinas que começam com a letra "A"
	e que tenham um segundo nome que começa com a letra "M" escreva <strong>A% M%</strong></p>
	</div>

	<fr:edit name="<%=PresentationConstants.CONTEXT_SELECTION_BEAN%>" schema="executionCourseContext.choose">
		<fr:destination name="intervalPostBack" path="/manageExecutionCourses.do?method=choosePostBack" />
		<fr:destination name="degreePostBack" path="/manageExecutionCourses.do?method=choosePostBack" />
		<fr:destination name="yearPostBack" path="/manageExecutionCourses.do?method=choosePostBack" />
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thright mtop15" />
			<fr:property name="columnClasses" value=",,tdclear tderror1" />
		</fr:layout>
	</fr:edit>

	<p><html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
		<bean:message key="label.choose" />
	</html:submit></p>
</fr:form>

<jsp:include page="listExecutionCourses.jsp" />