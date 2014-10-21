
public abstract class CommentParser {
    public abstract void onStart(CommentWriter writer);
    public abstract void onParser(String line);
    public abstract void onSave();
    public abstract void onEnd();
}
