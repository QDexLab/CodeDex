package com.github.qdexlab.codedex.utils;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiExpression;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiModifier;
import com.intellij.psi.PsiType;
import com.intellij.psi.PsiVariable;
import com.intellij.psi.util.PsiUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.function.Predicate;

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

    public static PsiType getPsiTypeFromElement(PsiElement element) {
        if (element == null) {
            return null;
        }
        // 场景1：变量类型获取
        if (element instanceof PsiVariable) {
            return ((PsiVariable) element).getType();
        }
        // 场景2：表达式类型获取
        if (element instanceof PsiExpression) {
            return ((PsiExpression) element).getType();
        }
        PsiElement parent = element.getParent();
        // 场景3：父元素获取
        if (parent != null) {
            return getPsiTypeFromElement(parent);
        }
        return null;
    }

    public static PsiField @NotNull [] getAllFields(PsiClass aClass) {
        if (aClass == null) {
            return PsiField.EMPTY_ARRAY;
        }
        return aClass.getAllFields();
    }

    public static PsiMethod @NotNull [] getAllMethods(PsiClass aClass) {
        if (aClass == null) {
            return PsiMethod.EMPTY_ARRAY;
        }
        return aClass.getAllMethods();
    }

    public static PsiMethod @NotNull [] getMethods(PsiClass aClass, Predicate<PsiMethod> predicate) {
        PsiMethod[] methods = getAllMethods(aClass);
        return Arrays.stream(methods).filter(predicate).toArray(PsiMethod[]::new);
    }
}
