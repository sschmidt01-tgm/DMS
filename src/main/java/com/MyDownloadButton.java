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
    /**
     * Creates a new vaadin DownloadButton, that downloads the given File with the filename.
     * @param filename declares the name of the file to download (If the file is not found, the file will still be downloaded but emty
     */
    public MyDownloadButton(String filename) {
        super("Download "+filename);
        FileResource res = new FileResource(new File(FileAssist.getUserDir("DMS") + File.separator + filename));
        FileDownloader fd = new FileDownloader(res);

        fd.extend(this);
    }
}
