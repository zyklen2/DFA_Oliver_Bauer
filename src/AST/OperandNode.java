/**@Autor: Johannes Herterich*/

package AST;

import java.util.Objects;

public class OperandNode extends SyntaxNode implements IVisitable {
    public int position;
    public String symbol;

    public OperandNode(String symbol) {
        position = -1; // bedeutet: noch nicht initialisiert
        this.symbol = symbol;
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
            OperandNode otherOp = (OperandNode) other;
            return Objects.equals(this.symbol, otherOp.symbol);
        }
    }

    @Override
    public String toString(){
        return symbol;
    }
}