package com;

import com.vaadin.server.FileDownloader;
import com.vaadin.server.FileResource;
import com.vaadin.ui.Button;

import java.io.File;

/**
 * Author: Ari Michael Ayvazyan
 * Date: 11.06.2014
 */
public class MyDownloadButton extends Button {
    public MyDownloadButton(String filename) {
        super("Download "+filename);
        FileResource res = new FileResource(new File(FileAssist.getUserDir("DMS") + File.separator + filename));
        FileDownloader fd = new FileDownloader(res);
        fd.extend(this);
    }
}
