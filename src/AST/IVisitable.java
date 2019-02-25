package AST;

public interface IVisitable
{
    void accept(IVisitor visitor);
}