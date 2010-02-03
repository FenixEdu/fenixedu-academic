<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt"%>
<html:xhtml/>

<h2>Gestão de Salas</h2>

<p>Seleccione a opção pretendida no menu lateral para criar ou editar as características de uma sala.</p>
<%--DevNote: Deveria de haver uma descrição do que se pretende fazer, e o que está envolvido, na criação de salas.--%>
<br/>

<bean:define id="url" type="java.lang.String">/dumpRoomAllocation.do?method=firstPage</bean:define>
<fr:form action="<%= url %>">
	<fr:edit id="dumpContextBean" name="dumpContextBean">
		<fr:schema type="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.DumpRoomAllocationDA$DumpContextBean" bundle="APPLICATION_RESOURCES">
			<fr:slot name="executionPeriod" layout="menu-select-postback">
				<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.ExecutionSemestersProvider"/>
				<fr:property name="choiceType" value="net.sourceforge.fenixedu.domain.ExecutionSemester"/>
				<fr:property name="format" value="${semester} - ${executionYear.year}" />
			</fr:slot>			
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes" value="form listInsideClear" />
			<fr:property name="columnClasses" value="width100px,,tderror" />
		</fr:layout>
	</fr:edit>
</fr:form>

<br/>

<ul>
	<li>
		<html:link action="/dumpRoomAllocation.do?method=downloadRoomLessonOccupationInfo" paramId="executionPeriodId" paramName="dumpContextBean" paramProperty="executionPeriod.externalId">
			Listagem de Ocupação de Salas com Aulas
		</html:link>
	</li>
</ul>