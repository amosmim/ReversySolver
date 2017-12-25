import javax.swing.tree.DefaultMutableTreeNode;
import java.util.ArrayList;

/**
 * MinMax Algorithm impalement
 * limited depth in build.
 * { note: the black is always Max player and the white is always Min player.}
 *
 */
public class MinMax {
    private int depth;

    /**
     * Constructor
     * @param depth limit to tree build
     */
    public MinMax(int depth) {
        this.depth = depth;
    }

    /**
     * return the ideal move from this state in this player using MinMax algorithm.
     * @param currentState start state
     * @return recommended next state
     */
    public GridState IdealStep(GridState currentState){
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(currentState);
        this.BuildTree(root, this.depth);
        return MinMax_Decision(root);
    }

    /**
     * inner interface
     * Unifies min and max
     */
    private interface Iselect {
        /**
         * return the best object from the two given ones
         * @param a first obj
         * @param b second obj
         * @return the good from the given
         */
        Integer select(Integer a, Integer b);

        /**
         * returns the worst value.
         * @return the worst value
         */
        Integer init();
    }

    /**
     * inner class for max.
     */
    private class max implements Iselect{

        @Override
        public Integer select(Integer a, Integer b) {
            return java.lang.Math.max(a, b);
        }

        @Override
        public Integer init() {
            return Integer.MIN_VALUE;
        }
    }

    private class min implements Iselect{

        @Override
        public Integer select(Integer a, Integer b) {
            return java.lang.Math.min(a, b);
        }

        @Override
        public Integer init() {
            return Integer.MAX_VALUE;
        }
    }

    /**
     * return the proper Iselect object for player
     * @param color player color
     * @return Iselect obj
     */
    private Iselect get_select_method(char color){
        if (color == 'B')
            return new max();
        return new min();
    }

    /**
     * return the recommended next state from the given tree.
     * @param root root of built tree states
     * @return next state
     */
    private GridState MinMax_Decision(DefaultMutableTreeNode root){
        GridState current_state = ((GridState)root.getUserObject());
        Iselect select_method = get_select_method(current_state.getTurn());
        Integer select_value = select_method.init();
        Integer temp_val;
        DefaultMutableTreeNode temp_state;
        DefaultMutableTreeNode select_state = (DefaultMutableTreeNode)root.getFirstChild();
        for (int i=0; i < root.getChildCount(); ++i){
            temp_state = ((DefaultMutableTreeNode)root.getChildAt(i));
            temp_val = MinMax_value(temp_state);
            if (select_method.select(temp_val, select_value).equals(temp_val)){
                select_state = temp_state;
                select_value = temp_val;
            }
        }
        return (GridState) select_state.getUserObject();

    }

    /**
     * helper function for MinMax_Decision.
     * return the the best Heuristic Number from this node on the tree.
     * @param root node in tree
     * @return the best Heuristic Number from given node
     */
    private Integer MinMax_value(DefaultMutableTreeNode root){
        GridState current_state = ((GridState)root.getUserObject());
        if (root.isLeaf()){
            return current_state.getHeuristicNumber();
        } else {
            Iselect select_method = get_select_method(current_state.getTurn());
            Integer select_value = select_method.init();
            Integer temp_val;
            DefaultMutableTreeNode temp_state;
            for (int i=0; i < root.getChildCount(); ++i){
                temp_state = ((DefaultMutableTreeNode)root.getChildAt(i));
                temp_val = MinMax_value(temp_state);
                if (select_method.select(temp_val, select_value).equals(temp_val)){
                    select_value = temp_val;
                }
            }
            return select_value;
        }
    }

    /**
     * recursive build a tree of states in given depth from given root.
     * {with tree.DefaultMutableTreeNode from Java library}
     * @param root root of the tree
     * @param depth remaining depth
     */
    private void BuildTree(DefaultMutableTreeNode root, int depth){
        if (depth == 0) return;

        GridState currentState = (GridState) root.getUserObject();
        ArrayList<GridState> nextMove = currentState.getAllStates();
        for(GridState g: nextMove){
            DefaultMutableTreeNode next = new DefaultMutableTreeNode(g);
            if (!g.isDone()){
                BuildTree(next , depth -1);
            }
            root.add(next);
        }
    }



}
