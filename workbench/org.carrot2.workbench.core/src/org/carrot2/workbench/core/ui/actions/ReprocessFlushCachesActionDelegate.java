
/*
 * Carrot2 project.
 *
 * Copyright (C) 2002-2019, Dawid Weiss, Stanisław Osiński.
 * All rights reserved.
 *
 * Refer to the full license file "carrot2.LICENSE"
 * in the root folder of the repository checkout or at:
 * http://www.carrot2.org/carrot2.LICENSE
 */

package org.carrot2.workbench.core.ui.actions;

import org.carrot2.core.CachingProcessingComponentManager;
import org.carrot2.workbench.core.ui.SearchEditor;
import org.eclipse.jface.action.IAction;
import org.eclipse.ui.IEditorPart;

/**
 * Restarts processing in the currently active editor, flushes any cached input data.
 */
public class ReprocessFlushCachesActionDelegate extends ActiveSearchEditorActionDelegate 
{
    @Override
    public void init(IAction action)
    {
        super.init(action);
    }
    
    @Override
    public void run(SearchEditor editor)
    {
        // Drop existing caches.
        editor.getSearchResult().getInput().setAttribute(
            CachingProcessingComponentManager.CACHE_BYPASS_ATTR, true, false);
        editor.reprocess();
    }

    /**
     * Is this action enabled for the given editor?
     */
    protected boolean isEnabled(IEditorPart activeEditor)
    {
        /*
         * Allow forcing of re-rendering of the editor's contents when 
         * auto-update is off. Alternatively, we could disable re-rendering with:
         *  
         * activeEditor.isDirty();
         */
        return activeEditor != null &&
            (activeEditor instanceof SearchEditor);
    }
}
