<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e" %>
<html:xhtml/>

<h2><bean:message key="title.statistics.students" /></h2>

<logic:present name="contextBean">
	<fr:form>
		<fr:edit name="contextBean" id="contextBean" type="net.sourceforge.fenixedu.presentationTier.Action.directiveCouncil.StudentStatisticsDA$ContextBean" 
				schema="student.statistics.bean">
			<fr:layout>
				<fr:property name="classes" value="thlight mbottom1"/>
			</fr:layout>
		</fr:edit>
	</fr:form>
</logic:present>

<logic:present name="statisticsBean">
	<logic:equal name="statisticsBean" property="showResult" value="true">
		<br/>
		<fr:view name="statisticsBean" type="net.sourceforge.fenixedu.presentationTier.Action.directiveCouncil.StudentStatisticsDA$StatisticsBean" 
				schema="student.statistics.bean.results">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2 thlight"/>
				<fr:property name="columnClasses" value="smalltxt color888,,acenter,acenter,"/>
			</fr:layout>
		</fr:view>
	</logic:equal>
</logic:present>

