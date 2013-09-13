<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ page import="org.apache.struts.util.RequestUtils" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
<logic:notPresent name="siteView">
	<span class="error"><!-- Error messages go here --><p><bean:message key="errors.invalidSiteExecutionCourse"/></p></span>
</logic:notPresent>

<logic:present name="siteView">
    <logic:notPresent name="siteView" property="component">
		<span class="error"><!-- Error messages go here --><p><bean:message key="message.public.notfound.executionCourse"/></p></span>
	</logic:notPresent>


	<bean:define id="component" name="siteView" property="commonComponent" />
	<bean:define id="curricularCoursesList" name="component" property="associatedDegreesByDegree" />

	<div id="contextual_nav">
		<h2 class="brown"><bean:message key="label.curricular.information"/></h2>
		<ul>
				<logic:iterate id="curricularCourse" name="curricularCoursesList">
					<bean:define id="curricularCourseId" name="curricularCourse" property="externalId" />
					<bean:define id="degreeID" name="curricularCourse" property="infoDegreeCurricularPlan.infoDegree.externalId" />
					<bean:define id="degreeCurricularPlanID" name="curricularCourse" property="infoDegreeCurricularPlan.externalId" />
					<bean:define id="executionYearID" name="component" property="executionCourse.infoExecutionPeriod.infoExecutionYear.externalId"/>

					<li>
						<logic:notEqual name="curricularCourse" property="bolonhaDegree" value="true">
							<html:link page="<%= "/showCourseSite.do?method=showCurricularCourseSite&amp;curricularCourseID=" +  pageContext.findAttribute("curricularCourseId") + "&amp;executionPeriodOID=" + request.getAttribute(PresentationConstants.EXECUTION_PERIOD_OID) + "&amp;degreeID=" +  pageContext.getAttribute("degreeID") %>">
								<bean:write name="curricularCourse" property="infoDegreeCurricularPlan.name"/>
							</html:link>
						</logic:notEqual>
						<logic:equal name="curricularCourse" property="bolonhaDegree" value="true">
							<html:link page="<%= "/degreeSite/viewCurricularCourse.faces?"
							        + "&amp;curricularCourseID=" +  pageContext.findAttribute("curricularCourseId")
							        + "&amp;executionYearID=" + pageContext.findAttribute("executionYearID")
							        + "&amp;organizeBy=" +  pageContext.getAttribute("groups")
							        + "&amp;showRules=" +  pageContext.getAttribute("false")
							        + "&amp;hideCourses=" +  pageContext.getAttribute("false")
							        + "&amp;action=" +  pageContext.getAttribute("null")
							        + "&amp;degreeCurricularPlanID=" +  pageContext.getAttribute("degreeCurricularPlanID")
							        + "&amp;degreeID=" +  pageContext.getAttribute("degreeID") %>">
								<bean:write name="curricularCourse" property="infoDegreeCurricularPlan.name"/>
							</html:link>
						</logic:equal>
					</li>
				</logic:iterate>
		</ul>
	</div>


	<logic:present name="siteView" property="component">
		<bean:define id="component" name="siteView" property="component" />
		<logic:notEmpty name="component" property="initialStatement">
			<div class="citation">
				<p><bean:write name="component" property="initialStatement" filter="false"/></p>
			</div>
		</logic:notEmpty>
        <logic:notEmpty name="lastAnnouncement">		
			<bean:define id="announcement" name="lastAnnouncement"/>
			<div id="announcs">
				<h2 class="announcs-head"><bean:message key="label.lastAnnouncements"/></h2>
				<div class="last-announc">
					<div class="last-announc-name"><fr:view name="announcement" property="subject"/></div>
					<div class="last-announc-post-date">
							<fr:view name="announcement" property="lastModification"/>
					</div>
					<p class="last-announc-info"><fr:view name="announcement" property="body"/></p>
				</div>
				<logic:empty name="lastFiveAnnouncements" ></div></logic:empty>
		</logic:notEmpty>

		<logic:notEmpty name="lastFiveAnnouncements">		    	
				<ul class="more-announc">
				<logic:iterate id="announcement" name="lastFiveAnnouncements">
					<bean:define id="announcementId" name ="announcement" property="externalId" />
					<li class="more-announc"><span class="more-announc-date">
						<fr:view name="announcement" property="lastModification"/> - </span>
						<html:link  page="<%="/announcementManagement.do"+"?method=viewAnnouncements&amp;objectCode=" + pageContext.findAttribute("objectCode") + "#" + announcementId%>"
									anchor="<%= announcementId.toString() %>">
							<fr:view name="announcement" property="subject"/>
						</html:link></li>
				</logic:iterate>
				</ul>
			</div>		    
        </logic:notEmpty>

		<logic:notEmpty name="component" property="alternativeSite">
			<div style="margin-top: 2em; margin-bottom: 1em;">
			<h2><bean:message key="message.siteAddress" /></h2>
			<bean:define id="alternativeSite" name="component" property="alternativeSite"/>
			<html:link href="<%=(String)pageContext.findAttribute("alternativeSite") %>" target="_blank">
				<bean:write name="alternativeSite" />
			</html:link>
			</div>
			<br/>
		</logic:notEmpty>			

		<logic:notEmpty name="component" property="introduction">
			<h2><bean:message key="message.introduction" /></h2>
			<p><bean:write name="component" property="introduction" filter="false" /></p>
         </logic:notEmpty>
	
        <logic:notEmpty name="component" property="responsibleTeachers">	
			<h2><bean:message key="label.lecturingTeachers"/></h2>	
            	<logic:iterate id="infoResponsableTeacher" name="component" property="responsibleTeachers">
				<p style="margin-top: 6px; margin-bottom: 6px;">
					<bean:define id="teacherID" type="java.lang.String" name="infoResponsableTeacher" property="externalId"/>
					<% net.sourceforge.fenixedu.domain.Person person = net.sourceforge.fenixedu.domain.AbstractDomainObject.fromExternalId(teacherID).getPerson();
					   request.setAttribute("person", person);
					%>

					<logic:present name="person" property="homepage">
						<logic:notPresent name="person" property="homepage.activated">
							<bean:write name="person" property="name"/>
						</logic:notPresent>
						<logic:present name="person" property="homepage.activated">
							<logic:equal name="person" property="homepage.activated" value="true">
								<% final String appContext = net.sourceforge.fenixedu._development.PropertiesManager.getProperty("app.context"); %>
								<% final String context = (appContext != null && appContext.length() > 0) ? "/" + appContext : ""; %>
								<bean:define id="homepageURL" type="java.lang.String"><%= request.getScheme() %>://<%= request.getServerName() %>:<%= request.getServerPort() %><%= context %>/homepage/<bean:write name="person" property="user.userUId"/></bean:define>
								<html:link href="<%= homepageURL %>"><bean:write name="person" property="name"/></html:link>
							</logic:equal>
							<logic:equal name="person" property="homepage.activated" value="false">
							<p style="margin-top: 6px; margin-bottom: 6px;"><bean:write name="person" property="name"/>
							</logic:equal>
						</logic:present>
					</logic:present>
					<logic:notPresent name="person" property="homepage">
						<bean:write name="person" property="name"/>
					</logic:notPresent>
					(<bean:message key="label.responsible"/>)
					</p>
				</logic:iterate>	
        </logic:notEmpty>
        
		<logic:notEmpty name="component" property="lecturingTeachers">	
			<logic:empty name="component" property="responsibleTeachers">	
                <h2><bean:message key="label.lecturingTeachers"/></h2>	
			</logic:empty>
            <logic:iterate id="infoTeacher" name="component" property="lecturingTeachers">
				<p style="margin-top: 6px; margin-bottom: 6px;">
					<bean:define id="teacherID" type="java.lang.String" name="infoTeacher" property="externalId"/>
					<% net.sourceforge.fenixedu.domain.Person person = net.sourceforge.fenixedu.domain.AbstractDomainObject.fromExternalId(teacherID).getPerson();
					   request.setAttribute("person", person);
					%>
					<logic:present name="person" property="homepage">
						<logic:notPresent name="person" property="homepage.activated">
							<bean:write name="person" property="name"/>
						</logic:notPresent>
						<logic:present name="person" property="homepage.activated">
							<logic:equal name="person" property="homepage.activated" value="true">
								<% final String appContext = net.sourceforge.fenixedu._development.PropertiesManager.getProperty("app.context"); %>
								<% final String context = (appContext != null && appContext.length() > 0) ? "/" + appContext : ""; %>
								<bean:define id="homepageURL" type="java.lang.String"><%= request.getScheme() %>://<%= request.getServerName() %>:<%= request.getServerPort() %><%= context %>/homepage/<bean:write name="person" property="user.userUId"/></bean:define>
								<html:link href="<%= homepageURL %>"><bean:write name="person" property="name"/></html:link>
							</logic:equal>
							<logic:equal name="person" property="homepage.activated" value="false">
								<bean:write name="person" property="name"/>
							</logic:equal>
						</logic:present>
					</logic:present>
					<logic:notPresent name="person" property="homepage">
						<bean:write name="person" property="name"/>
					</logic:notPresent>
				</p>
            </logic:iterate>	
        </logic:notEmpty>
		
	</logic:present>
</logic:present>
