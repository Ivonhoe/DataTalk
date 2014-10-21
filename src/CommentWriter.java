import java.util.List;

public abstract class CommentWriter {
    public abstract void open();
    
    public abstract void write(List<WebCommentUnit> list);

    public abstract void close();
}
