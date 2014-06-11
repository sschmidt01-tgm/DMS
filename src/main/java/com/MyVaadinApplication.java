package com;

import com.vaadin.server.*;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.*;

import java.io.File;

/**
 * Author: Ari Michael Ayvazyan
 * Date: 11.06.2014
 */
public class MyVaadinApplication extends UI {
    @Override
    public void init(VaadinRequest request) {
        VerticalLayout layout = new VerticalLayout();
        setContent(layout);
        MyUploader myup=new MyUploader();
        myup.init();
        layout.addComponent(myup);

        String[] listOfFiles= FileAssist.getFileNames();
        for(int i=0;i<listOfFiles.length;i++){
            Button btn = new MyDownloadButton(listOfFiles[i]);
            layout.addComponent(btn);
            Button btn2 = new MyFileDeleteButton(listOfFiles[i]);
            layout.addComponent(btn2);
        }

    }
}
