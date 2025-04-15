package com.github.qdexlab.codedex.action;

import com.intellij.codeInsight.generation.ClassMember;
import com.intellij.codeInsight.generation.GenerateMembersHandlerBase;
import com.intellij.codeInsight.generation.GenerationInfo;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class GenerateNonChooserMembersHandlerBase extends GenerateMembersHandlerBase {
    public GenerateNonChooserMembersHandlerBase() {
        // do not use chooser
        super("");
    }

    @Override
    protected ClassMember[] getAllOriginalMembers(PsiClass aClass) {
        // do not use
        return ClassMember.EMPTY_ARRAY;
    }

    @Override
    protected GenerationInfo[] generateMemberPrototypes(PsiClass aClass, ClassMember originalMember) throws IncorrectOperationException {
        // do not use
        return GenerationInfo.EMPTY_ARRAY;
    }

    @Override
    protected abstract ClassMember @Nullable [] chooseOriginalMembers(PsiClass aClass, Project project, Editor editor);

    @Override
    protected abstract @NotNull List<? extends GenerationInfo> generateMemberPrototypes(PsiClass aClass, ClassMember[] members) throws IncorrectOperationException;
}
