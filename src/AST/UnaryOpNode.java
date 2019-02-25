/**@Autor: Johannes Herterich*/
package AST;

import java.util.Objects;

public class UnaryOpNode extends SyntaxNode implements IVisitable {
    public String operator;
    public IVisitable subNode;

    public UnaryOpNode(String operator, IVisitable subNode) {
        this.operator = operator;
        this.subNode = subNode;
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
            UnaryOpNode otherUnOp = (UnaryOpNode) other;
            return Objects.equals(this.operator, otherUnOp.operator) && Objects.equals(this.subNode, otherUnOp.subNode);
        }
    }

    @Override
    public String toString() {
        return operator + " (" + subNode + ")";
    }
}