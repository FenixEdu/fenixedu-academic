package net.sourceforge.fenixedu.dataTransferObject.externalServices;

import java.io.Serializable;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import net.sourceforge.fenixedu.domain.Teacher;

import org.apache.commons.lang.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;

public class TeacherPublicationsInformation implements Serializable {
    private static final String BASE_URL = "https://sotis.tecnico.ulisboa.pt/";

    private static final String CURRICULUM_PATH = "api/sotis-core/curriculum/a3es";

    public static Map<Teacher, List<String>> getTeacherPublicationsInformations(Set<Teacher> teachers) {
        Map<Teacher, List<String>> teacherPublicationsInformationMap = new HashMap<Teacher, List<String>>();
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            @Override
            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        } };
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            ;
        }

        Client client = Client.create();
        WebResource resource = client.resource(BASE_URL);
        List<String> teacherIds = new ArrayList<String>();

        for (Teacher teacher : teachers) {
            teacherIds.add(teacher.getTeacherId());
        }

        resource = resource.path(CURRICULUM_PATH).queryParam("istids", StringUtils.join(teacherIds, ","));
        try {
            String allPublications = resource.get(String.class);
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
        } catch (UniformInterfaceException e) {
            System.out.println(e);
        } catch (ParseException e) {
            System.out.println(e);
        }
        return teacherPublicationsInformationMap;
    }

}