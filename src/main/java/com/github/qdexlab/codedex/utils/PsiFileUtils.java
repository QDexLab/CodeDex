package com.github.qdexlab.codedex.utils;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;

public class PsiFileUtils {

    public static void insertTextNextLineAndReformat(PsiFile file, int caretOffset, String text) {
        if (file == null) return;
        Project project = file.getProject();
        Document document = file.getFileDocument();
        int lineNumber = document.getLineNumber(caretOffset);
        int nextLineStartOffset = document.getLineStartOffset(lineNumber + 1);
        insertText(file, document, project, nextLineStartOffset, text);
    }
    public static void insertTextAndReformat(PsiFile file, int caretOffset, String text) {
        if (file == null) return;
        Project project = file.getProject();
        Document document = file.getFileDocument();
        insertText(file, document, project, caretOffset, text);
    }

    private static void insertText(PsiFile file, Document document, Project project, int caretOffset, String text) {
        document.insertString(caretOffset, text);
        int endOffset = caretOffset + text.length();
        JavaCodeStyleManager.getInstance(project).shortenClassReferences(file, caretOffset, endOffset);
        CodeStyleManager.getInstance(project).reformatText(file, caretOffset, endOffset);
    }
}
