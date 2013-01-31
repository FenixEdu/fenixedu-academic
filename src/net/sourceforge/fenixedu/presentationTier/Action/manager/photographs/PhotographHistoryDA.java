package net.sourceforge.fenixedu.presentationTier.Action.manager.photographs;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.photographs.PhotographFilterBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.PhotoState;
import net.sourceforge.fenixedu.domain.PhotoType;
import net.sourceforge.fenixedu.domain.Photograph;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

/**
 * @author Pedro Santos (pmrsa)
 */
@Mapping(path = "/photographs/history", module = "manager")
@Forwards({ @Forward(name = "history", path = "/manager/photographs/photographHistory.jsp"),
		@Forward(name = "listByState", path = "/manager/photographs/listByState.jsp") })
public class PhotographHistoryDA extends FenixDispatchAction {
	public class DatedRejections {
		private final DateTime date;

		private final List<Photograph> photographs = new ArrayList<Photograph>();

		public DatedRejections(DateTime date) {
			this.date = date;
		}

		public void addPhotograph(Photograph photograph) {
			photographs.add(photograph);
		}

		public DateTime getDate() {
			return date;
		}

		public List<Photograph> getPhotographs() {
			return photographs;
		}
	}

	public class UserHistory {
		private final Person person;

		private final SortedSet<Photograph> photographs = new TreeSet<Photograph>();

		public UserHistory(Person person) {
			this.person = person;
		}

		public Person getPerson() {
			return person;
		}

		public void addPhotograph(Photograph photograph) {
			photographs.add(photograph);
		}

		public Set<Photograph> getPhotographs() {
			return photographs;
		}
	}

	public ActionForward history(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		request.setAttribute("filter", new PhotographFilterBean());
		return mapping.findForward("history");
	}

	public ActionForward historyFilter(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		PhotographFilterBean filter = getRenderedObject("historyFilter");
		Set<Photograph> photos = rootDomainObject.getPhotographsSet();
		SortedMap<Person, UserHistory> history = new TreeMap<Person, UserHistory>();

		for (Photograph photograph : photos) {
			if (filter.accepts(photograph)) {
				Person person = photograph.getPerson();
				if (history.containsKey(person)) {
					history.get(person).addPhotograph(photograph);
				} else {
					UserHistory user = new UserHistory(person);
					user.addPhotograph(photograph);
					history.put(person, user);
				}
			}
		}
		request.setAttribute("filter", new PhotographFilterBean());
		request.setAttribute("history", history.values());
		return mapping.findForward("history");
	}

	public ActionForward rejections(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		SortedMap<DateTime, DatedRejections> rejections = getSortedPhotographsByState(PhotoState.REJECTED);
		request.setAttribute("photographs", rejections.values());
		return mapping.findForward("listByState");
	}

	public ActionForward approvals(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		SortedMap<DateTime, DatedRejections> approvals = getSortedPhotographsByState(PhotoState.APPROVED);
		request.setAttribute("photographs", approvals.values());
		return mapping.findForward("listByState");
	}

	private SortedMap<DateTime, DatedRejections> getSortedPhotographsByState(PhotoState state) {
		Set<Photograph> photos = rootDomainObject.getPhotographsSet();

		SortedMap<DateTime, DatedRejections> rejections = new TreeMap<DateTime, DatedRejections>();
		for (Photograph photograph : photos) {
			if (photograph.getState() == state && photograph.getPhotoType() == PhotoType.USER) {
				DateTime date = stripHours(photograph.getStateChange());
				if (rejections.containsKey(date)) {
					rejections.get(date).addPhotograph(photograph);
				} else {
					DatedRejections datedRejection = new DatedRejections(date);
					datedRejection.addPhotograph(photograph);
					rejections.put(date, datedRejection);
				}
			}
		}
		return rejections;
	}

	private static DateTime stripHours(DateTime date) {
		return new DateTime(date.toDateMidnight());
	}
}
