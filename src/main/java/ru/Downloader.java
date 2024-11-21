package ru;

import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.concurrent.ExecutorService;

public class Downloader {
    private final String folder;
    private final String[] urls;

    public Downloader(String folder, String[] urls) {
        this.folder = folder;
        this.urls = urls;
    }

    public void downloadInSingleThread() {
        for (String url : urls) {
            downloadFile(url);
        }
    }

    public void downloadInMultiThread(int threadCount) {
        ExecutorService pool = new ThreadPool(threadCount);
        for (String url : urls) {
            pool.execute(() -> downloadFile(url));
        }
        pool.shutdown();
    }

    private void downloadFile(String fileUrl) {
        try {
            URL url = new URL(fileUrl);
            String fileName = Paths.get(url.getPath()).getFileName().toString();
            File outputFile = new File(folder, fileName);

            try (InputStream in = url.openStream();
                 FileOutputStream out = new FileOutputStream(outputFile)) {

                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
            }
        } catch (IOException e) {
            System.err.println("Failed to download: " + fileUrl + " - " + e.getMessage());
        }
    }
}
