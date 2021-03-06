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

package it.francescotonini.univrorari.adapters;

import androidx.databinding.DataBindingUtil;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import java.util.ArrayList;
import java.util.List;
import it.francescotonini.univrorari.R;
import it.francescotonini.univrorari.databinding.ItemTeachingBinding;
import it.francescotonini.univrorari.models.Office;
import it.francescotonini.univrorari.models.Teaching;
import it.francescotonini.univrorari.models.Year;

/**
 * Adapter for a list of {@link Office}
 */
public class TeachingsAdapter extends RecyclerView.Adapter<TeachingsAdapter.ViewHolder> {
    /**
     * Initializes a new instance of this adapter
     * @param teachings list of {@link Teaching}
     * @param selectedYears list of {@link Year} already selected
     * @param allYears list of all {@link Year} available
     */
    public TeachingsAdapter(List<Teaching> teachings, List<Year> selectedYears, List<Year> allYears) {
        this.teachings = teachings;
        this.selectedTeachings = new ArrayList<>();
        this.allYears = allYears;

        // TODO: find better solution
        for (Year year : selectedYears) {
            for (Teaching teaching : teachings) {
                if (teaching.getYearId().hashCode() == year.getId().hashCode()) {
                    this.selectedTeachings.add(teaching);
                }
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            teachings.sort((t1, t2) -> {
                if (selectedTeachings.contains(t1) && selectedYears.contains(t2)) {
                    return 0;
                }
                else if (selectedTeachings.contains(t1)) {
                    return -1;
                }
                else if (selectedTeachings.contains(t2)) {
                    return 1;
                }

                return 0;
            });
        }
    }

    /**
     * Gets the list of {@link Teaching} selected
     * @return list of {@link Teaching} selected
     */
    public List<Teaching> getSelectedTeachings() {
        return selectedTeachings;
    }

    @NonNull @Override public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTeachingBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_teaching,
                parent, false
        );

        return new ViewHolder(binding.getRoot());
    }

    @Override public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.set(getTeachings().get(position));
    }

    @Override public int getItemCount() {
        return getTeachings().size();
    }

    /**
     * Gets the filter of this list
     * @return filter
     */
    public Filter getFilter() {
        return new Filter() {
            @Override protected FilterResults performFiltering(CharSequence constraint) {
                queryValue = constraint.toString();
                return null;
            }

            @Override protected void publishResults(CharSequence constraint, FilterResults results) {
                notifyDataSetChanged();
            }
        };
    }

    /**
     * ViewHolder for this adapter
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        /**
         * Initializes a new instance of this view holder
         * @param itemView view
         */
        public ViewHolder(View itemView) {
            super(itemView);

            binding = DataBindingUtil.bind(itemView);
            binding.getRoot().setOnClickListener(this);
            binding.itemTeachingCheck.setOnClickListener(this);
        }

        /**
         * Sets the teaching to show
         * @param teaching teaching
         */
        public void set(Teaching teaching) {
            this.teaching = teaching;

            binding.itemTeachingTeachingText.setText(this.teaching.getName());
            binding.itemTeachingYearText.setText(getYearName(this.teaching));
            binding.itemTeachingCheck.setChecked(selectedTeachings.contains(this.teaching));
        }

        @Override public void onClick(View v) {
            if (selectedTeachings.contains(this.teaching)) {
                // Deselect
                binding.itemTeachingCheck.setChecked(false);
                selectedTeachings.remove(this.teaching);
            }
            else {
                // Select
                binding.itemTeachingCheck.setChecked(true);
                selectedTeachings.add(this.teaching);
            }
        }

        private String getYearName(Teaching teaching) {
            for (Year year : allYears) {
                if (teaching.getYearId().equals(year.getId())) {
                    return year.getName();
                }
            }

            return "";
        }

        private Teaching teaching;
        private ItemTeachingBinding binding;
    }

    private List<Teaching> getTeachings() {
        if (queryValue == null || queryValue.isEmpty()) {
            return teachings;
        }

        List<Teaching> filteredTeachings = new ArrayList<>();
        for (Teaching teaching : this.teachings) {
            if (teaching.getName().toLowerCase().contains(queryValue.toLowerCase())) {
                filteredTeachings.add(teaching);
            }
        }

        return filteredTeachings;
    }

    private String queryValue;
    private List<Teaching> selectedTeachings;
    private List<Teaching> teachings;
    private List<Year> allYears;
}