package pdx.fo76.asasm.instruction;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class NewActivation extends IndentedSimpleNode {
    public NewActivation() {
        super("newactivation");
    }
}
