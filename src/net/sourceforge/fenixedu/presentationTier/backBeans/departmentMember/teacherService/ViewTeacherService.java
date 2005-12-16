package net.sourceforge.fenixedu.presentationTier.backBeans.departmentMember.teacherService;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.dataTransferObject.teacher.distribution.DistributionTeacherServicesByCourseDTO.ExecutionCourseDistributionServiceEntryDTO;
import net.sourceforge.fenixedu.dataTransferObject.teacher.distribution.DistributionTeacherServicesByTeachersDTO.TeacherDistributionServiceEntryDTO;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.backBeans.base.FenixBackingBean;

/**
 * 
 * @author amak, jpmsit
 * 
 */
public class ViewTeacherService extends FenixBackingBean {

	
	private List<TeacherDistributionServiceEntryDTO> teacherServiceDTO;
	
	private List<ExecutionCourseDistributionServiceEntryDTO> executionCourseServiceDTO;
	
    private Integer selectedExecutionYearID;

    private String selectedExecutionYearName;
    
      
    public Integer getSelectedExecutionYearID() throws FenixFilterException, FenixServiceException {    	
    	if(this.selectedExecutionYearID == null) {
		
			try {
				this.selectedExecutionYearID = new Integer(this.getRequestParameter("selectedExecutionYearID"));
			} catch(NumberFormatException ex) {
				
			}
    	}
    	
    	return selectedExecutionYearID;
    }

    public void setSelectedExecutionYearID(Integer selectedExecutionYearID) {

        this.selectedExecutionYearID = selectedExecutionYearID;
    }

    public String getSelectedExecutionYearName() throws FenixFilterException, FenixServiceException {

        return selectedExecutionYearName;
    }

    public void setSelectedExecutionYearName(String selectedExecutionYearName) {
        this.selectedExecutionYearName = selectedExecutionYearName;
    }

    public List getTeacherServiceDTO() throws FenixFilterException, FenixServiceException {
    	if(teacherServiceDTO == null){
    		loadDistributionServiceData();
    	}
		return teacherServiceDTO;
	}

    public void setTeacherServiceDTO(List<TeacherDistributionServiceEntryDTO> teacherServiceDTO) {
		this.teacherServiceDTO = teacherServiceDTO;
	}
    
    
	public List<ExecutionCourseDistributionServiceEntryDTO> getExecutionCourseServiceDTO() throws FenixFilterException, FenixServiceException {
    	if(executionCourseServiceDTO == null){
    		loadDistributionServiceDataByCourse();
    	}
		return executionCourseServiceDTO;
	}

	public void setExecutionCourseServiceDTO(
			List<ExecutionCourseDistributionServiceEntryDTO> executionCourseServiceDTO) {
		this.executionCourseServiceDTO = executionCourseServiceDTO;
	}  

    
    public List<SelectItem> getExecutionYears() throws FenixFilterException, FenixServiceException {

        List<InfoExecutionYear> executionYears = (List<InfoExecutionYear>) ServiceUtils.executeService(
                getUserView(), "ReadNotClosedExecutionYears", null);

        List<SelectItem> result = new ArrayList<SelectItem>(executionYears.size());
        for (InfoExecutionYear executionYear : executionYears) {
            result.add(new SelectItem(executionYear.getIdInternal(), executionYear.getYear()));
        }

        setSelectedExecutionYearID(executionYears.get(executionYears.size() - 1).getIdInternal());

        return result;

    }
    
    public String getDepartmentName() {
    	return getUserView().getPerson().getTeacher().getCurrentWorkingDepartment().getRealName();
    }


    public String getTeacherService() throws FenixFilterException, FenixServiceException {

        InfoExecutionYear infoExecutionYear = (InfoExecutionYear) ServiceUtils.executeService(
                getUserView(), "ReadExecutionYearByID", new Object[] { this.selectedExecutionYearID });
        
        this.selectedExecutionYearName = infoExecutionYear.getYear();

        loadDistributionServiceData();
        return "listDistributionTeachersByTeacher";
    }

    
    public String getTeacherServiceByCourse() throws FenixFilterException, FenixServiceException {
        loadDistributionServiceDataByCourse();
        return "listDistributionTeachersByCourse";   	
    }
    
      
    private void loadDistributionServiceData() throws FenixFilterException, FenixServiceException {
       
        Object[] args = { getUserView().getPerson().getUsername(), this.selectedExecutionYearID };

        this.teacherServiceDTO = (List<TeacherDistributionServiceEntryDTO>) ServiceUtils
        						.executeService(getUserView(), "ReadTeacherServiceDistributionByTeachers", args);

    }
    
    private void loadDistributionServiceDataByCourse() throws FenixFilterException, FenixServiceException {
    	    		
        Object[] args = { getUserView().getPerson().getUsername(), this.selectedExecutionYearID };

        this.executionCourseServiceDTO = (List<ExecutionCourseDistributionServiceEntryDTO>) ServiceUtils
        						.executeService(getUserView(), "ReadTeacherServiceDistributionByCourse", args);       
    }   
    
}
