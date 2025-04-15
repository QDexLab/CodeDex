package com.github.qdexlab.codedex.action;

import com.intellij.codeInsight.generation.actions.BaseGenerateAction;
import com.intellij.openapi.project.DumbAware;
import com.intellij.psi.PsiClass;

public class GenerateCopyConstructorAction extends BaseGenerateAction implements DumbAware {

    protected GenerateCopyConstructorAction() {
        super(new GenerateCopyConstructorHandler());
    }

    @Override
    protected boolean isValidForClass(PsiClass targetClass) {
        return !targetClass.isEnum()
                && !targetClass.isAnnotationType()
                && !targetClass.isInterface()
                && targetClass.getFields().length > 0;
    }
}
