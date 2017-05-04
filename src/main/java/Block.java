/**
 * Created by Admin on 4/19/2017.
 */
public class Block {

    private String content;
    private float fontSizeInPt;
    private String type;    // Block type can be: TITLE, AUTHOR, ABSTRACT, EMAIL, ...

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Block(String content, float fontSizeInPt) {
        this.content = content;
        this.fontSizeInPt = fontSizeInPt;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public float getFontSizeInPt() {
        return fontSizeInPt;
    }

    public void setFontSizeInPt(float fontSizeInPt) {
        this.fontSizeInPt = fontSizeInPt;
    }
}
