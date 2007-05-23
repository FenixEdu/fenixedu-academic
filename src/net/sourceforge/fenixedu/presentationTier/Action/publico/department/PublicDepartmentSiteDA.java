package net.sourceforge.fenixedu.presentationTier.Action.publico.department;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.DepartmentSite;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.UnitSite;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.messaging.Announcement;
import net.sourceforge.fenixedu.domain.messaging.AnnouncementBoard;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.teacher.Category;
import net.sourceforge.fenixedu.presentationTier.Action.manager.SiteVisualizationDA;
import net.sourceforge.fenixedu.presentationTier.servlets.filters.pathProcessors.DepartmentProcessor;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.RequestUtils;
import org.joda.time.YearMonthDay;

public class PublicDepartmentSiteDA extends SiteVisualizationDA {

    public static final int ANNOUNCEMENTS_NUMBER = 3;
    public static final String ANNOUNCEMENTS_NAME = "Anúncios";
    public static final String EVENTS_NAME = "Eventos";
    
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Integer id = getIntegerFromRequest(request, "selectedDepartmentUnitID");
        Unit unit = (Unit) RootDomainObject.getInstance().readPartyByOID(id);
        
        if (unit != null) {
            request.setAttribute("unit", unit);
            request.setAttribute("department", unit.getDepartment());
            
            DepartmentSite site = (DepartmentSite) unit.getSite();
            request.setAttribute("site", site);
        }
        
        request.setAttribute("announcementActionVariable", "/department/announcements.do");
        request.setAttribute("eventActionVariable", "/department/events.do");
        request.setAttribute("siteContextParam", "selectedDepartmentUnitID");
        request.setAttribute("siteContextParamValue", id);
        
        return super.execute(mapping, actionForm, request, response);
    }

    @Override
    protected String getDirectLinkContext(HttpServletRequest request) {
        Department department = getDepartment(request);
        try {
            return RequestUtils.absoluteURL(request, DepartmentProcessor.getDepartmentPath(department)).toString();
        } catch (MalformedURLException e) {
            return null;
        }
    }
    
    @Override
    protected ActionForward getSiteDefaultView(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return presentation(mapping, form, request, response);
    }

    public ActionForward presentation(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {
        Unit unit = getUnit(request);
        UnitSite site = unit.getSite();
        
        AnnouncementBoard announcementsBoard = null;
        AnnouncementBoard eventsBoard = null;
        
        for (AnnouncementBoard unitBoard : unit.getBoards()) {
            if (unitBoard.isPublicToRead() && unitBoard.getName().equals(ANNOUNCEMENTS_NAME)) {
                announcementsBoard = unitBoard;
            }

            if (unitBoard.isPublicToRead() && unitBoard.getName().equals(EVENTS_NAME)) {
                eventsBoard = unitBoard;
            }
        }
        
        if (announcementsBoard != null) {
            List<Announcement> announcements = announcementsBoard.getActiveAnnouncements();
            announcements = announcements.subList(0, Math.min(announcements.size(), ANNOUNCEMENTS_NUMBER));
            request.setAttribute("announcements", announcements);
        }
        
        if (eventsBoard != null) {
            List<Announcement> announcements = eventsBoard.getActiveAnnouncements();
            announcements = announcements.subList(0, Math.min(announcements.size(), ANNOUNCEMENTS_NUMBER));
            request.setAttribute("eventAnnouncements", announcements);
        }

        return mapping.findForward("frontPage-" + site.getLayout());
    }
    
    private Department getDepartment(HttpServletRequest request) {
        Unit unit = getUnit(request);
        if (unit == null) {
            return null;
        }
        else {
            return unit.getDepartment();
        }
    }

    private Unit getUnit(HttpServletRequest request) {
        return (Unit) request.getAttribute("unit");
    }
    
    public ActionForward employees(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {
        Unit unit = getUnit(request);
        
        BeanComparator employeeComparator = new BeanComparator("person", Party.COMPARATOR_BY_NAME_AND_ID);
        
        SortedSet<Unit> areas = new TreeSet<Unit>(Unit.COMPARATOR_BY_NAME_AND_ID);
		SortedSet<Employee> employeesNoArea = new TreeSet<Employee>(employeeComparator);
        Map<String, SortedSet<Employee>> employeesMap = new Hashtable<String, SortedSet<Employee>>();
        
        for (Employee employee : unit.getAllCurrentNonTeacherEmployees()) {
        	Unit area = employee.getCurrentSectionOrScientificArea();
        	
        	if (area != null) {
        		areas.add(area);
        		
        		String areaKey = area.getIdInternal().toString();
				SortedSet<Employee> employees = employeesMap.get(areaKey);
        		if (employees == null) {
        			employees = new TreeSet<Employee>(employeeComparator);
        			employeesMap.put(areaKey, employees);
        		}
        		
        		employees.add(employee);
        	}
        	else {
        		employeesNoArea.add(employee);
        	}
        }
        
        if (areas.isEmpty()) {
        	request.setAttribute("ignoreAreas", true);
        }
        
        request.setAttribute("areas", areas);
        request.setAttribute("employees", employeesMap);
        request.setAttribute("employeesNoArea", employeesNoArea);
        
        return mapping.findForward("department-employees");
    }

    public ActionForward degrees(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {
        Unit unit = getUnit(request);
        
        Department department = unit.getDepartment();
        if (department == null) {
            return presentation(mapping, actionForm, request, response);
        }
        
        Map<DegreeType, SortedSet<Degree>> degreeAndTypes = new HashMap<DegreeType, SortedSet<Degree>>();
        
        for (Degree degree : department.getDegrees()) {
            DegreeType type = degree.getDegreeType();
            
            SortedSet<Degree> current = degreeAndTypes.get(type);
            if (current == null) {
                current = new TreeSet<Degree>(Degree.COMPARATOR_BY_NAME_AND_ID);
                degreeAndTypes.put(type, current);
            }
            
            current.add(degree);
        }
        
        SortedSet<DegreeType> types = new TreeSet<DegreeType>(degreeAndTypes.keySet());
        request.setAttribute("types", types);
        
        for (DegreeType type : types) {
            request.setAttribute(type.getName(), degreeAndTypes.get(type));
        }

        return mapping.findForward("department-degrees");
    }
    
    public ActionForward teachers(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {
        Unit unit = getUnit(request);
        
        Department department = unit.getDepartment();
        if (department == null) {
            return presentation(mapping, actionForm, request, response);
        }

        String mode = getTeachersMode(request.getParameter("viewBy"));
        
        if (mode.equals("category")) {
        	setupTeachersCategories(request, department);
        }
        else if (mode.equals("area")) {
        	setupTeachersAreas(request, department);
        }
        
        return mapping.findForward("department-teachers-" + mode);
    }

	private String getTeachersMode(String parameter) {
		if (parameter == null) {
			return "category";
		}
		
		if (parameter.equals("category") || parameter.equals("area")) {
			return parameter;
		}
		
		return "category";
	}

	private void setupTeachersCategories(HttpServletRequest request, Department department) {
		YearMonthDay today = new YearMonthDay();
		YearMonthDay tomorrow = today.plusDays(1);
		
		SortedSet<Category> categories = new TreeSet<Category>();
		Map<String, SortedSet<Teacher>> teachers = new Hashtable<String, SortedSet<Teacher>>();
		
		for (Teacher teacher : department.getAllTeachers(today, tomorrow)) {
			categories.add(teacher.getCategory());
			addListTeacher(teachers, teacher.getCategory().getCode(), teacher);
		}
		
		request.setAttribute("categories", categories);
		request.setAttribute("teachers", teachers);
	}

	private void setupTeachersAreas(HttpServletRequest request, Department department) {
		YearMonthDay today = new YearMonthDay();
		YearMonthDay tomorrow = today.plusDays(1);
		
		SortedSet<Unit> areas = new TreeSet<Unit>(Unit.COMPARATOR_BY_NAME_AND_ID);
		SortedSet<Teacher> teachersNoArea = new TreeSet<Teacher>(Teacher.TEACHER_COMPARATOR_BY_CATEGORY_AND_NUMBER);
		Map<String, SortedSet<Teacher>> teachers = new Hashtable<String, SortedSet<Teacher>>();
		
		for (Teacher teacher : department.getAllTeachers(today, tomorrow)) {
			Unit area = teacher.getCurrentSectionOrScientificArea();
			
			if (area != null) {
				areas.add(area);
				addListTeacher(teachers, area.getIdInternal().toString(), teacher);
			}
			else {
				teachersNoArea.add(teacher);
			}
		}

		if (areas.isEmpty()) {
			request.setAttribute("ignoreAreas", true);
		}
		
		request.setAttribute("areas", areas);
		request.setAttribute("teachers", teachers);
		request.setAttribute("teachersNoArea", teachersNoArea);
	}
	
	private void addListTeacher(Map<String, SortedSet<Teacher>> teachersMap, String key, Teacher teacher) {
		SortedSet<Teacher> teachers = teachersMap.get(key);
		
		if (teachers == null) {
			teachers = new TreeSet<Teacher>(Teacher.TEACHER_COMPARATOR_BY_CATEGORY_AND_NUMBER);
			teachersMap.put(key, teachers);
		}
		
		teachers.add(teacher);
	}
}
