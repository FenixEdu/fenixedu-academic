<%@page import="org.apache.struts.action.ActionMessages"%>
<%@page import="net.sourceforge.fenixedu.domain.candidacy.GenericApplicationFile"%>
<%@page import="net.sourceforge.fenixedu.domain.candidacy.GenericApplication"%>
<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@page import="net.sourceforge.fenixedu.domain.period.GenericApplicationPeriod"%>
<%@page import="java.util.SortedSet"%>

<html:xhtml/>

<% final GenericApplication genericApplication = (GenericApplication) request.getAttribute("application"); %>

<script src="https://rawgithub.com/timrwood/moment/2.0.0/moment.js"></script>
<script>
   	function toggleById(id) {
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
   		if (!validateInputField("fiscalCode")) { allIsOk = false; };
   		if (!validateInputField("address")) { allIsOk = false; };
   		if (!validateInputField("areaCode")) { allIsOk = false; };
   		if (!validateInputField("area")) { allIsOk = false; };
   		if (!validateInputField("telephoneContact")) { allIsOk = false; };
   		return allIsOk;
	}
</script>


<h2>
	<bean:message bundle="CANDIDATE_RESOURCES" key="label.application.periods"/>: <%= genericApplication.getGenericApplicationPeriod().getTitle().getContent() %>
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
			</td>
		</tr>
		<tr class="personalInformation">
			<td>
				<bean:message bundle="CANDIDATE_RESOURCES" key="label.dateOfBirth"/>
			</td>
			<td>
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
				<div id="badDateFormatdateOfBirthYearMonthDay" class="error" style="display: none;">O formato deve ser dd/MM/yyyy</div>
			</td>
		</tr>
		<tr class="personalInformation">
			<td>
				<bean:message bundle="CANDIDATE_RESOURCES" key="label.identificationNumber"/>
			</td>
			<td>
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
			</td>
		</tr>
		<tr class="personalInformation">
			<td>
				<bean:message bundle="CANDIDATE_RESOURCES" key="label.nationality"/>
			</td>
			<td>
				<fr:edit id="genericApplicationFormGenderNationality" name="application">
					<fr:schema bundle="CANDIDATE_RESOURCES" type="net.sourceforge.fenixedu.domain.period.GenericApplicationPeriod">
						<fr:slot name="nationality" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator"
								layout="menu-select">
							<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.CountryProvider"/>
							<fr:property name="format" value="${countryNationality}" />
							<fr:property name="sortBy" value="countryNationality"/>
						</fr:slot>
					</fr:schema>
					<fr:layout name="flow">
						<fr:property name="labelExcluded" value="true"/>
					</fr:layout>
					<fr:destination name="invalid" path="<%= callbackUrl %>" />
					<fr:destination name="cancel" path="<%= callbackUrl %>" />
				</fr:edit>
				<div id="emptynationality" class="error" style="display: none;"><bean:message bundle="CANDIDATE_RESOURCES" key="label.field.is.required"/></div>
			</td>
		</tr>
		<tr class="personalInformation">
			<td>
				<bean:message bundle="CANDIDATE_RESOURCES" key="label.fiscalCode"/>
			</td>
			<td>
				<fr:edit id="genericApplicationFormGenderFiscalCode" name="application">
					<fr:schema bundle="CANDIDATE_RESOURCES" type="net.sourceforge.fenixedu.domain.period.GenericApplicationPeriod">
						<fr:slot name="fiscalCode" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
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
				<div id="emptyfiscalCode" class="error" style="display: none;"><bean:message bundle="CANDIDATE_RESOURCES" key="label.field.is.required"/></div>
			</td>
		</tr>
		<tr class="personalInformation">
			<td>
				<bean:message bundle="CANDIDATE_RESOURCES" key="label.address"/>
			</td>
			<td>
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
			</td>
		</tr>
		<tr class="personalInformation">
			<td>
				<bean:message bundle="CANDIDATE_RESOURCES" key="label.areaCode"/>
				/
				<bean:message bundle="CANDIDATE_RESOURCES" key="label.area"/>
			</td>
			<td>
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
			</td>
		</tr>
		<tr class="personalInformation">
			<td>
				<bean:message bundle="CANDIDATE_RESOURCES" key="label.phone"/>
			</td>
			<td>
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
			</td>
		</tr>
	</table>
	<p>
		<html:submit onclick="return validateInput();">
			<bean:message key="button.save" bundle="APPLICATION_RESOURCES"/>
		</html:submit>
	</p>
</fr:form>

<h3><bean:message key="label.documentation" bundle="CANDIDATE_RESOURCES"/></h3>

<% if (genericApplication.getGenericApplicationFileSet().isEmpty()) { %>
	<div><bean:message key="label.documentation.none" bundle="CANDIDATE_RESOURCES"/></div>
	<br/>
<% } else { %>
<bean:define id="downloadUrl">/publico/genericApplications.do?method=downloadFile&applicationExternalId=<%= genericApplication.getExternalId() %>&confirmationCode=<%= genericApplication.getConfirmationCode() %></bean:define>
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
			<bean:message bundle="CANDIDATE_RESOURCES" key="label.checksum"/>
		</th>
		<th>
		</th>
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
			<td style="color: gray;">
				<%= file.getChecksumAlgorithm() %>: <%= file.getChecksum() %>
			</td>
			<td>
				<a href="<%= request.getContextPath() +  deleteUrl + "&fileExternalId=" + file.getExternalId() %>"
						onclick="return confirm('<%= confirmDeleteMessage %>');">
					<bean:message bundle="CANDIDATE_RESOURCES" key="label.delete"/>
				</a>
			</td>
		</tr>
<%
	}
%>
</table>
<% } %>

<a href="#" onclick="toggleById('#genericApplicationDocumentUploadForm');">
	<bean:message bundle="CANDIDATE_RESOURCES" key="label.add.document"/>
</a>

<fr:form id="genericApplicationDocumentUploadForm" action="/genericApplications.do" encoding="multipart/form-data" style="display: none;">
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


<script>
	<%
		if (genericApplication.getGender() != null && genericApplication.getDateOfBirthYearMonthDay() != null
				&& genericApplication.getDocumentIdNumber() != null && !genericApplication.getDocumentIdNumber().isEmpty()
				&& genericApplication.getIdDocumentType() != null && genericApplication.getNationality() != null
				&& genericApplication.getFiscalCode() != null && !genericApplication.getFiscalCode().isEmpty()
				&& genericApplication.getAddress() != null && !genericApplication.getAddress().isEmpty()
				&& genericApplication.getAreaCode() != null && !genericApplication.getAreaCode().isEmpty()
				&& genericApplication.getArea() != null && !genericApplication.getArea().isEmpty()
				&& genericApplication.getTelephoneContact() != null && !genericApplication.getTelephoneContact().isEmpty()) {
	%>
		toggleById('.personalInformation');
	<%
		}
	%>
</script>
