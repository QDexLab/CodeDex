package com.github.qdexlab.codedex.utils;

import com.intellij.psi.PsiPrimitiveType;
import com.intellij.psi.PsiType;
import com.intellij.psi.util.PsiTypesUtil;

public class PsiTypeUtils {

    public static String boxIfPossible(PsiType type) {
        return PsiTypesUtil.boxIfPossible(type.getCanonicalText());
    }

    public static boolean isPrimitiveType(PsiType type) {
        return type instanceof PsiPrimitiveType;
    }

}
