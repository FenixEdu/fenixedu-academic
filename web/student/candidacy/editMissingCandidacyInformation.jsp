<%@page import="net.sourceforge.fenixedu.domain.candidacy.PersonalInformationBean"%>
<%@page import="org.joda.time.LocalDate"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@page import="net.sourceforge.fenixedu.presentationTier.Action.grant.utils.PresentationConstants"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="enum" %>

<div style="float: right;">
	<jsp:include page="../../i18n.jsp"/>
</div>
<script type="text/javascript">
hideButtons();
</script>

<bean:define id="userView" name="<%=pt.ist.fenixWebFramework.servlets.filters.SetUserViewFilter.USER_SESSION_ATTRIBUTE%>" ></bean:define>
<logic:present name="userView" property="person.student">

<h2><bean:message  key="label.fill.information" bundle="STUDENT_RESOURCES"/></h2>

<div class="infoop2 mtop2">
	<strong>
		<bean:message  key="label.fill.missing.candidacy.information.message" bundle="STUDENT_RESOURCES"/>
	</strong>	
</div>

<br/>

<bean:define id="personalInformationBean" name="personalInformationBean" type="net.sourceforge.fenixedu.domain.candidacy.PersonalInformationBean"/>
<div id="skip">
<% if (PersonalInformationBean.isPastLimitDate()) { %>
	<span class="warning0"><bean:message key="label.RAIDES.skip.enabled" bundle="STUDENT_RESOURCES"/> <%= personalInformationBean.getLimitDate().toString() %>.</span>
	<br/>
	<span class="warning0"><bean:message key="label.RAIDES.skip.enabled.more" bundle="STUDENT_RESOURCES"/></span>
<% } else { %>
	<span class="warning0"><bean:message key="label.RAIDES.skip.disabled" bundle="STUDENT_RESOURCES"/> <%= personalInformationBean.getLimitDate().toString() %>.</span>
<% } %>
</div>

<br/>

<bean:define id="personalInformationBean" name="personalInformationBean" type="net.sourceforge.fenixedu.domain.candidacy.PersonalInformationBean" />

<logic:messagesPresent message="true">
	<ul class="nobullet list6">
		<html:messages id="messages" message="true" bundle="APPLICATION_RESOURCES">
			<li><span class="error0"><bean:write name="messages" /></span></li>
		</html:messages>
	</ul>
</logic:messagesPresent>

<br/>

<h3><bean:write name="personalInformationBean" property="description"/></h3>
		
<br/>
<br/>

<fr:form action="/editMissingCandidacyInformation.do?method=edit">

	<fr:edit id="personalInformationBean" name="personalInformationBean" visible="false" />

	<h3><bean:message  key="label.previous.degree.information" bundle="STUDENT_RESOURCES"/></h3>
	
	<div><bean:message  key="label.previous.degree.information.details" bundle="STUDENT_RESOURCES"/></div>
	
	<fr:edit id="personalInformationBean.editPrecedentDegreeInformation" name="personalInformationBean">
		<fr:schema bundle="APPLICATION_RESOURCES" type="net.sourceforge.fenixedu.domain.candidacy.PersonalInformationBean">
			<fr:slot name="schoolLevel" layout="menu-select-postback" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
				<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.candidacy.SchoolLevelTypeForStudentProvider" />
			</fr:slot>
			<fr:slot name="otherSchoolLevel" />
			<fr:slot name="countryWhereFinishedPrecedentDegree" layout="menu-select-postback" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator"> 
				<fr:property name="format" value="${localizedName}"/>
				<fr:property name="sortBy" value="name=asc" />
				<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.DistinctCountriesProvider" />
			</fr:slot>	
			<% if (personalInformationBean.isUnitFromRaidesListMandatory()) { %>
				<fr:slot name="institutionUnitName" layout="autoComplete" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
					<fr:property name="size" value="50"/>
					<fr:property name="labelField" value="unit.name"/>
					<fr:property name="indicatorShown" value="true"/>
					<fr:property name="serviceName" value="SearchRaidesDegreeUnits"/>
					<fr:property name="serviceArgs" value="slot=name,size=50"/>
					<fr:property name="className" value="net.sourceforge.fenixedu.domain.organizationalStructure.UnitName"/>
					<fr:property name="minChars" value="3"/>
					<fr:property name="rawSlotName" value="institutionName"/>
				</fr:slot>
				<fr:slot name="raidesDegreeDesignation" layout="autoComplete" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
			    	<fr:property name="size" value="50"/>
					<fr:property name="labelField" value="description"/>
					<fr:property name="indicatorShown" value="true"/>
					<fr:property name="serviceName" value="SearchRaidesDegreeDesignations"/>
					<fr:property name="serviceArgs" value="slot=description,size=50"/>
					<fr:property name="className" value="net.sourceforge.fenixedu.domain.raides.DegreeDesignation"/>
					<fr:property name="minChars" value="3"/>
			    </fr:slot>
		    <% } else { %>
				<fr:slot name="institutionUnitName" layout="autoComplete" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
					<fr:property name="size" value="50"/>
					<fr:property name="labelField" value="unit.name"/>
					<fr:property name="indicatorShown" value="true"/>
					<fr:property name="serviceName" value="SearchExternalUnitsWithScore"/>
					<fr:property name="serviceArgs" value="slot=name,size=20"/>
					<fr:property name="className" value="net.sourceforge.fenixedu.domain.organizationalStructure.UnitName"/>
					<fr:property name="minChars" value="3"/>
					<fr:property name="rawSlotName" value="institutionName"/>
				</fr:slot>
				<fr:slot name="degreeDesignation" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
			    	<fr:property name="size" value="50"/>
					<fr:property name="maxLength" value="255"/>
			    </fr:slot>
		    <% } %>
			<fr:slot name="conclusionGrade" validator="net.sourceforge.fenixedu.presentationTier.Action.student.candidacy.EditMissingCandidacyInformationDA$ConclusionGradeRegexpValidator">
		    	<fr:property name="size" value="2"/>
				<fr:property name="maxLength" value="2"/>
			</fr:slot>		    
		    <fr:slot name="conclusionYear" validator="net.sourceforge.fenixedu.presentationTier.Action.student.candidacy.EditMissingCandidacyInformationDA$ConclusionYearRegexpValidator">
		       	<fr:property name="size" value="4"/>
				<fr:property name="maxLength" value="4"/>
		    </fr:slot>				
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thleft mtop15" />
			<fr:property name="columnClasses" value="width300px,,tdclear tderror1"/>
			<fr:destination name="invalid" path="/editMissingCandidacyInformation.do?method=prepareEditInvalid" />
			<fr:destination name="postback" path="/editMissingCandidacyInformation.do?method=prepareEditPostback" />
		</fr:layout>
	</fr:edit>
	
	<br/>

	<h3><bean:message  key="label.personal.information" bundle="STUDENT_RESOURCES"/></h3>
	
	<div><bean:message  key="label.personal.information.details" bundle="STUDENT_RESOURCES"/></div>
	
	<fr:edit id="personalInformationBean.editPersonalInformation" name="personalInformationBean">
		<fr:schema bundle="APPLICATION_RESOURCES" type="net.sourceforge.fenixedu.domain.candidacy.PersonalInformationBean">
			<fr:slot name="countryOfResidence" layout="menu-select" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator"> 
					<fr:property name="format" value="${localizedName}"/>
					<fr:property name="sortBy" value="name=asc" />
					<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.DistinctCountriesProvider" />
				</fr:slot>
				<fr:slot name="districtSubdivisionOfResidence" layout="autoComplete">
					<fr:property name="size" value="50"/>
					<fr:property name="labelField" value="name"/>
					<fr:property name="format" value="${name} - (${district.name})"/>
					<fr:property name="indicatorShown" value="true"/>		
					<fr:property name="serviceName" value="SearchDistrictSubdivisions"/>
					<fr:property name="serviceArgs" value="slot=name,size=50"/>
					<fr:property name="className" value="net.sourceforge.fenixedu.domain.DistrictSubdivision"/>
					<fr:property name="minChars" value="1"/>
				</fr:slot>
				<fr:slot name="dislocatedFromPermanentResidence" layout="radio" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
					<fr:property name="classes" value="dinline liinline nobullet"/>
				</fr:slot>
				<fr:slot name="schoolTimeDistrictSubdivisionOfResidence" layout="autoComplete">
					<fr:property name="size" value="50"/>
					<fr:property name="labelField" value="name"/>
					<fr:property name="format" value="${name} - (${district.name})"/>
					<fr:property name="indicatorShown" value="true"/>		
					<fr:property name="serviceName" value="SearchDistrictSubdivisions"/>
					<fr:property name="serviceArgs" value="slot=name,size=50"/>
					<fr:property name="className" value="net.sourceforge.fenixedu.domain.DistrictSubdivision"/>
					<fr:property name="minChars" value="1"/>
				</fr:slot>
				<fr:slot name="grantOwnerType" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator"/>
			   	<fr:slot name="grantOwnerProviderUnitName" layout="autoComplete">
					<fr:property name="size" value="50"/>
					<fr:property name="labelField" value="unit.name"/>
					<fr:property name="indicatorShown" value="true"/>
					<fr:property name="serviceName" value="SearchExternalUnits"/>
					<fr:property name="serviceArgs" value="slot=name,size=50"/>
					<fr:property name="className" value="net.sourceforge.fenixedu.domain.organizationalStructure.UnitName"/>
					<fr:property name="minChars" value="1"/>
					<fr:property name="rawSlotName" value="grantOwnerProviderName"/>
				</fr:slot>
				<% if ((personalInformationBean.getSchoolLevel() != null) && (personalInformationBean.getSchoolLevel().isHighSchoolOrEquivalent())) { %>
					<fr:slot name="highSchoolType" layout="menu-select">
						<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.candidacy.HighSchoolTypesProvider" />
						<fr:property name="eachLayout" value="this-does-not-exist" />
					</fr:slot>
				<% } %>
				<fr:slot name="maritalStatus" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
					<fr:property name="excludedValues" value="UNKNOWN" />
				</fr:slot>
				<fr:slot name="professionalCondition" layout="menu-select" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
					<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.ProfessionalSituationConditionTypeProviderForRaides2011"/>
				</fr:slot>
				<fr:slot name="professionType" layout="menu-select" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator"> 
					<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.ActiveProfessionTypeProvider" />
				</fr:slot>
				<fr:slot name="motherSchoolLevel" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator" layout="menu-select">
					<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.candidacy.SchoolLevelTypeForStudentHouseholdProvider" />
					<fr:property name="eachLayout" value="this-does-not-exist" />
				</fr:slot>	
				<fr:slot name="motherProfessionalCondition" layout="menu-select" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
					<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.ProfessionalSituationConditionTypeProviderForRaides2011"/>
				</fr:slot>
				<fr:slot name="motherProfessionType" layout="menu-select" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator"> 
					<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.ActiveProfessionTypeProvider" />
				</fr:slot>
				<fr:slot name="fatherSchoolLevel" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator" layout="menu-select">
					<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.candidacy.SchoolLevelTypeForStudentHouseholdProvider" />
					<fr:property name="eachLayout" value="this-does-not-exist" />
				</fr:slot>
				<fr:slot name="fatherProfessionalCondition" layout="menu-select" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
					<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.ProfessionalSituationConditionTypeProviderForRaides2011"/>
				</fr:slot>
				<fr:slot name="fatherProfessionType" layout="menu-select" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator"> 
					<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.ActiveProfessionTypeProvider" />
				</fr:slot>
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thleft mtop15" />
			<fr:property name="columnClasses" value="width300px,,tdclear tderror1"/>
			<fr:destination name="invalid" path="/editMissingCandidacyInformation.do?method=prepareEditInvalid" />
		</fr:layout>
	</fr:edit>

	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit"><bean:message bundle="APPLICATION_RESOURCES" key="label.submit"/></html:submit>
	
</fr:form>

<br/>

<bean:define id="personalInformationBean" name="personalInformationBean" type="net.sourceforge.fenixedu.domain.candidacy.PersonalInformationBean"/>
<div id="skip">
<% if (PersonalInformationBean.isPastLimitDate()) { %>
	<form action="<%= request.getContextPath() + "/home.do" %>">
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit"><bean:message bundle="APPLICATION_RESOURCES" key="button.inquiries.respond.later"/></html:submit>
	</form>
	<br/>
<% } %>
</div>

</logic:present>

