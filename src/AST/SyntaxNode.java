/**@Autor: Johannes Herterich*/
package AST;

import java.util.HashSet;
import java.util.Set;

public abstract class SyntaxNode
{
    public Boolean nullable;
    public final Set<Integer> firstpos = new HashSet<>();
    public final Set<Integer> lastpos = new HashSet<>();

    @Override
    public boolean equals(Object other){
        if (other == null || !other.getClass().equals(this.getClass())) {
            return super.equals(other);
        }else{
            IVisitable otherVis = (IVisitable)other;
            return this.equals(otherVis);
        }
    }

    public abstract boolean equals(IVisitable other);
}