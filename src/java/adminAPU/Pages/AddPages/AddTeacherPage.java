package adminAPU.Pages.AddPages;

import adminAPU.BasePage;

import adminAPU.Pages.TeachersPage;
import com.alee.archive3.Archive3ServerConnector;
import com.alee.archive3.api.data.AttributeValue;
import com.alee.archive3.api.data.Card;
import com.alee.archive3.api.network.Requisite;
import com.alee.archive3.api.ws.DataService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public class AddTeacherPage extends BasePage {

    private static final String TYPE_OF_CARD_ATTR_ID = "ALSFR-41175b28-2f62-430c-8f35-a3f3f01a55a2";
    private static final String TEACHER_ID = "ALSFR-77f68663-6166-49e9-b949-be91a45f7be6";

    private static final String TEACHER_NAME_ATTR_ID = "ALSFR-40afdf23-e1fb-4e99-a59d-5bd5526172ca";
    private static final String POSITION_ATTR_ID = "ALSFR-ea86c66f-327c-4930-b226-b252406ac21b";
    private static final String ACADEMIC_DEGREE_ATTR_ID = "ALSFR-b74e7d7b-8bf6-45d1-90b9-2bfac5ea164b";
    private static final String EDUCATION_LEVEL_ATTR_ID = "ALSFR-259f88b7-5fd3-4f04-bba9-8081026a29d6";
    private static final String PHONE_ATTR_ID = "ALSFR-9adef902-62c5-4f10-a09b-6124247aae0e";
    private static final String EMAIL_ATTR_ID = "ALSFR-2c56c9c9-3ec0-448d-b19c-29671f150ae2";
    private static final String SHORT_DESCRIPTION_ATTR_ID = "ALSFR-75c837fd-6557-417d-9971-8fe8f7af13b8";

    private String teacherNameValue;
    private String positionValue;
    private String academicDegreeValue;
    private List<String> educationLevelsValues;
    private String phoneValue;
    private String emailValue;
    private String shortDescriptionValue;

    private static final String DOCTOR_OF_TECHNICAL_SCIENCES_ID = "ALSFR-b8ad7bbc-4708-4031-8b53-1fccb3927af1";
    private static final String CANDIDATE_OF_TECHNICAL_SCIENCES_ID = "ALSFR-88134f22-f9f8-422f-b4a9-95dbf7bc237f";

    private static final List<String> academicDegreesList = Arrays.asList("Доктор технических наук", "Кандидат технических наук", "Нет");

    private static final String BACCALAUREATE_ID = "ALSFR-9fc82a1a-fa4d-4ab0-a53e-3e5d538161dd";
    private static final String MAGISTRACY_ID = "ALSFR-1d6ba1bf-5472-488b-934b-98b81b82eb1a";

    private boolean baccalaureateCheckboxValue;
    private boolean magistracyCheckboxValue;

    private String selectedAcademicDegree = "Нет";

    private Card card;
    private String parentID;

    public AddTeacherPage(PageParameters pageParameters) {

        parentID = pageParameters.get("parentID").toString();

        final Archive3ServerConnector archive3ServerConnector = new Archive3ServerConnector("apu.su", 80);
        final Requisite requisite = new Requisite();
        requisite.instanceRichSettings();
        archive3ServerConnector.authenticate("admin", "ApU_36oe", requisite);

        final DataService dataService = archive3ServerConnector.getDataService();

        final TextField<String> teacherName = new TextField<String>("teacherName", new PropertyModel<String>(this, "teacherNameValue"));
        final TextField<String> position = new TextField<String>("position", new PropertyModel<String>(this, "positionValue"));
        final RadioChoice<String> academicDegrees = new RadioChoice<String>("academicDegrees", new PropertyModel<String>(this, "selectedAcademicDegree"), academicDegreesList);
        final CheckBox baccalaureateCheckbox = new CheckBox("baccalaureateCheckbox", new PropertyModel<>(this, "baccalaureateCheckboxValue"));
        final CheckBox magistracyCheckbox = new CheckBox("magistracyCheckbox", new PropertyModel<>(this, "magistracyCheckboxValue"));
        final TextField<String> phone = new TextField<String>("phone", new PropertyModel<String>(this, "phoneValue"));
        final TextField<String> email = new TextField<String>("email", new PropertyModel<String>(this, "emailValue"));
        final TextArea<String> shortDescription = new TextArea<String>("shortDescription", new PropertyModel<String>(this, "shortDescriptionValue"));

        Form<?> addTeacherInfoForm = new Form<Void>("addTeacherInfoForm") {
            @Override
            public void onSubmit() {
                card = new Card(teacherNameValue);
                card.setParentObject(parentID);
                Map<String, List<AttributeValue>> attributeMap = new HashMap<>();

                attributeMap.put(TYPE_OF_CARD_ATTR_ID, new ArrayList<AttributeValue>());
                AttributeValue typeOfCardAttribute = new AttributeValue(TYPE_OF_CARD_ATTR_ID, TEACHER_ID);
                typeOfCardAttribute.setStringValue("Преподаватель");
                attributeMap.get(TYPE_OF_CARD_ATTR_ID).add(typeOfCardAttribute);

                attributeMap.put(TEACHER_NAME_ATTR_ID, new ArrayList<AttributeValue>());
                AttributeValue teacherNameAttribute = new AttributeValue(TEACHER_NAME_ATTR_ID, teacherNameValue);
                teacherNameAttribute.setStringValue(teacherNameValue);
                attributeMap.get(TEACHER_NAME_ATTR_ID).add(teacherNameAttribute);

                attributeMap.put(POSITION_ATTR_ID, new ArrayList<AttributeValue>());
                AttributeValue positionAttribute = new AttributeValue(POSITION_ATTR_ID, positionValue);
                positionAttribute.setStringValue(positionValue);
                attributeMap.get(POSITION_ATTR_ID).add(positionAttribute);

                attributeMap.put(ACADEMIC_DEGREE_ATTR_ID, new ArrayList<AttributeValue>());
                if ("Доктор технических наук".equals(selectedAcademicDegree)) {
                    AttributeValue academicDegree = new AttributeValue(ACADEMIC_DEGREE_ATTR_ID, DOCTOR_OF_TECHNICAL_SCIENCES_ID);
                    academicDegree.setStringValue("Доктор технических наук");
                    attributeMap.get(ACADEMIC_DEGREE_ATTR_ID).add(academicDegree);
                }
                if ("Кандидат технических наук".equals(selectedAcademicDegree)) {
                    AttributeValue academicDegree = new AttributeValue(ACADEMIC_DEGREE_ATTR_ID, CANDIDATE_OF_TECHNICAL_SCIENCES_ID);
                    academicDegree.setStringValue("Кандидат технических наук");
                    attributeMap.get(ACADEMIC_DEGREE_ATTR_ID).add(academicDegree);
                }
                if ("Другое".equals(selectedAcademicDegree)) {
                    AttributeValue academicDegree = new AttributeValue(ACADEMIC_DEGREE_ATTR_ID, null);
                    academicDegree.setStringValue(null);
                    attributeMap.get(ACADEMIC_DEGREE_ATTR_ID).add(academicDegree);
                }

                attributeMap.put(EDUCATION_LEVEL_ATTR_ID, new ArrayList<AttributeValue>());
                if (baccalaureateCheckboxValue) {
                    AttributeValue baccalaureateAttribute = new AttributeValue(EDUCATION_LEVEL_ATTR_ID, BACCALAUREATE_ID);
                    baccalaureateAttribute.setStringValue("Бакалавриат");
                    attributeMap.get(EDUCATION_LEVEL_ATTR_ID).add(baccalaureateAttribute);
                }
                if (magistracyCheckboxValue) {
                    AttributeValue magistracyAttribute = new AttributeValue(EDUCATION_LEVEL_ATTR_ID, MAGISTRACY_ID);
                    magistracyAttribute.setStringValue("Магистратура");
                    attributeMap.get(EDUCATION_LEVEL_ATTR_ID).add(magistracyAttribute);
                }

                attributeMap.put(PHONE_ATTR_ID, new ArrayList<AttributeValue>());
                AttributeValue phoneAttribute = new AttributeValue(PHONE_ATTR_ID, phoneValue);
                phoneAttribute.setStringValue(phoneValue);
                attributeMap.get(PHONE_ATTR_ID).add(phoneAttribute);

                attributeMap.put(EMAIL_ATTR_ID, new ArrayList<AttributeValue>());
                AttributeValue emailAttribute = new AttributeValue(EMAIL_ATTR_ID, emailValue);
                emailAttribute.setStringValue(emailValue);
                attributeMap.get(EMAIL_ATTR_ID).add(emailAttribute);

                attributeMap.put(SHORT_DESCRIPTION_ATTR_ID, new ArrayList<AttributeValue>());
                AttributeValue shortDescriptionAttribute = new AttributeValue(SHORT_DESCRIPTION_ATTR_ID, shortDescriptionValue);
                shortDescriptionAttribute.setStringValue(shortDescriptionValue);
                attributeMap.get(SHORT_DESCRIPTION_ATTR_ID).add(shortDescriptionAttribute);

                List<AttributeValue> attributesList = attributeMap.values().stream().flatMap(List::stream).collect(Collectors.toList());
                dataService.createCompleteCard(card, true, attributesList, false);

                archive3ServerConnector.logoff();
                pageParameters.remove("parentID");
                pageParameters.add("selectedTeacherName", teacherNameValue);
                setResponsePage(TeachersPage.class, pageParameters);
            }
        };

        Form<?> cancelForm = new Form<Void>("cancelForm") {
            @Override
            protected void onSubmit() {
                setResponsePage(TeachersPage.class, pageParameters);
            }
        };

        add(addTeacherInfoForm);
        addTeacherInfoForm.add(teacherName);
        addTeacherInfoForm.add(position);
        addTeacherInfoForm.add(academicDegrees);
        addTeacherInfoForm.add(baccalaureateCheckbox);
        addTeacherInfoForm.add(magistracyCheckbox);
        addTeacherInfoForm.add(phone);
        addTeacherInfoForm.add(email);
        addTeacherInfoForm.add(shortDescription);

        add(cancelForm);

    }

}