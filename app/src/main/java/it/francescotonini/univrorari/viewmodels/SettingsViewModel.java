/*
 * The MIT License
 *
 * Copyright (c) 2017-2019 Francesco Tonini - francescotonini.me
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

package it.francescotonini.univrorari.viewmodels;

import android.app.Application;
import androidx.annotation.NonNull;
import com.google.gson.Gson;
import it.francescotonini.univrorari.helpers.PreferenceHelper;
import it.francescotonini.univrorari.models.Course;

public class SettingsViewModel extends BaseViewModel {
    /**
     * Initializes a new instance of this class
     *
     * @param application
     */
    public SettingsViewModel(@NonNull Application application) {
        super(application);
    }

    /**
     * Get the {@link Course} saved
     * @return {@link Course} saved
     */
    public Course getCourse() {
        return new Gson().fromJson(PreferenceHelper.getString(PreferenceHelper.Keys.COURSE), Course.class);
    }

    /**
     * Sets the ui theme for this app
     * @param value
     */
    public void setUITheme(int value) {
        PreferenceHelper.setInt(PreferenceHelper.Keys.UI_THEME, value);
    }

    /**
     * Gets a boolean indicating whether or not dark theme is active
     * @return TRUE if dark mode is active; otherwise FALSE
     */
    public int getUITheme() {
        return PreferenceHelper.getInt(PreferenceHelper.Keys.UI_THEME, getApplication().getResources().getConfiguration().uiMode);
    }
}
