/*
 * Created on Jan 11, 2005
 *
 */
package DataBeans.projectsManagement;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.struts.util.LabelValueBean;

import DataBeans.DataTranferObject;
import Dominio.projectsManagement.IProject;
import Util.projectsManagement.ExcelStyle;

/**
 * @author Susana Fernandes
 * 
 */
public class InfoProject extends DataTranferObject {
    private String projectCode;

    private String title;

    private Integer coordinatorCode;

    private String coordinatorName;

    private String origin;

    private LabelValueBean type;

    private String cost;

    private String coordination;

    private Integer explorationUnit;

    public String getCoordination() {
        return coordination;
    }

    public void setCoordination(String coordination) {
        this.coordination = coordination;
    }

    public String getCoordinatorName() {
        return coordinatorName;
    }

    public void setCoordinatorName(String coordinatorName) {
        this.coordinatorName = coordinatorName;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public Integer getExplorationUnit() {
        return explorationUnit;
    }

    public void setExplorationUnit(Integer explorationUnit) {
        this.explorationUnit = explorationUnit;
    }

    public Integer getCoordinatorCode() {
        return coordinatorCode;
    }

    public void setCoordinatorCode(Integer coordinatorCode) {
        this.coordinatorCode = coordinatorCode;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LabelValueBean getType() {
        return type;
    }

    public void setType(LabelValueBean type) {
        this.type = type;
    }

    public String getProjectIdentification() {
        return this.explorationUnit + this.origin + this.type.getLabel() + this.cost + this.coordination + this.getFourDigitsProjectCode();
    }

    public String getFourDigitsProjectCode() {
        if (projectCode.length() == 1) {
            return "000" + projectCode;
        }
        if (projectCode.length() == 2) {
            return "00" + projectCode;

        }
        if (projectCode.length() == 3) {
            return "0" + projectCode;
        }
        return projectCode;
    }

    public boolean equals(Object obj) {
        if ((obj != null) && (obj instanceof InfoProject))
            return (projectCode != null) && projectCode.equals(((InfoProject) obj).getProjectCode());
        return false;
    }

    public void copyFromDomain(IProject project) {
        if (project != null) {
            setCoordination(project.getCoordination());
            setCoordinatorCode(project.getCoordinatorCode());
            setCoordinatorName(project.getCoordinatorName());
            setCost(project.getCost());
            setExplorationUnit(project.getExplorationUnit());
            setOrigin(project.getOrigin());
            setProjectCode(project.getProjectCode());
            setTitle(project.getTitle());
            setType(project.getType());
        }
    }

    public static InfoProject newInfoFromDomain(IProject project) {
        InfoProject infoProject = null;
        if (project != null) {
            infoProject = new InfoProject();
            infoProject.copyFromDomain(project);
        }
        return infoProject;
    }

    public HSSFSheet getProjectInformationToExcel(HSSFSheet sheet) {
        HSSFRow row = sheet.createRow((short) 2);
        HSSFCell cell = row.createCell((short) 0);
        cell.setCellValue("Acrónimo:");
        cell.setCellStyle(ExcelStyle.LABEL_STYLE);
        cell = row.createCell((short) 1);
        cell.setCellValue(getTitle());
        cell.setCellStyle(ExcelStyle.VALUE_STYLE);
        row = sheet.createRow((short) 3);
        cell = row.createCell((short) 0);
        cell.setCellValue("Projecto Nº:");
        cell.setCellStyle(ExcelStyle.LABEL_STYLE);
        cell = row.createCell((short) 1);
        cell.setCellValue(getProjectCode());
        cell.setCellStyle(ExcelStyle.VALUE_STYLE);
        row = sheet.createRow((short) 4);
        cell = row.createCell((short) 0);
        cell.setCellValue("Tipo:");
        cell.setCellStyle(ExcelStyle.LABEL_STYLE);
        cell = row.createCell((short) 1);
        cell.setCellValue(getType().getLabel() + " - " + getType().getValue());
        cell.setCellStyle(ExcelStyle.VALUE_STYLE);
        row = sheet.createRow((short) 5);
        cell = row.createCell((short) 0);
        cell.setCellValue("Coordenador:");
        cell.setCellStyle(ExcelStyle.LABEL_STYLE);
        cell = row.createCell((short) 1);
        cell.setCellValue(getCoordinatorName());
        cell.setCellStyle(ExcelStyle.VALUE_STYLE);
        row = sheet.createRow((short) 6);
        cell = row.createCell((short) 0);
        cell.setCellValue("Data:");
        cell.setCellStyle(ExcelStyle.LABEL_STYLE);
        cell = row.createCell((short) 1);
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy 'às' HH:mm");
        cell.setCellValue(formatter.format(new Date()));
        cell.setCellStyle(ExcelStyle.VALUE_STYLE);
        return sheet;
    }

}
