<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<bean:define id="selectedId" name="tsdProcess" property="idInternal"/>

<em><bean:message key="link.teacherServiceDistribution"/></em>
<h2><bean:message key="link.teacherServiceDistribution.omissionValuesValuation"/></h2>

<p class="breadcumbs">
	<em>
		<html:link page='/tsdProcess.do?method=prepareTSDProcess'>
			<bean:message key="link.teacherServiceDistribution"/>
		</html:link>
		>
		<html:link page='<%= "/tsdProcess.do?method=showTSDProcessServices&amp;tsdProcess=" + selectedId %>'>
			<bean:write name="tsdProcess" property="name"/>&nbsp;
			<bean:write name="tsdProcess" property="executionYear.year"/>
		</html:link>
		>
		<bean:message key="link.teacherServiceDistribution.omissionValuesValuation"/>
	</em>
</p>

<ul>
<li>
<html:link page="<%= "/tsdProcessPhasesManagement.do?method=prepareForOmissionValuesValuation&edit=yes&tsdProcess=" +  selectedId %>">
	<bean:message key="link.edit"/>
</html:link>
</li>
</ul>

<bean:define id="edit" value="<%= String.valueOf(request.getParameter("edit")) %>"/>

<logic:notEqual name="edit" value="yes">
<fr:view name="currentTSDProcessPhase" type="net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProcessPhase" schema="omissionValuesValuation.details">
	<fr:layout name="tabular">
         <fr:property name="classes" value="tstyle1 thlight thleft"/>
        <fr:property name="columnClasses" value=",,tdclear tderror1"/>
    </fr:layout>
</fr:view>
</logic:notEqual>

<logic:equal name="edit" value="yes">
<fr:form action="<%= "/tsdProcessPhasesManagement.do?method=prepareForOmissionValuesValuation&edit=no&tsdProcess=" + selectedId %>" >
<fr:edit name="currentTSDProcessPhase" type="net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDProcessPhase" schema="omissionValuesValuation.details">
	<fr:layout name="tabular">
       <fr:property name="classes" value="tstyle5 thlight thleft mbottom05"/>
        <fr:property name="columnClasses" value=",,tdclear tderror1"/>
    </fr:layout>
</fr:edit>

<p class="mtop0">
	<html:submit> 
		<bean:message key="button.submit"/>
	</html:submit>
</p>
</fr:form>
</logic:equal>

<br/>
<br/>

<html:link page='<%= "/tsdProcess.do?method=showTSDProcessServices&amp;tsdProcess=" + selectedId %>'>
	<bean:message key="link.back"/>
</html:link>