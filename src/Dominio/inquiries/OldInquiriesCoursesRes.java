/*
 * Created on Nov 17, 2004
 *
 */
package Dominio.inquiries;

import Dominio.Curso;
import Dominio.DomainObject;
import Dominio.ExecutionPeriod;

/**
 * @author João Fialho & Rita Ferreira
 *
 */
public class OldInquiriesCoursesRes extends DomainObject {
	
	// Specific statistic data for this query/table
	private Integer inquiryId;
	
	private Integer gepExecutionYear;
	private Integer semester;
	private Integer keyExecutionPeriod;
	private ExecutionPeriod executionPeriod;
	
	private Integer keyDegree;
	private Curso degree;
	
	private Integer curricularYear;
	
	private Integer gepCourseId;

	private String courseCode;

	private Integer numberAnswers;
	private Double numberEnrollments;
	private Double numberApproved;
	private Double numberEvaluated;
	
	private Double representationQuota;
	private Integer firstEnrollment;

	//Answers
	private Double average2_2;
	private Double deviation2_2;
	private Double tolerance2_2;
	private Integer numAnswers2_2;

	private Double average2_3;
	private Double deviation2_3;
	private Double tolerance2_3;
	private Integer numAnswers2_3;

	private Double average2_4;
	private Double deviation2_4;
	private Double tolerance2_4;
	private Integer numAnswers2_4;

	private Double average2_5;
	private Double deviation2_5;
	private Double tolerance2_5;
	private Integer numAnswers2_5_number;
	private Integer numAnswers2_5_text;

	private Double average2_6;
	private Double deviation2_6;
	private Double tolerance2_6;
	private Integer numAnswers2_6;

	private String average2_7;
	private Integer numAnswers2_7;

	private Double average2_8;
	private Double deviation2_8;
	private Double tolerance2_8;
	private Integer numAnswers2_8;

}
