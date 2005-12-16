package net.sourceforge.fenixedu.presentationTier.backBeans.departmentAdmOffice;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.IDepartment;
import net.sourceforge.fenixedu.presentationTier.backBeans.base.FenixBackingBean;

public class ManagementGroupsBackingBean extends FenixBackingBean {

    private List completeList = getDepartment().getCurrentActiveWorkingEmployees();
    //private List indexedList = indexFullList();
    
    private static int RESULTS_PER_PAGE = 15;
    private int currentPageIndex;
    private boolean listIndexing = true;
    private List<Integer> pageIndexes = initializePageIndexesList();
    
    public IDepartment getDepartment() {
        return getUserView().getPerson().getEmployee().getCurrentDepartmentWorkingPlace();
    }
    
    public List getCompleteList() {
        return completeList;
    }
    
    public int getIndexingSize() {
        return ManagementGroupsBackingBean.RESULTS_PER_PAGE;
    }

    public int getCurrentPageIndex() {
        return this.currentPageIndex;
    }
    
    public boolean getListIndexing() {
        return this.listIndexing;
    }
    
    public void setListIndexing(boolean listIndexing) {
        this.listIndexing =  listIndexing;
    }

    public List<Integer> getPageIndexes() {
        return this.pageIndexes;
    }

    private List<Integer> initializePageIndexesList() {
        int nrPages = completeList.size() / ManagementGroupsBackingBean.RESULTS_PER_PAGE;
        List<Integer> result = new ArrayList<Integer>(nrPages);
        for (int iter = 0; iter <= nrPages; iter++) {
            result.add(iter);
        }
        return result;
    }

    public void onPageChange() {
        this.currentPageIndex = Integer.valueOf(this.getRequestParameter("pageNumber"));
    }

}
