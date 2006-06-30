<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><strong><bean:message key="label.searchMarkSheet"/></strong></h2>

<fr:form action="/listDFACandidacy.do?method=listCandidacies">
	<fr:edit id="executionDegree"
			 name="candidacyBean"
			 type="net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.candidacy.CreateDFACandidacyBean"
			 schema="candidacy.choose.executionDegree">
		<fr:destination name="postBack" path="/listDFACandidacy.do?method=chooseExecutionDegreePostBack"/>
		<fr:layout name="tabular" >
				<fr:property name="classes" value="tstyle4"/>
		        <fr:property name="columnClasses" value="listClasses,,"/>
		</fr:layout>
	</fr:edit>	
	<html:submit/>
</fr:form>
<br/>
<br/>
<br/>
<logic:present name="candidacies">
	<logic:empty name="candidacies">
		<bean:message key="label.noCandidacies.found"/>
	</logic:empty>
	<logic:notEmpty name="candidacies">
		<fr:view name="candidacies" schema="candidacy.show.list.candidady">
			<fr:layout name="tabular">
		        <fr:property name="classes" value="tstyle2"/>
		        <fr:property name="columnClasses" value=",,,acenter"/>
		        <fr:property name="linkFormat(view)" value="/dfaCandidacy.do?method=viewCandidacy&candidacyID=${idInternal}"/>
				<fr:property name="key(view)" value="link.view"/>
		    </fr:layout>
		</fr:view>
	</logic:notEmpty>
</logic:present>