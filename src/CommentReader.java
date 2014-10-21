import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Fan.Yang.NB
 */

public class CommentReader {
    private static CommentParser mParser = null;
    private static CommentWriter mWriter = null;
    
    private static List<File> mFilesList = new ArrayList<File>();

    private static void init(){
        SimpleCommentParserFactory.initFileList();
        mFilesList = SimpleCommentParserFactory.getFileList();
        Log.d("Files count:"+mFilesList.size());
        
        mWriter = SimpleCommentParserFactory.createWriter(SimpleCommentParserFactory.TYPE_EXCEL_WRITER);
    }
    
    private static void doMain(){
        try {
            for (File file : mFilesList) {
                SimpleCommentParserFactory.setCurrentFile(file);
                BufferedInputStream fis = new BufferedInputStream(new
                        FileInputStream(file));
                BufferedReader reader = new BufferedReader(new InputStreamReader(fis,
                        "gbk"), 5 * 1024 * 1024);

                String line = "";
                mWriter.open();
                while ((line = reader.readLine()) != null) {
                    // TODO: write your business
                    if (line.startsWith("jsonp")) {
                        //Create different parser according to different data
                        if(mParser == null||!(mParser instanceof WebCommentParser)){
                            mParser = SimpleCommentParserFactory
                                    .createParser(SimpleCommentParserFactory.TYPE_WEB_PARSER);
                            mParser.onStart(mWriter);
                        }
                    } else if(line.startsWith("{\"code\":")){
                        continue;
                    } else{
                        continue;
                    }
                    mParser.onParser(line);
                    mParser.onSave();
                }
                mParser.onEnd();
                mParser = null;
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        //LTP requirement
        //LTPToolKit.analyzeHttpResponse();
        
        System.out.println("***********START***********");
        init();
        doMain();
        System.out.println("***********END***********");
    }
}
