/**@Autor: Johannes Herterich*/

package AST;

import java.util.Objects;

public class BinOpNode extends SyntaxNode implements IVisitable {
    public String operator;
    public IVisitable left;
    public IVisitable right;

    public BinOpNode(String operator, IVisitable left, IVisitable right) {
        this.operator = operator;
        this.left = left;
        this.right = right;
    }

    @Override
    public void accept(IVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public boolean equals(IVisitable other) {
        if (other == null || !other.getClass().equals(this.getClass())) {
            return false;
        } else {
            BinOpNode otherBinOp = (BinOpNode) other;
            return (Objects.equals(this.operator, otherBinOp.operator))
                    && Objects.equals(this.left, otherBinOp.left)
                    && Objects.equals(this.right, otherBinOp.right);
        }
    }

    @Override
    public String toString() {
        return "(" + left + ") " + operator + " (" + right + ")";
    }
}