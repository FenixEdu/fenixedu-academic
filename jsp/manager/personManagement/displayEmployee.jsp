<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<logic:notPresent name="employee">
	<span class="errors"><bean:message bundle="MANAGER_RESOURCES" key="error.manager.implossible.findPerson" /></span>
</logic:notPresent>

<logic:present name="employee">
		<table width="100%" cellpadding="0" cellspacing="0">
          <!-- Contactos -->
          <html:link module="/manager" page="">
          <tr>
            <td class="infoop" width="25"><span class="emphasis-box">3</span></td>
            <td class="infoop"><strong><bean:message bundle="MANAGER_RESOURCES" key="label.employee" /></strong></td>
          </tr>
          </html:link>
		</table>
    <hr/>
</logic:present>