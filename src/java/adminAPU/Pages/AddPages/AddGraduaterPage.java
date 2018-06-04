package adminAPU.Pages.AddPages;

import adminAPU.BasePage;
import adminAPU.Pages.GraduatersPage;
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

public class AddGraduaterPage extends BasePage {

    private static final String EDUCATION_LEVEL_ATTR_ID = "ALSFR-48d28b58-7ceb-4b6a-9375-f6734fcdc898";
    private static final String BACCALAUREATE_ID = "ALSFR-9fc82a1a-fa4d-4ab0-a53e-3e5d538161dd";
    private static final String MAGISTRACY_ID = "ALSFR-1d6ba1bf-5472-488b-934b-98b81b82eb1a";

    private String educationLevel;

    private static final String GRADUATION_YEAR_ATTR_ID = "ALSFR-180b591e-ca12-477e-94db-f5547853e9e2";

    private String selectedYear;

    private static final String GRADUATER_NAME_ATTR_ID = "ALSFR-1565d16b-3605-4d5f-b5b9-699425df3491";
    private static final String GRADUATION_WORK_NAME_ATTR_ID = "ALSFR-474fa9c9-b8e0-4c03-8008-6434df409b8a";
    private static final String TEACHER_NAME_ATTR_ID = "ALSFR-d5ed5c97-b216-4036-92fc-d511681ad6e1";
    private static final String ALT_TEACHER_NAME_ATTR_ID = "ALSFR-3e38cc5d-a730-4be2-9a7f-1ed887234c6d";
    private static final String JOB_PLACE_ATTR_ID = "ALSFR-aeb8fbb6-d691-4dce-8215-548951c52632";
    private static final String POSITION_ATTR_ID = "ALSFR-747d24dc-4437-4978-992c-5efbb0fbb70d";
    private static final String SHORT_REFERENCE_ATTR_ID = "ALSFR-02c8d4e0-ce02-4ddb-ad5d-095f0e358ddc";

    private String graduaterNameValue;
    private String graduationWorkNameValue;
    private String teacherNameValue;
    private String altTeacherNameValue;
    private String jobPlaceValue;
    private String positionValue;
    private String shortReferenceValue;

    private Card card;
    private String parentID;

    public AddGraduaterPage(PageParameters pageParameters) {

        parentID = pageParameters.get("parentID").toString();

        educationLevel = pageParameters.get("educationLevel").toString();
        selectedYear = pageParameters.get("selectedYear").toString();
        final Label educationLevelLabel = new Label("educationLevelLabel", educationLevel);
        final Label selectedYearLabel = new Label("selectedYearLabel", selectedYear);

        final Archive3ServerConnector archive3ServerConnector = new Archive3ServerConnector("apu.su", 80);
        final Requisite requisite = new Requisite();
        requisite.instanceRichSettings();
        archive3ServerConnector.authenticate("admin", "ApU_36oe", requisite);

        final DataService dataService = archive3ServerConnector.getDataService();

        final TextField<String> graduaterName = new TextField<>("graduaterName", new PropertyModel<>(this, "graduaterNameValue"));
        final TextField<String> graduationWorkName = new TextField<>("graduationWorkName", new PropertyModel<>(this, "graduationWorkNameValue"));
        final TextField<String> teacherName = new TextField<>("teacherName", new PropertyModel<>(this, "teacherNameValue"));
        final TextField<String> altTeacherName = new TextField<>("altTeacherName", new PropertyModel<>(this, "altTeacherNameValue"));
        final TextField<String> jobPlace = new TextField<>("jobPlace", new PropertyModel<>(this, "jobPlaceValue"));
        final TextField<String> position = new TextField<>("position", new PropertyModel<>(this, "positionValue"));
        final TextArea<String> shortReference = new TextArea<>("shortReference", new PropertyModel<>(this, "shortReferenceValue"));

        Form<?> titleForm = new Form<Void>("titleForm") {
        };

        Form<?> addGraduaterInfoForm = new Form<Void>("addGraduaterInfoForm") {
            @Override
            public void onSubmit() {
                card = new Card(graduaterNameValue);
                card.setParentObject(parentID);
                Map<String, List<AttributeValue>> attributeMap = new HashMap<>();

                attributeMap.put(EDUCATION_LEVEL_ATTR_ID, new ArrayList<>());
                if (educationLevel.equals("Бакалавриат")) {
                    AttributeValue educationLevelAttribute = new AttributeValue(EDUCATION_LEVEL_ATTR_ID, BACCALAUREATE_ID);
                    educationLevelAttribute.setStringValue("Бакалавриат");
                    attributeMap.get(EDUCATION_LEVEL_ATTR_ID).add(educationLevelAttribute);
                } else {
                    AttributeValue educationLevelAttribute = new AttributeValue(EDUCATION_LEVEL_ATTR_ID, MAGISTRACY_ID);
                    educationLevelAttribute.setStringValue("Магистратура");
                    attributeMap.get(EDUCATION_LEVEL_ATTR_ID).add(educationLevelAttribute);
                }

                attributeMap.put(GRADUATION_YEAR_ATTR_ID, new ArrayList<>());
                AttributeValue graduationYearAttribute = new AttributeValue(GRADUATION_YEAR_ATTR_ID, selectedYear);
                graduationYearAttribute.setStringValue(selectedYear);
                attributeMap.get(GRADUATION_YEAR_ATTR_ID).add(graduationYearAttribute);

                attributeMap.put(GRADUATER_NAME_ATTR_ID, new ArrayList<>());
                AttributeValue graduaterNameAttribute = new AttributeValue(GRADUATER_NAME_ATTR_ID, graduaterNameValue);
                graduaterNameAttribute.setStringValue(graduaterNameValue);
                attributeMap.get(GRADUATER_NAME_ATTR_ID).add(graduaterNameAttribute);

                attributeMap.put(GRADUATION_WORK_NAME_ATTR_ID, new ArrayList<>());
                AttributeValue graduationWorkNameAttribute = new AttributeValue(GRADUATION_WORK_NAME_ATTR_ID, graduationWorkNameValue);
                graduationWorkNameAttribute.setStringValue(graduationWorkNameValue);
                attributeMap.get(GRADUATION_WORK_NAME_ATTR_ID).add(graduationWorkNameAttribute);

                attributeMap.put(TEACHER_NAME_ATTR_ID, new ArrayList<>());
                AttributeValue teacherNameAttribute = new AttributeValue(TEACHER_NAME_ATTR_ID, teacherNameValue);
                teacherNameAttribute.setStringValue(teacherNameValue);
                attributeMap.get(TEACHER_NAME_ATTR_ID).add(teacherNameAttribute);

                attributeMap.put(ALT_TEACHER_NAME_ATTR_ID, new ArrayList<>());
                AttributeValue altTeacherNameAttribute = new AttributeValue(ALT_TEACHER_NAME_ATTR_ID, altTeacherNameValue);
                altTeacherNameAttribute.setStringValue(altTeacherNameValue);
                attributeMap.get(ALT_TEACHER_NAME_ATTR_ID).add(altTeacherNameAttribute);

                attributeMap.put(JOB_PLACE_ATTR_ID, new ArrayList<>());
                AttributeValue jobPlaceAttribute = new AttributeValue(JOB_PLACE_ATTR_ID, jobPlaceValue);
                jobPlaceAttribute.setStringValue(jobPlaceValue);
                attributeMap.get(JOB_PLACE_ATTR_ID).add(jobPlaceAttribute);

                attributeMap.put(POSITION_ATTR_ID, new ArrayList<>());
                AttributeValue positionAttribute = new AttributeValue(POSITION_ATTR_ID, positionValue);
                positionAttribute.setStringValue(positionValue);
                attributeMap.get(POSITION_ATTR_ID).add(positionAttribute);

                attributeMap.put(SHORT_REFERENCE_ATTR_ID, new ArrayList<>());
                AttributeValue shortReferenceAttribute = new AttributeValue(SHORT_REFERENCE_ATTR_ID, shortReferenceValue);
                shortReferenceAttribute.setStringValue(shortReferenceValue);
                attributeMap.get(SHORT_REFERENCE_ATTR_ID).add(shortReferenceAttribute);

                List<AttributeValue> attributesList = attributeMap.values().stream().flatMap(List::stream).collect(Collectors.toList());
                dataService.createCompleteCard(card, true, attributesList, false);

                archive3ServerConnector.logoff();
                pageParameters.remove("parentID");
                pageParameters.add("selectedGraduaterName", graduaterNameValue);
                setResponsePage(GraduatersPage.class, pageParameters);
            }
        };

        Form<?> cancelForm = new Form<Void>("cancelForm") {
            @Override
            public void onSubmit() {
                setResponsePage(GraduatersPage.class, pageParameters);
            }
        };

        add(titleForm);
        titleForm.add(educationLevelLabel);
        titleForm.add(selectedYearLabel);

        add(addGraduaterInfoForm);
        addGraduaterInfoForm.add(graduaterName);
        addGraduaterInfoForm.add(graduationWorkName);
        addGraduaterInfoForm.add(teacherName);
        addGraduaterInfoForm.add(altTeacherName);
        addGraduaterInfoForm.add(jobPlace);
        addGraduaterInfoForm.add(position);
        addGraduaterInfoForm.add(shortReference);

        add(cancelForm);

    }

}