package game2048;

import java.util.Formatter;
import java.util.Observable;


/** The state of a game of 2048.
 *  @author TODO: YOUR NAME HERE
 */
public class Model extends Observable {
    /** Current contents of the board. */
    private final Board _board;
    /** Current score. */
    private int _score;
    /** Maximum score so far.  Updated when game ends. */
    private int _maxScore;
    /** True iff game is ended. */
    private boolean _gameOver;

    /* Coordinate System: column C, row R of the board (where row 0,
     * column 0 is the lower-left corner of the board) will correspond
     * to board.tile(c, r).  Be careful! It works like (x, y) coordinates.
     */

    /** Largest piece value. */
    public static final int MAX_PIECE = 2048;

    /** A new 2048 game on a board of size SIZE with no pieces
     *  and score 0. */
    public Model(int size) {
        _board = new Board(size);
        _score = _maxScore = 0;
        _gameOver = false;
    }

    /** A new 2048 game where RAWVALUES contain the values of the tiles
     * (0 if null). VALUES is indexed by (row, col) with (0, 0) corresponding
     * to the bottom-left corner. Used for testing purposes. */
    public Model(int[][] rawValues, int score, int maxScore, boolean gameOver) {
        _board = new Board(rawValues);
        this._score = score;
        this._maxScore = maxScore;
        this._gameOver = gameOver;
    }

    /** Same as above, but gameOver is false. Used for testing purposes. */
    public Model(int[][] rawValues, int score, int maxScore) {
        this(rawValues, score, maxScore, false);
    }

    /** Return the current Tile at (COL, ROW), where 0 <= ROW < size(),
     *  0 <= COL < size(). Returns null if there is no tile there.
     *  Used for testing. Should be deprecated and removed.
     * */
    public Tile tile(int col, int row) {
        return _board.tile(col, row);
    }

    /** Return the number of squares on one side of the board.
     *  Used for testing. Should be deprecated and removed. */
    public int size() {
        return _board.size();
    }

    /** Return true if the game is over (there are no moves, or
     *  there is a tile with value 2048 on the board). */
    public boolean gameOver() {
        checkGameOver();
        if (_gameOver) {
            _maxScore = Math.max(_score, _maxScore);
        }
        return _gameOver;
    }

    /** Return the current score. */
    public int score() {
        return _score;
    }

    /** Return the current maximum game score (updated at end of game). */
    public int maxScore() {
        return _maxScore;
    }

    /** Clear the board to empty and reset the score. */
    public void clear() {
        _score = 0;
        _gameOver = false;
        _board.clear();
        setChanged();
    }

    /** Allow initial game board to announce a hot start to the GUI. */
    public void hotStartAnnounce() {
        setChanged();
    }

    /** Add TILE to the board. There must be no Tile currently at the
     *  same position. */
    public void addTile(Tile tile) {
        _board.addTile(tile);
        checkGameOver();
        setChanged();
    }

    /** Tilt the board toward SIDE.
     *
     * 1. If two Tile objects are adjacent in the direction of motion and have
     *    the same value, they are merged into one Tile of twice the original
     *    value and that new value is added to the score instance variable
     * 2. A tile that is the result of a merge will not merge again on that
     *    tilt. So each move, every tile will only ever be part of at most one
     *    merge (perhaps zero).
     * 3. When three adjacent tiles in the direction of motion have the same
     *    value, then the leading two tiles in the direction of motion merge,
     *    and the trailing tile does not.
     */
    public boolean tilt(Side side) {
        boolean changed=false;
        // TODO: Fill in this function.
        _board.setViewingPerspective(side);
        int size=_board.size();

        for(int c=0;c< size;c++){
            /** 为满足条件2，列内移动时不应有连续两次移动都是合并 */
            boolean lastMoveIsMerge=false;
            for(int r= size-2;r>=0;r--){
                Tile t=_board.tile(c,r);
                if(t!=null){
                    // 假如最上格是null，意味着前面全是空格，直接挪到最上面
                    if(_board.tile(c,size-1)==null){
                        lastMoveIsMerge=_board.move(c,size-1,t);
                        changed=true;
                        // 跳过本次循环下面部分，直接看下一个格子
                        continue;
                    }
                    // 找出t上面与t最近的非null格的行号
                    int neighborRow=size-1; // 赋值仅为了不报错
                    for(int i=size-2;i>r;i--){
                        if(_board.tile(c,i)==null){
                            neighborRow=i+1;
                            break;
                        }
                        // 能运行到这里说明上面无空格
                        if(i==r+1){
                            neighborRow=r+1;
                        }
                    }
                    Tile neighborTile=_board.tile(c,neighborRow);
                    if(neighborTile==null){
                        // 此条件方便debug
                        System.out.println("Weird! Neighbor tile should not be null!");
                    }else if(neighborTile.value()==t.value()&&!lastMoveIsMerge){
                        // 当相邻格与本格相等时，如果上次移动不伴随合并，则移动到相邻格（合并）
                       lastMoveIsMerge= _board.move(c,neighborRow,t);
                       _score+=t.value()*2;
                       changed=true;
                    }else if(neighborRow>r+1){
                        // 否则应移动到相邻格的下一格（括号内条件避免移动到原位置）
                        lastMoveIsMerge=_board.move(c,neighborRow-1,t);
                    }

                }
            }
        }

        _board.setViewingPerspective(Side.NORTH);

        checkGameOver();
        if(changed){
            setChanged();
        }
        return changed;
    }

    /** Checks if the game is over and sets the gameOver variable
     *  appropriately.
     */
    private void checkGameOver() {
        _gameOver = checkGameOver(_board);
    }

    /** Determine whether game is over. */
    private static boolean checkGameOver(Board b) {
        return maxTileExists(b) || !atLeastOneMoveExists(b);
    }

    /** Returns true if at least one space on the Board is empty.
     *  Empty spaces are stored as null.
     */
    public static boolean emptySpaceExists(Board b) {
        // TODO: Fill in this function.

        // 应该循环每行每列的格子，看其值是不是null
        // b.tile(0,0) 返回左下角格子的值
        // b.size() 返回 4 代表4行4列
//        System.out.println(b.tile(0,0).value());
//        System.out.println(b.size());

        int size=b.size();
        for(int i=0;i<size;i++){
            for(int j=0;j<size;j++){
                if(b.tile(i,j)==null) return true;
            }
        }

        return false;
    }

    /**
     * Returns true if any tile is equal to the maximum valid value.
     * Maximum valid value is given by this.MAX_PIECE. Note that
     * given a Tile object t, we get its value with t.value().
     */
    public static boolean maxTileExists(Board b) {
        // TODO: Fill in this function.
        int size=b.size();
        for(int col=0;col<size;col++){
            for(int row=0;row<size;row++){
                if(b.tile(col,row)!=null&&b.tile(col,row).value()==MAX_PIECE)return true;
            }
        }
        return false;
    }

    /**
     * Returns true if there are any valid moves on the board.
     * There are two ways that there can be valid moves:
     * 1. There is at least one empty space on the board.
     * 2. There are two adjacent tiles with the same value.
     */
    public static boolean atLeastOneMoveExists(Board b) {
        // TODO: Fill in this function.
        if(emptySpaceExists(b)) return true;
        // 判断是否存在相邻等值
        int size=b.size();
        // 循环方格，左下角到倒数第二行倒数第二列，
        // 每个方格检查上方和右方是否相同
        for(int col=0;col<size-1;col++){
            for(int row=0;row<size-1;row++){
                if(b.tile(col,row).value()==b.tile(col,row+1).value()||b.tile(col,row).value()==b.tile(col+1,row).value()){
                    return true;
                }
            }
        }
        // 最上排检查左右相邻，最右列检查上下相邻
        for(int col=0;col<size-1;col++){
            if(b.tile(col,size-1).value()==b.tile(col+1,size-1).value()){
                return true;
            }
        }
        for(int row=0;row<size-1;row++){
            if(b.tile(size-1,row).value()==b.tile(size-1,row+1).value()){
                return true;
            }
        }

        return false;
    }


    /** Returns the model as a string, used for debugging. */
    @Override
    public String toString() {
        Formatter out = new Formatter();
        out.format("%n[%n");
        for (int row = size() - 1; row >= 0; row -= 1) {
            for (int col = 0; col < size(); col += 1) {
                if (tile(col, row) == null) {
                    out.format("|    ");
                } else {
                    out.format("|%4d", tile(col, row).value());
                }
            }
            out.format("|%n");
        }
        String over = gameOver() ? "over" : "not over";
        out.format("] %d (max: %d) (game is %s) %n", score(), maxScore(), over);
        return out.toString();
    }

    /** Returns whether two models are equal. */
    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        } else if (getClass() != o.getClass()) {
            return false;
        } else {
            return toString().equals(o.toString());
        }
    }

    /** Returns hash code of Model’s string. */
    @Override
    public int hashCode() {
        return toString().hashCode();
    }
}
