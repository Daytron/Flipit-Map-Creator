/*
 * The MIT License
 *
 * Copyright 2014 Ryan Gilera.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.github.daytron.flipit.map.creator.model;

import com.github.daytron.flipit.map.creator.MainApp;
import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;
import org.controlsfx.dialog.Dialogs;

/**
 * The dialog static utility class for generating dialog windows.
 *
 * @author Ryan Gilera ryangilera@gmail.com
 */
public final class DialogManager {

    private DialogManager() {
    }

    /**
     * Creates and shows a confirmation dialog, with available actions,
     * <code>OK</code> and <code>CANCEL</code>.
     *
     * @param msgHead The masthead message for the dialog.
     * @param msgBody The message body for the dialog.
     * @param app The MainApp object for retrieving the stage.
     * @return returns an Action object as the response from the confirmation
     * dialog created
     */
    public static Action showConfirmDialog(String msgHead, String msgBody,
            MainApp app) {
        Action response = Dialogs.create()
                .owner(app.getStage())
                .title("Confirmation")
                .masthead(msgHead)
                .message(msgBody)
                .actions(Dialog.ACTION_OK, Dialog.ACTION_CANCEL)
                .showConfirm();

        return response;
    }

    /**
     * Creates and shows an exception dialog.
     *
     * @param e The exception to show.
     * @param app The MainApp object for retrieving the stage.
     */
    public static void showExceptionDialog(Exception e, MainApp app) {
        Dialogs.create()
                .owner(app.getStage())
                .title("Exception")
                .masthead(e.toString())
                .message(e.getMessage())
                .showException(e);
    }

    /**
     * Creates and shows an error dialog.
     *
     * @param msgHead The masthead message for the dialog.
     * @param msgBody The message body for the dialog.
     * @param app The MainApp object for retrieving the stage.
     */
    public static void showErrorDialog(String msgHead, String msgBody,
            MainApp app) {
        Dialogs.create()
                .owner(app.getStage())
                .title("Error")
                .masthead(msgHead)
                .message(msgBody)
                .showError();
    }

    /**
     * Creates and shows a warning dialog.
     *
     * @param msgHead The masthead message for the dialog.
     * @param msgBody The message body for the dialog.
     * @param app The MainApp object for retrieving the stage.
     */
    public static void showWarningDialog(String msgHead, String msgBody,
            MainApp app) {
        Dialogs.create()
                .owner(app.getStage())
                .title("Warning")
                .masthead(msgHead)
                .message(msgBody)
                .showWarning();
    }
}
