<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

	<p class="infoop"><span><h2 class="inline"><bean:message key="label.person.title.personal.info" /></h2></span></p>
	<fr:view name="candidacy" property="person" schema="candidate.personalData" >
	    <fr:layout name="tabular">
	    </fr:layout>
	</fr:view>

	<p class="infoop"><span><h2 class="inline"><bean:message key="label.person.title.filiation" /></h2></span></p>
	<fr:view name="candidacy" property="person" schema="candidate.filiation" >
	    <fr:layout name="tabular">
	    </fr:layout>
	</fr:view>

	<p class="infoop"><span><h2 class="inline"><bean:message key="label.person.title.addressInfo" /></h2></span></p>
	<fr:view name="candidacy" property="person" schema="candidate.address" >
	    <fr:layout name="tabular">
	    </fr:layout>
	</fr:view>
	
	<p class="infoop"><span><h2 class="inline"><bean:message key="label.person.title.contactInfo" /></h2></span></p>
	<fr:view name="candidacy" property="person" schema="candidate.contacts" >
	    <fr:layout name="tabular">
	    </fr:layout>
	</fr:view>


	<p class="infoop"><span><h2 class="inline"><bean:message key="label.person.title.precedenceDegreeInfo" bundle="ADMIN_OFFICE_RESOURCES"/></h2></span></p>	
	<fr:view name="candidacy" property="precedentDegreeInformation" schema="candidate.precedentDegreeInformation" >
	    <fr:layout name="tabular">
	    </fr:layout>
	</fr:view>	


<html:form action="/dfaCandidacy.do">	
	<html:hidden property="method" value="validateData"/>
	<bean:define id="number" name="candidacy" property="number"/>
	<html:hidden property="candidacyNumber" value="<%= number.toString() %>"/>
	<br/><br/>
	<html:submit onclick="this.form.method.value='invalidateData';this.form.submit();"><bean:message key="button.invalidate" bundle="ADMIN_OFFICE_RESOURCES"/></html:submit>	
	<html:submit><bean:message key="button.validate" bundle="ADMIN_OFFICE_RESOURCES"/></html:submit>	
</html:form>
