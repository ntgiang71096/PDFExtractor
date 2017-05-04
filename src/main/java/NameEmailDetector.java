import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinder;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.tokenize.SimpleTokenizer;
import opennlp.tools.util.Span;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Admin on 4/27/2017.
 */
public class NameEmailDetector {

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static boolean validateEmail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(emailStr);
        return matcher.find();
    }

    public static void main(String[] args) throws IOException {
        String sentence = "From nguyentruonggiang@gmail.com: Hi. How are you, Mark L. DeFond?";

        SimpleTokenizer simpleTokenizer = SimpleTokenizer.INSTANCE;

        String tokens[] = simpleTokenizer.tokenize(sentence);

        // Email Validation
        System.out.println(validateEmail("abac@gmail.com"));

        for (String token: tokens){
                System.out.println(token);
        }
        InputStream inputStreamFinder = new FileInputStream("C:\\Users\\Admin\\Desktop\\Recent\\PDFBoxXML\\openNLPModels\\en-ner-person.bin");
        TokenNameFinderModel model = new TokenNameFinderModel(inputStreamFinder);

        NameFinderME nameFinderME = new NameFinderME(model);


        Span nameSpans[] = nameFinderME.find(tokens);

        for (Span s: nameSpans){
            System.out.println(s.toString());
        }


    }
}
