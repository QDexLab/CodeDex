package com.github.qdexlab.codedex.action;

import com.github.qdexlab.codedex.utils.ClassMemberUtils;
import com.github.qdexlab.codedex.utils.PsiTypeUtils;
import com.intellij.codeInsight.generation.ClassMember;
import com.intellij.codeInsight.generation.GenerateMembersHandlerBase;
import com.intellij.codeInsight.generation.GenerationInfo;
import com.intellij.codeInsight.generation.PsiFieldMember;
import com.intellij.codeInsight.generation.PsiGenerationInfo;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;

import java.util.Optional;

public class GenerateEnumConverterHandler extends GenerateMembersHandlerBase {


    public GenerateEnumConverterHandler() {
        super("Select Fields to Generate Enum Converter");
    }

    @Override
    protected ClassMember[] getAllOriginalMembers(PsiClass aClass) {
        return ClassMemberUtils.getFieldClassMember(aClass);
    }

    @Override
    protected GenerationInfo[] generateMemberPrototypes(PsiClass aClass, ClassMember originalMember) throws IncorrectOperationException {
        PsiField psiField = ((PsiFieldMember) originalMember).getElement();
        aClass = Optional.ofNullable(PsiTreeUtil.getParentOfType(psiField, PsiClass.class)).orElse(aClass);
        String className = aClass.getName();
        String filedName = psiField.getName();
        String fieldType = PsiTypeUtils.boxIfPossible(psiField.getType());
        String upperCaseFieldName = StringUtil.toUpperCase(filedName);
        String javaBeanFieldName = StringUtil.capitalizeWithJavaBeanConvention(filedName);

        PsiElementFactory psiElementFactory = JavaPsiFacade.getInstance(aClass.getProject()).getElementFactory();

        String fieldBody = new StringBuilder("private static final java.util.Map<")
                .append(fieldType).append(", ").append(className).append("> ")
                .append(upperCaseFieldName).append("_MAP = java.util.Arrays.stream(")
                .append(className).append(".values()).collect(java.util.stream.Collectors.toMap(x -> x.")
                .append(filedName).append(", e -> e));").toString();
        PsiField field = psiElementFactory.createFieldFromText(fieldBody, aClass);

        String methodBody = new StringBuilder("public static ").append(className).append(" valueOf")
                .append(javaBeanFieldName).append("(").append(fieldType).append(" ").append(filedName).append(") {")
                .append("    return ").append(upperCaseFieldName).append("_MAP.get(").append(filedName).append(");}").toString();
        PsiMethod method = psiElementFactory.createMethodFromText(methodBody, aClass);

        return new GenerationInfo[]{new PsiGenerationInfo<>(field), new PsiGenerationInfo<>(method)};
    }
}
