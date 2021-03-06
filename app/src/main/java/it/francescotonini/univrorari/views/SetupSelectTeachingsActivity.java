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

package it.francescotonini.univrorari.views;

import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.DialogInterface;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.List;
import it.francescotonini.univrorari.R;
import it.francescotonini.univrorari.UniVROrariApp;
import it.francescotonini.univrorari.adapters.TeachingsAdapter;
import it.francescotonini.univrorari.databinding.ActivitySetupSelectTeachingsBinding;
import it.francescotonini.univrorari.helpers.DialogHelper;
import it.francescotonini.univrorari.helpers.SimpleDividerItemDecoration;
import it.francescotonini.univrorari.models.ApiResponse;
import it.francescotonini.univrorari.models.Course;
import it.francescotonini.univrorari.models.Teaching;
import it.francescotonini.univrorari.models.Year;
import it.francescotonini.univrorari.viewmodels.CoursesViewModel;

public class SetupSelectTeachingsActivity extends BaseActivity {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_setup_select_teachings;
    }

    @Override protected CoursesViewModel getViewModel() {
        if (viewModel == null) {
            CoursesViewModel.Factory factory = new CoursesViewModel.Factory(getApplication(), ((UniVROrariApp)getApplication()).getDataRepository().getCoursesRepository());
            viewModel = ViewModelProviders.of(this, factory).get(CoursesViewModel.class);
        }

        return viewModel;
    }

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Setup binding
        binding = DataBindingUtil.setContentView(this, getLayoutId());

        // Add toolbar + subtitle
        setSupportActionBar((Toolbar)binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Set layout manager and divider
        binding.activitySetupSelectTeachingsRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        binding.activitySetupSelectTeachingsRecyclerview.addItemDecoration(new SimpleDividerItemDecoration(getApplication().getApplicationContext(), this.getResources().getColor(R.color.divider), 3));

        if (!getIntent().hasExtra("course") || !getIntent().hasExtra("selectedYears")) {
            onBackPressed();
            return;
        }

        // Start progress bar
        binding.activitySetupSelectTeachingsRefreshlayout.setRefreshing(true);
        binding.activitySetupSelectTeachingsRefreshlayout.setEnabled(true);

        course = new Gson().fromJson(getIntent().getStringExtra("course"), Course.class);
        selectedYears = new Gson().fromJson(getIntent().getStringExtra("selectedYears"), new TypeToken<List<Year>>(){}.getType());

        // Click listener for the save button
        binding.activitySetupSelectTeachingsSaveButton.setEnabled(false);
        binding.activitySetupSelectTeachingsSaveButton.setOnClickListener(saveButtonClickListener);

        // Get list of offices to show + start animation
        binding.activitySetupSelectTeachingsRefreshlayout.setRefreshing(true);
        getViewModel().getTeachings(course.getAcademicYearId(), course.getId()).observe(this, teachingsObserver);
    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_setup_select_course, menu);

        MenuItem search = menu.findItem(R.id.menu_setup_select_course_search);
        SearchView searchView = (SearchView) search.getActionView();
        searchView.setOnQueryTextListener(searchTextListener);

        return true;
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        // Handles toolbar's back button
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private ActivitySetupSelectTeachingsBinding binding;
    private CoursesViewModel viewModel;
    private Course course;
    private List<Year> selectedYears;

    private View.OnClickListener saveButtonClickListener = click -> {
        List<Teaching> teachings = ((TeachingsAdapter)binding.activitySetupSelectTeachingsRecyclerview.getAdapter()).getSelectedTeachings();

        if (teachings.size() == 0) {
            if (!this.isFinishing()) {
                DialogHelper.show(this, R.string.activity_setup_select_teachings_no_selection_title, R.string.activity_setup_select_teachings_no_selection_description, R.string.ok, null);
            }

            return;
        }

        getViewModel().savePreferences(course, teachings);

        Intent goToMain = new Intent(this, MainActivity.class);
        goToMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        goToMain.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        goToMain.putExtra("clear", true);
        startActivity(goToMain);
    };

    private Observer<ApiResponse<List<Teaching>>> teachingsObserver = teachings -> {
        if (!teachings.isSuccessful()) {
            if (!this.isFinishing()) {
                DialogHelper.show(this, R.string.error_network_title, R.string.error_network_message, R.string.error_network_button_message, (dialog, which) -> {
                    onBackPressed();
                });
            }

            // Stop progress bar
            binding.activitySetupSelectTeachingsRefreshlayout.setRefreshing(false);
            binding.activitySetupSelectTeachingsRefreshlayout.setEnabled(false);

            return;
        }

        binding.activitySetupSelectTeachingsRecyclerview.setAdapter(new TeachingsAdapter(teachings.getData(), selectedYears, course.getYears()));

        // Update UI accordingly
        binding.activitySetupSelectTeachingsSaveButton.setEnabled(true);
        binding.activitySetupSelectTeachingsRefreshlayout.setRefreshing(false);
        binding.activitySetupSelectTeachingsRefreshlayout.setEnabled(false);
    };

    private SearchView.OnQueryTextListener searchTextListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            if (binding.activitySetupSelectTeachingsRecyclerview.getAdapter() == null) {
                return false;
            }

            ((TeachingsAdapter)binding.activitySetupSelectTeachingsRecyclerview.getAdapter()).getFilter().filter(query);
            return true;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            if (binding.activitySetupSelectTeachingsRecyclerview.getAdapter() == null) {
                return false;
            }

            ((TeachingsAdapter)binding.activitySetupSelectTeachingsRecyclerview.getAdapter()).getFilter().filter(newText);
            return true;
        }
    };
}
