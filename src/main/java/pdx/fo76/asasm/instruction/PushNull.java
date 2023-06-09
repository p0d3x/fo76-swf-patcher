package pdx.fo76.asasm.instruction;

import lombok.EqualsAndHashCode;
import pdx.fo76.asasm.SyntaxConstants;

@EqualsAndHashCode(callSuper = true)
public class PushNull extends IndentedSimpleNode {
    public PushNull() {
        super(SyntaxConstants.PUSH_NULL);
    }
}
