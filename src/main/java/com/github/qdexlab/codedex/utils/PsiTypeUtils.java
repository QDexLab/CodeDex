package com.github.qdexlab.codedex.utils;

import com.intellij.psi.PsiPrimitiveType;
import com.intellij.psi.PsiType;

public class PsiTypeUtils {

    public static String getBoxedTypeName(PsiType type) {
        if (type instanceof PsiPrimitiveType) {
            return ((PsiPrimitiveType) type).getBoxedTypeName();
        }
        return type.getCanonicalText();
    }

}
