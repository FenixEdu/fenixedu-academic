<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<html:xhtml/>

<!--LANGUAGE SWITCHER -->
<div id="version">
	<img class="activeflag" src="Candidato%20%20Licenciatura%20_%20IST_files/icon_pt.gif" alt="Português">
	<a href="<%= net.sourceforge.fenixedu.domain.Instalation.getInstance().getInstituitionURL() %>en/htm/profile/pstudent/lic/"><img src="Candidato%20%20Licenciatura%20_%20IST_files/icon_en.gif" alt="English" border="0"></a>
</div>
<!--END LANGUAGE SWITCHER -->
<bean:define id="mappingPath" name="mappingPath"/>
<bean:define id="fullPath"><%= request.getContextPath() + "/publico" + mappingPath + ".do" %></bean:define>

<div class="breadcumbs">
	<a href="#"><%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%></a> &gt;
	<a href="#">Candidato</a> &gt;
	<a href="#">Candidaturas</a> &gt;
	<a href='<%= fullPath + "?method=candidacyIntro" %>'>Licenciaturas</a> &gt;
	<a href='<%= fullPath + "?method=beginCandidacyProcessIntro" %>'><bean:write name="application.name"/> </a> &gt;
	Consultar Candidatura
</div>

<h1><bean:message key="label.candidacy" bundle="APPLICATION_RESOURCES"/>: <bean:write name="application.name" bundle="APPLICATION_RESOURCES"/></h1>
<fr:form action='<%= mappingPath + ".do?method=authenticationCandidacyRequest" %>' encoding="multipart/form-data">
	<fr:edit id="credentialsBean" 
		 name="credentialsBean" 
		 schema="PublicAccessCandidacy.credentials">
	  <fr:layout name="tabular">
 	    <fr:property name="columnClasses" value="nowrap,acenter"/>
	  </fr:layout>
	</fr:edit>
	
	<html:submit onclick="this.form.method.value='submitCandidacy';"><bean:message key="button.continue" bundle="APPLICATION_RESOURCES" /></html:submit>
	<html:cancel><bean:message key="label.back" bundle="APPLICATION_RESOURCES" /></html:cancel>		
</fr:form>
