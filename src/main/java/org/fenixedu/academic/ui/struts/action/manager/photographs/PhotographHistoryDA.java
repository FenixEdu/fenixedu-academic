/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.ui.struts.action.manager.photographs;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.Photograph;
import org.fenixedu.academic.dto.photographs.PhotographFilterBean;
import org.fenixedu.academic.ui.struts.action.base.FenixDispatchAction;
import org.fenixedu.academic.ui.struts.action.manager.ManagerApplications.ManagerPeopleApp;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;
import org.fenixedu.bennu.struts.portal.EntryPoint;
import org.fenixedu.bennu.struts.portal.StrutsFunctionality;
import org.joda.time.DateTime;

/**
 * @author Pedro Santos (pmrsa)
 */
@StrutsFunctionality(app = ManagerPeopleApp.class, path = "photo-history", titleKey = "label.operator.photo.title")
@Mapping(path = "/photographs/history", module = "manager")
@Forwards({ @Forward(name = "history", path = "/manager/photographs/photographHistory.jsp") })
public class PhotographHistoryDA extends FenixDispatchAction {
    public static class DatedRejections {
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

    public static class UserHistory {
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

    @EntryPoint
    public ActionForward history(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        request.setAttribute("filter", new PhotographFilterBean());
        return mapping.findForward("history");
    }

    public ActionForward historyFilter(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        PhotographFilterBean filter = getRenderedObject("historyFilter");
        Set<Photograph> photos = Bennu.getInstance().getPhotographsSet();
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

}
