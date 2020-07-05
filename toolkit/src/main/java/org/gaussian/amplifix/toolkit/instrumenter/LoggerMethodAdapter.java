package org.gaussian.amplifix.toolkit.instrumenter;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import static org.objectweb.asm.Opcodes.ASM5;

public class LoggerMethodAdapter extends MethodVisitor {

    public boolean enter;
    public boolean exit;

    public LoggerMethodAdapter(final MethodVisitor mv) {
        super(ASM5, mv);
    }

    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        return new AnnotationVisitor(Opcodes.ASM5) {
            public void visit(String name, Object value) {
                boolean isLogEntry = descriptor.equals("Ltoolkit/instrumentation/asm/calltraces/amplifix/LogEntry;");
                if (isLogEntry && name.equals("enter")) {
                    enter = Boolean.valueOf(value.toString());
                }
                if (isLogEntry && name.equals("exit")) {
                    exit = Boolean.valueOf(value.toString());
                }
            }
        };
    }

}