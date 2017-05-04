import java.util.List;

/**
 * Created by Admin on 3/29/2017.
 */
public class TextToWordsExtractor {
    private static final String PARAGRAPH_SPLIT_REGEX = "\\r\\n|\\r|\\n";
    public String[] textToPara(String text){
        String[] paras = text.split(PARAGRAPH_SPLIT_REGEX);
        return  paras;
    }
}
