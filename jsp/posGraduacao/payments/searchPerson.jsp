<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e"%>

<html:form action="/payments.do">

	<html:hidden property="method" />

	<h2><bean:message key="label.masterDegree.administrativeOffice.payments" /></h2>
	<hr>

	<logic:messagesPresent message="true">
		<ul>
			<html:messages id="messages" message="true">
				<li><span class="error0"><bean:write name="messages" /></span></li>
			</html:messages>
		</ul>
		<br />
	</logic:messagesPresent>

	<h3><bean:message
		key="label.masterDegree.administrativeOffice.payments.searchByCandidacyNumber" /></h3>
	<table>
		<tr>
			<td>
				<bean:message
					key="label.masterDegree.administrativeOffice.payments.number" />:
			</td>
			<td>
				<html:text property="candidacyNumber" />
			</td>
			<td>
	    		&nbsp;
	    	</td>
			<td>
				<html:submit styleClass="inputbutton"
					onclick="this.form.method.value='searchPersonByCandidacyNumber'">
					<bean:message
						key="label.masterDegree.administrativeOffice.payments.search" />
				</html:submit>
			</td>
		</tr>
	</table>

	<br />
	<br />
	<br />

	<h3><bean:message
		key="label.masterDegree.administrativeOffice.payments.searchByUsername" /></h3>
	<table>
	  <tr>
	    <td>
	    	<bean:message key="label.masterDegree.administrativeOffice.payments.username" />:
	    </td>
	    <td>
	    	<html:text property="username" />
	    </td>
	    <td>
	    	&nbsp;
	    </td>
  	    <td>
  	    	<html:submit styleClass="inputbutton" onclick="this.form.method.value='searchPersonByUsername'">
			<bean:message key="label.masterDegree.administrativeOffice.payments.search" />
			</html:submit>
		</td>
	  </tr>
	</table>

	<br />
	<br />
	<br />

	<h3><bean:message
		key="label.masterDegree.administrativeOffice.payments.searchByDocumentIDandType" /></h3>	
	<table>
	  <tr>
	    <td>
	    	<bean:message key="label.masterDegree.administrativeOffice.payments.documentType" />:
	    </td>
	    <td>
	    	<e:labelValues id="documentTypes" 
	    		enumeration="net.sourceforge.fenixedu.domain.person.IDDocumentType"
	    		bundle="ENUMERATION_RESOURCES" />
	    	<html:select property="documentType">
				<html:options collection="documentTypes" property="value" labelProperty="label" />
			</html:select>
		</td>
	  </tr>
	  <tr>
	    <td>
	    	<bean:message key="label.masterDegree.administrativeOffice.payments.documentNumber" />:
	    </td>
	    <td>
	    	<html:text property="documentNumber" />
	    </td>
	  </tr>
	  <tr>
	    <td align="left" colspan="2">
			<html:submit styleClass="inputbutton"
				onclick="this.form.method.value='searchPersonByDocumentIDandDocumentType'">
				<bean:message
					key="label.masterDegree.administrativeOffice.payments.search" />
			</html:submit>
	    </td>
	  </tr>
	</table>

</html:form>
