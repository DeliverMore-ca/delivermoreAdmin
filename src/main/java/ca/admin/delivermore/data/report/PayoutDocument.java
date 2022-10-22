package ca.admin.delivermore.data.report;

import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.server.StreamResource;
import org.apache.commons.io.FileUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

public class PayoutDocument {
    private File file = null;
    private String name = "";
    private Anchor anchor = new Anchor();

    private StreamResource resource;

    private String emailAddress = "";

    public PayoutDocument(String name, File file, String emailAddress) {
        this.file = file;
        this.name = name;
        this.emailAddress = emailAddress;
        //create the anchor
        resource = getStreamResource(file.getName(),file);
        //anchor.setHref(getStreamResource(file.getName(),file));
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getEmailPresentation() {
        if (emailAddress.isEmpty()){
            return "None";
        }
        return emailAddress;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Anchor getAnchor() {
        return anchor;
    }

    public StreamResource getResource() {
        System.out.println("getResource: returning resource name:" + resource.getName() + " resource:" + resource.toString());
        return resource;
    }

    private StreamResource getStreamResource(String filename, File content) {
        return new StreamResource(filename,
                () -> {
                    try {
                        return new ByteArrayInputStream(FileUtils.readFileToByteArray(content));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
    }
}
