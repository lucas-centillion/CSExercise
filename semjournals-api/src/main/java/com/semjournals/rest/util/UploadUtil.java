package com.semjournals.rest.util;

import java.io.*;

public class UploadUtil {
    public void writeToFile(InputStream uploadedInputStream, String uploadedFileLocation) throws IOException {
        int read;
        byte[] bytes = new byte[1024];

        OutputStream out = new FileOutputStream(new File(uploadedFileLocation));
        while ((read = uploadedInputStream.read(bytes)) != -1) {
            out.write(bytes, 0, read);
        }
        out.flush();
        out.close();
    }
}
