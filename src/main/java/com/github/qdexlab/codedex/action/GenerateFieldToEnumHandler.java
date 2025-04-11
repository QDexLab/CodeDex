package com.github.qdexlab.codedex.action;

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
import com.intellij.psi.PsiModifier;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;

import java.util.ArrayList;
import java.util.Optional;

public class GenerateFieldToEnumHandler extends GenerateMembersHandlerBase {


    public GenerateFieldToEnumHandler() {
        super("Select Fields to Generate FieldToEnum");
    }

    @Override
    protected ClassMember[] getAllOriginalMembers(PsiClass aClass) {
        PsiField[] fields = aClass.getFields();
        ArrayList<ClassMember> array = new ArrayList<>();
        for (PsiField field : fields) {
            if (field.hasModifierProperty(PsiModifier.STATIC)) continue;

            array.add(new PsiFieldMember(field));
        }
        return array.toArray(ClassMember.EMPTY_ARRAY);
    }

    @Override
    protected GenerationInfo[] generateMemberPrototypes(PsiClass aClass, ClassMember originalMember) throws IncorrectOperationException {
        PsiField psiField = ((PsiFieldMember) originalMember).getElement();
        aClass = Optional.ofNullable(PsiTreeUtil.getParentOfType(psiField, PsiClass.class)).orElse(aClass);
        String className = aClass.getName();
        String filedName = psiField.getName();
        String upperCaseFieldName = StringUtil.toUpperCase(filedName);
        String lowerCaseFieldName = StringUtil.toLowerCase(filedName);
        String javaBeanFieldName = StringUtil.capitalizeWithJavaBeanConvention(filedName);

        PsiElementFactory psiElementFactory = JavaPsiFacade.getInstance(aClass.getProject()).getElementFactory();
        String fieldBody = FIELD_TEMPLATE.formatted(className, upperCaseFieldName, className, filedName);
        PsiField field = psiElementFactory.createFieldFromText(fieldBody, aClass);
        String methodBody = METHOD_TEMPLATE.formatted(className, javaBeanFieldName, lowerCaseFieldName, upperCaseFieldName, lowerCaseFieldName);
        PsiMethod method = psiElementFactory.createMethodFromText(methodBody, aClass);
        return new GenerationInfo[]{new PsiGenerationInfo<>(field), new PsiGenerationInfo<>(method)};
    }

    private static final String FIELD_TEMPLATE = """
            private static final java.util.Map<String, %s> %s_MAP = new java.util.HashMap<>() {{
                java.util.Arrays.asList(%s.values()).forEach(x -> put(x.%s, x));
            }};
            """;
    private static final String METHOD_TEMPLATE = """
            public static %s valueOf%s(String %s) {
                return %s_MAP.get(%s);
            }
            """;


}
