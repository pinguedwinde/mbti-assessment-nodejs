package fr.lunatech.mbtiassessment.service.util;

import static fr.lunatech.mbtiassessment.router.RoutesConstants.USER_BASE_URL;

public class FileConstants {
    public static final String USER_IMAGE_PATH = USER_BASE_URL + "/image/";
    public static final String JPEG_EXTENSION = "jpg";
    public static final String IMAGE_TYPE = "image";
    public static final String USER_FOLDER = System.getProperty("user.home") + "/mbti_assessment/users/";
    public static final String DIRECTORY_CREATED = "Created directory : ";
    public static final String FILE_SAVED_IN_FILE_SYSTEM = "Saved file in file system by name : ";
    public static final String TEMP_IMAGE_BASE_URL = "https://robohash.org/";
    public static final String DEFAULT_USER_IMAGE_PATH = USER_BASE_URL + "/profile-image/";
    public static final String NOT_AN_IMAGE_FILE = " is not an image file. Please upload an image file";
    public static final String IMAGE_FILE_NOT_FOUND = "Image file not found. Please provide an image filename";
}
