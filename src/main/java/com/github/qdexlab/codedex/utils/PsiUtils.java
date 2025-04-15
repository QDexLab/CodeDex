package com.github.qdexlab.codedex.utils;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiModifier;
import com.intellij.psi.util.PsiUtil;
import org.jetbrains.annotations.NotNull;

/**
 * @see PsiUtil 扩充
 */
public class PsiUtils {
    public static boolean extendsObject(PsiClass aClass) {
        PsiClass superClass = aClass.getSuperClass();
        return superClass == null || "java.lang.Object".equals(superClass.getQualifiedName());
    }

    public static boolean hasCopyConstructor(@NotNull PsiClass clazz) {
        return hasCopyConstructor(clazz, false);
    }

    public static boolean hasCopyConstructor(@NotNull PsiClass clazz, boolean allowProtected) {
        return hasCopyConstructor(clazz, allowProtected, false);
    }

    public static boolean hasCopyConstructor(@NotNull PsiClass clazz, boolean allowProtected, boolean allowPrivateAndPackagePrivate) {
        PsiMethod[] constructors = clazz.getConstructors();
        for (PsiMethod cls: constructors) {
            if ((cls.hasModifierProperty(PsiModifier.PUBLIC)
                    || allowProtected && cls.hasModifierProperty(PsiModifier.PROTECTED)
                    || allowPrivateAndPackagePrivate && !cls.hasModifierProperty(PsiModifier.PROTECTED))
                    && cls.getParameterList().getParameters().length == 1
                    && cls.getParameterList().getParameters()[0].getType().getCanonicalText().equals(clazz.getQualifiedName())) {
                return true;
            }
        }
        return false;
    }
}
