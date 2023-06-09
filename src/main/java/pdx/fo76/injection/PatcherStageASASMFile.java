package pdx.fo76.injection;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import pdx.fo76.asasm.instruction.InstructionReader;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public abstract class PatcherStageASASMFile<T extends PatcherStageASASMBundle<?>> extends PatcherStageASASMBundle<T> {

    protected final List<ASASMEdit> edits = new ArrayList<>();

    private PatcherStageASASMFile(T parent) {
        super(parent, parent.asmPath);
    }

    public PatcherStageASASMFile<T> addEdit(ASASMEdit edit) {
        edits.add(edit);
        return this;
    }

    public abstract void execute() throws PatcherException;

    static class ManipulateClassASM<T extends PatcherStageABC<?>> extends PatcherStageASASMFile<PatcherStageASASMBundle<T>> {
        private final String className;
        private final String namespace;

        public ManipulateClassASM(PatcherStageASASMBundle<T> parent, String namespace, String className) {
            super(parent);
            this.namespace = namespace;
            this.className = className;
        }

        @Override
        public void execute() throws PatcherException {
            try {
                var namespaceDir = namespace != null ? namespace.replace(".", "\\\\") + "\\" : "";
                String inputFile = String.format("%s/%s%s.class.asasm", asmPath, namespaceDir, className);
                var sourceFile = buildPath.resolve(inputFile).toFile();
                List<String> lines = FileUtils.readLines(sourceFile, StandardCharsets.UTF_8);

                var root = InstructionReader.readTree(lines);
                var classNode = root.getInstructions().get(0);

                // run manipulations
                for (ASASMEdit e : edits) {
                    e.execute(this, namespace, className, classNode);
                }

                // Write the modified text back to the input file
                var out = classNode.stream(0).toList();
                FileUtils.writeLines(sourceFile, StandardCharsets.UTF_8.name(), out);
            } catch (IOException e) {
                throw new PatcherException("IOException while editing asasm class file: " + e.getMessage(), e);
            }
        }
    }
}
