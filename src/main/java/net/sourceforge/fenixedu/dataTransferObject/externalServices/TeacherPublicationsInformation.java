package net.sourceforge.fenixedu.dataTransferObject.externalServices;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import net.sourceforge.fenixedu.domain.Teacher;

import org.apache.commons.lang.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TeacherPublicationsInformation implements Serializable {

    private static final Logger logger = LoggerFactory.getLogger(TeacherPublicationsInformation.class);

    private static final String BASE_URL = "https://sotis.tecnico.ulisboa.pt/";

    private static final String CURRICULUM_PATH = "api/sotis-core/curriculum/a3es";

    public static Map<Teacher, List<String>> getTeacherPublicationsInformations(Set<Teacher> teachers) {
        Map<Teacher, List<String>> teacherPublicationsInformationMap = new HashMap<Teacher, List<String>>();

        Client client = ClientBuilder.newClient();
        WebTarget resource = client.target(BASE_URL);
        List<String> teacherIds = new ArrayList<String>();

        for (Teacher teacher : teachers) {
            teacherIds.add(teacher.getTeacherId());
        }

        resource = resource.path(CURRICULUM_PATH).queryParam("istids", StringUtils.join(teacherIds, ","));
        try {
            String allPublications = resource.request().get(String.class);
            JSONParser parser = new JSONParser();
            for (Object teacherPublications : (JSONArray) parser.parse(allPublications)) {
                JSONObject teacherPublicationsInfo = (JSONObject) teacherPublications;
                final String username = (String) teacherPublicationsInfo.get("istID");
                final Teacher teacher = Teacher.readByIstId(username);
                JSONArray preferredPublications = (JSONArray) teacherPublicationsInfo.get("preferred");

                List<String> teacherPublicationsList = new ArrayList<String>();
                for (Object publication : preferredPublications) {
                    teacherPublicationsList.add(publication.toString());
                }

                teacherPublicationsInformationMap.put(teacher, teacherPublicationsList);
            }
        } catch (ParseException e) {
            logger.error(e.getMessage(), e);
        } finally {
            client.close();
        }

        return teacherPublicationsInformationMap;
    }
}