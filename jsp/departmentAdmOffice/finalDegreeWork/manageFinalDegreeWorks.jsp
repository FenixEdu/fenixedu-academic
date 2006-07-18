<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<bean:define id="degreeCurricularPlanID" name="degreeCurricularPlanID" scope="request" />

<html:form action="/manageFinalDegreeWorks">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="prepare"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeCurricularPlanID" property="degreeCurricularPlanID" value="<%= degreeCurricularPlanID.toString() %>"/>

	<bean:define id="executionDegrees" name="degreeCurricularPlan" property="executionDegrees"/>
	<html:select bundle="HTMLALT_RESOURCES" altKey="select.executionDegreeID" property="executionDegreeID" onchange="this.form.submit();">
		<html:option value=""/>
		<html:options collection="executionDegrees" property="idInternal" labelProperty="executionYear.nextExecutionYear.year"/>
	</html:select>
</html:form>
<br/>
<br/>

<logic:present name="executionDegree">
	<logic:notPresent name="executionDegree" property="scheduling">
		<fr:create
				type="net.sourceforge.fenixedu.domain.finalDegreeWork.Scheduleing"
				schema="final.degree.work.scheduleing">
			<fr:hidden slot="currentProposalNumber" value="1" />
			<fr:hidden slot="executionDegrees" multiple="true" name="executionDegree"/>
			<fr:layout name="tabular">
				<fr:property name="classes" value="style1" />
				<fr:property name="columnClasses" value="listClasses,," />
			</fr:layout>
		</fr:create>
	</logic:notPresent>
	<logic:present name="executionDegree" property="scheduling">
		<fr:edit name="executionDegree" property="scheduling"
				type="net.sourceforge.fenixedu.domain.finalDegreeWork.Scheduleing"
				schema="final.degree.work.scheduleing">
			<fr:layout name="tabular">
				<fr:property name="classes" value="style1" />
				<fr:property name="columnClasses" value="listClasses,," />
			</fr:layout>
		</fr:edit>

		<br/>
		<br/>

        <fr:view name="executionDegree" property="scheduling.proposals" schema="final.degree.work.proposal.short">
			<fr:layout name="tabular">
        	    <fr:property name="headerClasses" value="listClasses-header"/>
    	        <fr:property name="columnClasses" value="listClasses"/>
	            <fr:property name="style" value="width: 100%"/>
			</fr:layout>
        </fr:view> 
	</logic:present>
</logic:present>