package AST;

import FPTable.DepthFirstIterator;

/**
 * @Author: Lars Eckenfels
 */
public class FirstVisitor implements IVisitor
{
    public FirstVisitor()
    {
        counter = 1;
    }

    private int counter;

    public static void execute(IVisitable node) {
        DepthFirstIterator.traverse(node, new FirstVisitor());
    }

    @Override
    public void visit(BinOpNode node)
    {
        SyntaxNode left = (SyntaxNode) node.left;
        SyntaxNode right = (SyntaxNode) node.right;

        if (node.operator != null) {
            switch (node.operator)
            {
                case "|": //OR-Node
                    //fill nullable of OR-Node
                    node.nullable = left.nullable || right.nullable;

                    //fill firstpos of OR-Node
                    node.firstpos.addAll(left.firstpos);
                    node.firstpos.addAll(right.firstpos);

                    //fill lastpos of OR-Node
                    node.lastpos.addAll(left.lastpos);
                    node.lastpos.addAll(right.lastpos);
                    break;

                case "°": //AND-Node
                    //fill nullable of AND-Node
                    node.nullable = left.nullable && right.nullable;

                    //fill firstpos of AND-Node
                    if (left.nullable)
                    {
                        node.firstpos.addAll(left.firstpos);
                        node.firstpos.addAll(right.firstpos);
                    }
                    else
                    {
                        node.firstpos.addAll(left.firstpos);
                    }

                    //fill lastpos of AND-Node
                    if (right.nullable)
                    {
                        node.lastpos.addAll(left.lastpos);
                        node.lastpos.addAll(right.lastpos);
                    }
                    else
                    {
                        node.lastpos.addAll(right.lastpos);
                    }
                    break;
                default:
                    throw new IllegalArgumentException("Wrong BinOpNode operator: " + node.operator);
            }
        }
        else
            throw new IllegalArgumentException("No BinOpNode operator found");


    }

    @Override
    public void visit(OperandNode node)
    {
        if (node.symbol != null) {
            node.position = counter++;

            //fill nullable of node
            node.nullable =  false;

            //fill firstpos for node
            node.firstpos.add(node.position);

            //fill lastpos for node
            node.lastpos.add(node.position);
        }
        else {
            node.nullable = true;
        }
    }

    @Override
    public void visit(UnaryOpNode node)
    {
        SyntaxNode childNode = (SyntaxNode)node.subNode;

        if (node.operator != null) {
            switch (node.operator) {
                case "*":
                    //fill nullable for *-Node
                    node.nullable = true;
                    break;
                case "+":
                    //fill nullable for +-Node
                    node.nullable = childNode.nullable;
                    break;
                case "?":
                    //fill nullable for ?-Node
                    node.nullable = true;
                    break;
                default:
                    throw new IllegalArgumentException("Wrong UnaryOpNode operator: " + node.operator);
            }

            //fill firstpos for all nodes
            node.firstpos.addAll(childNode.firstpos);

            //fill lastpos for all nodes
            node.lastpos.addAll(childNode.lastpos);
        }
        else
            throw new IllegalArgumentException("No UnaryOpNode operator found");
    }
}
