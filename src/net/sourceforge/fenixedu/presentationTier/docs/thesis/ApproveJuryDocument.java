package net.sourceforge.fenixedu.presentationTier.docs.thesis;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.thesis.Thesis;
import net.sourceforge.fenixedu.domain.thesis.ThesisEvaluationParticipant;
import net.sourceforge.fenixedu.presentationTier.docs.FenixReport;
import net.sourceforge.fenixedu.util.LanguageUtils;

public class ApproveJuryDocument extends FenixReport {

    /**
     * Serialization id. 
     */
    private static final long serialVersionUID = 1L;
    
    private final Thesis thesis;

    public ApproveJuryDocument(Thesis thesis) {
        	super();

        this.thesis = thesis;
        this.dataSource = new ArrayList();
        this.resourceBundle = ResourceBundle.getBundle("resources.ApplicationResources", LanguageUtils.getLocale());
        
        fillReport();
    }

    @Override
    protected void fillReport() {
        	parameters.put("institutionName", neverNull(RootDomainObject.getInstance().getInstitutionUnit().getName()).toUpperCase());
        
        	final Degree degree = thesis.getDegree();
        	parameters.put("studentDegreeName", neverNull(getPresentationName(degree)));
        
        	final Student student = thesis.getStudent();
        	parameters.put("studentNumber", student.getNumber());
        
        	final Person person = student.getPerson();
        	parameters.put("studentName", person.getName());
        
        	final ThesisEvaluationParticipant orientator = thesis.getOrientator();
        	parameters.put("orientatorName", orientator.getPerson().getName());
        	parameters.put("orientatorCategory", participantCategoryName(orientator));
        	parameters.put("orientatorAffiliation", neverNull(orientator.getAffiliation()));
        
        	final ThesisEvaluationParticipant coorientator = thesis.getCoorientator();
        	parameters.put("coorientatorName", coorientator.getPerson().getName());
        	parameters.put("coorientatorCategory", participantCategoryName(coorientator));
        	parameters.put("coorientatorAffiliation", neverNull(coorientator.getAffiliation()));
        
        	parameters.put("thesisTitle", thesis.getTitle().getContent());
        
        	final ThesisEvaluationParticipant juryPresident = thesis.getPresident();
        	parameters.put("juryPresidentName", juryPresident.getPerson().getName());
        	parameters.put("juryPresidentCategory", participantCategoryName(juryPresident));
        	parameters.put("juryPresidentAffiliation", neverNull(juryPresident.getAffiliation()));
        
        	final Set<ThesisEvaluationParticipant> vowels = new TreeSet<ThesisEvaluationParticipant>(ThesisEvaluationParticipant.COMPARATOR_BY_PERSON_NAME);
        	vowels.addAll(thesis.getVowels());
            
        Iterator<ThesisEvaluationParticipant> iterator = vowels.iterator();
        for (int i = 1; i < 4; i++) {
            final String vowelPrefix = "vowel" + i;

            if (iterator.hasNext()) {
                ThesisEvaluationParticipant vowel = iterator.next();
                
                parameters.put(vowelPrefix + "Name", vowel.getPerson().getName());
                parameters.put(vowelPrefix + "Category", participantCategoryName(vowel));
                parameters.put(vowelPrefix + "Affiliation", neverNull(vowel.getAffiliation()));
            }
            else {
                parameters.put(vowelPrefix + "Name", "");
                parameters.put(vowelPrefix + "Category", "");
                parameters.put(vowelPrefix + "Affiliation", "");
            }
        }
    }

    private String getPresentationName(Degree degree) {
        ResourceBundle enumResourceBundle = ResourceBundle.getBundle("resources.EnumerationResources", new Locale("pt"));
        ResourceBundle appResourceBundle = ResourceBundle.getBundle("resources.ApplicationResources", new Locale("pt"));
        
        return enumResourceBundle.getString(degree.getTipoCurso().toString())
                + " " + appResourceBundle.getString("label.in") + " "
                + degree.getNome();
    }

    private String neverNull(String value) {
        return value == null ? "" : value;
    }
    
    private String participantCategoryName(ThesisEvaluationParticipant participant) {
        if (participant == null) {
            return "";
        }
        else if (participant.getCategory() == null) {
            return "";
        }
        else {
            return participant.getCategory().getName().getContent();
        }
    }
    
    @Override
    public String getReportFileName() {
        return "pedido-homologacao-aluno-" + thesis.getStudent().getNumber();
    }

}
