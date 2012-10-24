<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<html:xhtml/>

<logic:present name="a3esBean">
	<fr:edit id="a3esBean" name="a3esBean" action="/a3es.do?method=exportA3es">
		<fr:schema bundle="GEP_RESOURCES" type="net.sourceforge.fenixedu.presentationTier.Action.gep.a3es.A3esBean">
			<fr:slot name="degreeType" key="label.degreeType" layout="menu-postback" required="true">
				<fr:property name="destination" value="cycleType-postBack"/>
			</fr:slot>
			<fr:slot name="degree" key="label.gep.degree" layout="menu-select" required="true">
				<fr:property name="from" value="availableDegrees"/>
				<fr:property name="format" value="${presentationName}"/>
			</fr:slot>
			<fr:slot name="executionSemester" key="label.gep.courseSemester" layout="menu-select" required="true">
				<fr:property name="from" value="availableExecutionSemesters"/>
				<fr:property name="format" value="${previousExecutionPeriod.qualifiedName} - ${qualifiedName}"/>
			</fr:slot>
			<fr:destination name="cycleType-postBack" path="/a3es.do?method=chooseDegreeAndSemesters" />
		</fr:schema>
	</fr:edit>
</logic:present>