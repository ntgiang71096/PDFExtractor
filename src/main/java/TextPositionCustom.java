import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.apache.pdfbox.util.TextPosition;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by Admin on 3/30/2017.
 */
public class TextPositionCustom extends TextPosition {
//    public void addCharacterToList(TextPosition text, List<Character> characterList){
//        Character newChar = new Character();
//        newChar.setValue(text.getCharacter());
//        newChar.setFontInPt(text.getFontSize());
//        characterList.add(newChar);
//    }

    public static void main(String[] args) {
        File file = new File("C:\\Users\\Admin\\Desktop\\Recent\\PDFBoxXML\\files\\pdf\\cermineExplain.pdf");
        try {
            PDDocument document = PDDocument.load(file);
            PDFTextStripper pdfTextStripper = new PDFTextStripper();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
