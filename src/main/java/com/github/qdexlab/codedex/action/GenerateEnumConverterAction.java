package com.github.qdexlab.codedex.action;

import com.intellij.codeInsight.generation.actions.BaseGenerateAction;
import com.intellij.openapi.project.DumbAware;
import com.intellij.psi.PsiClass;

public class GenerateEnumConverterAction extends BaseGenerateAction implements DumbAware {

    protected GenerateEnumConverterAction() {
        super(new GenerateEnumConverterHandler());
    }

    @Override
    protected boolean isValidForClass(PsiClass targetClass) {
        return targetClass.isEnum() && targetClass.getFields().length > 0;
    }
}
