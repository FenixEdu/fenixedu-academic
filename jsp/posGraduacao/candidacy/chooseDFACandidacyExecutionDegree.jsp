<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<html:messages id="message" message="true" bundle="ADMIN_OFFICE_RESOURCES">
	<span class="error"><bean:write name="message" /></span>
	<br/>
</html:messages>
<h2><strong><bean:message key="label.dfaCandidacy.create" bundle="ADMIN_OFFICE_RESOURCES"/></strong></h2>

<fr:form action="/dfaCandidacy.do?method=createCandidacy">
	<fr:edit id="executionDegree"
			 name="candidacyBean"
			 type="net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.candidacy.CreateDFACandidacyBean"
			 schema="candidacy.choose.executionDegree">
		<fr:destination name="degreePostBack" path="/dfaCandidacy.do?method=chooseDegreePostBack"/>
		<fr:destination name="degreeCurricularPlanPostBack" path="/dfaCandidacy.do?method=chooseDegreeCurricularPlanPostBack"/>		
		<fr:destination name="invalid" path="/dfaCandidacy.do?method=chooseExecutionDegreeInvalid"/>		
		<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4"/>
		        <fr:property name="columnClasses" value="listClasses,,"/>
		</fr:layout>
	</fr:edit>
	
	<logic:present name="candidacyBean" property="executionDegree">
		<br/>
		<fr:edit id="person"
			 name="candidacyBean"
			 type="net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.candidacy.CreateDFACandidacyBean"
			 schema="candidacy.create.choose.person">
			<fr:destination name="invalid" path="/dfaCandidacy.do?method=chooseExecutionDegreeInvalid"/>					 
			<fr:layout name="tabular" >
				<fr:property name="classes" value="tstyle4"/>
		        <fr:property name="columnClasses" value="listClasses,,"/>
			</fr:layout>
		</fr:edit>
		<br/>
		<html:submit><bean:message key="button.submit" bundle="ADMIN_OFFICE_RESOURCES"/></html:submit>		
	</logic:present>
</fr:form>