package net.sourceforge.fenixedu.presentationTier.Action.publico.scientificalArea;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.ResearchUnitSite;
import net.sourceforge.fenixedu.domain.ScientificAreaSite;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.organizationalStructure.CompetenceCourseGroupUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.Contract;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.organizationalStructure.ScientificAreaUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.teacher.Category;
import net.sourceforge.fenixedu.presentationTier.Action.publico.UnitSiteVisualizationDA;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.joda.time.YearMonthDay;

public class PublicScientificAreaSiteDA extends UnitSiteVisualizationDA {

    @Override
    protected ActionForward getSiteDefaultView(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {
	return presentation(mapping, form, request, response);
    }

    public ActionForward viewTeachers(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {
	ScientificAreaUnit scientificArea = (ScientificAreaUnit) getUnit(request);

	YearMonthDay today = new YearMonthDay();
	YearMonthDay tomorrow = today.plusDays(1);

	SortedSet<Category> categories = new TreeSet<Category>();
	Map<String, SortedSet<Person>> teachers = new Hashtable<String, SortedSet<Person>>();
	
	for (Teacher teacher : scientificArea.getDepartmentUnit().getDepartment().getAllTeachers(today, tomorrow)) {
	    if (teacher.getCurrentSectionOrScientificArea() == scientificArea) {
		categories.add(teacher.getCategory());
		addListTeacher(teachers, teacher.getCategory().getCode(), teacher);
	    }
	}

	request.setAttribute("categories", categories);
	request.setAttribute("teachers", teachers);

	return mapping.findForward("view-teachers");
    }

    public ActionForward viewEmployees(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {
	ScientificAreaUnit scientificArea = (ScientificAreaUnit) getUnit(request);
	
	List<Person> employees = new ArrayList<Person>();
	for (Employee employee : scientificArea.getAllCurrentNonTeacherEmployees()) {
	    employees.add(employee.getPerson());
	}
	
	Collections.sort(employees, Party.COMPARATOR_BY_NAME_AND_ID);
	request.setAttribute("employees", employees);
	return mapping.findForward("view-employees");
	    
    }

    public ActionForward viewCourses(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) {
	
	ScientificAreaUnit scientificArea = (ScientificAreaUnit) getUnit(request);
	
	request.setAttribute("courseGroupUnits", scientificArea.getCompetenceCourseGroupUnits());
	
	return mapping.findForward("view-courses");
    }
    
    private void addListTeacher(Map<String, SortedSet<Person>> teachersMap, String key, Teacher teacher) {
	SortedSet<Person> teachers = teachersMap.get(key);

	if (teachers == null) {
	    teachers = new TreeSet<Person>(new BeanComparator("teacher",Teacher.TEACHER_COMPARATOR_BY_CATEGORY_AND_NUMBER));
	    teachersMap.put(key, teachers);
	}

	teachers.add(teacher.getPerson());
    }

}
