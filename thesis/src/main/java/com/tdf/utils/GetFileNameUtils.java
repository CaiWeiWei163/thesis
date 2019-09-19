package com.tdf.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author cww
 * @create 2019-09-19 10:15
 */
public class GetFileNameUtils {
    public static String getFileNames(String path) {
        File file = new File(path);
        String[] fileNames = file.list();
        return Arrays.toString(fileNames);
    }
}



