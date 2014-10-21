import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Create Comment Parser by comments type
 * 
 * @author Fan.Yang.NB
 */

public class SimpleCommentParserFactory {

    public static final String SEPARATOR = "\\";
    public static final int TYPE_ANDAPP_PARSER = 0x00;
    public static final int TYPE_WEB_PARSER = 0x01;

    public static final int TYPE_TEXT_WRITER = 0X10;
    public static final int TYPE_EXCEL_WRITER = 0x11;

    public static final String filePath = "C:\\Users\\Yang Fan\\Desktop\\";
    public static final String[] TITLE_STRINGS = {
            "nickname", "userLevelName", "productColor", "userProvince", "title", "referenceName",
            "content", "creationTime", "commentTags", "score", "usefulVoteCount",
            "uselessVoteCount"
    };
    private static ArrayList<File> mFileList = new ArrayList<File>();
    private static File currentFile;
    private static String currentFileName;
    private static int commentType;

    public static void initFileList() {
        mFileList.clear();
        File f = null;
        f = new File(filePath);
        File[] files = f.listFiles();
        for (File file : files) {
            if (!file.isDirectory() && isDigitalFileName(file.getAbsolutePath())
                    && isTxtType(file.getAbsolutePath())) {
                mFileList.add(file);
            }
        }
    }

    public static CommentParser createParser(int type) {
        CommentParser parser = null;
        commentType = type;
        if (type == TYPE_ANDAPP_PARSER) {
            Log.d("Create andriod app writer...");
        } else if (type == TYPE_WEB_PARSER) {
            Log.d("Create web parser...");
            parser = new WebCommentParser();
        } else {
            Log.d("Create parser error...");
        }
        return parser;
    }

    public static CommentWriter createWriter(int type) {

        if (type == TYPE_EXCEL_WRITER) {
            Log.d("Create excel writer...");
            return new ExlWriter();
        } else if(type == TYPE_TEXT_WRITER) {
            Log.d("Create txt writer...");
            return new TextWriter();
        } else {
            Log.d("Create writer error...");
            return null;
        }
    }

    public static String getJSONString(String source) {
        if (source == null) {
            return null;
        }
        int index = source.indexOf("(");
        if (index >= 0) {
            return source.substring(index + 1, source.length() - 1);
        }
        return null;
    }

    public static boolean isDigitalFileName(String absolutePath) {
        if (absolutePath == null) {
            return false;
        }
        if (isNumber(getFileNameWithoutFileType(absolutePath))) {
            return true;
        }
        return false;
    }

    private static String getFileNameWithoutFileType(String absolutePath) {
        if (absolutePath == null) {
            return null;
        }
        int index1 = absolutePath.lastIndexOf(SEPARATOR);
        int index2 = absolutePath.lastIndexOf(".");
        if (index1 > 0 && index2 > index1) {
            return absolutePath.substring(index1 + 1, index2);
        }
        return null;
    }

    public static boolean isNumber(String str) {
        if (str == null) {
            return false;// return false without take this file
        }
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    private static boolean isTxtType(String fileName) {
        if (fileName == null) {
            return false;
        } else if (fileName.toLowerCase().endsWith(".txt")) {
            return true;
        }
        return false;
    }

    public static int getCurrentType() {
        return commentType;
    }

    public static List<File> getFileList() {
        return mFileList;
    }

    public static String getCurrentFileName() {
        return currentFileName;
    }

    public static File getCurrentFile() {
        return currentFile;
    }

    public static void setCurrentFile(File currentFile) {
        SimpleCommentParserFactory.currentFile = currentFile;
        currentFileName = getFileNameWithoutFileType(currentFile.getAbsolutePath());
        System.out.println("--currentFileName=" + currentFileName);
    }
}
