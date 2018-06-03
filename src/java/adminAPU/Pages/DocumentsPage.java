package adminAPU.Pages;

import adminAPU.BasePage;
import com.alee.archive3.Archive3ServerConnector;
import com.alee.archive3.api.data.ArchiveDocument;
import com.alee.archive3.api.data.ArchiveObject;
import com.alee.archive3.api.data.CompleteCard;
import com.alee.archive3.api.filetransfer.ArchiveFileUploader;
import com.alee.archive3.api.filetransfer.FileUploadListener;
import com.alee.archive3.api.network.Requisite;
import com.alee.archive3.api.ws.DataService;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.ListChoice;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.http.WebResponse;
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
        } else {
            documentsCard = dataService.getCompleteCard(MAGISTRACY_DOCUMENTS_CARD_ID, true);
        }

        List<ArchiveDocument> archiveDocuments = documentsCard.getDocuments();
        Collections.sort(archiveDocuments, (ArchiveObject ao1, ArchiveObject ao2) -> ao1.getName().compareTo(ao2.getName()));

        documentsNames = archiveDocuments.stream().map(searchResult -> searchResult.getName()).collect(Collectors.toList());
        if (!documentsNames.isEmpty()) {
        selectedDocumentName = documentsNames.get(0);
        }
        ListChoice<String> documentsNamesList = new ListChoice<>("documentsNamesList", new PropertyModel<String>(this, "selectedDocumentName"), documentsNames);
        documentsNamesList.setMaxRows(10);

        Form<?> uploadDocument = new Form<Void>("uploadDocument") {
            @Override
            protected void onSubmit() {
                final FileUpload fileUpload = fileUploadField.getFileUpload();
                if (fileUpload != null) {
                    File file = new File(fileUpload.getClientFileName());

                    if (file.exists()) {
                        file.delete();
                    }

                    try {
                        file.createNewFile();
                        fileUpload.writeTo(file);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }

                    final List<ArchiveDocument> documents = new ArrayList<>();
                    ArchiveDocument document = new ArchiveDocument();
                    document.setName(fileUpload.getClientFileName());
                    document.setParentObject(documentsCard.getId());
                    document.setDocumentURL(file.getName());
                    document.setDocumentSize(file.length());
                    document = (ArchiveDocument) archive3ServerConnector.getDataService().createArchiveObject(document);
                    documents.add(document);

                    final String sessionID = archive3ServerConnector.getDocumentService().openSession(documents);
                    final ArchiveFileUploader uploader = new ArchiveFileUploader(archive3ServerConnector);

                    try {
                        uploader.uploadFile(new File(document.getDocumentURL()), document, sessionID, new FileUploadListener() {});
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    } finally {
                        file.delete();
                    }

                    archive3ServerConnector.logoff();
                    setResponsePage(DocumentsPage.class, pageParameters);
                }
            }
        };

        Form<?> documentsForm = new Form<Void>("documentsForm") {
            @Override
            protected void onSubmit() {
                try {
                    final ArchiveDocument selectedDocumentArchiveObject = archiveDocuments.stream().filter(searchResult -> searchResult.getName().equals(selectedDocumentName)).findFirst().orElse(null);
                    URI url = archive3ServerConnector.getDocumentDownloadUrl(selectedDocumentArchiveObject, null, selectedDocumentArchiveObject.getMimeType(), false);
                    final File downloadedFile = downloadFile(archive3ServerConnector, url, selectedDocumentArchiveObject.getName());
                    WebResponse response = (WebResponse) RequestCycle.get().getResponse();

                    response.setContentType(makeMimeType(selectedDocumentArchiveObject));
                    response.addHeader("Content-Disposition", "attachment; filename=" + downloadedFile.getName());
                    response.setContentLength((int) downloadedFile.length());

                    final FileInputStream fileInputStream = new FileInputStream(downloadedFile);
                    OutputStream responseOutputStream = response.getOutputStream();
                    int bytes;
                    while ((bytes = fileInputStream.read()) != -1) {
                        responseOutputStream.write(bytes);
                    }
                    downloadedFile.delete();
                } catch (IOException | URISyntaxException e) {
                    Logger.getLogger(DocumentsPage.class.getName()).log(Level.SEVERE, null, e);
                }
            }

            private File downloadFile(final Archive3ServerConnector archive3ServerConnector, final URI url, final String documentName) throws IOException {
                try (final CloseableHttpClient client = archive3ServerConnector.makeHttpClient()) {
                    final CloseableHttpResponse response = client.execute(new HttpGet(url));
                    final InputStream inputStream = response.getEntity().getContent();
                    final File tempFile;
                    if (educationLevel.equals("Бакалавриат")) {
                        tempFile = new File("C:\\AdminAPU Temp Files\\Бакалавриат\\" + documentName);
                    } else {
                        tempFile = new File("C:\\AdminAPU Temp Files\\Магистратура\\" + documentName);
                    }
                    tempFile.getParentFile().mkdirs();

                    try {
                        final OutputStream outputStream = new FileOutputStream(tempFile);
                        final byte[] buffer = new byte[1024];
                        int length;
                        while ((length = inputStream.read(buffer)) > 0) {
                            outputStream.write(buffer, 0, length);
                        }
                        outputStream.close();
                        inputStream.close();
                    } catch (final Exception e) {
                        Logger.getLogger(DocumentsPage.class.getName()).log(Level.SEVERE, null, e);
                    }

                    return tempFile;

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            private String makeMimeType(final ArchiveDocument selectedDocumentArchiveObject) {
                if (selectedDocumentArchiveObject.getMimeType() != null) {
                    return "application/" + selectedDocumentArchiveObject.getMimeType();
                } else if (selectedDocumentArchiveObject.getName().contains(".doc")) {
                    return "application/msword";
                } else if (selectedDocumentArchiveObject.getName().contains(".pdf")) {
                    return "application/pdf";
                } else if (selectedDocumentArchiveObject.getName().contains(".jpg") || selectedDocumentArchiveObject.getName().contains(".jpeg")) {
                    return "image/jpeg";
                } else if (selectedDocumentArchiveObject.getName().contains(".png")) {
                    return "image/png";
                }
                return null;
            }
        };

        Form<?> deleteDocument = new Form<Void>("deleteDocument") {
            @Override
            protected void onSubmit() {
                final ArchiveObject selectedDocumentArchiveObject = archiveDocuments.stream().filter(searchResult -> searchResult.getName().equals(selectedDocumentName)).findFirst().orElse(null);
                dataService.deleteObjects(selectedDocumentArchiveObject.getObjectId());

                archive3ServerConnector.logoff();
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