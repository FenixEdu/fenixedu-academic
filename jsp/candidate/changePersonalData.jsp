<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>


<fr:form action="/changePersonalData.do?method=change">

	<p class="infoop"><span><h2 class="inline"><bean:message key="label.person.title.personal.info" /></h2></span></p>
	<fr:edit id="personData" name="UserView" property="person" schema="candidate.personalData-edit" >
	    <fr:layout name="tabular">
	    </fr:layout>
	</fr:edit>

	<p class="infoop"><span><h2 class="inline"><bean:message key="label.person.title.filiation" /></h2></span></p>
	<fr:edit id="personFiliation" name="UserView" property="person" schema="candidate.filiation-edit" >
	    <fr:layout name="tabular">
	    </fr:layout>
	</fr:edit>

	<p class="infoop"><span><h2 class="inline"><bean:message key="label.person.title.addressInfo" /></h2></span></p>
	<fr:edit id="personAddress" name="UserView" property="person" schema="candidate.address-edit" >
	    <fr:layout name="tabular">
	    </fr:layout>
	</fr:edit>
	
	<p class="infoop"><span><h2 class="inline"><bean:message key="label.person.title.contactInfo" /></h2></span></p>
	<fr:edit id="personContacts" name="UserView" property="person" schema="candidate.contacts-edit" >
	    <fr:layout name="tabular">
	    </fr:layout>
	</fr:edit>

	<logic:present name="precedentDegreeInformation">
		<p class="infoop"><span><h2 class="inline"><bean:message key="label.person.title.precedenceDegreeInfo" /></h2></span></p>	
		<fr:edit id="precedentDegreeInformation" name="precedentDegreeInformation" schema="candidate.precedentDegreeInformation-edit" >
		    <fr:layout name="tabular">
		    </fr:layout>
		</fr:edit>	
	</logic:present>

	<html:submit><bean:message key="button.submit" /></html:submit>	
	
</fr:form>
