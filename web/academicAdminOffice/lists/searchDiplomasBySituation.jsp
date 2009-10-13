<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<html:xhtml/>

<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
<h2><bean:message key="label.diplomasListBySituation" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>

<html:messages id="message" message="true" bundle="ACADEMIC_OFFICE_RESOURCES">
	<p>
		<span class="error0"><!-- Error messages go here --><bean:write name="message" /></span>
	</p>
</html:messages>

<fr:form id="searchForm" action="/diplomasListBySituation.do">
	<html:hidden property="method" value="searchBySituation"/>
	<html:hidden property="extendedInfo" value="false"/>
		<fr:edit name="searchParametersBean" schema="diploma.list.searchBySituation.chooseDegree" id="chooseSituation">
			<fr:destination name="postBack" path="/diplomasListBySituation.do?method=postBack"/>
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thlight thright mtop025 thmiddle"/>
		        <fr:property name="columnClasses" value=",,tdclear tderror1"/>
			</fr:layout>
		</fr:edit>
	
		<p class="mtop1">
			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton" onclick="this.form.method.value='searchBySituation';this.form.submit();">
				<bean:message key="button.search" bundle="ACADEMIC_OFFICE_RESOURCES"/>
			</html:submit>
		</p>
	
	<logic:present name="diplomasList">
		<bean:size id="diplomasListSize" name="diplomasList" />
		<p class="mtop2">
			 <bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.diplomas.lists.total"/>: <%= diplomasListSize %>
		</p>
		<fr:view schema="diplomasList.view" name="diplomasList">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2 thright thlight thcenter"/>
			</fr:layout>	
		</fr:view>
		<logic:greaterThan name="diplomasListSize" value="0">
			<p class="mtop15 mbottom15">
				<a href="javascript:var form = document.getElementById('searchForm');form.extendedInfo.value='false';form.method.value='exportInfoToExcel';form.submit();">
					<html:image border="0" src="<%= request.getContextPath() + "/images/excel.gif"%>" altKey="excel" bundle="IMAGE_RESOURCES"></html:image>
					<bean:message key="link.lists.xlsFileToDownload" bundle="ACADEMIC_OFFICE_RESOURCES"/>
				</a>
			</p>
			<p class="mtop15 mbottom15">
				<a href="javascript:var form = document.getElementById('searchForm');form.extendedInfo.value='true';form.method.value='exportInfoToExcel';form.submit();">
					<html:image border="0" src="<%= request.getContextPath() + "/images/excel.gif"%>" altKey="excel" bundle="IMAGE_RESOURCES"></html:image>
					<bean:message key="link.lists.xlsFileToDownload.extended.info" bundle="ACADEMIC_OFFICE_RESOURCES"/>
				</a>
			</p>
		</logic:greaterThan>
	</logic:present>

</fr:form>
