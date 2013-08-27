<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt"%>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants"%>

<html:xhtml/>


<h2><bean:message key="link.firstYearShifts.export"/></h2>

<span class="error"><!-- Error messages go here --><html:errors /></span>

<fr:form action="/exportFirstYearShifts.do?method=export">
	<fr:edit name="first_year_shifts_export">
		<fr:schema type="net.sourceforge.fenixedu.dataTransferObject.resourceAllocationManager.FirstYearShiftsBean" bundle="APPLICATION_RESOURCES">
		    <fr:slot name="executionYear" layout="menu-select" key="property.executionPeriod"
		        validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
		        <fr:property name="providerClass"
		            value="net.sourceforge.fenixedu.presentationTier.renderers.providers.ExecutionYearsProvider" />
		        <fr:property name="format" value="${name}" />
		        <fr:property name="nullOptionHidden" value="true" />
		    </fr:slot>
		    <fr:slot name="entryPhase" key="label.firstTimeStudents.phase"
		        validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
		    </fr:slot>
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes" value="form listInsideClear" />
			<fr:property name="columnClasses" value="width100px,,tderror" />
		</fr:layout>
	</fr:edit>

<p><html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
	<bean:message key="label.choose" />
</html:submit></p>
</fr:form>

