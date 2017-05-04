import java.util.List;

/**
 * Created by Admin on 5/4/2017.
 */
public class Page {

    private int pageNumber;
    private List<Block> blockList;

    public Page(int pageNumber, List<Block> blockList){
        this.pageNumber = pageNumber;
        this.blockList = blockList;
    }
    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public List<Block> getBlockList() {
        return blockList;
    }

    public void setBlockList(List<Block> blockList) {
        this.blockList = blockList;
    }
}
