package toolkit.instrumentation.asm.calltraces.amplifix;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.TypePath;


class ClassAdapter extends ClassVisitor implements Opcodes {

    public ClassAdapter(final ClassVisitor cv) {
        super(ASM5, cv);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor mv = cv.visitMethod(access,  name,  desc,  signature,  exceptions);
        mv = new AmplifixMethodAdapter(mv);
        System.out.println(String.format("%s %s %s", name, desc, signature));
        mv = new AmplifixAdviceAdapter(Opcodes.ASM5, mv, access, name, desc);
        return mv;
    }
}