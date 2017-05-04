import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.List;

/**
 * Created by Admin on 5/4/2017.
 */
public class Application {

    static final String FILE_PATH = "C:\\Users\\Admin\\Desktop\\Recent\\NCKH\\files\\pdf\\4063-49-7531-1-10-20170404.pdf";

    // XML FILE IS IN "../files/xml/pdfToXml.xml"
    public static void main(String[] args) throws IOException, TransformerException, ParserConfigurationException {

        PrintTextLocations printer = new PrintTextLocations();
        printer.pdfToXml(FILE_PATH);
        List<Block> blockList = printer.pageList.get(0).getBlockList();

        // retrieve some metadata
        System.out.println("TITLE:");
        System.out.println(printer.getTitle(blockList));
        System.out.println("*********************************************");
        System.out.println("AUTHORS:");
        System.out.println(printer.getAuthors(blockList));
        System.out.println("*********************************************");
        System.out.println("JOURNAL INFORMATION:");
        System.out.println(printer.getJournalInformation(blockList));
        System.out.println("*********************************************");

    }
}
