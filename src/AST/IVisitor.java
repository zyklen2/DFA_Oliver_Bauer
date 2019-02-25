package AST;

import AST.BinOpNode;
import AST.OperandNode;
import AST.UnaryOpNode;

public interface IVisitor
{
    public void visit(OperandNode node);
    public void visit(BinOpNode node);
    public void visit(UnaryOpNode node);
}