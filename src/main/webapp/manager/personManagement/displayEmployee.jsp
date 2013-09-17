<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>

<logic:notPresent name="employee">
	<span class="errors"><bean:message bundle="MANAGER_RESOURCES" key="error.manager.implossible.findPerson" /></span>
</logic:notPresent>

<logic:present name="employee">
		<table width="98%" cellpadding="0" cellspacing="0">
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