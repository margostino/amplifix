package org.gaussian.amplifix.toolkit.instrumenter;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;


class ClassAdapter extends ClassVisitor implements Opcodes {

    public ClassAdapter(final ClassVisitor cv) {
        super(ASM5, cv);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor mv = cv.visitMethod(access,  name,  desc,  signature,  exceptions);
        mv = new LoggerMethodAdapter(mv);
        System.out.println(String.format("%s %s %s", name, desc, signature));
        mv = new LoggerAdviceAdapter(Opcodes.ASM5, mv, access, name, desc);
        return mv;
    }
}