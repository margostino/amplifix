package org.gaussian.amplifix.toolkit.instrumenter;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.commons.AdviceAdapter;

public class LoggerAdviceAdapter extends AdviceAdapter {

    protected LoggerAdviceAdapter(int api,
                                  MethodVisitor mv,
                                  int access,
                                  String name,
                                  String desc) {
        super(api, mv, access, name, desc);
    }

    @Override
    protected void onMethodEnter() {
        if (((LoggerMethodAdapter)mv).enter) {
            addLogger(mv);
        }
    }

    @Override
    protected void onMethodExit(int opcode) {
        super.onMethodExit(opcode);
        if (((LoggerMethodAdapter)mv).exit) {
            addLogger(mv);
        }
    }

    private void addLogger(MethodVisitor mv) {
        mv.visitVarInsn(ALOAD, 0);
        mv.visitMethodInsn(INVOKESTATIC, "toolkit/instrumentation/asm/calltraces/AmplifixStatic", "send", "(Ljava/lang/Object;)V", false);
    }
}