/*
 * Copyright (C) 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.systemui.statusbar.notification.stack;

import android.content.Context;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.systemui.R;
import com.android.systemui.statusbar.notification.row.ActivatableNotificationView;

/**
 * Similar in size and appearance to the NotificationShelf, appears at the beginning of some
 * notification sections. Currently only used for gentle notifications.
 */
public class SectionHeaderView extends ActivatableNotificationView {
    private View mContents;
    private TextView mLabelView;
    private ImageView mClearAllButton;

    private final RectF mTmpRect = new RectF();

    public SectionHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mContents = findViewById(R.id.content);
        mLabelView = findViewById(R.id.header_label);
        mClearAllButton = findViewById(R.id.btn_clear_all);
    }

    @Override
    protected View getContentView() {
        return mContents;
    }

    /** Must be called whenever the UI mode changes (i.e. when we enter night mode). */
    void onUiModeChanged() {
        updateBackgroundColors();
        mLabelView.setTextColor(
                getContext().getColor(R.color.notification_section_header_label_color));
        mClearAllButton.setImageResource(
                R.drawable.status_bar_notification_section_header_clear_btn);
    }

    void setAreThereDismissableGentleNotifs(boolean areThereDismissableGentleNotifs) {
        mClearAllButton.setVisibility(areThereDismissableGentleNotifs ? View.VISIBLE : View.GONE);
    }

    @Override
    protected boolean disallowSingleClick(MotionEvent event) {
        // Disallow single click on lockscreen if user is tapping on clear all button
        mTmpRect.set(
                mClearAllButton.getLeft(),
                mClearAllButton.getTop(),
                mClearAllButton.getLeft() + mClearAllButton.getWidth(),
                mClearAllButton.getTop() + mClearAllButton.getHeight());
        return mTmpRect.contains(event.getX(), event.getY());
    }

    /**
     * Fired whenever the user clicks on the body of the header (e.g. no sub-buttons or anything).
     */
    void setOnHeaderClickListener(View.OnClickListener listener) {
        mContents.setOnClickListener(listener);
    }

    /** Fired when the user clicks on the "X" button on the far right of the header. */
    void setOnClearAllClickListener(View.OnClickListener listener) {
        mClearAllButton.setOnClickListener(listener);
    }
}
