package com.github.qdexlab.codedex.utils;

import com.intellij.codeInsight.generation.PsiFieldMember;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiModifier;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class ClassMemberUtils {

    public static @Nullable PsiFieldMember[] getFieldClassMember(PsiClass aClass) {
        PsiField[] fields = aClass.getFields();
        ArrayList<PsiFieldMember> array = new ArrayList<>();
        for (PsiField field : fields) {
            if (field.hasModifierProperty(PsiModifier.STATIC)) continue;

            array.add(new PsiFieldMember(field));
        }
        return array.toArray(PsiFieldMember[]::new);
    }
}
