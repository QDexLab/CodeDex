package com.github.qdexlab.codedex.action;

import com.intellij.codeInsight.generation.actions.BaseGenerateAction;
import com.intellij.openapi.project.DumbAware;
import com.intellij.psi.PsiClass;

public class GenerateFieldToEnumAction extends BaseGenerateAction implements DumbAware {

    protected GenerateFieldToEnumAction() {
        super(new GenerateFieldToEnumHandler());
    }

    @Override
    protected boolean isValidForClass(PsiClass targetClass) {
        return targetClass.isEnum() && targetClass.getFields().length > 0;
    }
}
