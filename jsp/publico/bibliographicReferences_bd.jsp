<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<logic:notPresent name="BibliographicReferences">
	<span class="error">
         <bean:message key="message.bibliography.not.available"/>
	</span>
</logic:notPresent>
<logic:present name="BibliographicReferences" >
<table border="0" style="text-align: left;">
	<tbody>
		<tr>
        	<td>                               
            	<h2>
                	<bean:message key="message.recommendedBibliography"/>
				</h2>
			</td>
		</tr>
            <logic:iterate id="bibliographicReference" name="BibliographicReferences">
                <logic:notEqual name="bibliographicReference" property="optional" value="true">
                    <tr>
                        <td>                                               
                            <bean:write name="bibliographicReference" property="title"/>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <bean:write name="bibliographicReference" property="authors"/>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <bean:write name="bibliographicReference" property="reference"/>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <bean:write name="bibliographicReference" property="year"/>                            
                        </td>
                    </tr>
                </logic:notEqual>
                </br>
            </logic:iterate>
            
            <tr>
                <td>
                    <h2>
                     	<bean:message key="message.optionalBibliography"/>
                    </h2>
                </td>
            </tr>           

             <logic:iterate id="bibliographicReference" name="BibliographicReferences">
                <logic:notEqual name="bibliographicReference" property="optional" value="false">
                    <tr>
                        <td>                            
                            <bean:write name="bibliographicReference" property="title"/>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <bean:write name="bibliographicReference" property="authors"/>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <bean:write name="bibliographicReference" property="reference"/>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <bean:write name="bibliographicReference" property="year"/>                            
                        </td>
                    </tr>
                </logic:notEqual>
            </logic:iterate>
        </tbody>
</table>
</logic:present>