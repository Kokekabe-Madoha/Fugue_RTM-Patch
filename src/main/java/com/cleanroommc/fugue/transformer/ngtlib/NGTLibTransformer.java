package com.cleanroommc.fugue.transformer.ngtlib;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.*;
import top.outlands.foundation.IExplicitTransformer;

public class NGTLibTransformer implements IExplicitTransformer {
    @Override
    public byte[] transform(byte[] bytes) {
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(bytes);
        classReader.accept(classNode, 0);
        if (classNode.methods != null)
        {
            out:
            for (MethodNode methodNode : classNode.methods)
            {
                if (methodNode.name.equals("<clinit>")) {
                    InsnList instructions = methodNode.instructions;
                    if (instructions != null)
                    {
                        for (AbstractInsnNode insnNode : instructions)
                        {
                            if (insnNode instanceof LdcInsnNode ldcInsnNode)
                            {
                                if (ldcInsnNode.cst.equals("jdk.nashorn"))
                                {
                                    ldcInsnNode.cst = "org.openjdk.nashorn";
                                }
                            }
                        }
                    }
                }
            }
        }
        ClassWriter classWriter = new ClassWriter(0);
        classNode.accept(classWriter);
        return classWriter.toByteArray();
    }
}
