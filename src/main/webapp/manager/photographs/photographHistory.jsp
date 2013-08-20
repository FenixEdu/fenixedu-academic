<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml />

<h2>Histórico de Fotografias<!-- bean:message key="documents.management.title" bundle="MANAGER_RESOURCES" / --></h2>

<logic:present role="MANAGER">

<fr:form action="/photographs/history.do?method=historyFilter">
    <h3>Foto</h3>
    <fr:edit id="historyFilter" name="filter" schema="photographs.filter.main">
        <fr:layout name="tabular-editable">
            <fr:property name="classes" value="tstyle1" />
            <fr:property name="columnClasses" value=",,noborder" />
        </fr:layout>
    </fr:edit>
    <h3>Pessoa</h3>
    <fr:edit id="historyFilterPerson" name="filter" schema="photographs.filter.person">
        <fr:layout name="tabular-editable">
            <fr:property name="classes" value="tstyle1" />
            <fr:property name="columnClasses" value=",,noborder" />
        </fr:layout>
    </fr:edit>
    <html:submit />
</fr:form>
<logic:present name="history">
	<logic:iterate id="userHistory" name="history">
	
		<p class="separator2 mtop2" style="font-size: 1.1em;">
            <bean:write name="userHistory" property="person.username" /> - <bean:write name="userHistory" property="person.name"/>
        </p>
		
		<logic:iterate id="photo" name="userHistory" property="photographs">
		
			<bean:define id="photoId" name="photo" property="externalId"/>

            <table class="dinline">
	            <tr>
		            <td>
						<html:img align="middle"
						    src="<%=request.getContextPath() + "/person/retrievePersonalPhoto.do?method=retrievePendingByID&amp;photoCode=" + photoId %>"
						altKey="personPhoto" bundle="IMAGE_RESOURCES"
						style="border: 1px solid #aaa; padding: 3px;" />
		            </td>
	            </tr>
	            <tr>
		            <td>
                        <fr:view name="photo" property="photoType"/><br/>
			            <fr:view name="photo" property="state"/><br/>
                        <logic:notEmpty name="photo" property="stateChange">
                            <fr:view name="photo" property="stateChange" layout="no-time" />
                        </logic:notEmpty>
		            </td>
	            </tr>
            </table>

		</logic:iterate>
		
	</logic:iterate>
</logic:present>

</logic:present>