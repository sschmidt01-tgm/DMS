package com;

/**
 * Author: Ari Michael Ayvazyan
 * Date: 11.06.2014
 */

import java.io.*;
import com.vaadin.server.Page;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.ProgressListener;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.StartedEvent;
import com.vaadin.ui.Upload.StartedListener;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;
import com.vaadin.ui.VerticalLayout;

public class MyUploader extends CustomComponent implements Serializable{
    private static final long serialVersionUID = -4292553844521293140L;
    //Removed User Support - private String user;
    public MyUploader(/* Removed User Support -String user)*/){
        //Removed User Support - this.user=user;
        super();
    }
    public void init () {
        VerticalLayout layout = new VerticalLayout();
        basic(layout/*Removed User Support -,user*/);
        setCompositionRoot(layout);
    }

    void basic(VerticalLayout layout/*Removed User Support -, final String username*/) {
        // Show uploaded file in this placeholder
        //final Image image = new Image("Uploaded Image");
        //image.setVisible(false);
        // Implement both receiver that saves upload in a file and
        // listener for successful upload
        class ImageReceiver implements Receiver, SucceededListener {
            private static final long serialVersionUID = -1276759102490466761L;
            public File file;

            public OutputStream receiveUpload(String filename,
                                              String mimeType) {
                // Create upload stream
                FileOutputStream fos = null; // Stream to write to
                try {
                    // Open the file for writing.
                    file = new File(FileAssist.getUserDir("DMS")+File.separator+/*Removed User Support - username+File.separator+*/ filename);
                    file.getParentFile().mkdirs();
                    file.createNewFile();
                    fos = new FileOutputStream(file);
                } catch (final java.io.FileNotFoundException e) {
                    new Notification("No File selected/Could not open file: ",
                            e.getMessage(),
                            Notification.Type.ERROR_MESSAGE)
                            .show(Page.getCurrent());
                    return null;
                } catch (IOException e) {
                    new Notification("Could not create file: ",
                            e.getMessage(),
                            Notification.Type.ERROR_MESSAGE)
                            .show(Page.getCurrent());
                    return null;
                }
                return fos; // Return the output stream to write to
            }
            public void uploadSucceeded(SucceededEvent event) {
                new Notification("Upload Finished.",
                        "Yeah, I'm serious.",
                        Notification.Type.HUMANIZED_MESSAGE)
                        .show(Page.getCurrent());
                // Show the uploaded file in the image viewer
                //image.setVisible(true);
                //image.setSource(new FileResource(file));
            }
        };

        ImageReceiver receiver = new ImageReceiver();
        // Create the upload with a caption and set receiver later
        final Upload upload = new Upload("Upload it here", receiver);
        upload.setButtonCaption("Start Upload");
        upload.addSucceededListener(receiver);

        // Prevent too big Files
        final long UPLOAD_LIMIT = 1000000l;
        upload.addStartedListener(new StartedListener() {
            private static final long serialVersionUID = 4728847902678459488L;
            @Override
            public void uploadStarted(StartedEvent event) {
                if (event.getContentLength() > UPLOAD_LIMIT) {
                    Notification.show("Too big file",
                            Notification.Type.ERROR_MESSAGE);
                    upload.interruptUpload();
                }
            }
        });

        // Check the size also during progress
        upload.addProgressListener(new ProgressListener() {
            private static final long serialVersionUID = 8587352676703174995L;
            @Override
            public void updateProgress(long readBytes, long contentLength) {
                if (readBytes > UPLOAD_LIMIT) {
                    Notification.show("Too big file",
                            Notification.Type.ERROR_MESSAGE);
                    upload.interruptUpload();
                }
            }
        });

        // Put the components in a panel
        Panel panel = new Panel("Thunders cool File Storage");
        Layout panelContent = new VerticalLayout();
        panelContent.addComponent(upload);
        panel.setContent(panelContent);

        // Create uploads directory
        //File uploads = new File(FileAssist.getUserDir("DMS")+File.separator+username);
        //if (!uploads.exists() && !uploads.mkdir())
        //    layout.addComponent(new Label("ERROR: Could not create upload dir"));
        ((VerticalLayout) panel.getContent()).setSpacing(true);
        panel.setWidth("-1");
        layout.addComponent(panel);
    }
}