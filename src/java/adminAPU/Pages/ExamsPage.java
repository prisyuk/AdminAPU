package adminAPU.Pages;

import adminAPU.BasePage;
import adminAPU.Pages.AddPages.AddExamPage;
import com.alee.archive3.Archive3ServerConnector;
import com.alee.archive3.api.data.AttributeValue;
import com.alee.archive3.api.data.Card;
import com.alee.archive3.api.data.CompleteCard;
import com.alee.archive3.api.exceptions.ArchiveSystemError;
import com.alee.archive3.api.network.Requisite;
import com.alee.archive3.api.ws.AttributeService;
import com.alee.archive3.api.ws.DataService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.ListChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public class ExamsPage extends BasePage {

    private static final String BACCALAUREATE_GRADUATERS_CARD_ID = "ALSFR-7686b5b7-1825-46c0-9583-4f349a4a7cbd";
    private static final String MAGISTRACY_GRADUATERS_CARD_ID = "ALSFR-18495abe-b76d-4a67-9455-38dbf671b257";

    private String educationLevel;

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

    private String newGroupValue;
    private Map<String, String> unsortedArchiveGroups;
    private List<String> groups;
    private String selectedGroup;
    private Map<String, String> unsortedArchiveExams;
    private List<String> examsNames;
    private String selectedExamName;

    private Card card;

    public ExamsPage(PageParameters pageParameters) {

        educationLevel = pageParameters.get("educationLevel").toString();
        selectedGroup = pageParameters.get("selectedGroup").toString();
        selectedExamName = pageParameters.get("selectedExamName").toString();
        final Label educationLevelLabel = new Label("educationLevelLabel", educationLevel);
        final TextField<String> newGroup = new TextField<>("newGroup", new PropertyModel<>(this, "newGroupValue"));

        final Archive3ServerConnector archive3ServerConnector = new Archive3ServerConnector("apu.su", 80);
        final Requisite requisite = new Requisite();
        requisite.instanceRichSettings();
        archive3ServerConnector.authenticate("admin", "ApU_36oe", requisite);

        final DataService dataService = archive3ServerConnector.getDataService();
        final AttributeService attributeService = archive3ServerConnector.getAttributeService();

        if (educationLevel.equals("Бакалавриат")) {
            unsortedArchiveGroups = dataService.getChildrenCards(BACCALAUREATE_GRADUATERS_CARD_ID);
        } else {
            unsortedArchiveGroups = dataService.getChildrenCards(MAGISTRACY_GRADUATERS_CARD_ID);
        }

        unsortedArchiveGroups = unsortedArchiveGroups.entrySet().stream().collect(Collectors.toMap(entry -> entry.getValue(), entry -> entry.getKey()));
        Map<String, String> archiveYears = unsortedArchiveGroups.entrySet().stream().sorted(Map.Entry.comparingByKey()).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));
        groups = new ArrayList<>(archiveYears.keySet());

        if (selectedGroup == null) {
            selectedGroup = groups.get(0);
            pageParameters.add("selectedGroup", selectedGroup);
        }
        final Label selectedGroupLabel = new Label("selectedGroupLabel", selectedGroup);

        ListChoice<String> groupsList = new ListChoice<>("groupsList", new PropertyModel<String>(this, "selectedGroup"), groups);
        groupsList.setMaxRows(4);

        unsortedArchiveExams = dataService.getChildrenCards(archiveYears.get(selectedGroup));

        unsortedArchiveExams = unsortedArchiveExams.entrySet().stream().collect(Collectors.toMap(entry -> entry.getValue(), entry -> entry.getKey()));
        Map<String, String> archiveExams = unsortedArchiveExams.entrySet().stream().sorted(Map.Entry.comparingByKey()).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));
        examsNames = new ArrayList<>(archiveExams.keySet());
        ListChoice<String> examsNamesList = new ListChoice<>("examsNamesList", new PropertyModel<String>(this, "selectedExamName"), examsNames);
        examsNamesList.setMaxRows(4);

        CompleteCard selectedExamCard = retrieveExamCard(dataService, archiveExams, pageParameters);

        List<AttributeValue> attributesList = selectedExamCard != null ? selectedExamCard.getAttributes() : new ArrayList<>();
        Map<String, List<AttributeValue>> attributeMap = new HashMap(attributesList.stream().collect(Collectors.groupingBy(AttributeValue::getAttributeId)));

        retrieveAttributesValues(attributeMap);

        final TextField<String> subjectName = new TextField<>("subjectName", new PropertyModel<>(this, "subjectNameValue"));
        final TextField<String> altSubjectName = new TextField<>("altSubjectName", new PropertyModel<>(this, "altSubjectNameValue"));
        final TextField<String> date = new TextField<>("date", new PropertyModel<>(this, "dateValue"));
        final TextField<String> time = new TextField<>("time", new PropertyModel<>(this, "timeValue"));
        final TextField<String> lectureHall = new TextField<>("lectureHall", new PropertyModel<>(this, "lectureHallValue"));
        final TextField<String> teacherName = new TextField<>("teacherName", new PropertyModel<>(this, "teacherNameValue"));
        final TextField<String> altTeacherName = new TextField<>("altTeacherName", new PropertyModel<>(this, "altTeacherNameValue"));

        Form<?> addNewGroupForm = new Form<Void>("addNewGroupForm") {
            @Override
            public void onSubmit() {
                if (!groups.contains(newGroupValue)) {
                    card = new Card(newGroupValue);
                    if (educationLevel.equals("Бакалавриат")) {
                        card.setParentObject(BACCALAUREATE_GRADUATERS_CARD_ID);
                    } else {
                        card.setParentObject(MAGISTRACY_GRADUATERS_CARD_ID);
                    }
                    dataService.createCompleteCard(card, true, new ArrayList<>(), false);
                    pageParameters.remove("selectedExamName");
                    pageParameters.remove("selectedGroup");
                    pageParameters.add("selectedGroup", newGroupValue);
                    archive3ServerConnector.logoff();
                    setResponsePage(ExamsPage.class, pageParameters);
                }
            }
        };

        Form<?> groupsForm = new Form<Void>("groupsForm") {
            @Override
            public void onSubmit() {
                pageParameters.remove("selectedExamName");
                pageParameters.remove("selectedGroup");
                pageParameters.add("selectedGroup", selectedGroup);
                archive3ServerConnector.logoff();
                setResponsePage(ExamsPage.class, pageParameters);
            }
        };

        Form<?> deleteGroupForm = new Form<Void>("deleteGroupForm") {
            @Override
            public void onSubmit() {
                dataService.deleteObjects(archiveYears.get(selectedGroup));
                pageParameters.remove("selectedExamName");
                pageParameters.remove("selectedGroup");
                archive3ServerConnector.logoff();
                setResponsePage(ExamsPage.class, pageParameters);
            }
        };

        Form<?> examsNamesForm = new Form<Void>("examsNamesForm") {
            @Override
            public void onSubmit() {
                pageParameters.remove("selectedExamName");
                pageParameters.add("selectedExamName", selectedExamName);
                archive3ServerConnector.logoff();
                setResponsePage(ExamsPage.class, pageParameters);
            }
        };

        Form<?> addExamForm = new Form<Void>("addExamForm") {
            @Override
            public void onSubmit() {
                pageParameters.remove("selectedExamName");
                pageParameters.add("parentID", archiveYears.get(selectedGroup));
                archive3ServerConnector.logoff();
                setResponsePage(AddExamPage.class, pageParameters);
            }
        };

        Form<?> selectedExamInfoForm = new Form<Void>("selectedExamInfoForm") {
            @Override
            public void onSubmit() {
                attributeMap.get(SUBJECT_NAME_ATTR_ID).get(0).setStringValue(subjectNameValue);
                attributeMap.get(ALT_SUBJECT_NAME_ATTR_ID).get(0).setValue(altSubjectNameValue);
                attributeMap.get(DATE_ATTR_ID).get(0).setStringValue(dateValue);
                attributeMap.get(TIME_ATTR_ID).get(0).setValue(timeValue);
                attributeMap.get(LECTURE_HALL_ATTR_ID).get(0).setValue(lectureHallValue);
                attributeMap.get(TEACHER_NAME_ATTR_ID).get(0).setStringValue(teacherNameValue);
                attributeMap.get(ALT_TEACHER_NAME_ATTR_ID).get(0).setValue(altTeacherNameValue);

                List<AttributeValue> updatedAttributesList = attributeMap.values().stream().flatMap(List::stream).collect(Collectors.toList());
                attributeService.setAttributeValuesForAnObject(selectedExamCard.getId(), updatedAttributesList);

                pageParameters.remove("selectedExamName");
                if (subjectNameValue != null) {
                    dataService.renameObject(selectedExamCard.getId(), subjectNameValue);
                    pageParameters.add("selectedExamName", subjectNameValue);
                } else {
                    dataService.renameObject(selectedExamCard.getId(), altSubjectNameValue);
                    pageParameters.add("selectedExamName", altSubjectNameValue);
                }
                archive3ServerConnector.logoff();
                setResponsePage(ExamsPage.class, pageParameters);
            }
        };

        Form<?> deleteExamForm = new Form<Void>("deleteExamForm") {
            @Override
            public void onSubmit() {
                dataService.deleteObjects(selectedExamCard.getId());
                pageParameters.remove("selectedExamName");
                archive3ServerConnector.logoff();
                setResponsePage(ExamsPage.class, pageParameters);
            }
        };

        add(addNewGroupForm);
        addNewGroupForm.add(educationLevelLabel);
        addNewGroupForm.add(selectedGroupLabel);
        addNewGroupForm.add(newGroup);

        add(groupsForm);
        groupsForm.add(groupsList);

        add(deleteGroupForm);

        add(examsNamesForm);
        examsNamesForm.add(examsNamesList);

        add(addExamForm);

        add(selectedExamInfoForm);
        selectedExamInfoForm.add(subjectName);
        selectedExamInfoForm.add(altSubjectName);
        selectedExamInfoForm.add(date);
        selectedExamInfoForm.add(time);
        selectedExamInfoForm.add(lectureHall);
        selectedExamInfoForm.add(teacherName);
        selectedExamInfoForm.add(altTeacherName);
        add(deleteExamForm);

    }

    private CompleteCard retrieveExamCard(final DataService dataService, final Map<String, String> archiveExams, PageParameters pageParameters) throws ArchiveSystemError {

        CompleteCard selectedExamCard;

        if (archiveExams.isEmpty()) {
            return null;
        }
        if (selectedExamName != null) {
            selectedExamCard = dataService.getCompleteCard(archiveExams.get(selectedExamName));
        } else {
            selectedExamName = examsNames.get(0);
            pageParameters.add("selectedExamName", selectedExamName);
            selectedExamCard = dataService.getCompleteCard(archiveExams.get(selectedExamName));
        }

        return selectedExamCard;

    }

    private void retrieveAttributesValues(Map<String, List<AttributeValue>> attributeMap) {

        subjectNameValue = attributeMap.get(SUBJECT_NAME_ATTR_ID) != null ? attributeMap.get(SUBJECT_NAME_ATTR_ID).get(0).getStringValue() : new String();
        altSubjectNameValue = attributeMap.get(ALT_SUBJECT_NAME_ATTR_ID) != null ? attributeMap.get(ALT_SUBJECT_NAME_ATTR_ID).get(0).getValue() : new String();
        dateValue = attributeMap.get(DATE_ATTR_ID) != null ? attributeMap.get(DATE_ATTR_ID).get(0).getStringValue() : new String();
        timeValue = attributeMap.get(TIME_ATTR_ID) != null ? attributeMap.get(TIME_ATTR_ID).get(0).getValue() : new String();
        lectureHallValue = attributeMap.get(LECTURE_HALL_ATTR_ID) != null ? attributeMap.get(LECTURE_HALL_ATTR_ID).get(0).getValue() : new String();
        teacherNameValue = attributeMap.get(TEACHER_NAME_ATTR_ID) != null ? attributeMap.get(TEACHER_NAME_ATTR_ID).get(0).getStringValue() : new String();
        altTeacherNameValue = attributeMap.get(ALT_TEACHER_NAME_ATTR_ID) != null ? attributeMap.get(ALT_TEACHER_NAME_ATTR_ID).get(0).getValue() : new String();

    }

}