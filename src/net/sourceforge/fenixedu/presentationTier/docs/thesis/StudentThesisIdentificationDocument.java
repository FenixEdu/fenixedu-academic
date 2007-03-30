package net.sourceforge.fenixedu.presentationTier.docs.thesis;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.domain.Language;
import net.sourceforge.fenixedu.domain.thesis.Thesis;
import net.sourceforge.fenixedu.domain.thesis.ThesisFile;

import org.joda.time.DateTime;

public class StudentThesisIdentificationDocument extends ThesisDocument {

    /**
     * Serialization id. 
     */
    private static final long serialVersionUID = 1L;
    
    public StudentThesisIdentificationDocument(Thesis thesis) {
        	super(thesis);
    }
    
    @Override
    protected void fillThesisInfo() {
        Thesis thesis = getThesis();

        ThesisFile file = thesis.getDissertation();
        if (file != null) {
            parameters.put("thesisTitle", file.getTitle());
            parameters.put("thesisSubtitle", neverNull(file.getSubTitle()));
            parameters.put("thesisLanguage", getLanguage(file));
        }
        else {
            parameters.put("thesisTitle", "");
            parameters.put("thesisSubtitle", "");
            parameters.put("thesisLanguage", "");
        }
        
        String date = null;

        DateTime discussion = thesis.getDiscussed();
        if (discussion != null) {
            date = String.format(new Locale("pt"), "%1$td/%1$tm/%1$tY", discussion.toDate());
        }
        
        parameters.put("discussion", neverNull(date));

        int index = 0;
        for (String keyword : splitKeywords(thesis.getKeywordsPt())) {
            parameters.put("keywordPt" + index++, keyword);
        }

        while (index < 6) {
            parameters.put("keywordPt" + index++, "");
        }
        
        index = 0;
        for (String keyword : splitKeywords(thesis.getKeywordsEn())) {
            parameters.put("keywordEn" + index++, keyword);
        }

        while (index < 6) {
            parameters.put("keywordEn" + index++, "");
        }
        
        parameters.put("abstractPt", neverNull(thesis.getThesisAbstractPt()));
        parameters.put("abstractEn", neverNull(thesis.getThesisAbstractEn()));
    }

    private String getLanguage(ThesisFile file) {
        Language language = file.getLanguage();
        
        if (language == null) {
            return "";
        }
        
        ResourceBundle enumResourceBundle = ResourceBundle.getBundle("resources.EnumerationResources", new Locale("pt"));
        return enumResourceBundle.getString(language.name());
    }

    private List<String> splitKeywords(String keywords) {
        List<String> result = new ArrayList<String>();
        
        if (keywords == null) {
            return result;
        }
        
        for (String part : keywords.split(",")) {
            String trimmed = part.trim();
            
            if (trimmed.length() > 0) {
                result.add(trimmed);
            }
        }
        
        return result;
    }

    @Override
    public String getReportFileName() {
        Thesis thesis = getThesis();
        return "identificacao-aluno-" + thesis.getStudent().getNumber();
    }

}
