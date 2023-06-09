package pdx.fo76.asasm.instruction;

import lombok.EqualsAndHashCode;
import pdx.fo76.asasm.SyntaxConstants;

@EqualsAndHashCode(callSuper = true)
public class Code extends IndentedSimpleNode {
    public Code() {
        super(SyntaxConstants.CODE);
    }
}
