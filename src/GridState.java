
import java.util.ArrayList;

/**
 * GridState class
 * represent a State in Reversy game.
 */
public class GridState {
    private ArrayList<String> map;
    private Integer realScore;
    private final char EMPTY_MARK = 'E';
    private char turn;

    /**
     * Constructor A - found the turn with private method
     * @param inputMap list of char that represent the map
     */
    public GridState(ArrayList<String> inputMap) {
        this.realScore = null;
        this.map = inputMap;
        this.turn = findTurn();
    }

    /**
     * Constructor B - the turn given
     * @param map list of char that represent the map
     * @param myColor player mark that should play in this turn
     */
    public GridState(ArrayList<String> map, char myColor) {
        this.map = map;
        this.realScore = null;
        this.turn = myColor;
    }

    /**
     * return cached Heuristic Number
     * @return Heuristic Number
     */
    public Integer getHeuristicNumber() {
        if(null != realScore){
           return realScore;
        }
        return this.calHeuristicNumber();
    }

    /**
     * calculate who turn it is, assuming that the black started at the beginning.
     * @return player mark that need to play now
     */
    private char findTurn(){
        int unempty = 0;
        for (String i: this.map) {
            for (int j =0; j< i.length() ; ++j){
                switch (i.charAt(j)) {
                    case ('B'):
                    case ('W'):
                        ++unempty;
                        break;
                    default:
                        break;
                }
            }
        }
        if (unempty % 2 == 0){
            return 'B';
        }
        return 'W';
    }

    /**
     * return the block mark ib this point.
     * need to be followed after inRange() check!!!
     * print the exception if it is out range - and exit 'r'...
     * @param p point in the map
     * @return block mark
     */
    public char getColor(Point p) {
        try{
            return this.map.get(p.getY()).charAt(p.getX());
        } catch (StringIndexOutOfBoundsException e){
            System.out.print( p.toString());
            System.out.print(e.getMessage());
            System.exit(1);
            // false return - never returns...
            return 'r';
        }
    }

    /**
     * turn getter
     * @return player mark that need to play now
     */
    public char getTurn() {
        return turn;
    }

    /**
     * check if the point is in the map.
     * @param p point to check
     * @return Boolean
     */
    private Boolean inRange(Point p){
        return ((p.getX() < map.size()) && (p.getY() < map.size()) && (p.getY() >= 0) && (p.getX() >= 0));
    }

    /**
     * check if this point in the map have coin of the rival.
     * @param myColor player color
     * @param point point in the map
     * @return Boolean
     */
    private Boolean isOppositeColor(char myColor, Point point) {
        if(!inRange(point)) return false;
        return ((myColor == 'B') && (this.getColor(point) == 'W')) || ((myColor == 'W') && (this.getColor(point) == 'B'));
    }

    /**
     * return the oppositeColor
     * @param mycolor player color
     * @return rival color
     */
    private char oppositeColor(char mycolor){
        switch (mycolor) {
            case 'B':
                return 'W';
            case 'W':
                return 'B';
            default:
                return 'E';
        }
    }

    /**
     * change the coin in the given point
     * @param p point in the map
     * @param color color of the coin to put in the point
     */
    private void setColor(Point p, char color){

            StringBuilder temp = new StringBuilder(this.map.remove(p.getY()));
            temp.setCharAt(p.getX(), color);
            this.map.add(p.getY(), temp.toString());
    }

    /**
     * check if player can put coin in start point considering the given direction only.
     * if yes return true. if flipFlag is true flip the coins that this move flips.
     * @param player_color color of the current player
     * @param start point to put the coin on the map
     * @param x_step x direction from start point
     * @param y_step y direction from start point
     * @param flipFlag flag: flip if can?
     * @return if the move is valid
     */
    private Boolean check_direction(char player_color, Point start, int x_step, int y_step, Boolean flipFlag){
        int x = start.getX(),y = start.getY();
        Point place = new Point(x,y);
        if(this.inRange(place)){
            place = new Point(place.getX() + x_step, place.getY() + y_step);
            // check if the first coin is the rivel's coin
            if (!this.inRange(place) || this.getColor(place) == EMPTY_MARK){
                return false;
                }
            else {
                // coin is next to other coin - so put just the coin in start place.
                this.setColor(start, player_color);
                // try to flip
                while((this.inRange(place) && this.isOppositeColor(player_color, place))) {
                    place = new Point(place.getX() + x_step, place.getY() + y_step);
                }
                // check if the last coin we stop on is my coin.
                if (this.inRange(place) && (this.getColor(place) == player_color) && flipFlag) {

                    flipCoins(player_color, start, place, x_step, y_step);
                }
                    return true; // valid direction!

            }
        }
        return false; // invalid direction!
    }

    /**
     * flip all the coins from start point to end point with x, y steps.
     * @param my_color_num player color
     * @param start point to start flip
     * @param end point to finish at
     * @param x_step x direction from start point
     * @param y_step y direction from start point
     */
    private void flipCoins(char my_color_num, Point start, Point end, int x_step, int y_step){
        Point place = start;
        while (!place.equals(end)){
            this.setColor(place ,my_color_num);
            place = new Point(place.getX() + x_step, place.getY() +y_step);
        }
    }


    /**
     * try to play at the given point.
     * return the new state if it valid or null if not.
     * @param point point on map to play
     * @param playerColor
     * @return new state if the move is valid or null if not
     */
    public GridState tryPlayOn(Point point , char playerColor){
        ArrayList<String> mapCopy = new ArrayList<>(this.map);
        if (!this.inRange(point)){
            return null;
        }
        // check if the place is empty
        if (this.getColor(point) != 'E'){
            return null;
        }
        GridState temp = new GridState(mapCopy, this.oppositeColor(this.turn));
        // try to find at list one valid direction and flip it to my color
        Boolean bool = temp.check_direction(playerColor, point,   1 , 1, true);
        bool = temp.check_direction(playerColor, point,  -1 ,-1, true) || bool;
        bool = temp.check_direction(playerColor, point,  -1 , 1, true) || bool;
        bool = temp.check_direction(playerColor, point,   1 ,-1, true) || bool;
        bool = temp.check_direction(playerColor, point,   0 ,-1, true) || bool;
        bool = temp.check_direction(playerColor, point,    0 , 1, true) || bool;
        bool = temp.check_direction(playerColor, point,   -1 , 0, true) || bool;
        bool = temp.check_direction(playerColor, point,    1 , 0, true) || bool;


        if (bool) return temp;
        // when point is invalid place to play on.
        return null;
    }

    /**
     * calculate Heuristic Number.
     * saved in cache for public method
     * @return Heuristic number
     */
    private Integer calHeuristicNumber(){
        int black = 0, white = 0;
        Boolean empty = false;
        for (String i: this.map) {
            for (int j =0; j< i.length() ; ++j){
                switch (i.charAt(j)) {
                    case ('B'):
                        ++black;
                        break;
                    case ('W'):
                        ++white;
                        break;
                    default:
                        empty = true;
                        break;
                }
            }
        }

        StringBuilder borders = new StringBuilder(this.map.get(0) + this.map.get(this.map.size() - 1));
        for(int j = 1; j < this.map.size() -1 ; ++j ) {
            borders.append(this.map.get(j).charAt(0));
            borders.append(this.map.get(j).charAt(this.map.size() - 1));
        }

        int score = black - white;
        // check if there is empty place
        if (empty){
            String border = borders.toString();
            this.realScore = score + ( border.length()
                        - border.replace("B","").replace("W", "WW").length());
        } else {
            if (score > 0) {
                this.realScore = Integer.MAX_VALUE;
            } else {
                if (score<0) this.realScore = Integer.MIN_VALUE;
                else this.realScore = 0;
            }
        }
        return this.realScore;

    }

    /**
     * check if this state is final state.
     * @return Boolean
     */
    public Boolean isDone(){
        for (String i: this.map) {
            for (int j = 0; j < i.length(); ++j) {
                if (EMPTY_MARK == i.charAt(j)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * return all the possible states from this state
     * @return ArrayList of GridState object
     */
    public ArrayList<GridState> getAllStates () {
        ArrayList<GridState> gridList = new ArrayList<>();
        GridState temp;
        int size = this.map.size();
        for(int x = 0; x < size ; ++x){
            for (int y = 0; y < size; ++y){
                temp = this.tryPlayOn(new Point(x,y), this.turn);
                if(temp != null) {
                    gridList.add(temp);
                }
            }
        }
        return gridList;
    }

    /**
     * check who is win the game.
     * Should be done after isDone return True.
     * if the state is not final state return 'T'.
     * @return mark of the winning player
     */
    public char whoWin(){
        int black = 0, white = 0;
        Boolean empty = false;
        for (String i: this.map) {
            for (int j =0; j< i.length() ; ++j){
                switch (i.charAt(j)) {
                    case ('B'):
                        ++black;
                        break;
                    case ('W'):
                        ++white;
                        break;
                    default:
                        empty = true;
                        break;
                }
            }
        }
        if (empty){
            return 'T';
        }
        if (black > white) return 'B';
        return 'W';
    }

    /**
     * Returns a string representation of the object. In general, the
     * {@code toString} method returns a string that
     * "textually represents" this object. The result should
     * be a concise but informative representation that is easy for a
     * person to read.
     * It is recommended that all subclasses override this method.
     * <p>
     * The {@code toString} method for class {@code Object}
     * returns a string consisting of the name of the class of which the
     * object is an instance, the at-sign character `{@code @}', and
     * the unsigned hexadecimal representation of the hash code of the
     * object. In other words, this method returns a string equal to the
     * value of:
     * <blockquote>
     * <pre>
     * getClass().getName() + '@' + Integer.toHexString(hashCode())
     * </pre></blockquote>
     *
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for (String aMap : this.map) {
            str.append(aMap).append("\n");
        }
        return str.toString();
    }
}
