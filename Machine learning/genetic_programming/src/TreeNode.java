public class TreeNode {

    String type;    // "function", "terminal", "constant"
    String value;   // "+", "-", "*", "/", "Open", "Close", or "0.5" etc.
    TreeNode left;
    TreeNode right;

    TreeNode(String type, String value) {
        this.type = type;
        this.value = value;
        this.left = null;
        this.right = null;
    }

    public void print(String indent, boolean isLast) {
        System.out.print(indent);
        
        if (isLast) {
            System.out.print("└── ");
            indent += "    ";
        } else {
            System.out.print("├── ");
            indent += "│   ";
        }
    
        System.out.println(value + " [" + type + "]");
    
        if (left != null && right != null) {
            left.print(indent, false);
            right.print(indent, true);
        } else if (left != null) {
            left.print(indent, true);
        } else if (right != null) {
            right.print(indent, true);
        }
    }

    // Overloaded method for the initial call
    public void print() {
        System.out.println(value + " [" + type + "]");
        
        if (left != null && right != null) {
            left.print("", false);
            right.print("", true);
        } else if (left != null) {
            left.print("", true);
        } else if (right != null) {
            right.print("", true);
        }
    }
        
    // measure a nodes depth
    public static int getTreeDepth(TreeNode node) {
        if (node == null) return 0;
        int leftDepth = getTreeDepth(node.left);
        int rightDepth = getTreeDepth(node.right);
        return 1 + Math.max(leftDepth, rightDepth);
    }

    public String getType() {
        return type;
    }

    public String toFunctionString() {
        if (type.equals("terminal")) {
            return value;
        } else if (type.equals("constant")) {
            return value;
        } else if (type.equals("function")) {
            // For function nodes, combine operator with child expressions
            if (left == null && right == null) {
                return value;  
            } else if (right == null) {
                return value + "(" + left.toFunctionString() + ")";
            } else {
                return value + "(" + left.toFunctionString() + ", " + right.toFunctionString() + ")";
            }
        }
        return "";  
    }

}
