

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.common.PDStream;

//import org.apache.pdfbox.util.PDFTextStripper;
//import org.apache.pdfbox.util.Splitter;

import org.apache.pdfbox.util.PDFTextStripper;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Admin on 3/28/2017.
 */
public class PDFExtraction extends PDFTextStripper {
    public PDFExtraction() throws IOException {
        super.setSortByPosition(true);
    }

    public void PDFToXml(String pdfFilePath, String desFilePath){
        File file = new File(pdfFilePath);
        try {
            PDDocument document = PDDocument.load(file);

        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        File file = new File("C:\\Users\\Admin\\Desktop\\Recent\\NCKH\\files\\pdf\\1150-1-2242-1-10-20160523.pdf");
        try {
            // document - Doc of Pdf
            PDDocument document = PDDocument.load(file);

//            Splitter splitter = new Splitter();
//            List<PDDocument> pages = splitter.split(document);
            PDFTextStripper pdfTextStripper = new PDFTextStripper();
            List<PDPage> pdPages = document.getDocumentCatalog().getAllPages();

            int pageNumber = 1;
            List<PDDocument> pages = new ArrayList<PDDocument>();
            for (PDPage page : pdPages){
                PDDocument newDocument = new PDDocument();
                newDocument.addPage(page);
                pages.add(newDocument);
                pageNumber++;
            }







            int numberOfPages = pages.size();
            String text = pdfTextStripper.getText(pages.get(0));

            TextToWordsExtractor textToWordsExtractor = new TextToWordsExtractor();
            String[] paras = textToWordsExtractor.textToPara(text);
            for (String para : paras){
                System.out.println(para);
                System.out.println("**********************************");
            }
//            String[] paras = text.split( "(?<=(^|\\n))\\(\\d+\\)" );
//            for (String para : paras){
//                System.out.println(para);
//                System.out.println("**********************************");
//            }

//            String lines[] = pdfTextStripper.getText(pages.get(0)).split("\\r?\\n");
//            String words[] = lines[5].split(" ");
//            for (String ss : words){
//                System.out.println(ss);
//                System.out.println("*****************************************");
//            }
            //System.out.println(text);

            // Create XML from PDF
            try {
                DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
                Document doc = documentBuilder.newDocument();

                // Create content
                    // root element
                Element rootElement = doc.createElement("pdf");
                doc.appendChild(rootElement);

                for (int i = 0; i < pages.size(); i++){
                    System.out.println("PDF to XML, Page: " + i);
                    Element pageI = doc.createElement("document");
                    Attr attrPage = doc.createAttribute("page");
                    attrPage.setValue(String.valueOf(i + 1));

                    pageI.appendChild(doc.createTextNode(pdfTextStripper.getText(pages.get(i))));
                    //System.out.println(pdfTextStripper.getText(pages.get(2)));
                    pageI.setAttributeNode(attrPage);
                    rootElement.appendChild(pageI);
                }

                // Write content into xml file
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                DOMSource source = new DOMSource(doc);
                StreamResult consoleResult =
                        new StreamResult(System.out);

                StreamResult result = new StreamResult(new File("C:\\Users\\Admin\\Desktop\\Recent\\NCKH\\files\\xml\\test.xml"));
                transformer.transform(source,result);
            } catch (Exception e){
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
