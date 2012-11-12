<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml />

<logic:present name="selection">
	<fr:form action="/a3es.do?method=select">
		<fr:edit id="process" name="process">
			<fr:schema bundle="GEP_RESOURCES" type="net.sourceforge.fenixedu.presentationTier.Action.gep.a3es.A3ESDegreeProcess">
				<fr:slot name="user" key="label.gep.a3es.user" required="true" />
				<fr:slot name="password" key="label.gep.a3es.password" required="true" />
				<fr:slot name="executionSemester" key="label.gep.a3es.period" layout="menu-select" required="true">
					<fr:property name="from" value="availableExecutionSemesters" />
					<fr:property name="format" value="${previousExecutionPeriod.qualifiedName} - ${qualifiedName}" />
				</fr:slot>
			</fr:schema>
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thright mtop05" />
				<fr:property name="columnClasses" value=",,tdclear tderror1" />
			</fr:layout>
		</fr:edit>
		<html:submit>
			<bean:message bundle="GEP_RESOURCES" key="button.gep.a3es.select" />
		</html:submit>
	</fr:form>
</logic:present>

<logic:notPresent name="selection">
	<html:link action="/a3es.do?method=prepare">
		<bean:message bundle="GEP_RESOURCES" key="button.gep.a3es.changeSelection" />
	</html:link>

	<fr:view name="process">
		<fr:schema bundle="GEP_RESOURCES" type="net.sourceforge.fenixedu.presentationTier.Action.gep.a3es.A3ESDegreeProcess">
			<fr:slot name="degree.name" key="label.gep.a3es.degree" />
			<fr:slot name="executionSemester" layout="format" key="label.gep.a3es.period">
				<fr:property name="format" value="${previousExecutionPeriod.qualifiedName} - ${qualifiedName}" />
			</fr:slot>
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thright mtop05" />
			<fr:property name="columnClasses" value=",,tdclear tderror1" />
		</fr:layout>
	</fr:view>

	<fr:form action="/a3es.do">
		<input type="hidden" name="method" />
		<fr:edit id="process" name="process" visible="false" />
		<html:submit onclick="this.form.method.value='uploadCompetenceCourses';">
			<bean:message bundle="GEP_RESOURCES" key="button.gep.a3es.uploadCompetenceCourses" />
		</html:submit>
		<html:submit onclick="this.form.method.value='uploadTeacherCurriculum';">
			<bean:message bundle="GEP_RESOURCES" key="button.gep.a3es.uploadTeacherCurriculum" />
		</html:submit>
		<html:submit onclick="this.form.method.value='exportTeacherCurriculum';">
			<bean:message bundle="GEP_RESOURCES" key="button.gep.a3es.exportTeacherCurriculum" />
		</html:submit>
	</fr:form>
</logic:notPresent>

<logic:present name="output">
	<bean:message bundle="GEP_RESOURCES" key="label.gep.a3es.output" />
	<div style="margin: 15px 0px; overflow-y: scroll; height: 300px;">
		<logic:iterate id="line" name="output">
			<div>
				<bean:write name="line" />
			</div>
		</logic:iterate>
	</div>
</logic:present>