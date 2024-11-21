package ru;

import com.beust.jcommander.Parameter;

public class Arguments {
    @Parameter(names = "--mode")
    private String mode;

    @Parameter(names = "--count")
    private Integer count = 1;

    @Parameter(names = "--files")
    private String files;

    @Parameter(names = "--folder")
    private String folder;

    public String getMode() {
        return mode;
    }

    public Integer getCount() {
        return count;
    }

    public String getFiles() {
        return files;
    }

    public String getFolder() {
        return folder;
    }
}
