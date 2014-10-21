import java.io.FileWriter;
import java.io.IOException;
import java.util.List;


public class TextWriter extends CommentWriter{

    private int count =0;
    
    @Override
    public void write(List<WebCommentUnit> list) {
        // TODO Auto-generated method stub
        FileWriter fileWriter;
        try {
            fileWriter = new
                    FileWriter(SimpleCommentParserFactory.filePath + "out_" +
                            SimpleCommentParserFactory.getCurrentFileName() + ".txt", true);
            for (int i = 0; i < list.size(); i++) {
                count++;
                WebCommentUnit commentUnit = list.get(i);
                fileWriter.write(commentUnit.getContent() + "\r\n");
                fileWriter.write(commentUnit.getCommentTags() + "\r\n");
            }
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    @Override
    public void close() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void open() {
        // TODO Auto-generated method stub
        
    }

}
