/**
 * Project Sop 
 * 
 * Package ServidorApresentacao.Constants
 * 
 * Created on 3/Dez/2002
 *
 */
package ServidorApresentacao.Action.sop.utils;

/**
 * @author jpvl
 *
 * 
 */
public abstract class SessionConstants {

	

	public static final String CONTEXT_PREFIX = "context.";

	public static final String CONTEXT_KEY =
		CONTEXT_PREFIX + "anoCurricularAndSemestreAndInfoLicenciaturaExecucao";
	public static final String INFO_EXECUTION_DEGREE_KEY =
		CONTEXT_PREFIX + "infoLicenciaturaExecucao";
	public static final String U_VIEW = "UserView";
	public static final String CLASS_VIEW = CONTEXT_PREFIX + "classView";
	public static final String EXECUTION_COURSE_KEY = "infoDisciplinaExecucao";
	public static final String LESSON_LIST_ATT = "lessonList";
	public static final String EXECUTION_COURSE_LIST_KEY =
		CONTEXT_PREFIX + "disciplinasExecucao";
	public static final String INFO_STUDENT_KEY = "infoStudent";

	public static final String SEMESTER_LIST_KEY = "semester.list";
	public static final String INFO_DEGREE_LIST_KEY = "infoDegree.list";
	public static final String CURRICULAR_YEAR_LIST_KEY = "curricularYear.list";
	public static final String SHIFT_VIEW = "shiftview";
	public static final String INFO_SHIFTS_EXECUTION_COURSE_KEY =
		"infoTurnosDeDisciplinaExecucao";
	public static final String INFO_EXECUTION_PERIOD_KEY =
		"info_execution_period_key";
	public static final String INFO_EXECUTION_DEGREE_LIST_KEY =
		"info_execution_degree_list_key";
	public static final String CURRICULAR_YEAR_KEY = "curricular_year_key";

	public static final String CLASS_INFO_SHIFT_LIST_KEY =
		"class_info_shift_list_key";

	public static final String AVAILABLE_INFO_SHIFT_LIST_KEY =
		"available_info_shift_list_key";

	public static final String ORIGINAL_MAPPING_KEY = "original_mapping_key";

	public static final String EXCEPTION_STACK_TRACE = "exception_stack_trace";
	
	public static final String REQUEST_CONTEXT="request_context";
	
	public static final String SESSION_IS_VALID="session_is_valid";
	
	//gesdis constants
	public static final String INFO_SITE="info_site";
	public static final String EXECUTION_COURSE_CURRICULUM= "execution_course_curriculum";
	public static final String INFO_SITE_ANNOUNCEMENT= "info_site_announcement";
	public static final String INFO_SITE_SECTION= "info_site_section";
	public static final String INFO_SITE_ITEM= "info_site_item";
	public static final String INFO_SITE_ANNOUNCEMENT_LIST= "info_site_announcement_list";
	public static final String INFO_SITES_LIST = "info_sites_list";
	public static final String INFO_TEACHER = "info_teacher";	
	public static final String INFO_BIBLIOGRAPHIC_REFERENCE_LIST = "BibliographicReferences";		
	public static final String INFO_BIBLIOGRAPHIC_REFERENCE = "BibliographicReference";
	public static final String TEACHERS_LIST ="teachers_list";
	public static final String RESPONSIBLE_TEACHERS_LIST = "responsible_teachers_list";
	// Master Degree 
	public static final String SPECIALIZATIONS = "specializations"; 
	public static final String DEGREE_LIST = "degree_list";
	public static final String NEW_MASTER_DEGREE_CANDIDATE = "new_master_degree_candidate";
	public static final String APPLICATION_INFO = "application_info";
	public static final String CANDIDATE_SITUATION = "candidate_situation";
	public static final String CANDIDATE_SITUATION_LIST = "candidate_situation_list";
	
	// Person constants
	public static final String IDENTIFICATION_DOCUMENT_TYPE_LIST = "identification_document_type_list";


	public static final String PERSONAL_INFO_KEY = "PERSONAL_INFO_KEY";
	public static final String EXPIRATION_YEARS_KEY = "EXPIRATION_YEARS_KEY";
	public static final String YEARS_KEY = "YEARS_KEY";
	public static final String MONTH_LIST_KEY = "MONTH_LIST_KEY";
	public static final String MONTH_DAYS_KEY = "MONTH_DAYS_KEY";

	public static final String IDENTIFICATION_DOCUMENT_TYPE_LIST_KEY =
		"IDENTIFICATION_DOCUMENT_TYPE_LIST_KEY";

	public static final String MARITAL_STATUS_LIST_KEY =
		"MARITAL_STATUS_LIST_KEY";

	public static final String NATIONALITY_LIST_KEY = "NATIONALITY_LIST_KEY";
	public static final String SEX_LIST_KEY ="SEX_LIST_KEY";	
	
	
	public static final String INFO_EXAMS_KEY = "INFO_EXAMS_KEY";

}
