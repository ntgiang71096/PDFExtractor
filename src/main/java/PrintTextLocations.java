import java.io.*;

import org.apache.pdfbox.exceptions.CryptographyException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDStream;
import org.apache.pdfbox.util.PDFTextStripper;
import org.apache.pdfbox.util.TextPosition;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PrintTextLocations extends PDFTextStripper {
    static ArrayList<Character> charList;
    static List<Page> pageList;
    public static final String filePDF = "C:\\Users\\Admin\\Desktop\\Recent\\NCKH\\files\\pdf\\4059-37-7505-1-10-20170404.pdf";

    public PrintTextLocations() throws IOException {
        super.setSortByPosition(true);

    }
    public List<Page> getListOfPage(String filePath) throws  IOException{
        PDDocument document = null;
        try {
            File input = new File(filePath);

            document = PDDocument.load(input);

            //Instantiate PDFTextStripper class
            PDFTextStripper pdfStripper = new PDFTextStripper();

            // get Character and there position then try to construct it with its fontSizeInPt
            if (document.isEncrypted()) {
                document.decrypt("");
            }

            List allPages = document.getDocumentCatalog().getAllPages();


            // create List of page of pdf
            List<Page> pageList = new ArrayList<Page>();
            // allPages.size()
            for (int i = 0; i < allPages.size(); i++) {
                charList = new ArrayList<Character>();

                pdfStripper.setStartPage(i + 1);
                pdfStripper.setEndPage(i + 1);
                PDPage pdPage = (PDPage) allPages.get(i);


//                System.out.println("Processing page: " + i);
                PDStream contents = pdPage.getContents();

                if (contents != null) {
                    processStream(pdPage, pdPage.findResources(), pdPage.getContents().getStream());
                }

                // Create blocks of text
                List<Block> blockList = new PrintTextLocations().getListOfBlock();

                Page page = new Page(i + 1, blockList);
                pageList.add(page);

            }
            return pageList;


        } catch (CryptographyException e) {
            e.printStackTrace();
        } finally {
            if (document != null) {
                document.close();
            }
        }
        return null;
    }

    public void pdfToXml(String filePath) throws IOException, ParserConfigurationException, TransformerException {

        List<Page> pageList = getListOfPage(filePath);
        PrintTextLocations.pageList = pageList;
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document doc = documentBuilder.newDocument();

        // Create content
        // root element
        Element rootElement = doc.createElement("pdf");
        doc.appendChild(rootElement);
        int pageCount = 0;
        for (Page page : pageList){
            pageCount++;
            Element pageI = doc.createElement("document");
            Attr attrPage = doc.createAttribute("page");
            attrPage.setValue(String.valueOf(pageCount));

            for (Block block : page.getBlockList()){
                Element blockI = doc.createElement("block");
                Attr attrBlock = doc.createAttribute("font-size-in-pt");
                attrBlock.setValue(String.valueOf(block.getFontSizeInPt()));
                blockI.setAttributeNode(attrBlock);
                blockI.appendChild(doc.createTextNode(block.getContent()));

                pageI.appendChild(blockI);
            }
//            pageI.appendChild(doc.createTextNode(pdfTextStripper.getText(pages.get(i))));
            //System.out.println(pdfTextStripper.getText(pages.get(2)));
            pageI.setAttributeNode(attrPage);
            rootElement.appendChild(pageI);

        }
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult consoleResult =
                new StreamResult(System.out);

        StreamResult result = new StreamResult(new File("..\\files\\xml\\pdfToXml.xml"));
        transformer.transform(source,result);
    }

    public String getTitle(List<Block> blockList){
        // Return block with max font size in pt
        float maxFontSize = 0;
        String title = "";
        Block titleBlock = null;
        for (Block block : blockList){
            if (block.getFontSizeInPt() > maxFontSize){
                maxFontSize = block.getFontSizeInPt();
                title = block.getContent();
                titleBlock = block;
            }
        }
        titleBlock.setType("TITLE");
        return title;
    }

    public String getJournalInformation(List<Block> blockList){
        // set type JOURNAL for block
        blockList.get(0).setType("JOURNAL");
        return blockList.get(0).getContent();
    }

    public String getAuthors(List<Block> blockList){

        // 13pt == Author Font Size
        String authorString = "";
        for (Block block : blockList){
            if (block.getFontSizeInPt() == 13 || block.getFontSizeInPt() == 12){
                // set type AUTHORS
                block.setType("AUTHOR");
                authorString += block.getContent();
            }
        }
        // Trim special symbols
        authorString = authorString.replace("*","").replace("-","").replace("_","");
        return authorString;
    }

    public String getAbstractKeywordBlock(List<Block> blockList){
        for (Block block: blockList){
            if ((block.getContent().contains("Tóm tắt") && block.getContent().contains("Từ khóa"))
                    || (block.getContent().contains("Abstract") && (block.getContent().contains("Keywords")))){
                return block.getContent();
            }
        }
        return null;
    }

    public List<Block> getListOfBlock(){

        List<Block> blockList = new ArrayList<Block>();
        float currentFontSize = charList.get(0).getFontInPt();
        String st = "" + charList.get(0).getValue();
        charList.remove(0);
        for (Character chr : charList){
            if (chr.getFontInPt() != currentFontSize){
                Block block = new Block(st,currentFontSize);
                blockList.add(block);
                st = "" + chr.getValue();
                currentFontSize = chr.getFontInPt();
            } else {
                st += chr.getValue();
            }
        }
        Block lastBlock = new Block(st,currentFontSize);
        blockList.add(lastBlock);


        return blockList;
    }

    /**
     * @param text The text to be processed
     */
    @Override /* this is questionable, not sure if needed... */
    protected void processTextPosition(TextPosition text) {

        // Sometime pdfbox dectects special character as combination of other Unicode character
        // or recogize 2 - 3 consecutive characters as 1
        String stringDectected = text.getCharacter();

        float fontSizeInPt = text.getFontSizeInPt();
        for (int i = 0; i < stringDectected.length(); i ++){
            Character chr = new Character();
            chr.setValue(stringDectected.charAt(i));
            chr.setFontInPt(fontSizeInPt);
            charList.add(chr);
        }

//        System.out.println("String[" + text.getXDirAdj() + ","
//                + text.getYDirAdj() + " fs=" + text.getFontSize() + " xscale="
//                + text.getXScale() + " yscale=" + text.getYScale() + " height=" + text.getHeightDir() + " space="
//                + text.getWidthOfSpace() + " width="
//                + text.getWidthDirAdj() + "]" + text.getCharacter() + " fontPt " + text.getFontSizeInPt());
    }
}