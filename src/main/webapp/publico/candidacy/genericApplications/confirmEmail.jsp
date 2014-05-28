<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page isELIgnored="true"%>
<%@page import="net.sourceforge.fenixedu.domain.candidacy.util.GenericApplicationRecommendationBean"%>
<%@page import="net.sourceforge.fenixedu.domain.candidacy.GenericApplicationRecomentation"%>
<%@page import="org.apache.struts.action.ActionMessages"%>
<%@page import="net.sourceforge.fenixedu.domain.candidacy.GenericApplicationFile"%>
<%@page import="net.sourceforge.fenixedu.domain.candidacy.GenericApplication"%>
<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@page import="net.sourceforge.fenixedu.domain.period.GenericApplicationPeriod"%>
<%@page import="java.util.SortedSet"%>

<html:xhtml/>

<%
	final GenericApplication genericApplication = (GenericApplication) request.getAttribute("application");
	final GenericApplicationRecommendationBean recommendationBean = (GenericApplicationRecommendationBean) request.getAttribute("recommendationBean");
%>

<bean:define id="unsavedChangesMessage" type="java.lang.String"><bean:message bundle="CANDIDATE_RESOURCES" key="label.application.unsaved.changes"/></bean:define>

<script src="https://rawgithub.com/timrwood/moment/2.0.0/moment.js"></script>
<script>
	var fieldChanged = false;

   	function toggleById(id) {
   		$(id).toggle();
	}

   	function toggleByIdWithChangeCheck(id) {
   		if (fieldChanged) {
   			alert("<%= unsavedChangesMessage.replace('\n', ' ') %>");
   		}
   		$(id).toggle();
	}

   	function validateInputField(field) {
   		var fieldName = "<%= GenericApplication.class.getName() + ":" + genericApplication.getExternalId() %>" + ":" + field;
   		var className = '#empty' + field;
   		var fieldValue = $('input[name$="' + fieldName + '"]').val();
   		if (!fieldValue) { $(className).show(); return false; } else { $(className).hide(); return true; }
   	}

   	function validateSelectField(field) {
   		var fieldName = "<%= GenericApplication.class.getName() + ":" + genericApplication.getExternalId() %>" + ":" + field;
   		var className = '#empty' + field;
   		var fieldValue = $('select[name$="' + fieldName + '"]').val();
   		if (!fieldValue) { $(className).show(); return false; } else { $(className).hide(); return true; }
   	}

   	function validateDateField(field) {
   		var fieldName = "<%= GenericApplication.class.getName() + ":" + genericApplication.getExternalId() %>" + ":" + field;
   		var classNameEmpty = '#empty' + field;
   		var classNameFormat = '#badDateFormat' + field;
   		var fieldValue = $('input[name$="' + fieldName + '"]').val();
   		if (!fieldValue) {
   			$(classNameEmpty).show();
   			$(classNameFormat).hide();
   			return false;
   		} else {
   			$(classNameEmpty).hide();
   			if (moment(fieldValue, "DD/MM/YYYY").isValid()) {
   				$(classNameFormat).hide();
   				return true;
   			} else {
   				$(classNameFormat).show();
   				return false;
   			}
   		}
   	}
   	
   	function validateInput() {
   		var allIsOk = true;
   		if (!validateInputField("name")) { allIsOk = false; };
   		if (!validateSelectField("gender")) { allIsOk = false; };
   		if (!validateDateField("dateOfBirthYearMonthDay")) { allIsOk = false; };
   		if (!validateInputField("documentIdNumber")) { allIsOk = false; };
   		if (!validateSelectField("idDocumentType")) { allIsOk = false; };
   		if (!validateSelectField("nationality")) { allIsOk = false; };
   		// if (!validateInputField("fiscalCode")) { allIsOk = false; };
   		if (!validateInputField("address")) { allIsOk = false; };
   		if (!validateInputField("areaCode")) { allIsOk = false; };
   		if (!validateInputField("area")) { allIsOk = false; };
   		if (!validateInputField("telephoneContact")) { allIsOk = false; };
   		return allIsOk;
	}

<% if (recommendationBean != null) { %>

   	function validateRecommendationInputField(field) {
   		var fieldName = "<%= GenericApplicationRecommendationBean.class.getName() + ":" + recommendationBean.hashCode() %>" + ":" + field;
   		var className = '#emptyRecommendationAllFields';
   		var fieldValue = $('input[name$="' + fieldName + '"]').val();
   		if (!fieldValue) { $(className).show(); return false; } else { $(className).hide(); return true; }
   	}

   	function validateRecommendationInput() {
   		var allIsOk = true;
   		if (!validateRecommendationInputField("title")) { allIsOk = false; };
   		if (!validateRecommendationInputField("name")) { allIsOk = false; };
   		if (!validateRecommendationInputField("email")) { allIsOk = false; };
   		if (!validateRecommendationInputField("institution")) { allIsOk = false; };
   		return allIsOk;
	}
<% } %>

	function addChangeListnerToInputField(type, field) {
   		var fieldName = "<%= GenericApplication.class.getName() + ":" + genericApplication.getExternalId() %>" + ":" + field;
   		$(type + '[name$="' + fieldName + '"]').change(function() {
   			fieldChanged = true;
        });
   	}
</script>


<h2>
	<bean:message bundle="CANDIDATE_RESOURCES" key="label.application.periods"/>
	<br/>
	<%= genericApplication.getGenericApplicationPeriod().getTitle().getContent() %>
</h2>

<br/>

<div class="infoop">
	<%= genericApplication.getGenericApplicationPeriod().getDescription() %>
</div>

<logic:present name="applicationSaved">
	<br/>
	<div class="infoop5">
		<bean:message bundle="CANDIDATE_RESOURCES" key="label.application.saved"/>
	</div>
</logic:present>

<bean:define id="callbackUrl">/genericApplications.do?method=confirmEmail&applicationExternalId=<%= genericApplication.getExternalId() %>&confirmationCode=<%= genericApplication.getConfirmationCode() %></bean:define>
<fr:form id="genericApplicationForm" action="/genericApplications.do" encoding="multipart/form-data">
	<input type="hidden" name="method" value="saveApplication"/>
	<input type="hidden" name="applicationExternalId" value="<%= genericApplication.getExternalId() %>"/>
	<input type="hidden" name="confirmationCode" value="<%= genericApplication.getConfirmationCode() %>"/>
	<table class="tstyle2 thlight thcenter mtop15">
		<tr>
			<td>
				<bean:message bundle="CANDIDATE_RESOURCES" key="label.application.number"/>
			</td>
			<td>
				<%= genericApplication.getApplicationNumber() %>
			</td>
		</tr>
		<tr>
			<td>
				<bean:message bundle="CANDIDATE_RESOURCES" key="label.application.candidate.email"/>
			</td>
			<td>
				<%= genericApplication.getEmail() %>
			</td>
		</tr>
		<tr>
			<td>
				<bean:message bundle="CANDIDATE_RESOURCES" key="label.full.name"/>
			</td>
			<td>
				<logic:present name="uploadBean">
					<fr:edit id="genericApplicationFormName" name="application">
						<fr:schema bundle="CANDIDATE_RESOURCES" type="net.sourceforge.fenixedu.domain.period.GenericApplicationPeriod">
							<fr:slot name="name" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
								<fr:property name="size" value="50"/>
							</fr:slot>
						</fr:schema>
						<fr:layout name="flow">
							<fr:property name="labelExcluded" value="true"/>
						</fr:layout>
						<fr:destination name="invalid" path="<%= callbackUrl %>" />
						<fr:destination name="cancel" path="<%= callbackUrl %>" />
					</fr:edit>
					<div id="emptyname" class="error" style="display: none;"><bean:message bundle="CANDIDATE_RESOURCES" key="label.field.is.required"/></div>
				</logic:present>
				<logic:notPresent name="uploadBean">
					<%= genericApplication.getName() %>
				</logic:notPresent>
			</td>
		</tr>
		<tr>
			<td>
				<br/>
				<h2>
					<bean:message bundle="CANDIDATE_RESOURCES" key="label.application.personal.data"/>
				</h2>
			</td>
			<td style="margin: 0 0 0.5em 0;">
				<br/>
				<a href="#" onclick="toggleById('.personalInformation');" style="float: right;" class="personalInformation">
					(<bean:message bundle="CANDIDATE_RESOURCES" key="label.hide"/>)
				</a>
				<a href="#" onclick="toggleById('.personalInformation');" style="float: right; display: none;" class="personalInformation">
					(<bean:message bundle="CANDIDATE_RESOURCES" key="label.show"/>)			
				</a>
			</td>
		</tr>
		<tr class="personalInformation">
			<td>
				<bean:message bundle="CANDIDATE_RESOURCES" key="label.gender"/>
			</td>
			<td>
				<logic:present name="uploadBean">
					<fr:edit id="genericApplicationFormGender" name="application">
						<fr:schema bundle="CANDIDATE_RESOURCES" type="net.sourceforge.fenixedu.domain.period.GenericApplicationPeriod">
							<fr:slot name="gender" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator"/>
						</fr:schema>
						<fr:layout name="flow">
							<fr:property name="labelExcluded" value="true"/>
						</fr:layout>
						<fr:destination name="invalid" path="<%= callbackUrl %>" />
						<fr:destination name="cancel" path="<%= callbackUrl %>" />
					</fr:edit>
					<div id="emptygender" class="error" style="display: none;"><bean:message bundle="CANDIDATE_RESOURCES" key="label.field.is.required"/></div>
				</logic:present>
				<logic:notPresent name="uploadBean">
					<%= genericApplication.getGender() == null ? "" : genericApplication.getGender().getLocalizedName() %>
				</logic:notPresent>
			</td>
		</tr>
		<tr class="personalInformation">
			<td>
				<bean:message bundle="CANDIDATE_RESOURCES" key="label.dateOfBirth"/>
			</td>
			<td>
				<logic:present name="uploadBean">
					<fr:edit id="genericApplicationFormDateOfBirth" name="application">
						<fr:schema bundle="CANDIDATE_RESOURCES" type="net.sourceforge.fenixedu.domain.period.GenericApplicationPeriod">
							<fr:slot name="dateOfBirthYearMonthDay" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
								<fr:validator name="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator"/>
								<fr:validator name="pt.ist.fenixWebFramework.renderers.validators.AdvancedDateValidator">
									<fr:property name="validationPeriod" value="PAST"/>
									<fr:property name="bundle" value="CANDIDATE_RESOURCES"/>
									<fr:property name="message" value="error.birth.date.not.less.actual.date"/>
								</fr:validator>
								<fr:property name="size" value="10"/>
								<fr:property name="maxLength" value="10"/>
							</fr:slot>
						</fr:schema>
						<fr:layout name="flow">
							<fr:property name="labelExcluded" value="true"/>
						</fr:layout>
						<fr:destination name="invalid" path="<%= callbackUrl %>" />
						<fr:destination name="cancel" path="<%= callbackUrl %>" />
					</fr:edit>
					<div id="emptydateOfBirthYearMonthDay" class="error" style="display: none;"><bean:message bundle="CANDIDATE_RESOURCES" key="label.field.is.required"/></div>
					<div id="badDateFormatdateOfBirthYearMonthDay" class="error" style="display: none;"><bean:message bundle="CANDIDATE_RESOURCES" key="label.the.correct.format.is"/> dd/MM/yyyy</div>
				</logic:present>
				<logic:notPresent name="uploadBean">
					<%= genericApplication.getDateOfBirthYearMonthDay() == null ? "" : genericApplication.getDateOfBirthYearMonthDay().toString("dd/MM/yyyy") %>
				</logic:notPresent>
			</td>
		</tr>
		<tr class="personalInformation">
			<td>
				<bean:message bundle="CANDIDATE_RESOURCES" key="label.identificationNumber"/>
			</td>
			<td>
				<logic:present name="uploadBean">
					<fr:edit id="genericApplicationFormGenderIdNumber" name="application">
						<fr:schema bundle="CANDIDATE_RESOURCES" type="net.sourceforge.fenixedu.domain.period.GenericApplicationPeriod">
							<fr:slot name="documentIdNumber" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
								<fr:validator name="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator"/>
								<fr:validator name="net.sourceforge.fenixedu.presentationTier.renderers.validators.TextLengthValidator">
									<fr:property name="type" value="character"/>
									<fr:property name="length" value="50"/>
								</fr:validator>
								<fr:property name="size" value="12"/>
								<fr:property name="maxLength" value="50"/>
							</fr:slot>
						</fr:schema>
						<fr:layout name="flow">
							<fr:property name="labelExcluded" value="true"/>
						</fr:layout>
						<fr:destination name="invalid" path="<%= callbackUrl %>" />
						<fr:destination name="cancel" path="<%= callbackUrl %>" />
					</fr:edit>
					<fr:edit id="genericApplicationFormGenderIdDocType" name="application">
						<fr:schema bundle="CANDIDATE_RESOURCES" type="net.sourceforge.fenixedu.domain.period.GenericApplicationPeriod">
							<fr:slot name="idDocumentType" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator"/>
						</fr:schema>
						<fr:layout name="flow">
							<fr:property name="labelExcluded" value="true"/>
						</fr:layout>
						<fr:destination name="invalid" path="<%= callbackUrl %>" />
						<fr:destination name="cancel" path="<%= callbackUrl %>" />
					</fr:edit>
					<div id="emptydocumentIdNumber" class="error" style="display: none;"><bean:message bundle="CANDIDATE_RESOURCES" key="label.field.is.required"/></div>
					<div id="emptyidDocumentType" class="error" style="display: none;"><bean:message bundle="CANDIDATE_RESOURCES" key="label.field.is.required"/></div>
				</logic:present>
				<logic:notPresent name="uploadBean">
					<%= genericApplication.getDocumentIdNumber() %> &nbsp;&nbsp;&nbsp; <%= genericApplication.getIdDocumentType() == null ? "" : genericApplication.getIdDocumentType().getLocalizedName() %>
				</logic:notPresent>
			</td>
		</tr>
		<tr class="personalInformation">
			<td>
				<bean:message bundle="CANDIDATE_RESOURCES" key="label.nationality"/>
			</td>
			<td>
				<logic:present name="uploadBean">
					<fr:edit id="genericApplicationFormGenderNationality" name="application">
						<fr:schema bundle="CANDIDATE_RESOURCES" type="net.sourceforge.fenixedu.domain.period.GenericApplicationPeriod">
							<fr:slot name="nationality" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator"
									layout="menu-select">
								<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.CountryProvider"/>
								<fr:property name="format" value="${localizedName}" />
								<fr:property name="sortBy" value="localizedName"/>
							</fr:slot>
						</fr:schema>
						<fr:layout name="flow">
							<fr:property name="labelExcluded" value="true"/>
						</fr:layout>
						<fr:destination name="invalid" path="<%= callbackUrl %>" />
						<fr:destination name="cancel" path="<%= callbackUrl %>" />
					</fr:edit>
					<div id="emptynationality" class="error" style="display: none;"><bean:message bundle="CANDIDATE_RESOURCES" key="label.field.is.required"/></div>
				</logic:present>
				<logic:notPresent name="uploadBean">
					<%= genericApplication.getNationality() == null ? "" : genericApplication.getNationality().getCountryNationality() %>
				</logic:notPresent>
			</td>
		</tr>
		<tr class="personalInformation">
			<td>
				<bean:message bundle="CANDIDATE_RESOURCES" key="label.fiscalCode"/>
			</td>
			<td>
				<logic:present name="uploadBean">
					<fr:edit id="genericApplicationFormGenderFiscalCode" name="application">
						<fr:schema bundle="CANDIDATE_RESOURCES" type="net.sourceforge.fenixedu.domain.period.GenericApplicationPeriod">
							<fr:slot name="fiscalCode">
								<fr:validator name="net.sourceforge.fenixedu.presentationTier.renderers.validators.TextLengthValidator">
									<fr:property name="type" value="character"/>
									<fr:property name="length" value="50"/>
								</fr:validator>			 	
								<fr:property name="size" value="20" />
								<fr:property name="maxLength" value="50" />
							</fr:slot>
						</fr:schema>
						<fr:layout name="flow">
							<fr:property name="labelExcluded" value="true"/>
						</fr:layout>
						<fr:destination name="invalid" path="<%= callbackUrl %>" />
						<fr:destination name="cancel" path="<%= callbackUrl %>" />
					</fr:edit>
					<html:img page="/images/icon_help.gif" module="" titleKey="label.fiscalCode.help" bundle="CANDIDATE_RESOURCES" onclick="toggleById('#fiscalCodeHelpExplanation');"/>
					<div id="fiscalCodeHelpExplanation" class="infoop4" style="display: none;">
						<pre><bean:message key="label.fiscalCode.help" bundle="CANDIDATE_RESOURCES"/></pre>
					</div>
					<div id="emptyfiscalCode" class="error" style="display: none;"><bean:message bundle="CANDIDATE_RESOURCES" key="label.field.is.required"/></div>
				</logic:present>
				<logic:notPresent name="uploadBean">
					<%= genericApplication.getFiscalCode() %>
				</logic:notPresent>
			</td>
		</tr>
		<tr class="personalInformation">
			<td>
				<bean:message bundle="CANDIDATE_RESOURCES" key="label.address"/>
			</td>
			<td>
				<logic:present name="uploadBean">
					<fr:edit id="genericApplicationFormGenderAddress" name="application">
						<fr:schema bundle="CANDIDATE_RESOURCES" type="net.sourceforge.fenixedu.domain.period.GenericApplicationPeriod">
							<fr:slot name="address" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
								<fr:validator name="net.sourceforge.fenixedu.presentationTier.renderers.validators.TextLengthValidator">
									<fr:property name="type" value="character"/>
									<fr:property name="length" value="100"/>
								</fr:validator>			 	
								<fr:property name="size" value="50" />
								<fr:property name="maxLength" value="100" />
							</fr:slot>
						</fr:schema>
						<fr:layout name="flow">
							<fr:property name="labelExcluded" value="true"/>
						</fr:layout>
						<fr:destination name="invalid" path="<%= callbackUrl %>" />
						<fr:destination name="cancel" path="<%= callbackUrl %>" />
					</fr:edit>
					<div id="emptyaddress" class="error" style="display: none;"><bean:message bundle="CANDIDATE_RESOURCES" key="label.field.is.required"/></div>
				</logic:present>
				<logic:notPresent name="uploadBean">
					<%= genericApplication.getAddress() %>
				</logic:notPresent>				
			</td>
		</tr>
		<tr class="personalInformation">
			<td>
				<bean:message bundle="CANDIDATE_RESOURCES" key="label.areaCode"/>
				/
				<bean:message bundle="CANDIDATE_RESOURCES" key="label.area"/>
			</td>
			<td>
				<logic:present name="uploadBean">
					<fr:edit id="genericApplicationFormGenderAreaCode" name="application">
						<fr:schema bundle="CANDIDATE_RESOURCES" type="net.sourceforge.fenixedu.domain.period.GenericApplicationPeriod">
							<fr:slot name="areaCode" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
								<fr:validator name="net.sourceforge.fenixedu.presentationTier.renderers.validators.TextLengthValidator">
									<fr:property name="type" value="character"/>
									<fr:property name="length" value="20"/>
								</fr:validator>			 	
								<fr:property name="size" value="10" />
								<fr:property name="maxLength" value="20" />
							</fr:slot>
						</fr:schema>
						<fr:layout name="flow">
							<fr:property name="labelExcluded" value="true"/>
						</fr:layout>
						<fr:destination name="invalid" path="<%= callbackUrl %>" />
						<fr:destination name="cancel" path="<%= callbackUrl %>" />
					</fr:edit>
					<fr:edit id="genericApplicationFormGenderArea" name="application">
						<fr:schema bundle="CANDIDATE_RESOURCES" type="net.sourceforge.fenixedu.domain.period.GenericApplicationPeriod">
							<fr:slot name="area" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
								<fr:validator name="net.sourceforge.fenixedu.presentationTier.renderers.validators.TextLengthValidator">
									<fr:property name="type" value="character"/>
									<fr:property name="length" value="50"/>
								</fr:validator>			 	
								<fr:property name="size" value="20" />
								<fr:property name="maxLength" value="50" />
							</fr:slot>
						</fr:schema>
						<fr:layout name="flow">
							<fr:property name="labelExcluded" value="true"/>
						</fr:layout>
						<fr:destination name="invalid" path="<%= callbackUrl %>" />
						<fr:destination name="cancel" path="<%= callbackUrl %>" />
					</fr:edit>
					<div id="emptyareaCode" class="error" style="display: none;"><bean:message bundle="CANDIDATE_RESOURCES" key="label.field.is.required"/></div>
					<div id="emptyarea" class="error" style="display: none;"><bean:message bundle="CANDIDATE_RESOURCES" key="label.field.is.required"/></div>
				</logic:present>
				<logic:notPresent name="uploadBean">
					<%= genericApplication.getAreaCode() %> <%= genericApplication.getArea() %>
				</logic:notPresent>
			</td>
		</tr>
		<tr class="personalInformation">
			<td>
				<bean:message bundle="CANDIDATE_RESOURCES" key="label.phone"/>
			</td>
			<td>
				<logic:present name="uploadBean">
					<fr:edit id="genericApplicationFormGenderPhone" name="application">
						<fr:schema bundle="CANDIDATE_RESOURCES" type="net.sourceforge.fenixedu.domain.period.GenericApplicationPeriod">
							<fr:slot name="telephoneContact" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
								<fr:validator name="pt.ist.fenixWebFramework.renderers.validators.RegexpValidator">
            						<fr:property name="regexp" value="(\+?\d{4,15})?"/>
            						<fr:property name="message" value="error.phone.invalidFormat"/>
            						<fr:property name="key" value="true"/>
            						<fr:property name="bundle" value="CANDIDATE_RESOURCES"/>
            						<fr:property name="isKey" value="true"/>
								</fr:validator>			 	
								<fr:property name="size" value="15" />
								<fr:property name="maxLength" value="15" />
							</fr:slot>
						</fr:schema>
						<fr:layout name="flow">
							<fr:property name="labelExcluded" value="true"/>
						</fr:layout>
						<fr:destination name="invalid" path="<%= callbackUrl %>" />
						<fr:destination name="cancel" path="<%= callbackUrl %>" />
					</fr:edit>
					<div id="emptytelephoneContact" class="error" style="display: none;"><bean:message bundle="CANDIDATE_RESOURCES" key="label.field.is.required"/></div>
				</logic:present>
				<logic:notPresent name="uploadBean">
					<%= genericApplication.getTelephoneContact() %>
				</logic:notPresent>
			</td>
		</tr>
	</table>
	<logic:present name="uploadBean">
		<p>
			<html:submit onclick="return validateInput();">
				<bean:message key="button.save" bundle="APPLICATION_RESOURCES"/>
			</html:submit>
		</p>
	</logic:present>
	<logic:notPresent name="uploadBean">
		<br/>
	</logic:notPresent>
</fr:form>

<h3><bean:message key="label.documentation" bundle="CANDIDATE_RESOURCES"/></h3>

<div class="infoop8">
	<bean:message key="label.documentation.information" bundle="CANDIDATE_RESOURCES"/>
</div>

<% if (genericApplication.getGenericApplicationFileSet().isEmpty()) { %>
	<br/>
	<div><bean:message key="label.documentation.none" bundle="CANDIDATE_RESOURCES"/></div>
	<br/>
<% } else { %>
<bean:define id="downloadUrl">/publico/genericApplications.do?method=downloadFile&applicationExternalId=<%= genericApplication.getExternalId() %><logic:present name="uploadBean">&confirmationCode=<%= genericApplication.getConfirmationCode() %></logic:present></bean:define>
<bean:define id="deleteUrl">/publico/genericApplications.do?method=deleteFile&applicationExternalId=<%= genericApplication.getExternalId() %>&confirmationCode=<%= genericApplication.getConfirmationCode() %></bean:define>
<bean:define id="confirmDeleteMessage"><bean:message bundle="CANDIDATE_RESOURCES" key="label.confirm.delete.file"/></bean:define>
<table class="tstyle2 thlight thcenter mtop15">
	<tr>
		<th>
			<bean:message bundle="CANDIDATE_RESOURCES" key="label.description"/>
		</th>
		<th>
			<bean:message bundle="CANDIDATE_RESOURCES" key="label.filename"/>
		</th>
		<th>
			<bean:message bundle="CANDIDATE_RESOURCES" key="label.size.in.bytes"/>
		</th>
		<th>
			<bean:message bundle="CANDIDATE_RESOURCES" key="label.submissionDate"/>
		</th>
		<th>
			<bean:message bundle="CANDIDATE_RESOURCES" key="label.checksum"/>
		</th>
		<logic:present name="uploadBean">
			<th>
			</th>
		</logic:present>
	</tr>
<%
	for (final GenericApplicationFile file : genericApplication.getGenericApplicationFileSet()) {
%>
		<tr>
			<td>
				<%= file.getDisplayName() %>
			</td>
			<td>
				<a href="<%= request.getContextPath() +  downloadUrl + "&fileExternalId=" + file.getExternalId() %>">
					<%= file.getFilename() %>
				</a>
			</td>
			<td style="text-align: right;">
				<%= file.getSize() %>
			</td>
			<td style="text-align: right;">
				<%= file.getUploadTime().toLocalDateTime().toString("yyyy-MM-dd HH:mm") %>
			</td>
			<td style="color: gray;">
				<%= file.getChecksumAlgorithm() %>: <%= file.getChecksum() %>
			</td>
			<logic:present name="uploadBean">
				<td>
					<a href="<%= request.getContextPath() +  deleteUrl + "&fileExternalId=" + file.getExternalId() %>"
							onclick="return confirm('<%= confirmDeleteMessage %>');">
						<bean:message bundle="CANDIDATE_RESOURCES" key="label.delete"/>
					</a>
				</td>
			</logic:present>
		</tr>
<%
	}
%>
</table>
<% } %>

<logic:present name="uploadBean">
	<a href="#" onclick="toggleByIdWithChangeCheck('#genericApplicationDocumentUploadForm');">
		<bean:message bundle="CANDIDATE_RESOURCES" key="label.add.document"/>
	</a>

	<% final String uploadBeanStyle = request.getAttribute("hasUploadFileError") == null ? "display: none;" : ""; %>
	<fr:form id="genericApplicationDocumentUploadForm" action="/genericApplications.do" encoding="multipart/form-data" style="<%= uploadBeanStyle %>">
		<input type="hidden" name="method" value="uploadDocument"/>
		<input type="hidden" name="applicationExternalId" value="<%= genericApplication.getExternalId() %>"/>
		<input type="hidden" name="confirmationCode" value="<%= genericApplication.getConfirmationCode() %>"/>

		<fr:edit id="genericApplicationDocumentUploadFormFile" name="uploadBean">
			<fr:schema bundle="CANDIDATE_RESOURCES" type="net.sourceforge.fenixedu.domain.candidacy.util.GenericApplicationUploadBean">
				<fr:slot name="displayName" key="label.description" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
					<fr:property name="size" value="50" />
					<fr:property name="maxLength" value="50" />
				</fr:slot>
				<fr:slot name="stream" key="label.candidacy.document.file">
    				<fr:property name="fileNameSlot" value="fileName"/>
    				<fr:property name="fileSizeSlot" value="fileSize"/>
					<fr:validator name="pt.ist.fenixWebFramework.renderers.validators.FileValidator">
						<fr:property name="maxSize" value="3698688"/>
						<fr:property name="acceptedExtensions" value="pdf" />
						<fr:property name="acceptedTypes" value="application/pdf" />
					</fr:validator>
  				</fr:slot>
			</fr:schema>
			<fr:layout>
				<fr:property name="classes" value="tstyle2 thcenter mtop15"/>
			</fr:layout>
		</fr:edit>
		<p><em><bean:message key="message.max.file.size" bundle="CANDIDATE_RESOURCES"/></em></p>
		<p><em><bean:message key="message.candidacy.upload.pdf.documents" bundle="CANDIDATE_RESOURCES"/></em></p>
		<html:submit><bean:message key="button.submit" bundle="APPLICATION_RESOURCES" /></html:submit>		
	</fr:form>
</logic:present>

<logic:messagesPresent message="true">
     <html:messages id="messages" message="true" bundle="CANDIDATE_RESOURCES">
      <p><span class="error0"><bean:write name="messages" filter="false" /></span></p>
     </html:messages>
</logic:messagesPresent>

<h3><bean:message key="label.recommendations" bundle="CANDIDATE_RESOURCES"/></h3>

<div class="infoop8">
	<bean:define id="letterMessage"><bean:message key="label.recommendations.information" bundle="CANDIDATE_RESOURCES"/></bean:define>
	<%= letterMessage.toString().replace("\n", "<br/>") %>
</div>

<% if (genericApplication.getGenericApplicationRecomentationSet().isEmpty()) { %>
	<br/>
	<div><bean:message key="label.recommendations.none" bundle="CANDIDATE_RESOURCES"/></div>
	<br/>
<% } else { %>
<bean:define id="resendRequestUrl">/publico/genericApplications.do?method=resendRecommendationRequest&applicationExternalId=<%= genericApplication.getExternalId() %>&confirmationCode=<%= genericApplication.getConfirmationCode() %></bean:define>
<table class="tstyle2 thlight thcenter mtop15">
	<tr>
		<th>
			<bean:message bundle="CANDIDATE_RESOURCES" key="label.name.person"/>
		</th>
		<th>
			<bean:message bundle="CANDIDATE_RESOURCES" key="label.recommendation.institution"/>
		</th>
		<th>
			<bean:message bundle="CANDIDATE_RESOURCES" key="label.email"/>
		</th>
		<th>
			<bean:message bundle="CANDIDATE_RESOURCES" key="label.requestDate"/>
		</th>
		<th>
			<bean:message bundle="CANDIDATE_RESOURCES" key="label.recommendation.document"/>
		</th>
		<logic:present name="recommendationBean">
			<th>
			</th>
		</logic:present>
		<logic:notPresent name="recommendationBean">
			<th>
				<bean:message bundle="CANDIDATE_RESOURCES" key="label.size.in.bytes"/>
			</th>
			<th>
			<bean:message bundle="CANDIDATE_RESOURCES" key="label.submissionDate"/>
			</th>
			<th>
				<bean:message bundle="CANDIDATE_RESOURCES" key="label.checksum"/>
			</th>
		</logic:notPresent>
	</tr>
<%
	for (final GenericApplicationRecomentation recomentation : genericApplication.getGenericApplicationRecomentationSet()) {
%>
		<tr>
			<td>
				<%= recomentation.getName() %>
			</td>
			<td>
				<%= recomentation.getInstitution() %>
			</td>
			<td>
				<%= recomentation.getEmail() %>
			</td>
			<td style="text-align: right;">
				<% if (recomentation.getRequestTime() != null) { %>
					<%= recomentation.getRequestTime().toLocalDateTime().toString("yyyy-MM-dd HH:mm") %>
				<% } %>
			</td>
			<logic:present name="recommendationBean">
				<td>
					<% if (recomentation.hasLetterOfRecomentation()) { %>
						<bean:message bundle="CANDIDATE_RESOURCES" key="label.recommendation.document.submitted"/>
					<% } else { %>
						<bean:message bundle="CANDIDATE_RESOURCES" key="label.recommendation.document.request.sent"/>
					<% } %>
				</td>
				<td>
					<% if (!recomentation.hasLetterOfRecomentation()) { %>
						<a href="<%= request.getContextPath() +  resendRequestUrl + "&recomentationId=" + recomentation.getExternalId() %>">
							<bean:message bundle="CANDIDATE_RESOURCES" key="label.recommendation.request.resend"/>
						</a>
					<% } %>
				</td>
			</logic:present>
			<logic:notPresent name="recommendationBean">
				<td>
					<% if (recomentation.hasLetterOfRecomentation()) { %>
						<a href="<%= request.getContextPath() +  "/publico/genericApplications.do?method=downloadRecomendationFile&fileExternalId=" + recomentation.getLetterOfRecomentation().getExternalId() %>">
							<%= recomentation.getLetterOfRecomentation().getFilename() %>
						</a>
					<% } %>
				</td>
				<td style="text-align: right;">
					<% if (recomentation.hasLetterOfRecomentation()) { %>
						<%= recomentation.getLetterOfRecomentation().getSize() %>
					<% } %>	
				</td>
				<td style="text-align: right;">
					<% if (recomentation.hasLetterOfRecomentation()) { %>
						<%= recomentation.getLetterOfRecomentation().getUploadTime().toLocalDateTime().toString("yyyy-MM-dd HH:mm") %>
					<% } %>	
				</td>
				<td style="color: gray;">
					<% if (recomentation.hasLetterOfRecomentation()) { %>
						<%= recomentation.getLetterOfRecomentation().getChecksumAlgorithm() %>: <%= recomentation.getLetterOfRecomentation().getChecksum() %>
					<% } %>
				</td>
			</logic:notPresent>
		</tr>
<%
	}
%>
</table>
<% } %>

<logic:present name="recommendationBean">
<% if (genericApplication.getName() != null && genericApplication.getName().length() > 0) { %>
	<a href="#" onclick="toggleByIdWithChangeCheck('#genericApplicationRecommendationForm');">
		<bean:message bundle="CANDIDATE_RESOURCES" key="label.request.recommendation"/>
	</a>

	<fr:form id="genericApplicationRecommendationForm" action="/genericApplications.do" encoding="multipart/form-data" style="display: none;">
		<input type="hidden" name="method" value="requestRecommendation"/>
		<input type="hidden" name="applicationExternalId" value="<%= genericApplication.getExternalId() %>"/>
		<input type="hidden" name="confirmationCode" value="<%= genericApplication.getConfirmationCode() %>"/>

		<fr:edit id="recommendationBean" name="recommendationBean">
			<fr:schema bundle="CANDIDATE_RESOURCES" type="net.sourceforge.fenixedu.domain.candidacy.util.GenericApplicationUploadBean">
				<fr:slot name="title" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
					<fr:property name="size" value="10"/>
				</fr:slot>
				<fr:slot name="name" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
					<fr:property name="size" value="50"/>
				</fr:slot>
				<fr:slot name="email" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
					<fr:property name="size" value="50"/>
				</fr:slot>
				<fr:slot name="institution" key="label.recommendation.institution" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
					<fr:property name="size" value="50"/>
				</fr:slot>
			</fr:schema>
			<fr:layout>
				<fr:property name="classes" value="tstyle2 thcenter mtop15"/>
			</fr:layout>
		</fr:edit>
		<div id="emptyRecommendationAllFields" class="error" style="display: none; margin-bottom: 15px;"><bean:message bundle="CANDIDATE_RESOURCES" key="label.all.fields.are.required"/></div>
		<html:submit onclick="return validateRecommendationInput();">
			<bean:message key="button.submit" bundle="APPLICATION_RESOURCES" />
		</html:submit>
	</fr:form>
<% } %>
</logic:present>

<logic:present name="uploadBean">
	<script>
		<%
			if (genericApplication.isAllPersonalInformationFilled()) {
		%>
				toggleById('.personalInformation');
		<%
			}
		%>
		addChangeListnerToInputField("input", "name");
		addChangeListnerToInputField("select", "gender");
		addChangeListnerToInputField("input", "dateOfBirthYearMonthDay");
		addChangeListnerToInputField("input", "documentIdNumber");
		addChangeListnerToInputField("select", "idDocumentType");
		addChangeListnerToInputField("select", "nationality");
		addChangeListnerToInputField("input", "fiscalCode");
		addChangeListnerToInputField("input", "address");
		addChangeListnerToInputField("input", "areaCode");
		addChangeListnerToInputField("input", "area");
		addChangeListnerToInputField("input", "telephoneContact");
	</script>
</logic:present>
