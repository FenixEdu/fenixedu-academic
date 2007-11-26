<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<html:xhtml/>

<h2>
	<bean:message bundle="CARD_GENERATION_RESOURCES" key="link.manage.card.generation.show.degree.codes.and.labels"/>
</h2>

<br/>

<bean:message bundle="CARD_GENERATION_RESOURCES" key="label.degree.type"/>:
<logic:iterate id="degree" name="degrees" length="1">
	<strong>
		<bean:message bundle="ENUMERATION_RESOURCES" name="degree" property="degreeType.name"/>
	</strong>
</logic:iterate>
<br/>
<table class="tstyle4 thlight mtop05">
  <tr>
    <th><bean:message bundle="CARD_GENERATION_RESOURCES" key="label.degree.name"/></th>
    <th><bean:message bundle="CARD_GENERATION_RESOURCES" key="label.degree.ministery.code"/></th>
    <th><bean:message bundle="CARD_GENERATION_RESOURCES" key="label.degree.card.name"/></th>
    <th></th>
  </tr>
  	<logic:iterate id="degree" name="degrees">
	  	<tr>
    		<td><bean:write name="degree" property="name"/></td>
    		<td>
    			<logic:present name="degree" property="ministryCode">
	    			<bean:write name="degree" property="ministryCode"/>
    			</logic:present>
    			<logic:notPresent name="degree" property="ministryCode">
    				<logic:equal name="degree" property="degreeType.name" value="DEGREE">
    					<font color="red">
    					    ----
    					</font>
    				</logic:equal>    				
    				<logic:equal name="degree" property="degreeType.name" value="BOLONHA_DEGREE">
    					<font color="red">
    					    ----
    					</font>
    				</logic:equal>
    				<logic:equal name="degree" property="degreeType.name" value="BOLONHA_INTEGRATED_MASTER_DEGREE">
    					<font color="red">
    					    ----
    					</font>
    				</logic:equal>
    				<logic:notEqual name="degree" property="degreeType.name" value="DEGREE">
    					<logic:notEqual name="degree" property="degreeType.name" value="BOLONHA_DEGREE">
    						<logic:notEqual name="degree" property="degreeType.name" value="BOLONHA_INTEGRATED_MASTER_DEGREE">
    							----
    						</logic:notEqual>
    					</logic:notEqual>
    				</logic:notEqual>    				
    			</logic:notPresent>
    		</td>
    		<td>
    			<bean:define id="idCardName" name="degree" property="idCardName" type="java.lang.String"/>
    			<% if (idCardName.length() > 42) { %>
    				<font color="red">
    			<% } %>
		    			<bean:write name="degree" property="idCardName"/>
		    	<% if (idCardName.length() > 42) { %>
    				</font>
    			<% } %>
    		</td>
    		<td>
    			<html:link page="/manageCardGeneration.do?method=editDegree" paramId="degreeID" paramName="degree" paramProperty="idInternal">
	    			<bean:message bundle="CARD_GENERATION_RESOURCES" key="link.edit"/>
    			</html:link>
    		</td>
  		</tr>
	</logic:iterate>
</table>
