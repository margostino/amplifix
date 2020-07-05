package toolkit.instrumentation.asm.calltraces.amplifix;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class AmplifixInstrumenter {

    //public static void main(final String args[]) throws Exception {
    public static void execute(String source) throws Exception {
        final String dest = source + ".bak";
        copyFile(source, dest);

        FileInputStream is = new FileInputStream(dest);
        byte[] b;

        ClassReader cr = new ClassReader(is);
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        ClassVisitor cv = new ClassAdapter(cw);
        cr.accept(cv, 0);
        b = cw.toByteArray();

        FileOutputStream fos = new FileOutputStream(source);
        fos.write(b);
        fos.close();
    }

    private static void copyFile(String source, String dest) throws IOException {
        File original = new File(source);

        Path copied = Paths.get(dest);
        Path originalPath = original.toPath();
        Files.copy(originalPath, copied, StandardCopyOption.REPLACE_EXISTING);

    }
}

