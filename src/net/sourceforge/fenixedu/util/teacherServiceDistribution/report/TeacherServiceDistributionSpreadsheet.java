package net.sourceforge.fenixedu.util.teacherServiceDistribution.report;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.dataTransferObject.teacherServiceDistribution.TSDCourseDTOEntry;
import net.sourceforge.fenixedu.dataTransferObject.teacherServiceDistribution.TSDProfessorshipDTOEntry;
import net.sourceforge.fenixedu.dataTransferObject.teacherServiceDistribution.TSDTeacherDTOEntry;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.TSDTeacher;
import pt.utl.ist.fenix.tools.util.i18n.Language;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.Region;

import pt.ist.utl.fenix.utils.Pair;

public class TeacherServiceDistributionSpreadsheet {
	private List<TSDCourseDTOEntry> tsdCourseDTOEntryList;

	private List<TSDTeacherDTOEntry> tsdTeacherDTOEntryList;

	private String spreadsheetName = null;

	private HSSFWorkbook workbook = null;
	private ResourceBundle resourceBundle = ResourceBundle.getBundle("resources.DepartmentMemberResources", Language.getLocale());

	public TeacherServiceDistributionSpreadsheet(
			List<TSDCourseDTOEntry> _tsdCourseDTOEntryList,
			List<TSDTeacherDTOEntry> _tsdTeacherDTOEntryList,
			String _spreadsheetName) {
		this.tsdCourseDTOEntryList = _tsdCourseDTOEntryList;
		this.tsdTeacherDTOEntryList = _tsdTeacherDTOEntryList;
		this.spreadsheetName = _spreadsheetName;
		
		if(!tsdCourseDTOEntryList.isEmpty()) {
			Collections.sort(tsdCourseDTOEntryList, new BeanComparator("TSDCourse.name"));
		}
		
		if(!tsdTeacherDTOEntryList.isEmpty()) {
			Collections.sort(tsdTeacherDTOEntryList, new BeanComparator("name"));
		}
	}

	public List<TSDCourseDTOEntry> getTSDCourseDTOEntryList() {
		return tsdCourseDTOEntryList;
	}

	public void setTSDCourseDTOEntryList(List<TSDCourseDTOEntry> tsdCourseDTOEntryList) {
		this.tsdCourseDTOEntryList = tsdCourseDTOEntryList;
	}

	public List<TSDTeacherDTOEntry> getTSDTeacherDTOEntryList() {
		return tsdTeacherDTOEntryList;
	}

	public void setTSDTeacherDTOEntryList(List<TSDTeacherDTOEntry> tsdTeacherDTOEntryList) {
		this.tsdTeacherDTOEntryList = tsdTeacherDTOEntryList;
	}

	public void exportToXLSSheet(final OutputStream outputStream) throws IOException {
		workbook = new HSSFWorkbook();

		//buildTeachersResume();
		buildTSD();

		workbook.write(outputStream);
	}

	private void buildTSD() {
		HSSFSheet dsdSheet = workbook.createSheet(resourceBundle.getString("label.teacherServiceDistribution.TSD"));

		HSSFRow row = dsdSheet.createRow(0);
		HSSFCellStyle style = createSheetNameStyle();
		HSSFCell cellTitle = row.createCell((short) 0);
		cellTitle.setCellValue(resourceBundle.getString("link.teacherServiceDistribution"));
		cellTitle.setCellStyle(style);

		dsdSheet.addMergedRegion(new Region(0, (short) 0, 4, (short) 6));

		buildCourseInformationIntoTSD(dsdSheet, (short) 0, (short) 8);

		buildTeacherInformationIntoTSD(dsdSheet, (short) 15, (short) 0);

		fillTSDProcessIntoTSD(dsdSheet, (short) 15, (short) 10);

		buildTeachersCreditsIntoTSD(dsdSheet, (short) 0, (short) (15 + tsdTeacherDTOEntryList.size()));
	}

private void fillTSDProcessIntoTSD(HSSFSheet dsdSheet, short rowStart, short columnStart) {
		short i = rowStart;
		HSSFCellStyle style = createProfessorshipValuesStyle();
		for (TSDTeacherDTOEntry tsdTeacherDTOEntry : tsdTeacherDTOEntryList) {
			short j = columnStart;
			HSSFRow row = dsdSheet.createRow(i);

			for (TSDCourseDTOEntry tsdCourseDTOEntry : tsdCourseDTOEntryList) {
				HSSFCell cell = row.createCell(j);
				Double hours = 0d;
				
				TSDProfessorshipDTOEntry tsdProfessorshipDTOEntry = tsdTeacherDTOEntry.getTSDProfessorshipDTOEntryByTSDCourseDTOEntry(
						tsdCourseDTOEntry);
				
				if (tsdProfessorshipDTOEntry != null) {
					hours = tsdProfessorshipDTOEntry.getTotalHours();
				}

				if (hours > 0d) {
					cell.setCellValue(hours);
				}

				cell.setCellStyle(style);
				j += 1;
			}

			i += 1;
		}
	}	private void buildTeacherInformationIntoTSD(HSSFSheet dsdSheet, short rowStart, short columnStart) {
		HSSFCellStyle style = createTeacherHeaderTitleStyle();
		HSSFRow row = dsdSheet.createRow(rowStart);
		HSSFCell cell = row.createCell((short) (columnStart + 1));
		cell.setCellValue(resourceBundle.getString("label.teacherServiceDistribution.name"));
		cell.setCellStyle(style);

		cell = row.createCell((short) (columnStart + 2));
		cell.setCellValue(resourceBundle.getString("label.teacherServiceDistribution.hours"));
		cell.setCellStyle(style);

		cell = row.createCell((short) (columnStart + 3));
		cell.setCellValue(resourceBundle.getString("label.teacherServiceDistribution.extraCredits"));
		cell.setCellStyle(style);

		style = createMissingLecturedHoursTitleStyle();

		cell = row.createCell((short) (columnStart + 4));
		cell.setCellValue(resourceBundle.getString("label.teacherServiceDistribution.availability"));
		cell.setCellStyle(style);

		fillTeacherInformationIntoTSD(dsdSheet, (short) (rowStart + 1), columnStart);
	}

	private void buildTeachersCreditsIntoTSD(HSSFSheet dsdSheet, short rowStart, short columnStart) {
		columnStart = (short) (15 + tsdTeacherDTOEntryList.size());

		HSSFCellStyle style = createTeacherHeaderTitleStyle();
		HSSFRow row = dsdSheet.createRow(rowStart);
		HSSFCell cell = row.createCell((short) (columnStart + 1));
		cell.setCellValue("");
		cell.setCellStyle(style);
	}

	private void fillTeacherInformationIntoTSD(HSSFSheet dsdSheet, short rowStart, short columnStart) {
		short i = rowStart;
		double totalRequiredHours = 0d;
		double totalCredits = 0d;
		double totalOption = 0d;
		double totalAvailability = 0d;
		
		HSSFCellStyle style = createTeacherValuesStyle();

		for (TSDTeacherDTOEntry tsdTeacherDTOEntry : tsdTeacherDTOEntryList) {
			
			HSSFRow row = dsdSheet.createRow(i);
			HSSFCell cell = row.createCell((short) (columnStart + 0));
			TSDTeacher tsdTeacher = tsdTeacherDTOEntry.getTSDTeachers().get(0);
			Integer teacherNumber = tsdTeacher.getTeacherNumber();
			cell.setCellValue(teacherNumber == null ? "" : teacherNumber.toString());
			cell.setCellStyle(style);

			cell = row.createCell((short) (columnStart + 1));
			cell.setCellValue(tsdTeacherDTOEntry.getName());
			cell.setCellStyle(style);

			cell = row.createCell((short) (columnStart + 2));
			totalRequiredHours += tsdTeacherDTOEntry.getRequiredHours();
			cell.setCellValue(tsdTeacherDTOEntry.getRequiredHours());
			cell.setCellStyle(style);

			cell = row.createCell((short) (columnStart + 3));
			if(tsdTeacherDTOEntry.getUsingExtraCredits()) {
				cell.setCellValue(tsdTeacherDTOEntry.getExtraCreditsValue());
			}
			cell.setCellStyle(style);

			Double availability = tsdTeacherDTOEntry.getAvailability();
			totalAvailability += availability;
			cell = row.createCell((short) (columnStart + 4));
			cell.setCellValue(availability);
			cell.setCellStyle(style);

			i += 1;
		}

		style = createMissingLecturedHoursValuesStyle();
		HSSFRow row = dsdSheet.createRow(i);
		HSSFCell cell = row.createCell((short) (columnStart + 0));
		cell.setCellValue(resourceBundle.getString("label.teacherServiceDistribution.total"));
		cell.setCellStyle(style);
		
		style = createMissingLecturedHoursValuesStyle();
		row = dsdSheet.createRow(i);
		cell = row.createCell((short) (columnStart + 2));
		cell.setCellValue(totalRequiredHours);
		cell.setCellStyle(style);

		cell = row.createCell((short) (columnStart + 3));
		cell.setCellValue(totalCredits);
		cell.setCellStyle(style);

		cell = row.createCell((short) (columnStart + 4));
		cell.setCellValue(totalOption);
		cell.setCellStyle(style);

		cell = row.createCell((short) (columnStart + 5));
		cell.setCellValue(totalAvailability);
		cell.setCellStyle(style);

	}

	private void buildCourseInformationIntoTSD(HSSFSheet dsdSheet, short rowStart, short columnStart) {
		HSSFRow row = dsdSheet.createRow(rowStart + 1);
		HSSFCellStyle style = createRotatedCourseBannerStyle();
		HSSFCell cellTitle = row.createCell(columnStart);
		cellTitle.setCellValue(resourceBundle.getString("label.teacherServiceDistribution.competenceCourse"));
		cellTitle.setCellStyle(style);

		style = createCourseInformationTitleStyle();
		row = dsdSheet.createRow(rowStart + 2);
		HSSFCell cell = row.createCell(columnStart);
		cell.setCellValue(resourceBundle.getString("label.teacherServiceDistribution.campus"));
		cell.setCellStyle(style);

		row = dsdSheet.createRow(rowStart + 3);
		cell = row.createCell(columnStart);
		cell.setCellValue(resourceBundle.getString("label.teacherServiceDistribution.course"));
		cell.setCellStyle(style);

		row = dsdSheet.createRow(rowStart + 4);
		cell = row.createCell(columnStart);
		cell.setCellValue(resourceBundle.getString("label.teacherServiceDistribution.curricularYears"));
		cell.setCellStyle(style);

		row = dsdSheet.createRow(rowStart + 5);
		cell = row.createCell(columnStart);
		cell.setCellValue(resourceBundle.getString("label.teacherService.course.semester"));
		cell.setCellStyle(style);

		style = createStudentInformationTitleStyle();
		row = dsdSheet.createRow(rowStart + 6);
		cell = row.createCell(columnStart);
		cell.setCellValue(resourceBundle.getString("label.teacherServiceDistribution..num.firstTimeEnrolledStudents"));
		cell.setCellStyle(style);

		row = dsdSheet.createRow(rowStart + 7);
		cell = row.createCell(columnStart);
		cell.setCellValue(resourceBundle.getString("label.teacherServiceDistribution..num.secondTimeEnrolledStudents"));
		cell.setCellStyle(style);

		row = dsdSheet.createRow(rowStart + 8);
		cell = row.createCell(columnStart);
		cell.setCellValue(resourceBundle.getString("label.teacherServiceDistribution.students.total"));
		cell.setCellStyle(style);

		style = createCourseHoursInformationTitleStyle();
		
		
		/*row = dsdSheet.createRow(rowStart + 9);
		cell = row.createCell(columnStart);
		cell.setCellValue(resourceBundle.getString("label.teacherServiceDistribution.theoreticalHours"));
		cell.setCellStyle(style);

		row = dsdSheet.createRow(rowStart + 10);
		cell = row.createCell(columnStart);
		cell.setCellValue(resourceBundle.getString("label.teacherServiceDistribution.praticalHours"));
		cell.setCellStyle(style);

		row = dsdSheet.createRow(rowStart + 11);
		cell = row.createCell(columnStart);
		cell.setCellValue(resourceBundle.getString("label.teacherServiceDistribution.theoPratHours"));
		cell.setCellStyle(style);

		row = dsdSheet.createRow(rowStart + 12);
		cell = row.createCell(columnStart);
		cell.setCellValue(resourceBundle.getString("label.teacherServiceDistribution.laboratorialHours"));
		cell.setCellStyle(style);
*/
		row = dsdSheet.createRow(rowStart + 9);
		cell = row.createCell(columnStart);
		cell.setCellValue(resourceBundle.getString("label.teacherServiceDistribution.hours.total"));
		cell.setCellStyle(style);

		style = createMissingLecturedHoursTitleStyle();
		row = dsdSheet.createRow(rowStart + 10);
		cell = row.createCell(columnStart);
		cell.setCellValue(resourceBundle.getString("label.teacherServiceDistribution.missing"));
		cell.setCellStyle(style);

		fillCourseInformationIntoTSD(dsdSheet, rowStart, (short) (columnStart + 1));
	}

	private void fillCourseInformationIntoTSD(HSSFSheet dsdSheet, short rowStart, short columnStart) {
		int totalFirstTimeEnrolledStudents = 0;
		int totalSecondOrMoreTimeEnrolledStudents = 0;
		/*double totalTheoreticalHours = 0d;
		double totalPraticalHours = 0d;
		double totalTheoreticalPraticalHours = 0d;
		double totalLaboratorialHours = 0d;*/
		double sumTotalHours = 0d;
		double sumTotalHoursNotLectured = 0d;

		short j = (short) (columnStart + 1);
		
		HSSFCellStyle rotatedStyle = createRotatedCourseNameStyle();
		HSSFCellStyle courseInfostyle = createCourseInformationValuesStyle();
		HSSFCellStyle studentInfoStyle = createStudentInformationValuesStyle();
		HSSFCellStyle courseHoursStyle = createCourseHoursInformationValuesStyle();
		HSSFCellStyle missingStyle = createMissingLecturedHoursValuesStyle();

		for (TSDCourseDTOEntry tsdCourseDTOEntry : tsdCourseDTOEntryList) {
			HSSFRow row = dsdSheet.createRow(rowStart + 1);
			
			HSSFCell cell = row.createCell(j);
			cell.setCellValue(tsdCourseDTOEntry.getTSDCourse().getCompetenceName());
			cell.setCellStyle(rotatedStyle);

			row = dsdSheet.createRow(rowStart + 2);
			cell = row.createCell(j);
			String campusString = "";
			for (String campus : tsdCourseDTOEntry.getTSDCourse().getCampus()) {
				campusString += campus + "\n";
			}
			cell.setCellValue(campusString);
			cell.setCellStyle(courseInfostyle);

			row = dsdSheet.createRow(rowStart + 3);
			cell = row.createCell(j);
			String degreesString = "";
			for (Pair<String, List<String>> degree : tsdCourseDTOEntry.getCurricularCoursesInformation()) {
				degreesString += degree.getKey() + "\n";
			}
			cell.setCellValue(degreesString);
			cell.setCellStyle(courseInfostyle);

			row = dsdSheet.createRow(rowStart + 4);
			cell = row.createCell(j);
			String curricularYears = "";
			for (Pair<String, List<String>> degree : tsdCourseDTOEntry.getCurricularCoursesInformation()) {
				for (String year : degree.getValue()) {
					curricularYears += year + "\n";
				}
			}
			cell.setCellValue(curricularYears);
			cell.setCellStyle(courseInfostyle);

			row = dsdSheet.createRow(rowStart + 5);
			cell = row.createCell(j);
			cell.setCellValue(tsdCourseDTOEntry.getTSDCourse().getExecutionPeriod().getSemester());
			cell.setCellStyle(courseInfostyle);

			row = dsdSheet.createRow(rowStart + 6);
			cell = row.createCell(j);
			int firstTimeEnrolledStudentsNumber = tsdCourseDTOEntry.getTSDCourse().getFirstTimeEnrolledStudents();
			totalFirstTimeEnrolledStudents += firstTimeEnrolledStudentsNumber;
			cell.setCellValue(firstTimeEnrolledStudentsNumber);
			cell.setCellStyle(studentInfoStyle);

			row = dsdSheet.createRow(rowStart + 7);
			cell = row.createCell(j);
			int secondTimeEnrolledStudentsNumber = tsdCourseDTOEntry.getTSDCourse().getSecondTimeEnrolledStudents();
			totalSecondOrMoreTimeEnrolledStudents += secondTimeEnrolledStudentsNumber;
			cell.setCellValue(secondTimeEnrolledStudentsNumber);
			cell.setCellStyle(studentInfoStyle);

			row = dsdSheet.createRow(rowStart + 8);
			cell = row.createCell(j);
			cell.setCellValue(firstTimeEnrolledStudentsNumber + secondTimeEnrolledStudentsNumber);
			cell.setCellStyle(studentInfoStyle);

			/*row = dsdSheet.createRow(rowStart + 9);
			cell = row.createCell(j);
			totalTheoreticalHours += tsdCourseDTOEntry.getTSDCourse().getHours(ShiftType.TEORICA);
			cell.setCellValue(tsdCourseDTOEntry.getTSDCourse().getHours(ShiftType.TEORICA));
			cell.setCellStyle(courseHoursStyle);

			row = dsdSheet.createRow(rowStart + 10);
			cell = row.createCell(j);
			totalPraticalHours = tsdCourseDTOEntry.getTSDCourse().getHours(ShiftType.PRATICA);
			cell.setCellValue(tsdCourseDTOEntry.getTSDCourse().getHours(ShiftType.PRATICA));
			cell.setCellStyle(courseHoursStyle);

			row = dsdSheet.createRow(rowStart + 11);
			cell = row.createCell(j);
			totalTheoreticalPraticalHours += tsdCourseDTOEntry.getTSDCourse().getHours(ShiftType.TEORICO_PRATICA);
			cell.setCellValue(tsdCourseDTOEntry.getTSDCourse().getHours(ShiftType.TEORICO_PRATICA));
			cell.setCellStyle(courseHoursStyle);

			row = dsdSheet.createRow(rowStart + 12);
			cell = row.createCell(j);
			totalLaboratorialHours += tsdCourseDTOEntry.getTSDCourse().getHours(ShiftType.LABORATORIAL);
			cell.setCellValue(tsdCourseDTOEntry.getTSDCourse().getHours(ShiftType.LABORATORIAL));
			cell.setCellStyle(courseHoursStyle);
			*/
			row = dsdSheet.createRow(rowStart + 9);
			cell = row.createCell(j);
			double totalHours = tsdCourseDTOEntry.getTSDCourse().getTotalHours();
			sumTotalHours += totalHours;
			cell.setCellValue(totalHours);
			cell.setCellStyle(courseHoursStyle);

			row = dsdSheet.createRow(rowStart + 10);
			cell = row.createCell(j);
			double totalHoursNotLectured = tsdCourseDTOEntry.getTSDCourse().getTotalHoursNotLectured();
			sumTotalHoursNotLectured += totalHoursNotLectured;
			cell.setCellValue(totalHoursNotLectured);
			cell.setCellStyle(missingStyle);

			j += 1;
		}

		HSSFCellStyle style = createRotatedCourseBannerStyle();
		HSSFRow row = dsdSheet.createRow(rowStart + 1);
		HSSFCell cell = row.createCell(j);
		cell.setCellValue(resourceBundle.getString("label.teacherServiceDistribution.total"));
		cell.setCellStyle(style);

		style = createStudentInformationValuesStyle();
		row = dsdSheet.createRow(rowStart + 6);
		cell = row.createCell(j);
		cell.setCellValue(totalFirstTimeEnrolledStudents);
		cell.setCellStyle(style);

		row = dsdSheet.createRow(rowStart + 7);
		cell = row.createCell(j);
		cell.setCellValue(totalSecondOrMoreTimeEnrolledStudents);
		cell.setCellStyle(style);

		row = dsdSheet.createRow(rowStart + 8);
		cell = row.createCell(j);
		cell.setCellValue(totalFirstTimeEnrolledStudents + totalSecondOrMoreTimeEnrolledStudents);
		cell.setCellStyle(style);

		style = createCourseHoursInformationValuesStyle();
		/*row = dsdSheet.createRow(rowStart + 9);
		cell = row.createCell(j);
		cell.setCellValue(totalTheoreticalHours);
		cell.setCellStyle(style);

		row = dsdSheet.createRow(rowStart + 10);
		cell = row.createCell(j);
		cell.setCellValue(totalPraticalHours);
		cell.setCellStyle(style);

		row = dsdSheet.createRow(rowStart + 11);
		cell = row.createCell(j);
		cell.setCellValue(totalTheoreticalPraticalHours);
		cell.setCellStyle(style);

		row = dsdSheet.createRow(rowStart + 12);
		cell = row.createCell(j);
		cell.setCellValue(totalLaboratorialHours);
		cell.setCellStyle(style);
*/
		row = dsdSheet.createRow(rowStart + 9);
		cell = row.createCell(j);
		cell.setCellValue(sumTotalHours);
		cell.setCellStyle(style);

		style = createMissingLecturedHoursValuesStyle();
		row = dsdSheet.createRow(rowStart + 10);
		cell = row.createCell(j);
		cell.setCellValue(sumTotalHoursNotLectured);
		cell.setCellStyle(style);
	}

	private void buildTeachersResume() {
		double totalHours = 0d;
		double totalRequiredTeachingHours = 0d;
		double totalExtraCredits = 0d;
	
		HSSFSheet teachersResumeSheet = workbook.createSheet(resourceBundle.getString("label.teacherServiceDistribution.tsdTeacher"));

		HSSFRow row = teachersResumeSheet.createRow(0);
		HSSFCellStyle style = createSheetNameStyle();

		HSSFCell cellTitle = row.createCell((short) 0);
		cellTitle.setCellValue(resourceBundle.getString("label.teacherServiceDistribution.tsdTeacher") + " " + spreadsheetName);
		cellTitle.setCellStyle(style);

		row = teachersResumeSheet.createRow(2);
		style = createHeadingStyle();
		HSSFCell cell = row.createCell((short) 0);
		cell.setCellValue(resourceBundle.getString("label.teacherServiceDistribution.tsdTeacher.singular"));
		cell.setCellStyle(style);

		cell = row.createCell((short) 1);
		cell.setCellValue(resourceBundle.getString("label.teacherServiceDistribution.category"));
		cell.setCellStyle(style);

		cell = row.createCell((short) 2);
		cell.setCellValue(resourceBundle.getString("label.teacherServiceDistribution.hours.total"));
		cell.setCellStyle(style);

		cell = row.createCell((short) 3);
		cell.setCellValue(resourceBundle.getString("label.teacherServiceDistribution.executionHours"));
		cell.setCellStyle(style);

		cell = row.createCell((short) 4);
		cell.setCellValue(resourceBundle.getString("label.teacherServiceDistribution.extraCredits"));
		cell.setCellStyle(style);
		
		cell = row.createCell((short) 5);
		cell.setCellValue(resourceBundle.getString("label.teacherServiceDistribution.observations"));
		cell.setCellStyle(style);

		short i = 3;
		for (TSDTeacherDTOEntry tsdTeacherDTOEntry : tsdTeacherDTOEntryList) {
			row = teachersResumeSheet.createRow(i);
			cell = row.createCell((short) 0);
			cell.setCellValue(tsdTeacherDTOEntry.getName());

			cell = row.createCell((short) 1);
			cell.setCellValue(tsdTeacherDTOEntry.getCategory().getShortName());

			cell = row.createCell((short) 2);
			cell.setCellValue(tsdTeacherDTOEntry.getRequiredHours());
			totalHours += tsdTeacherDTOEntry.getRequiredHours();

			cell = row.createCell((short) 3);
			cell.setCellValue(tsdTeacherDTOEntry.getRequiredTeachingHours());
			totalRequiredTeachingHours += tsdTeacherDTOEntry.getRequiredTeachingHours();

			cell = row.createCell((short) 4);
			if(tsdTeacherDTOEntry.getUsingExtraCredits()) {
				cell.setCellValue(tsdTeacherDTOEntry.getExtraCreditsValue());
				totalExtraCredits += tsdTeacherDTOEntry.getExtraCreditsValue();
			}

			cell = row.createCell((short) 5);
			if(tsdTeacherDTOEntry.getUsingExtraCredits()) {
				cell.setCellValue(tsdTeacherDTOEntry.getExtraCreditsName());
			}
			
			i += 1;
		}

		row = teachersResumeSheet.createRow(i);
		style = createHeadingStyle();
		cell = row.createCell((short) 0);
		cell.setCellValue(resourceBundle.getString("label.teacherServiceDistribution.hours.total"));
		cell.setCellStyle(style);

		cell = row.createCell((short) 2);
		cell.setCellValue(totalHours);
		cell.setCellStyle(style);

		cell = row.createCell((short) 3);
		cell.setCellValue(totalRequiredTeachingHours);
		cell.setCellStyle(style);

		cell = row.createCell((short) 4);
		cell.setCellValue(totalExtraCredits);
		cell.setCellStyle(style);

		row = teachersResumeSheet.createRow(i + 2);
		style = createHeadingStyle();
		cell = row.createCell((short) 0);
		cell.setCellValue(resourceBundle.getString("label.teacherServiceDistribution.tsdTeacher.availability.total"));
		cell.setCellStyle(style);

		cell = row.createCell((short) 1);
		cell.setCellValue(totalRequiredTeachingHours);
		cell.setCellStyle(style);

	}

	private HSSFCellStyle createHeadingStyle() {
		HSSFFont font = workbook.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font.setFontHeightInPoints((short) 13);
		font.setFontName("Verdana");

		HSSFCellStyle style = workbook.createCellStyle();
		style.setFont(font);

		return style;
	}

	private HSSFCellStyle createSheetNameStyle() {
		HSSFFont font = workbook.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font.setFontHeightInPoints((short) 18);
		font.setFontName("Verdana");

		HSSFCellStyle style = workbook.createCellStyle();
		style.setFont(font);

		return style;
	}

	private HSSFCellStyle createRotatedCourseBannerStyle() {
		HSSFFont font = workbook.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font.setFontHeightInPoints((short) 16);
		font.setFontName("Verdana");
		font.setColor(HSSFColor.CORAL.index);
		HSSFCellStyle style = workbook.createCellStyle();
		style.setFont(font);
		style.setRotation((short) 90);
		setBorder(style);

		return style;
	}

	private HSSFCellStyle createCourseInformationTitleStyle() {
		HSSFFont font = workbook.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font.setFontHeightInPoints((short) 12);
		font.setFontName("Verdana");
		font.setColor(HSSFColor.BLUE_GREY.index);
		HSSFCellStyle style = workbook.createCellStyle();
		style.setFont(font);
		setBorder(style);

		return style;
	}

	private HSSFCellStyle createStudentInformationTitleStyle() {
		HSSFFont font = workbook.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font.setFontHeightInPoints((short) 12);
		font.setFontName("Verdana");
		font.setColor(HSSFColor.DARK_RED.index);
		HSSFCellStyle style = workbook.createCellStyle();
		style.setFont(font);
		setBorder(style);

		return style;
	}

	private HSSFCellStyle createCourseHoursInformationTitleStyle() {
		HSSFFont font = workbook.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font.setFontHeightInPoints((short) 12);
		font.setFontName("Verdana");
		font.setColor(HSSFColor.SEA_GREEN.index);
		HSSFCellStyle style = workbook.createCellStyle();
		style.setFont(font);
		setBorder(style);

		return style;
	}

	private HSSFCellStyle createRotatedCourseNameStyle() {
		HSSFFont font = workbook.createFont();
		font.setFontHeightInPoints((short) 12);
		font.setFontName("Verdana");
		font.setColor(HSSFColor.DARK_BLUE.index);
		HSSFCellStyle style = workbook.createCellStyle();
		style.setFont(font);
		style.setRotation((short) 90);
		setBorder(style);

		return style;
	}

	private HSSFCellStyle createCourseInformationValuesStyle() {
		HSSFFont font = workbook.createFont();
		font.setFontHeightInPoints((short) 12);
		font.setFontName("Verdana");
		font.setColor(HSSFColor.BLUE_GREY.index);
		HSSFCellStyle style = workbook.createCellStyle();
		style.setFont(font);
		setBorder(style);

		return style;
	}

	private HSSFCellStyle createStudentInformationValuesStyle() {
		HSSFFont font = workbook.createFont();
		font.setFontHeightInPoints((short) 12);
		font.setFontName("Verdana");
		font.setColor(HSSFColor.DARK_RED.index);
		HSSFCellStyle style = workbook.createCellStyle();
		style.setFont(font);
		setBorder(style);

		return style;
	}

	private HSSFCellStyle createCourseHoursInformationValuesStyle() {
		HSSFFont font = workbook.createFont();
		font.setFontHeightInPoints((short) 12);
		font.setFontName("Verdana");
		font.setColor(HSSFColor.SEA_GREEN.index);
		HSSFCellStyle style = workbook.createCellStyle();
		style.setFont(font);
		setBorder(style);

		return style;
	}

	private HSSFCellStyle createMissingLecturedHoursTitleStyle() {
		HSSFFont font = workbook.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font.setFontHeightInPoints((short) 12);
		font.setFontName("Verdana");
		font.setColor(HSSFColor.RED.index);
		HSSFCellStyle style = workbook.createCellStyle();
		style.setFont(font);
		setBorder(style);

		return style;
	}

	private HSSFCellStyle createMissingLecturedHoursValuesStyle() {
		HSSFFont font = workbook.createFont();
		font.setFontHeightInPoints((short) 12);
		font.setFontName("Verdana");
		font.setColor(HSSFColor.RED.index);
		HSSFCellStyle style = workbook.createCellStyle();
		style.setFont(font);
		setBorder(style);

		return style;
	}

	private HSSFCellStyle createTeacherHeaderTitleStyle() {
		HSSFFont font = workbook.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font.setFontHeightInPoints((short) 12);
		font.setFontName("Verdana");
		font.setColor(HSSFColor.DARK_TEAL.index);
		HSSFCellStyle style = workbook.createCellStyle();
		style.setFont(font);
		setBorder(style);

		return style;
	}

	private HSSFCellStyle createTeacherValuesStyle() {
		HSSFFont font = workbook.createFont();
		font.setFontHeightInPoints((short) 12);
		font.setFontName("Verdana");
		font.setColor(HSSFColor.DARK_TEAL.index);
		HSSFCellStyle style = workbook.createCellStyle();
		style.setFont(font);
		setBorder(style);

		return style;
	}

	private void setBorder(HSSFCellStyle style) {
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setBottomBorderColor(HSSFColor.BLACK.index);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style.setLeftBorderColor(HSSFColor.BLACK.index);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style.setRightBorderColor(HSSFColor.BLACK.index);
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style.setTopBorderColor(HSSFColor.BLACK.index);
	}

	private HSSFCellStyle createProfessorshipValuesStyle() {
		HSSFFont font = workbook.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font.setFontHeightInPoints((short) 12);
		font.setFontName("Verdana");
		font.setColor(HSSFColor.BLACK.index);
		HSSFCellStyle style = workbook.createCellStyle();
		style.setFont(font);
		setBorder(style);
		style.setFillBackgroundColor(HSSFColor.WHITE.index);
		style.setFillPattern(HSSFCellStyle.NO_FILL);

		return style;
	}

}
