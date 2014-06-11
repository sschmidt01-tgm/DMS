package com;

import com.vaadin.server.Page;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;

import java.io.File;

/**
 * Author: Ari Michael Ayvazyan
 * Date: 11.06.2014
 */
public class MyFileDeleteButton extends Button {
    /**
     *
     * @param filename creates a button that deletes the file with the given filename
     */
    public MyFileDeleteButton(final String filename) {
        super("X");
        //Delete the file on button push
        this.addClickListener(new Button.ClickListener() {
            public void buttonClick(ClickEvent event) {
                //if(new File(FileAssist.getUserDir("DMS") + File.separator + filename+".lck").exists())return; //return if file is currently locked
                if(!new File(FileAssist.getUserDir("DMS") + File.separator + filename).delete()){
                    new Notification("Could not delete File",
                            "Oops!",
                            Notification.Type.ERROR_MESSAGE)
                            .show(Page.getCurrent());
                    return;

                }
                new Notification("File deleted.",
                        "Woah!",
                        Notification.Type.HUMANIZED_MESSAGE)
                        .show(Page.getCurrent());
            }
        });
    }
}
