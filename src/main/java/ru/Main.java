package ru;

import com.beust.jcommander.JCommander;

public class Main {
    public static void main(String[] args) {
        Arguments argv = new Arguments();

        JCommander.newBuilder()
                .addObject(argv)
                .build()
                .parse(args);

        Downloader downloader = new Downloader(argv.getFolder(), argv.getFiles().split(";"));
        if ("one-thread".equalsIgnoreCase(argv.getMode())) {
            downloader.downloadInSingleThread();
        } else if ("multi-thread".equalsIgnoreCase(argv.getMode())) {
            downloader.downloadInMultiThread(argv.getCount());
        } else {
            System.out.println("Invalid mode. Use one-thread or multi-thread.");
        }
    }
}
