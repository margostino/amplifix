package toolkit.instrumentation.asm.calltraces.amplifix;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

class MethodAdapter extends MethodVisitor implements Opcodes {

    private String loggerName;

    public MethodAdapter(final MethodVisitor mv) {
        super(ASM5, mv);
    }

    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        return new AnnotationVisitor(Opcodes.ASM5) {
            public void visit(String name, Object value) {
                if (desc.equals("LbytecodeAnnotations/LogEntry;") && name.equals("logger")) {
                    loggerName = value.toString();
                }
            }
        };
    }

    public void onMethodEnter() {
        if (loggerName != null) {
            visitLdcInsn(loggerName);
            visitMethodInsn(INVOKESTATIC, "java/util/logging/Logger", "getLogger",
                            "(Ljava/lang/String;)Ljava/util/logging/Logger;", false);
            visitLdcInsn("ToolkitInstrumented");
            visitLdcInsn("mutate");
            visitMethodInsn(INVOKEVIRTUAL, "java/util/logging/Logger", "entering",
                            "(Ljava/lang/String;Ljava/lang/String;)V", false);
            loggerName = null;
        }
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {

//        /* System.err.println("CALL" + name); */
//
//        mv.visitFieldInsn(GETSTATIC, "java/lang/System", "err", "Ljava/io/PrintStream;");
//        mv.visitLdcInsn("CALL " + name);
//        mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
//
//        /* do call */
//        mv.visitMethodInsn(opcode, owner, name, desc, itf);
//
//        /* System.err.println("RETURN" + name); */
//
//        mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "err", "Ljava/io/PrintStream;");
//        mv.visitLdcInsn("RETURN " + name);
//        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);

        //mv = cw.visitMethod(ACC_PUBLIC, "mutate", "(Ltoolkit/factory/domain/Status;)V", null, null);
        if (name.equalsIgnoreCase("mutate")) {
            //mv = cw.visitMethod(ACC_PUBLIC, "mutate", "(Ltoolkit/factory/domain/Status;)V", null, null);
            mv.visitCode();
            mv.visitMethodInsn(INVOKESTATIC, "toolkit/instrumentation/asm/calltraces/AmplifixStatic", "send", "(Ljava/lang/Object;)V", false);
            mv.visitInsn(RETURN);
            mv.visitMaxs(2, 2);
            mv.visitEnd();

        }

    }
}