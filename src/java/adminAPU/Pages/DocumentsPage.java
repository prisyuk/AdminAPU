package adminAPU.Pages;

import adminAPU.BasePage;
import com.alee.archive3.Archive3ServerConnector;
import com.alee.archive3.api.data.ArchiveDocument;
import com.alee.archive3.api.data.ArchiveObject;
import com.alee.archive3.api.data.CompleteCard;
import com.alee.archive3.api.network.Requisite;
import com.alee.archive3.api.ws.DataService;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.ListChoice;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public class DocumentsPage extends BasePage {

    private static final String BACCALAUREATE_DOCUMENTS_CARD_ID = "ALSFR-f72aacbc-15c5-4de1-83dd-377c055b8a5e";
    private static final String MAGISTRACY_DOCUMENTS_CARD_ID = "ALSFR-6b2ad49a-4f34-484e-8963-952f385ac36d";

    private String educationLevel;
    private CompleteCard documentsCard;
    private List<String> documentsNames;
    private String selectedDocumentName;

    private FileUploadField fileUploadField;

    public DocumentsPage(PageParameters pageParameters) {

        educationLevel = pageParameters.get("educationLevel").toString();
        final Label educationLevelLabel = new Label("educationLevelLabel", educationLevel);

        final Archive3ServerConnector archive3ServerConnector = new Archive3ServerConnector("apu.su", 80);
        final Requisite requisite = new Requisite();
        requisite.instanceRichSettings();
        archive3ServerConnector.authenticate("admin", "ApU_36oe", requisite);

        final DataService dataService = archive3ServerConnector.getDataService();

        if (educationLevel.equals("Бакалавриат")) {
            documentsCard = dataService.getCompleteCard(BACCALAUREATE_DOCUMENTS_CARD_ID, true);
        }
        if (educationLevel.equals("Магистратура")) {
            documentsCard = dataService.getCompleteCard(MAGISTRACY_DOCUMENTS_CARD_ID, true);
        }

        List<ArchiveDocument> archiveDocuments = documentsCard.getDocuments();
        Collections.sort(archiveDocuments, new Comparator<ArchiveObject>() {
            public int compare(ArchiveObject ao1, ArchiveObject ao2) {
                return ao1.getName().compareTo(ao2.getName());
            }
        });

        documentsNames = archiveDocuments.stream().map(searchResult -> searchResult.getName()).collect(Collectors.toList());
        selectedDocumentName = documentsNames.get(0);
        ListChoice<String> documentsNamesList = new ListChoice<>("documentsNamesList", new PropertyModel<String>(this, "selectedDocumentName"), documentsNames);
        documentsNamesList.setMaxRows(10);

        Form<?> uploadDocument = new Form<Void>("uploadDocument") {
            @Override
            protected void onSubmit() {
//                final FileUpload fileUpload = fileUploadField.getFileUpload();
////                if (fileUpload != null) {
////
////                }
            }
        };

        Form<?> documentsForm = new Form<Void>("documentsForm") {};

        Form<?> deleteDocument = new Form<Void>("deleteDocument") {
            @Override
            protected void onSubmit() {
                final ArchiveObject selectedDocumentArchiveObject = archiveDocuments.stream().filter(searchResult -> searchResult.getName().equals(selectedDocumentName)).findFirst().orElse(null);
                dataService.deleteObjects(selectedDocumentArchiveObject.getObjectId());
                setResponsePage(DocumentsPage.class, pageParameters);
            }
        };

        add(uploadDocument);
        uploadDocument.add(educationLevelLabel);
        uploadDocument.add(fileUploadField = new FileUploadField("fileUploadField"));

        add(documentsForm);
        documentsForm.add(documentsNamesList);

        add(deleteDocument);

    }

}