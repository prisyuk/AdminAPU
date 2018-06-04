package adminAPU.Pages.AddPages;

import adminAPU.BasePage;
import adminAPU.Pages.ExamsPage;
import com.alee.archive3.Archive3ServerConnector;
import com.alee.archive3.api.data.AttributeValue;
import com.alee.archive3.api.data.Card;
import com.alee.archive3.api.network.Requisite;
import com.alee.archive3.api.ws.DataService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public class AddExamPage extends BasePage {

    private static final String BACCALAUREATE_GRADUATERS_CARD_ID = "ALSFR-7686b5b7-1825-46c0-9583-4f349a4a7cbd";
    private static final String MAGISTRACY_GRADUATERS_CARD_ID = "ALSFR-18495abe-b76d-4a67-9455-38dbf671b257";

    private String educationLevel;

    private String selectedGroup;

    private static final String SUBJECT_NAME_ATTR_ID = "ALSFR-0115854a-0ad4-4e3c-9de2-cb7ef268297c";
    private static final String ALT_SUBJECT_NAME_ATTR_ID = "ALSFR-adbe90f3-80c2-48b5-bec7-9c0da5bd5c0d";
    private static final String DATE_ATTR_ID = "ALSFR-f03221ba-01ea-42fc-9b94-0ae814c258b2";
    private static final String TIME_ATTR_ID = "ALSFR-17be906e-57c7-472d-8c0b-4494195cbcfd";
    private static final String LECTURE_HALL_ATTR_ID = "ALSFR-93f9641e-c646-4a80-a927-a263566d297b";
    private static final String TEACHER_NAME_ATTR_ID = "ALSFR-fe777802-145b-4f86-94c9-0d110ea6700a";
    private static final String ALT_TEACHER_NAME_ATTR_ID = "ALSFR-adbe90f3-80c2-48b5-bec7-9c0da5bd5c0d";

    private String subjectNameValue;
    private String altSubjectNameValue;
    private String dateValue;
    private String timeValue;
    private String lectureHallValue;
    private String teacherNameValue;
    private String altTeacherNameValue;

    private Card card;
    private String parentID;

    public AddExamPage(PageParameters pageParameters) {

        parentID = pageParameters.get("parentID").toString();

        educationLevel = pageParameters.get("educationLevel").toString();
        selectedGroup = pageParameters.get("selectedGroup").toString();
        final Label educationLevelLabel = new Label("educationLevelLabel", educationLevel);
        final Label selectedGroupLabel = new Label("selectedGroupLabel", selectedGroup);

        final Archive3ServerConnector archive3ServerConnector = new Archive3ServerConnector("apu.su", 80);
        final Requisite requisite = new Requisite();
        requisite.instanceRichSettings();
        archive3ServerConnector.authenticate("admin", "ApU_36oe", requisite);

        final DataService dataService = archive3ServerConnector.getDataService();

        final TextField<String> subjectName = new TextField<>("subjectName", new PropertyModel<>(this, "subjectNameValue"));
        final TextField<String> altSubjectName = new TextField<>("altSubjectName", new PropertyModel<>(this, "altSubjectNameValue"));
        final TextField<String> date = new TextField<>("date", new PropertyModel<>(this, "dateValue"));
        final TextField<String> time = new TextField<>("time", new PropertyModel<>(this, "timeValue"));
        final TextField<String> lectureHall = new TextField<>("lectureHall", new PropertyModel<>(this, "lectureHallValue"));
        final TextField<String> teacherName = new TextField<>("teacherName", new PropertyModel<>(this, "teacherNameValue"));
        final TextField<String> altTeacherName = new TextField<>("altTeacherName", new PropertyModel<>(this, "altTeacherNameValue"));

        Form<?> titleForm = new Form<Void>("titleForm") {
        };

        Form<?> addExamInfoForm = new Form<Void>("addExamInfoForm") {
            @Override
            public void onSubmit() {
                if (subjectNameValue != null) {
                    card = new Card(subjectNameValue);
                } else {
                    card = new Card(altSubjectNameValue);
                }
                card.setParentObject(parentID);
                Map<String, List<AttributeValue>> attributeMap = new HashMap<>();

                attributeMap.put(SUBJECT_NAME_ATTR_ID, new ArrayList<>());
                AttributeValue subjectNameAttribute = new AttributeValue(SUBJECT_NAME_ATTR_ID, subjectNameValue);
                subjectNameAttribute.setStringValue(subjectNameValue);
                attributeMap.get(SUBJECT_NAME_ATTR_ID).add(subjectNameAttribute);

                attributeMap.put(ALT_SUBJECT_NAME_ATTR_ID, new ArrayList<>());
                AttributeValue altSubjectNameAttribute = new AttributeValue(ALT_SUBJECT_NAME_ATTR_ID, altSubjectNameValue);
                altSubjectNameAttribute.setStringValue(altSubjectNameValue);
                attributeMap.get(ALT_SUBJECT_NAME_ATTR_ID).add(altSubjectNameAttribute);

                attributeMap.put(DATE_ATTR_ID, new ArrayList<>());
                AttributeValue dateAttribute = new AttributeValue(DATE_ATTR_ID, dateValue);
                dateAttribute.setStringValue(dateValue);
                attributeMap.get(DATE_ATTR_ID).add(dateAttribute);

                attributeMap.put(TIME_ATTR_ID, new ArrayList<>());
                AttributeValue timeAttribute = new AttributeValue(TIME_ATTR_ID, timeValue);
                timeAttribute.setStringValue(timeValue);
                attributeMap.get(TIME_ATTR_ID).add(timeAttribute);

                attributeMap.put(LECTURE_HALL_ATTR_ID, new ArrayList<>());
                AttributeValue lectureHallAttribute = new AttributeValue(LECTURE_HALL_ATTR_ID, lectureHallValue);
                lectureHallAttribute.setStringValue(lectureHallValue);
                attributeMap.get(LECTURE_HALL_ATTR_ID).add(lectureHallAttribute);

                attributeMap.put(TEACHER_NAME_ATTR_ID, new ArrayList<>());
                AttributeValue teacherNameAttribute = new AttributeValue(TEACHER_NAME_ATTR_ID, teacherNameValue);
                teacherNameAttribute.setStringValue(teacherNameValue);
                attributeMap.get(TEACHER_NAME_ATTR_ID).add(teacherNameAttribute);

                attributeMap.put(ALT_TEACHER_NAME_ATTR_ID, new ArrayList<>());
                AttributeValue altTeacherNameAttribute = new AttributeValue(ALT_TEACHER_NAME_ATTR_ID, altTeacherNameValue);
                altTeacherNameAttribute.setStringValue(altTeacherNameValue);
                attributeMap.get(ALT_TEACHER_NAME_ATTR_ID).add(altTeacherNameAttribute);

                List<AttributeValue> attributesList = attributeMap.values().stream().flatMap(List::stream).collect(Collectors.toList());
                dataService.createCompleteCard(card, true, attributesList, false);

                archive3ServerConnector.logoff();
                pageParameters.remove("parentID");
                if (subjectNameValue != null) {
                    pageParameters.add("selectedExamName", subjectNameValue);
                } else {
                    pageParameters.add("selectedExamName", altSubjectNameValue);
                }
                setResponsePage(ExamsPage.class, pageParameters);
            }
        };

        Form<?> cancelForm = new Form<Void>("cancelForm") {
            @Override
            public void onSubmit() {
                setResponsePage(ExamsPage.class, pageParameters);
            }
        };

        add(titleForm);
        titleForm.add(educationLevelLabel);
        titleForm.add(selectedGroupLabel);

        add(addExamInfoForm);
        addExamInfoForm.add(subjectName);
        addExamInfoForm.add(altSubjectName);
        addExamInfoForm.add(date);
        addExamInfoForm.add(time);
        addExamInfoForm.add(lectureHall);
        addExamInfoForm.add(teacherName);
        addExamInfoForm.add(altTeacherName);

        add(cancelForm);

    }

}