<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>

<logic:notPresent name="siteView">
<table align="center" cellpadding='0' cellspacing='0'>
    <tr align="center">
        <td>
            <span class="error">
                <bean:message key="errors.invalidSiteExecutionCourse"/>
            </span>
        </td>
    </tr>
</table>
</logic:notPresent>

<logic:present name="siteView">
    <logic:notPresent name="siteView" property="component">
        <table align="center"  cellpadding='0' cellspacing='0'>
			<tr align="center">
				<td>
					<span class="error">
					     <bean:message key="message.public.notfound.executionCourse"/> 
					 </span>
				</td>
			</tr>
		</table>
    </logic:notPresent>


    <logic:present name="siteView" property="component">
        <bean:define id="component" name="siteView" property="component" />
	
        <logic:notEmpty name="component" property="initialStatement">
            <table align="center" cellspacing="0" width="90%">
             <tr>
               <td class="citation">
                 <p><bean:write name="component" property="initialStatement" filter="false"/></p>
               </td>
            </tr>
            </table>		
         <br/>
         <br/>
        </logic:notEmpty>
		
        <logic:notEmpty name="component" property="lastAnnouncement" >		
			<bean:define id="announcement" name="component" property="lastAnnouncement"/>
			<div id="announcs">
				<h2 class="announcs-head"><bean:message key="label.lastAnnouncements"/></h2>
				<div class="last-announc">
					<div class="last-announc-name">
						<bean:write name="announcement" property="title"/>
					</div>
					<div class="last-announc-post-date">
						<dt:format pattern="dd/MM/yyyy | HH:mm">
							<bean:write name="announcement" property="lastModifiedDate.time"/>
						</dt:format>
					</div>
					<p class="last-announc-info">
						<bean:write name="announcement" property="information" filter="false"/>
					</p>
				</div>
				<logic:empty name="component" property="lastFiveAnnouncements" ></div></logic:empty>
		</logic:notEmpty>

		<logic:notEmpty name="component" property="lastFiveAnnouncements" >		    	
				<ul>
				<logic:iterate id="announcement" name="component" property="lastFiveAnnouncements">
					<bean:define id="announcementId" name ="announcement" property="idInternal" />
					<li class="more-announc"><span class="more-announc-date"><dt:format pattern="dd/MM/yyyy">
						<bean:write name="announcement" property="lastModifiedDate.time"/></dt:format> - </span>
						<html:link  page="<%="/viewSite.do"+"?method=announcements&amp;objectCode=" + pageContext.findAttribute("objectCode")%>"
									anchor="<%= announcementId.toString() %>">
							<bean:write name="announcement" property="title"/>
						</html:link></li>
				</logic:iterate>
				</ul>
			</div>		    
        </logic:notEmpty>
<br />
<br />
<br />

    
     <logic:notEmpty 	name="component" property="alternativeSite" >	
            <h2><bean:message key="message.siteAddress" /></h2>
            <bean:define id="alternativeSite" name="component" property="alternativeSite"/>
            <html:link href="<%=(String)pageContext.findAttribute("alternativeSite") %>" target="_blank">
			<bean:write name="alternativeSite" />
            </html:link>
			<br/>
			<br/>
		
	</logic:notEmpty>			

   
     <logic:notEmpty name="component" property="introduction">
     	
        <h2><bean:message key="message.introduction" /></h2>
          <p><bean:write name="component" property="introduction" filter="false" /></p>
         <br/>
        <br/>
      </logic:notEmpty>
	
	
        <table>
            
        <logic:notEmpty name="component" property="responsibleTeachers">	
	
           
                <tr>
            	<td>
            		<h2><bean:message key="label.lecturingTeachers"/></h2>	
            	</td>
                </tr>	

            <logic:iterate id="infoResponsableTeacher" name="component" property="responsibleTeachers">
            	<tr>
            	<td>
				<bean:write name="infoResponsableTeacher" property="infoPerson.nome" /> <bean:message key="label.responsible"/>
                </td>
                </tr>
            </logic:iterate>	
        </logic:notEmpty>
        <logic:notEmpty name="component" property="lecturingTeachers" >	
             
            <logic:empty name="component" property="responsibleTeachers">	
	            
               
                <tr>
                <td>
                <h2><bean:message key="label.lecturingTeachers"/></h2>	
                </td>
                </tr>	
             </logic:empty>
            <logic:iterate id="infoTeacher" name="component" property="lecturingTeachers">
                <tr>
                <td>
				<bean:write name="infoTeacher" property="infoPerson.nome" /> 
                </td>
                </tr>
            </logic:iterate>	

        </logic:notEmpty>
     
         </table>
       
    </logic:notEmpty> 
   </logic:present>
</logic:present>