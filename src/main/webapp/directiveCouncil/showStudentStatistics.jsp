<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/enum" prefix="e" %>
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

