package com.github.qdexlab.codedex.action.generate;

import com.github.qdexlab.codedex.utils.ClassMemberUtils;
import com.github.qdexlab.codedex.utils.PsiTypeUtils;
import com.github.qdexlab.codedex.utils.PsiUtils;
import com.intellij.codeInsight.generation.ClassMember;
import com.intellij.codeInsight.generation.GenerationInfo;
import com.intellij.codeInsight.generation.PsiFieldMember;
import com.intellij.codeInsight.generation.PsiGenerationInfo;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiType;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class GenerateCopyConstructorHandler extends GenerateNonChooserMembersHandlerBase {

    @Override
    protected ClassMember @Nullable [] chooseOriginalMembers(PsiClass aClass, Project project, Editor editor) {
        return ClassMemberUtils.getFieldClassMember(aClass);
    }

    @Override
    protected @NotNull List<? extends GenerationInfo> generateMemberPrototypes(PsiClass aClass, ClassMember[] members) throws IncorrectOperationException {
        String className = aClass.getName();

        StringBuilder fieldCopy = new StringBuilder();
        if (!PsiUtils.extendsObject(aClass)) {
            fieldCopy.append("super(other);");
        }
        for (ClassMember member : members) {
            PsiField psiField = ((PsiFieldMember) member).getElement();
            PsiType fieldType = psiField.getType();
            String fieldClassName = fieldType.getCanonicalText();
            if (fieldClassName.startsWith("java.") || PsiTypeUtils.isPrimitiveType(fieldType)) {
                fieldCopy.append("this.").append(psiField.getName())
                        .append(" = other.").append(psiField.getName())
                        .append(";");
            } else {
                // deep copy
                fieldCopy.append("this.").append(psiField.getName())
                        .append(" = ").append("new ").append(fieldClassName)
                        .append("(other.").append(psiField.getName()).append(")")
                        .append(";");
            }

        }
        String body = COPY_PARAM.formatted(className, className, fieldCopy.toString());
        PsiElementFactory psiElementFactory = JavaPsiFacade.getInstance(aClass.getProject()).getElementFactory();
        return Arrays.asList(
                new PsiGenerationInfo<>(psiElementFactory.createMethodFromText(NON_PARAM.formatted(className), aClass))
                , new PsiGenerationInfo<>(psiElementFactory.createMethodFromText(body, aClass))
        );
    }

    private static final String NON_PARAM = """
            public %s() {
            }
            """;
    private static final String COPY_PARAM = """
            public %s(%s other) {
                %s
            }
            """;
}
