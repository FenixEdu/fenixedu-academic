<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>

<div class="row">
	<div class="col-lg-2">
		<br />
		<ul class="nav nav-pills nav-stacked">
			<li>
				<html:link page="/index.do">
					<bean:message key="link.website.listSites"/>
				</html:link>
			</li>
		</ul>
		<br />
		<ul class="nav nav-pills nav-stacked">
			<c:forEach items="${announcementBoards}" var="board">
        		<li>
        			<html:link action="${contextPrefix}method=viewAnnouncementBoard&announcementBoardId=${board.externalId}&${extraParameters}">
        				${board.name}
        			</html:link>
        		</li>
			</c:forEach>
		</ul>
	</div>
	<div class="col-lg-10">
		<ol class="breadcrumb">
			<em>${site.unit.nameWithAcronym}</em>
		</ol>
		<jsp:include page="${actual$page}" />
	</div>
</div>
