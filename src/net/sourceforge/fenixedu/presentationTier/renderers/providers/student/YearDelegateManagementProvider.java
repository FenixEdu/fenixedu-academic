package net.sourceforge.fenixedu.presentationTier.renderers.providers.student;



import net.sourceforge.fenixedu.dataTransferObject.student.elections.StudentVoteBean;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import net.sourceforge.fenixedu.renderers.DataProvider;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;



public class YearDelegateManagementProvider implements DataProvider {

    public Object provide(Object source, Object currentValue) {
    	final StudentVoteBean studentVoteBean = (StudentVoteBean) source;
    	return studentVoteBean.getSelectedStudentVote("notCandidate");
    }

    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }
    
   
}
