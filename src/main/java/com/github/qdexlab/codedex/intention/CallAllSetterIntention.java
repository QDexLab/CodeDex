package com.github.qdexlab.codedex.intention;

import com.github.qdexlab.codedex.utils.PsiFileUtils;
import com.github.qdexlab.codedex.utils.PsiUtils;
import com.intellij.codeInsight.intention.BaseElementAtCaretIntentionAction;
import com.intellij.codeInspection.util.IntentionFamilyName;
import com.intellij.codeInspection.util.IntentionName;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.util.PsiUtil;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class CallAllSetterIntention extends BaseElementAtCaretIntentionAction {

    @Override
    public @NotNull @IntentionName String getText() {
        return getFamilyName();
    }

    @Override
    public @NotNull @IntentionFamilyName String getFamilyName() {
        return "Call all setter";
    }

    @Override
    public boolean isAvailable(@NotNull Project project, @NotNull Editor editor, @NotNull PsiElement element) {
        return getMethods(element).length > 0;
    }

    @Override
    public void invoke(@NotNull Project project, @NotNull Editor editor, @NotNull PsiElement element) throws IncorrectOperationException {
        PsiFile file = element.getContainingFile();
        PsiMethod[] methods = getMethods(element);
        StringBuilder callChain = new StringBuilder();
        for (PsiMethod method : methods) {
            callChain.append(element.getText()).append(".").append(method.getName()).append("();\n");
        }
        int offset = editor.getCaretModel().getOffset();
        PsiFileUtils.insertTextNextLineAndReformat(file, offset, callChain.toString());
    }

    private PsiMethod @NotNull [] getMethods(PsiElement element) {
        PsiClass aClass = Optional.ofNullable(element)
                .map(PsiUtils::getPsiTypeFromElement)
                .map(PsiUtil::resolveClassInClassTypeOnly)
                .orElse(null);
        return PsiUtils.getMethods(aClass, m -> m.getName().startsWith("set"));
    }

}
